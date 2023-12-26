package ltd.newbee.mall.entity;

import java.util.List;

public class UpdateInfoRequest {
    private int goodsId;
    private List<List<?>> userSelectParams;
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public List<List<?>> getUserSelectParams() {
		return userSelectParams;
	}
	public void setUserSelectParams(List<List<?>> userSelectParams) {
		this.userSelectParams = userSelectParams;
	}
    
    
}
