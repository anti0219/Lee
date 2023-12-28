/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.controller.mall;

import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.common.NewBeeMallException;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.NewBeeMallGoodsDetailVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.controller.vo.SearchPageCategoryVO;
import ltd.newbee.mall.entity.Answer;
import ltd.newbee.mall.entity.GoodsReview;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.NewBeeMallShoppingCartItem;
import ltd.newbee.mall.entity.Review;
import ltd.newbee.mall.entity.ShoppingCartItem;
import ltd.newbee.mall.entity.SkuUpdateInfo;
import ltd.newbee.mall.entity.UpdateInfoRequest;
import ltd.newbee.mall.service.NewBeeMallCategoryService;
import ltd.newbee.mall.service.NewBeeMallGoodsService;
import ltd.newbee.mall.util.BeanUtil;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsController {

	@Resource
	private NewBeeMallGoodsService newBeeMallGoodsService;
	@Resource
	private NewBeeMallCategoryService newBeeMallCategoryService;

	@GetMapping({ "/search", "/search.html" })
	public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		if (ObjectUtils.isEmpty(params.get("page"))) {
			params.put("page", 1);
		}
		params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
		// 封装分类数据
		if (params.containsKey("goodsCategoryId") && StringUtils.hasText(params.get("goodsCategoryId") + "")) {
			Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
			SearchPageCategoryVO searchPageCategoryVO = newBeeMallCategoryService.getCategoriesForSearch(categoryId);
			if (searchPageCategoryVO != null) {
				request.setAttribute("goodsCategoryId", categoryId);
				request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
			}
		}
		// 封装参数供前端回显
		if (params.containsKey("orderBy") && StringUtils.hasText(params.get("orderBy") + "")) {
			request.setAttribute("orderBy", params.get("orderBy") + "");
		}
		String keyword = "";
		// 对keyword做过滤 去掉空格
		if (params.containsKey("keyword") && StringUtils.hasText((params.get("keyword") + "").trim())) {
			keyword = params.get("keyword") + "";
		}
		request.setAttribute("keyword", keyword);
		params.put("keyword", keyword);
		// 搜索上架状态下的商品
		params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
		// 封装商品数据
		PageQueryUtil pageUtil = new PageQueryUtil(params);
		request.setAttribute("pageResult", newBeeMallGoodsService.searchNewBeeMallGoods(pageUtil));
		return "mall/search";
	}

	@GetMapping("/answers")
	@ResponseBody
	public Result<?> answers(@RequestBody List<Long> answerId) {
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.getAnswerById(answerId));

	}

	@PostMapping("/answers")
	@ResponseBody
	public Result<?> delete(@RequestParam long answerId) {
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.deleteAnswerById(answerId));
	}

	@PostMapping("/answers/insert")
	@ResponseBody
	public Result<?> insert(@RequestBody Answer questionInfo) {
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.insertAnswerQuestion(questionInfo));
	}

	/*------------分页功能重写------------*/
	@GetMapping("/answers/tmplist")
	@ResponseBody
	public Result<?> tmpList(@RequestParam Map<String, Object> params) {
		if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		PageQueryUtil pageUtil = new PageQueryUtil(params);
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.getTempList(pageUtil));
	}

	// 商品详情页
	@GetMapping("/goods/detail/{goodsId}")
	public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
		HttpSession session = request.getSession();
		NewBeeMallUserVO user = (NewBeeMallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);// 获取用户id

		if (goodsId < 1) {
			NewBeeMallException.fail("参数异常");
		}
		NewBeeMallGoods goods = newBeeMallGoodsService.getNewBeeMallGoodsById(goodsId);
		if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
			NewBeeMallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
		}
		NewBeeMallGoodsDetailVO goodsDetailVO = new NewBeeMallGoodsDetailVO();
		BeanUtil.copyProperties(goods, goodsDetailVO);
		goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
		request.setAttribute("goodsDetail", goodsDetailVO);

		Long userId = user.getUserId();

		newBeeMallGoodsService.recordUserBrowseHistory(userId, goodsId);// 将用户id和商品id写入数据库

		return "mall/detail";
	}

	// 选择不同属性返回结果
	@ResponseBody
	@PostMapping("/goods/updateInfo")
    public ResponseEntity<?> goodsInfoUpdate (@RequestBody UpdateInfoRequest userSelectParams){
    	
		int goodsId = userSelectParams.getGoodsId();
		List<List<?>>  paramsList = new ArrayList<List<?>>(userSelectParams.getUserSelectParams());
		
		SkuUpdateInfo updateInfo = newBeeMallGoodsService.getSkuInfoByUserSelect(goodsId,paramsList);

		Map<String,Object> responseData = new HashMap<>();
		responseData.put("stock",updateInfo.getStock());
		responseData.put("price",updateInfo.getPrice());
		responseData.put("imageUrl",updateInfo.getImageUrl());
		responseData.put("skuId",updateInfo.getSkuId());
		
    	return ResponseEntity.ok(responseData);
	}

	//QA/review模块
	@ResponseBody
	@GetMapping("/goods/review")//goodsId,page,limit
	public Result<?> userReview (@RequestBody Map<String,Object> params){
		

		if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
			return ResultGenerator.genFailResult("参数异常！");
		}
		
		PageQueryUtil pageUtil = new PageQueryUtil(params);
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.getReviewList(pageUtil));
	}
	
	//提交评论
	@ResponseBody
	@PostMapping("/goods/submit")
	public Result<?> submitComment (@ModelAttribute Review params){
		
		
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.insertComment(params));
		
	}
	
	//like
	@PostMapping("/goods/likes")
	@ResponseBody
	public Result<?> handleLike (@RequestBody GoodsReview goodsReview){
		
		return ResultGenerator.genSuccessResult(newBeeMallGoodsService.handleUserLikeState(goodsReview));
	}
	
	//添加购物车
    @PostMapping("/goods/cart")
    @ResponseBody
    public Result saveShoppingCartItem(@RequestBody ShoppingCartItem shoppingCartItem,
                                                 HttpSession httpSession) {
    	
    	Long a = (long) 10000;
        //NewBeeMallUserVO user = (NewBeeMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);//获取userId
        shoppingCartItem.setUserId(a);
        String saveResult = newBeeMallGoodsService.saveCartItem(shoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }
}
