package net.joycool.wap.bean.tong;

import java.sql.Timestamp;

/**
 * @author macq
 * @datetime 2006-12-24 下午01:54:38
 * @explain 城帮系统
 */
public class TongBean {
	int id;

	int locationId;

	String title;

	int totalCount;

	int guestCount;

	String description;

	long fund;

	long fundTotal;

	int reward;

	int mark;

	int userId;

	int userIdA;

	int userIdB;

	int userCount;

	int rate;

	int nowEndure;

	int highEndure;

	String createDatetime;

	int cadreCount;

	int stockId;

	// 商店开发度
	int shop;

	// 帮会昵称
	String shortTitle = "";

	long destroyDatetime;

	int honor;

	int honorDrop;

	public Timestamp tongAssaultTime;

	public Timestamp tongRecoveryTime;
	
	int depot;		// 仓库总开发度
	int depotWeek;	// 本周开发度
	int depotLast;	// 上周开发度

	public int getDepot() {
		return depot;
	}

	public void setDepot(int depot) {
		this.depot = depot;
	}

	public int getDepotLast() {
		return depotLast;
	}

	public void setDepotLast(int depotLast) {
		this.depotLast = depotLast;
	}

	public int getDepotWeek() {
		return depotWeek;
	}

	public void setDepotWeek(int depotWeek) {
		this.depotWeek = depotWeek;
	}

	/**
	 * @return 返回 honorDrop。
	 */
	public int getHonorDrop() {
		return honorDrop;
	}

	/**
	 * @param honorDrop
	 *            要设置的 honorDrop。
	 */
	public void setHonorDrop(int honorDrop) {
		this.honorDrop = honorDrop;
	}

	/**
	 * @return 返回 honor。
	 */
	public int getHonor() {
		return honor;
	}

	/**
	 * @param honor
	 *            要设置的 honor。
	 */
	public void setHonor(int honor) {
		this.honor = honor;
	}

	/**
	 * @return 返回 destroyDatetime。
	 */
	public long getDestroyDatetime() {
		return destroyDatetime;
	}

	/**
	 * @param destroyDatetime
	 *            要设置的 destroyDatetime。
	 */
	public void setDestroyDatetime(long destroyDatetime) {
		this.destroyDatetime = destroyDatetime;
	}

	/**
	 * @return 返回 shortTitle。
	 */
	public String getShortTitle() {
		return shortTitle;
	}

	/**
	 * @param shortTitle
	 *            要设置的 shortTitle。
	 */
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public int getShop() {
		return shop;
	}

	public void setShop(int shop) {
		this.shop = shop;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

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
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            要设置的 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return fund
	 */
	public long getFund() {
		return fund;
	}

	/**
	 * @param fund
	 *            要设置的 fund
	 */
	public void setFund(long fund) {
		this.fund = fund;
	}

	/**
	 * @return fundTotal
	 */
	public long getFundTotal() {
		return fundTotal;
	}

	/**
	 * @param fundTotal
	 *            要设置的 fundTotal
	 */
	public void setFundTotal(long fundTotal) {
		this.fundTotal = fundTotal;
	}

	/**
	 * @return guestCount
	 */
	public int getGuestCount() {
		return guestCount;
	}

	/**
	 * @param guestCount
	 *            要设置的 guestCount
	 */
	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
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
	 * @return locationId
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            要设置的 locationId
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
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
	 * @return rate
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            要设置的 rate
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            要设置的 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            要设置的 totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return userCount
	 */
	public int getUserCount() {
		return userCount;
	}

	/**
	 * @param userCount
	 *            要设置的 userCount
	 */
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	/**
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return userIdA
	 */
	public int getUserIdA() {
		return userIdA;
	}

	/**
	 * @param userIdA
	 *            要设置的 userIdA
	 */
	public void setUserIdA(int userIdA) {
		this.userIdA = userIdA;
	}

	/**
	 * @return userIdB
	 */
	public int getUserIdB() {
		return userIdB;
	}

	/**
	 * @param userIdB
	 *            要设置的 userIdB
	 */
	public void setUserIdB(int userIdB) {
		this.userIdB = userIdB;
	}

	/**
	 * @return highEndure
	 */
	public int getHighEndure() {
		return highEndure;
	}

	/**
	 * @param highEndure
	 *            要设置的 highEndure
	 */
	public void setHighEndure(int highEndure) {
		this.highEndure = highEndure;
	}

	/**
	 * @return nowEndure
	 */
	public int getNowEndure() {
		return nowEndure;
	}

	/**
	 * @param nowEndure
	 *            要设置的 nowEndure
	 */
	public void setNowEndure(int nowEndure) {
		this.nowEndure = nowEndure;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	/**
	 * @return cadreCount
	 */
	public int getCadreCount() {
		return cadreCount;
	}

	/**
	 * @param cadreCount
	 *            要设置的 cadreCount
	 */
	public void setCadreCount(int cadreCount) {
		this.cadreCount = cadreCount;
	}

	/**
	 * @return 返回 tongAssaultTime。
	 */
	public Timestamp getTongAssaultTime() {
		return tongAssaultTime;
	}

	/**
	 * @param tongAssaultTime
	 *            要设置的 tongAssaultTime。
	 */
	public void setTongAssaultTime(Timestamp tongAssaultTime) {
		this.tongAssaultTime = tongAssaultTime;
	}

	/**
	 * @return 返回 tongRecoveryTime。
	 */
	public Timestamp getTongRecoveryTime() {
		return tongRecoveryTime;
	}

	/**
	 * @param tongRecoveryTime
	 *            要设置的 tongRecoveryTime。
	 */
	public void setTongRecoveryTime(Timestamp tongRecoveryTime) {
		this.tongRecoveryTime = tongRecoveryTime;
	}

}
