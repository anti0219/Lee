package ltd.newbee.mall.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodsReview {
	String reviewId;
	int goodsId;
	int userId;
	private String title;
	int scores;
	int likeNum;
	String skuId;
	Date askDate;
	String content;
	boolean isLikedByCurrentUser;
	int currentUserId;
	List<String> imagUrl = new ArrayList<>(); ;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImagUrl() {
		return imagUrl;
	}
	public void setImagUrl(List<String> imagUrl) {
		this.imagUrl = imagUrl;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public Date getAskDate() {
		return askDate;
	}
	public void setAskDate(Date askDate) {
		this.askDate = askDate;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public boolean isLikedByCurrentUser() {
		return isLikedByCurrentUser;
	}
	public void setLikedByCurrentUser(boolean isLikedByCurrentUser) {
		this.isLikedByCurrentUser = isLikedByCurrentUser;
	}
	public int getCurrentUserId() {
		return currentUserId;
	}
	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
