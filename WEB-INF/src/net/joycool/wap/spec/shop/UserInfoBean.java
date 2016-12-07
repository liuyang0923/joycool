package net.joycool.wap.spec.shop;

public class UserInfoBean {

	int uid;
	float gold;
	int favoriteCount;		//收藏夹的数量
	float consumeCount;		//消费总额
	public float getConsumeCount() {
		return consumeCount;
	}
	public void setConsumeCount(float consumeCount) {
		this.consumeCount = consumeCount;
	}
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public float getGold() {
		if(gold < 0) gold = 0;
		return gold;
	}
	
	public String getGoldString(){
		return ShopUtil.formatPrice2(gold);
		
	}
	
	public void setGold(float gold) {
		this.gold = gold;
	}
	
	public void increaseGold(float count) {
		gold = ShopUtil.calMoney(count, gold, true);
	}
	
	public void decreaseGold(float count) {
		//System.out.println(count);
		gold = ShopUtil.calMoney(count, gold, false);
	}
}
