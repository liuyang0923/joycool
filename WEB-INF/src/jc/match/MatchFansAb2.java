package jc.match;

/**
 * 每日粉丝记录
 */
public class MatchFansAb2 {
	int id;
	int leftUid;
	int rightUid;
	int good[] = new int[8];
	int prices;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrices() {
		return prices;
	}
	public void setPrices(int prices) {
		this.prices = prices;
	}
	public int getLeftUid() {
		return leftUid;
	}
	public void setLeftUid(int leftUid) {
		this.leftUid = leftUid;
	}
	public int getRightUid() {
		return rightUid;
	}
	public void setRightUid(int rightUid) {
		this.rightUid = rightUid;
	}
	public int[] getGood() {
		return good;
	}
	public void setGood(int[] good) {
		this.good = good;
	}
}
