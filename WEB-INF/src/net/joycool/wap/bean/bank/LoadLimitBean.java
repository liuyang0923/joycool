package net.joycool.wap.bean.bank;
/**
 * 
 * @author Administrator
 * 等级贷款上限表
 */
public class LoadLimitBean {
	
	// 记录id
	private int id;
	
	// 用户等级
	private int rank;
	
	// 等级贷款上限
	private int limit;

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the limit.
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit The limit to set.
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
}
