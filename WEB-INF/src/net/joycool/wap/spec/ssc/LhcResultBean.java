package net.joycool.wap.spec.ssc;

import java.sql.Timestamp;

public class LhcResultBean {
	int id;
	int num1;
	int num2;
	int num3;
	int num4;
	int num5;
	int num6;
	int num7;

	int flag;
	Timestamp createDatetime;
	int term; // 当天第几期
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
	 * @return 返回 num1。
	 */
	public int getNum1() {
		return num1;
	}

	/**
	 * @param num1
	 *            要设置的 num1。
	 */
	public void setNum1(int num1) {
		this.num1 = num1;
	}

	/**
	 * @return 返回 num2。
	 */
	public int getNum2() {
		return num2;
	}

	/**
	 * @param num2
	 *            要设置的 num2。
	 */
	public void setNum2(int num2) {
		this.num2 = num2;
	}

	/**
	 * @return 返回 num3。
	 */
	public int getNum3() {
		return num3;
	}

	/**
	 * @param num3
	 *            要设置的 num3。
	 */
	public void setNum3(int num3) {
		this.num3 = num3;
	}

	/**
	 * @return 返回 num4。
	 */
	public int getNum4() {
		return num4;
	}

	/**
	 * @param num4
	 *            要设置的 num4。
	 */
	public void setNum4(int num4) {
		this.num4 = num4;
	}

	/**
	 * @return 返回 num5。
	 */
	public int getNum5() {
		return num5;
	}

	/**
	 * @param num5
	 *            要设置的 num5。
	 */
	public void setNum5(int num5) {
		this.num5 = num5;
	}

	/**
	 * @return 返回 num6。
	 */
	public int getNum6() {
		return num6;
	}

	/**
	 * @param num6
	 *            要设置的 num6。
	 */
	public void setNum6(int num6) {
		this.num6 = num6;
	}

	/**
	 * @return 返回 num7。
	 */
	public int getNum7() {
		return num7;
	}

	/**
	 * @param num7
	 *            要设置的 num7。
	 */
	public void setNum7(int num7) {
		this.num7 = num7;
	}

	/**
	 * @return 返回 flag。
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            要设置的 flag。
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public boolean checkPingMaNum(int number) {
		if (num1 == number || num2 == number || num3 == number
				|| num4 == number || num5 == number || num6 == number) {
			return true;
		}
		return false;
	}

	public boolean checkTeMaNum(int number) {
		if (num7 == number) {
			return true;
		}
		return false;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}
}
