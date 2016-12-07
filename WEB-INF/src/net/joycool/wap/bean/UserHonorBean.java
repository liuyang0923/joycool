package net.joycool.wap.bean;

import java.util.Date;

public class UserHonorBean {

	int id = 0;
	int userId;
	int honor = 0;
	int lastWeek = 0;	// 上周荣誉
	int place = 0;		// 排名
	int rank = 0;		// 荣誉等级，0是最低
	Date createDatetime;

	/**
	 * @return Returns the createDatetime.
	 */
	public Date getCreateDatetime() {
		return createDatetime;
	}

	/**
	 * @param createDatetime The createDatetime to set.
	 */
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the honor.
	 */
	public int getHonor() {
		return honor;
	}

	/**
	 * @param honor The honor to set.
	 */
	public void setHonor(int honor) {
		this.honor = honor;
	}

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
	 * @return Returns the lastWeek.
	 */
	public int getLastWeek() {
		return lastWeek;
	}

	/**
	 * @param lastWeek The lastWeek to set.
	 */
	public void setLastWeek(int lastWeek) {
		this.lastWeek = lastWeek;
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

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the place.
	 */
	public int getPlace() {
		return place;
	}

	/**
	 * @param place The place to set.
	 */
	public void setPlace(int place) {
		this.place = place;
	}

	public static int HONOR3 = 100;	// 达到多少算是三星
	public static int PLACE3 = 18;	// 达到多少位一定算是三星
	public void calcHonorRank() {
		if(lastWeek > 0 && place > 0) {
			if(place <= 1) {
				rank = 5;
			} else if(place <= 3) {
				rank = 4;
			} else if(place <= PLACE3) {
				rank = 3;
			} else if(place <= 30) {
				rank = 2;
			} else  {
				rank = 1;
			}
		}
	}

	public static String[] rankName = {"无", "1星", "2星", "3星", "4星", "5星"};
	public static String[] rankNameShort = {"无", "1", "2", "3", "4", "5"};
	
	public String getRankNameShort() {
		return rankNameShort[rank];
	}

	public String getRankName() {
		return rankName[rank];
	}
	
	/**
	 * @return 神秘商店购买物品折扣
	 */
	public static float[] discount = {1.0f, 1.0f, 0.9f, 0.8f, 0.6f, 0.5f};
	public float getDiscount() {
		return discount[rank];
	}
	
	public static float[] tongRate = {1.0f, 1.0f, 1.35f, 2.0f, 3.0f, 4.0f};
	/**
	 *  
	 * @author macq
	 * @explain：
	 * @datetime:2007-8-20 15:55:33
	 * @return
	 * @return float
	 */
	public float getTongRate() {
		return tongRate[rank];
	}
	
	public static String getRankName(int rank) {
		return rankName[rank];
	}

}
