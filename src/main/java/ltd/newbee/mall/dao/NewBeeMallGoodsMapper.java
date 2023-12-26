/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.Answer;
import ltd.newbee.mall.entity.GoodsReview;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.Review;
import ltd.newbee.mall.entity.SkuInfo;
import ltd.newbee.mall.entity.SkuUpdateInfo;
import ltd.newbee.mall.entity.StockNumDTO;
import ltd.newbee.mall.entity.TmpList;
import ltd.newbee.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface NewBeeMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(NewBeeMallGoods record);

    int insertSelective(NewBeeMallGoods record);

    NewBeeMallGoods selectByPrimaryKey(Long goodsId);

    NewBeeMallGoods selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);

    int updateByPrimaryKeySelective(NewBeeMallGoods record);

    int updateByPrimaryKeyWithBLOBs(NewBeeMallGoods record);

    int updateByPrimaryKey(NewBeeMallGoods record);

    List<NewBeeMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    List<NewBeeMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<NewBeeMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("newBeeMallGoodsList") List<NewBeeMallGoods> newBeeMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int recoverStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);
    
    List<Long> getAnswerById(List<Long> answerId); //ctrl shift o
    
    int deleteAnswerById (long answerId);
    
    int insertAnswer (long answerId);
    
    void insertQuestion (Answer questionInfo);
    
    List<TmpList> findTmpList(PageQueryUtil pageUtil);
    
    int getTotalTmpList(PageQueryUtil pageUtil);
    
    void insertBrwoseHistory(Long userId,Long goodsId);
    
    List<NewBeeMallGoods> selectByBrowseTime(List<Long> goodsIds);

	int getTotalHistory();
	
	
	//SKU
	List<SkuInfo> getSkuListByGoodsId(int goodsId);

	List<String> getColumnByParams(int goodsId, List<Object> filterForColumn);
	
	List<String> getImageUrl(String skuId);
	
	//review
	List<GoodsReview> getReviewList(PageQueryUtil pageUtil);
	int getTotalReview(PageQueryUtil pageUtil);
	List<String> getReviewImageUrl (String reviewId,int userId);
	
	//提交评论
	int insertUserComment (Review params);
	int insertReviewImage(String reviewId,int userId,String path,List<String> imageNames);
	
	//like
	int handleUserLikeState (String reviewId,int commentUserId,int userId,boolean likeState);
	int increaseLikeNum(String reviewId,int userId);
}