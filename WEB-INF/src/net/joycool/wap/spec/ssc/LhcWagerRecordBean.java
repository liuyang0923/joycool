package net.joycool.wap.spec.ssc;

import java.sql.Timestamp;

public class LhcWagerRecordBean {
	int id;
	int lhcId;
	int userId;
	int type;
	int num;
	long money;
	int mark;
	int num2;
	int num3;
	long lhcDate;
	int term;
	long prize;	// 中奖金额

	Timestamp createDatetime;

	/**
	 * @return 返回 createDatetime。
	 */
	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            要设置的 createDatetime。
	 */
	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 money。
	 */
	public long getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            要设置的 money。
	 */
	public void setMoney(long money) {
		this.money = money;
	}

	/**
	 * @return 返回 num。
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            要设置的 num。
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return 返回 type。
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            要设置的 type。
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return 返回 lhcId。
	 */
	public int getLhcId() {
		return lhcId;
	}

	/**
	 * @param lhcId
	 *            要设置的 lhcId。
	 */
	public void setLhcId(int lhcId) {
		this.lhcId = lhcId;
	}

	/**
	 * @return 返回 userId。
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId。
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public long getLhcDate() {
		return lhcDate;
	}

	public void setLhcDate(long lhcDate) {
		this.lhcDate = lhcDate;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public long getPrize() {
		return prize;
	}

	public void setPrize(long prize) {
		this.prize = prize;
	}
}
