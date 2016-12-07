package jc.match;

/**
 * 粉丝表
 */
public class MatchFans {
	int userId;
	int good[] = new int[8];
	int prices;

	public int getPrices() {
		return prices;
	}

	public void setPrices(int prices) {
		this.prices = prices;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int[] getGood() {
		return good;
	}

	public void setGood(int[] good) {
		this.good = good;
	}
	

}
