package ltd.newbee.mall.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class Review {
	    String reviewId;
		int goodsId;
		int userId;
	    private Integer rating; // 商品评分
	    String skuId;
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    Date askDate;
	    private String title;
	    private String content; // 评论文本
	    private String nickName;
	    private MultipartFile[] images; // 用户上传的图片


		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Integer getRating() {
			return rating;
		}
		public void setRating(Integer rating) {
			this.rating = rating;
		}
		public MultipartFile[] getImages() {
			return images;
		}
		public void setImages(MultipartFile[] images) {
			this.images = images;
		}
		public String getSkuId() {
			return skuId;
		}
		public void setSkuId(String skuId) {
			this.skuId = skuId;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Date getAskDate() {
			return askDate;
		}
		public void setAskDate(Date askDate) {
			this.askDate = askDate;
		}
		public String getReviewId() {
			return reviewId;
		}
		public void setReviewId(String reviewId) {
			this.reviewId = reviewId;
		}
		public int getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(int goodsId) {
			this.goodsId = goodsId;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
	    
	    

}
