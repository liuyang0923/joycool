package jc.match;

/**
 * 粉丝投票总记录
 */
public class MatchVoted {
	int userId;
	int good[] = new int[7];
	int prices;
	int voteCount;
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
	public int getPrices() {
		return prices;
	}
	public void setPrices(int prices) {
		this.prices = prices;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
}
