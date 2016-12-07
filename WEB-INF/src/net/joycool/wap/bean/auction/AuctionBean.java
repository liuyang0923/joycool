package net.joycool.wap.bean.auction;

import java.util.Calendar;

import net.joycool.wap.util.StringUtil;

/**
 * @author macq
 * @datetime 2006-12-12 下午04:42:40
 * @explain 物品拍卖表
 */
public class AuctionBean {
	int id;

	int leftUserId;

	int rightUserId;

	int productId;
	
	int userBagId;

	int dummyId;

	int time;

	long startPrice;

	long bitePrice;

	long currentPrice;

	int mark;
	
	int hallId;		// 某个拍卖，0表示乐酷以前的拍卖

	String createDatetime;

	/**
	 * @return createDatetime
	 */
	public String getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime
	 *            要设置的 createDatetime
	 */
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return currentPrice
	 */
	public long getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice 要设置的 currentPrice
	 */
	public void setCurrentPrice(long currentPrice) {
		this.currentPrice = currentPrice;
	}

	/**
	 * @return dummyId
	 */
	public int getDummyId() {
		return dummyId;
	}

	/**
	 * @param dummyId
	 *            要设置的 dummyId
	 */
	public void setDummyId(int dummyId) {
		this.dummyId = dummyId;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return leftUserId
	 */
	public int getLeftUserId() {
		return leftUserId;
	}

	/**
	 * @param leftUserId
	 *            要设置的 leftUserId
	 */
	public void setLeftUserId(int leftUserId) {
		this.leftUserId = leftUserId;
	}

	/**
	 * @return mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return productId
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            要设置的 productId
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}

	/**
	 * @return rightUserId
	 */
	public int getRightUserId() {
		return rightUserId;
	}

	/**
	 * @param rightUserId
	 *            要设置的 rightUserId
	 */
	public void setRightUserId(int rightUserId) {
		this.rightUserId = rightUserId;
	}

	/**
	 * @return time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 *            要设置的 time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return bitePrice
	 */
	public long getBitePrice() {
		return bitePrice;
	}

	/**
	 * @param bitePrice 要设置的 bitePrice
	 */
	public void setBitePrice(long bitePrice) {
		this.bitePrice = bitePrice;
	}

	/**
	 * @return startPrice
	 */
	public long getStartPrice() {
		return startPrice;
	}

	/**
	 * @param startPrice 要设置的 startPrice
	 */
	public void setStartPrice(long startPrice) {
		this.startPrice = startPrice;
	}

	public String getTimeLength() {
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinite = cal.get(Calendar.MINUTE);
		int createHour = StringUtil.toInt(createDatetime.substring(11, 13));
		int createMinite = StringUtil.toInt(createDatetime.substring(14, 16));
		int hour = 0;
		if (currentHour >= createHour)

		{
			hour = ((currentHour - createHour) * 60 + currentMinite - createMinite) / 60;
		} else {
			hour = ((currentHour + 24 - createHour) * 60 + currentMinite - createMinite) / 60;
		}
		if (hour < 5)
			return "长";
		else if (hour > 4 && hour < 7)
			return "中";
		else
			return "短";

	}

	/**
	 * @return Returns the userBagId.
	 */
	public int getUserBagId() {
		return userBagId;
	}

	/**
	 * @param userBagId The userBagId to set.
	 */
	public void setUserBagId(int userBagId) {
		this.userBagId = userBagId;
	}

	public int getHallId() {
		return hallId;
	}

	public void setHallId(int hallId) {
		this.hallId = hallId;
	}
}
