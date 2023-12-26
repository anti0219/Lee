package ltd.newbee.mall.entity;

import java.util.Date;

public class UserBrowseHistory {
	private Long id;
	private Long userId;
    private Long goodsId;
    private Date browseTime;
    private Byte configType;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Date getBrowseTime() {
		return browseTime;
	}
	public void setBrowseTime(Date browseTime) {
		this.browseTime = browseTime;
	}
	public Byte getConfigType() {
		return configType;
	}
	public void setConfigType(Byte configType) {
		this.configType = configType;
	}
    
    
}
