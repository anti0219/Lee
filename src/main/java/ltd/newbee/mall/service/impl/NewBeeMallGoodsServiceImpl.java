/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



import org.apache.commons.beanutils.PropertyUtils;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.beanutils.BeanUtils;
import ltd.newbee.mall.common.NewBeeMallCategoryLevelEnum;
import ltd.newbee.mall.common.NewBeeMallException;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.NewBeeMallSearchGoodsVO;
import ltd.newbee.mall.dao.GoodsCategoryMapper;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.entity.Answer;
import ltd.newbee.mall.entity.GoodsCategory;
import ltd.newbee.mall.entity.GoodsReview;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.Review;
import ltd.newbee.mall.entity.SkuInfo;
import ltd.newbee.mall.entity.SkuUpdateInfo;
import ltd.newbee.mall.entity.TmpList;
import ltd.newbee.mall.service.NewBeeMallGoodsService;
import ltd.newbee.mall.util.BeanUtil;
import ltd.newbee.mall.util.NewBeeMallUtils;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;

@Service
public class NewBeeMallGoodsServiceImpl implements NewBeeMallGoodsService {

	@Autowired
	private NewBeeMallGoodsMapper goodsMapper;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;

	@Override
	public PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil) {
		List<NewBeeMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsList(pageUtil);
		int total = goodsMapper.getTotalNewBeeMallGoods(pageUtil);
		PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public String saveNewBeeMallGoods(NewBeeMallGoods goods) {
		GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
		// 分类不存在或者不是三级分类，则该参数字段异常
		if (goodsCategory == null
				|| goodsCategory.getCategoryLevel().intValue() != NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
			return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
			
					
		}
		if (goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId()) != null) {
			return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
		}
		goods.setGoodsName(NewBeeMallUtils.cleanString(goods.getGoodsName()));
		goods.setGoodsIntro(NewBeeMallUtils.cleanString(goods.getGoodsIntro()));
		goods.setTag(NewBeeMallUtils.cleanString(goods.getTag()));
		if (goodsMapper.insertSelective(goods) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public void batchSaveNewBeeMallGoods(List<NewBeeMallGoods> newBeeMallGoodsList) {
		if (!CollectionUtils.isEmpty(newBeeMallGoodsList)) {
			goodsMapper.batchInsert(newBeeMallGoodsList);
		}
	}

	@Override
	public String updateNewBeeMallGoods(NewBeeMallGoods goods) {
		GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
		// 分类不存在或者不是三级分类，则该参数字段异常
		if (goodsCategory == null
				|| goodsCategory.getCategoryLevel().intValue() != NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
			return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
		}
		NewBeeMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
		if (temp == null) {
			return ServiceResultEnum.DATA_NOT_EXIST.getResult();
		}
		NewBeeMallGoods temp2 = goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId());
		if (temp2 != null && !temp2.getGoodsId().equals(goods.getGoodsId())) {
			// name和分类id相同且不同id 不能继续修改
			return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
		}
		goods.setGoodsName(NewBeeMallUtils.cleanString(goods.getGoodsName()));
		goods.setGoodsIntro(NewBeeMallUtils.cleanString(goods.getGoodsIntro()));
		goods.setTag(NewBeeMallUtils.cleanString(goods.getTag()));
		goods.setUpdateTime(new Date());
		if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public NewBeeMallGoods getNewBeeMallGoodsById(Long id) {
		NewBeeMallGoods newBeeMallGoods = goodsMapper.selectByPrimaryKey(id);
		if (newBeeMallGoods == null) {
			NewBeeMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
		}
		return newBeeMallGoods;
	}

	@Override
	public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
		return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
	}

	@Override
	public PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil) {
		List<NewBeeMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsListBySearch(pageUtil);
		int total = goodsMapper.getTotalNewBeeMallGoodsBySearch(pageUtil);
		List<NewBeeMallSearchGoodsVO> newBeeMallSearchGoodsVOS = new ArrayList<>();
		if (!CollectionUtils.isEmpty(goodsList)) {
			newBeeMallSearchGoodsVOS = BeanUtil.copyList(goodsList, NewBeeMallSearchGoodsVO.class);
			for (NewBeeMallSearchGoodsVO newBeeMallSearchGoodsVO : newBeeMallSearchGoodsVOS) {
				String goodsName = newBeeMallSearchGoodsVO.getGoodsName();
				String goodsIntro = newBeeMallSearchGoodsVO.getGoodsIntro();
				// 字符串过长导致文字超出的问题
				if (goodsName.length() > 28) {
					goodsName = goodsName.substring(0, 28) + "...";
					newBeeMallSearchGoodsVO.setGoodsName(goodsName);
				}
				if (goodsIntro.length() > 30) {
					goodsIntro = goodsIntro.substring(0, 30) + "...";
					newBeeMallSearchGoodsVO.setGoodsIntro(goodsIntro);
				}
			}
		}
		PageResult pageResult = new PageResult(newBeeMallSearchGoodsVOS, total, pageUtil.getLimit(),
				pageUtil.getPage());
		return pageResult;
	}

	@Override
	public List<Long> getAnswerById(List<Long> answerId) {
		List<Long> answer = goodsMapper.getAnswerById(answerId);// goodsMapper.getAnswerById负责获取sql结果
		return answer;
	}

	@Override
	public int deleteAnswerById(long answerId) {
		return goodsMapper.deleteAnswerById(answerId);
	}

	@Override
	public int insertAnswerQuestion(Answer questionInfo) {
		int resultAnswer = goodsMapper.insertAnswer(questionInfo.getAnswerId());
		goodsMapper.insertQuestion(questionInfo);
		return resultAnswer;
	}

	/*------------分页功能重写------------*/
	@Override
	public PageResult getTempList(PageQueryUtil pageUtil) {
		List<TmpList> tmpList = goodsMapper.findTmpList(pageUtil);// 返回数据库里分页范围内的数据放入list
		int total = goodsMapper.getTotalTmpList(pageUtil);// 获取返回数据总件数
		PageResult pageResult = new PageResult(tmpList, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}
	
	/*------------浏览历史记录------------*/
	@Override
	public void recordUserBrowseHistory(Long userId, Long goodsId) {
		goodsMapper.insertBrwoseHistory(userId, goodsId);
	}
					
	/*------------根据用户选择返回SKU------------*/
	@Override
	public SkuUpdateInfo getSkuInfoByUserSelect (int goodsId,List<List<?>> userSelectParams) {
		
		int stock = 0;
		int price = 0;
		List<String> imageUrl = new ArrayList<String>();
		String skuId = "";
		SkuUpdateInfo skuUpdateInfo = new SkuUpdateInfo();
		
		//取出sku表里符合goodsId的行
		List<SkuInfo> skuList = goodsMapper.getSkuListByGoodsId(goodsId);
		
		//抽取用户选择的属性名用于column筛选
		List<Object> filterForColumn = new ArrayList<Object>();
		List<Object> attributes = new ArrayList<Object>();//用户选择属性放入列表,用于sku判断
		for(List<?> a : userSelectParams) {
			if(a.size() > 1) {
			filterForColumn.add(a.get(0));
			attributes.add(a.get(1));
			}
		}
		List<String> columnList = new ArrayList<String>(goodsMapper.getColumnByParams(goodsId,filterForColumn));
		
		
		outerLoop: 
		for(SkuInfo skuInfo : skuList ) {
			int cnt = 0;//统计符合条件的次数,等于columnList.size()说明筛选出
			for(String column : columnList) {
				String skuTmp = "";
				try {
					skuTmp = PropertyUtils.getProperty(skuInfo, column).toString();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(attributes.contains(skuTmp)) {
					cnt ++;
				}
				if (cnt == userSelectParams.size()) {
					skuId = skuInfo.getSkuId();
					stock = skuInfo.getStock();
					price = skuInfo.getPrice();
					break outerLoop;
				}
			}
		}
		
		//获取imageUrl
		imageUrl = goodsMapper.getImageUrl(skuId);
		
		skuUpdateInfo.setPrice(price);
		skuUpdateInfo.setStock(stock);
		skuUpdateInfo.setImageUrl(imageUrl);
		return skuUpdateInfo;

	}

	
	//获取商品评论
	@Override
	public PageResult getReviewList(PageQueryUtil pageUtil) {
		
		int total = goodsMapper.getTotalReview(pageUtil);
		List<GoodsReview> goodsReviewList = goodsMapper.getReviewList(pageUtil);
		for(GoodsReview a : goodsReviewList) {
			List<String> imageUrlList = goodsMapper.getReviewImageUrl(a.getReviewId(), a.getUserId());
			a.setImagUrl(imageUrlList);
		}

		PageResult pageResult = new PageResult(goodsReviewList,total,pageUtil.getLimit(),pageUtil.getPage());
		
		return pageResult;
		
	}
	
	//添加用户评论
	@Override
	public int insertComment(Review params) {
		
		int a = goodsMapper.insertUserComment(params);//录入用户评论
		String reviewId = params.getReviewId();
		int userId = params.getUserId();
		
		MultipartFile[] images = params.getImages(); 
		String path = "/Users/johnnyzhang/Documents";
		List<String> imageNames = new ArrayList();
		for(MultipartFile image : images) {
			imageNames.add(image.getOriginalFilename());
		}
		int b = goodsMapper.insertReviewImage(reviewId, userId, path,imageNames);//存储图片链接
		
		return a + b;
	}
	
	//like
	@Override
	public int handleUserLikeState(GoodsReview params){

		String reviewId = params.getReviewId();
		int commentUserId = params.getUserId();
		int userId = params.getCurrentUserId();
		boolean likeState = true;
		
		Integer result = goodsMapper.handleUserLikeState(reviewId,commentUserId,userId,likeState);
		goodsMapper.increaseLikeNum(reviewId, commentUserId);
		
		return result;
	}
	
}
