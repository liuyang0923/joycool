/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.bean;

import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.util.UserBagCacheUtil;

/**
 * @author lbj
 * 
 */
public class UserStatusBean {
	int userId;

	int gamePoint;

	int point;

	int rank;

	int loginCount;

	String lastLoginTime;
	long lastLoginTime2;

	String lastLogoutTime;
	long lastLogoutTime2;

	long totalOnlineTime;
	int imagePathId;		// 帽子图片，<0为系统帽子，>0为玩家帽子

	int spirit;

	int pk;

	// 社交指数
	int social;

	int mark;
	int charitarian;

	int userBag;

	// mcq_2006-12-25_帮会判断字段_start
	int tong;

	// mcq_2006-12-25_帮会判断字段_end

	/**
	 * @return userBag
	 */
	public int getUserBag() {
		return userBag;
	}

	/**
	 * @param userBag
	 *            要设置的 userBag
	 */
	public void setUserBag(int userBag) {
		this.userBag = userBag;
	}
	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the pk.
	 */
	public int getPk() {
		return pk;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setPk(int pk) {
		this.pk = pk;
	}

	/**
	 * @return Returns the imagePathId.
	 */
	public int getImagePathId() {
		return imagePathId;
	}

	/**
	 * @param imagePathId
	 *            The imagePathId to set.
	 */
	public void setImagePathId(int imagePathId) {
		this.imagePathId = imagePathId;
	}

	// mcq_end 时间:2006-6-6
	/**
	 * @return Returns the point.
	 */
	public int getPoint() {
		return point;
	}

	/**
	 * @param point
	 *            The point to set.
	 */
	public void setPoint(int point) {
		this.point = point;
	}

	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	UserBean user;

	/**
	 * @return Returns the user.
	 */
	public UserBean getUser() {
		return user;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}

	/**
	 * @return Returns the gamePoint.
	 */
	public int getGamePoint() {
		return gamePoint;
	}

	/**
	 * @param gamePoint
	 *            The gamePoint to set.
	 */
	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/*
	 * mcq_start_增加数据库字段set\get方法 时间:2006-6-6
	 */
	/**
	 * @return Returns the lastLoginTime.
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 *            The lastLoginTime to set.
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return Returns the lastLogoutTime.
	 */
	public String getLastLogoutTime() {
		return lastLogoutTime;
	}

	/**
	 * @param lastLogoutTime
	 *            The lastLogoutTime to set.
	 */
	public void setLastLogoutTime(String lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	/**
	 * @return Returns the loginCount.
	 */
	public int getLoginCount() {
		return loginCount;
	}

	/**
	 * @param loginCount
	 *            The loginCount to set.
	 */
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	/**
	 * @return Returns the nicknameChange.
	 */
	public int getNicknameChange() {
		return 0;
	}

	/**
	 * @param nicknameChange
	 *            The nicknameChange to set.
	 */
	public void setNicknameChange(int nicknameChange) {
	}

	/**
	 * @return Returns the totalOnlineTime.
	 */
	public long getTotalOnlineTime() {
		return totalOnlineTime;
	}

	/**
	 * @param totalOnlineTime
	 *            The totalOnlineTime to set.
	 */
	public void setTotalOnlineTime(long totalOnlineTime) {
		this.totalOnlineTime = totalOnlineTime;
	}

	/*
	 * mcq_end_增加数据库字段set\get方法 时间:2006-6-6
	 */
	public String getLastLoginTimeStr() {
		String tempLastLoginTime = null;
		if (lastLoginTime != null) {
			if (lastLoginTime.indexOf("-") != -1) {
				tempLastLoginTime = lastLoginTime.substring(lastLoginTime
						.indexOf("-") + 1);
			}
			if (lastLoginTime.lastIndexOf(":") != -1) {
				tempLastLoginTime = lastLoginTime.substring(0, lastLoginTime
						.lastIndexOf(":"));
			}
		}
		return tempLastLoginTime;
	}

	/**
	 * @return Returns the spirit.
	 */
	public int getSpirit() {
		return spirit;
	}

	/**
	 * @param spirit
	 *            The spirit to set.
	 */
	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}

	/**
	 * @return Returns the social.
	 */
	public int getSocial() {
		return social;
	}

	/**
	 * @param social
	 *            The social to set.
	 */
	public void setSocial(int social) {
		this.social = social;
	}

	/**
	 * @return 返回 charitarian。
	 */
	public int getCharitarian() {
		return charitarian;
	}

	/**
	 * @param charitarian
	 *            要设置的 charitarian。
	 */
	public void setCharitarian(int charitarian) {
		this.charitarian = charitarian;
	}

	/**
	 * @return tong
	 */
	public int getTong() {
		return tong;
	}

	/**
	 * @param tong
	 *            要设置的 tong
	 */
	public void setTong(int tong) {
		this.tong = tong;
	}

	static ICacheMap userHatCache = CacheManage.userHat;
	
	public String getHatShow() {
		if(imagePathId == 0)
			return "";
		if(imagePathId < 0){
			StringBuilder extra = new StringBuilder();
			extra.append("<img src=\"/rep/lx/t");
			extra.append(-imagePathId);
			extra.append(".gif\" alt=\"o\" />");
			return extra.toString();
		}
		Integer key = Integer.valueOf(userId);
		synchronized(userHatCache) {
			String hatPath = (String) userHatCache.get(key);
			if(hatPath == null) {
				UserBagBean userBag = UserBagCacheUtil.getUserBagCache(imagePathId);
				if(userBag == null || userBag.getUserId() != userId){
					imagePathId = 0;
					userHatCache.rm(key);
					return "";
				}else{
					StringBuilder extra = new StringBuilder();
					extra.append("<img src=\"/rep/lx/e");
					extra.append(userBag.getProductId());
					extra.append(".gif\" alt=\"o\" />");
					hatPath=extra.toString();
				}
				userHatCache.put(key, hatPath);
			}
			return hatPath;
		}
	}
	public static String[] hatString = {"帽", "冠", "冠", "冠", "冠", "冠", "冠", "冠"
		, "荣", "恶", "赌", "赌", "赌", "赌", "管"};
	// 文本方式显示帽子
	public String getHatShowText() {
		if(imagePathId < 0){
			int imagePathId2 = -imagePathId;
			StringBuilder extra = new StringBuilder();
			extra.append('[');
			if(imagePathId2 < hatString.length)
				extra.append(hatString[imagePathId2]);
			else
				extra.append('帽');
			extra.append(']');
			return extra.toString();
		}
		return "";
	}
	
	public static void flushUserHat(int userId){
		Integer key = Integer.valueOf(userId);
		userHatCache.srm(key);
	}
	
	/**
	 * @param 判断行囊中能否增加一定数量的物品
	 * @return
	 */
	public boolean isBagFull(int add) {
		List friendUserBagList = UserBagCacheUtil.getUserBagListCacheById(userId);
		return friendUserBagList.size() + add > userBag;
	}
	
	public boolean isBagFull() {
		return isBagFull(1);
	}
	// 返回在线的小时数
	public int getOnlineTime() {
		return (int) (totalOnlineTime / 60);
	}
	
	static float otrate = 0.56f;
	static int markd = 4;
	
	public int getOTLevel() {
		return (int)Math.pow(totalOnlineTime / 300, otrate);
	}
	public String getOTString() {
		return getOTString(getOTLevel());
	}
	public static String getOTString(int level) {
		StringBuilder sb = new StringBuilder();
		int n1 = level / markd;
		if(n1 > 0) {
			level -= n1 * markd;
			int n2 = n1 / markd;
			if(n2 > 0) {
				n1 -= n2 * markd;
				for(int i = 0;i < n2;i++)
					sb.append('★');
			}
			for(int i = 0;i < n1;i++)
				sb.append('◆');
		}
		for(int i = 0;i < level;i++)
			sb.append('▲');
		return sb.toString();
	}

	public long getLastLoginTime2() {
		return lastLoginTime2;
	}

	public void setLastLoginTime2(long lastLoginTime2) {
		this.lastLoginTime2 = lastLoginTime2;
	}

	public long getLastLogoutTime2() {
		return lastLogoutTime2;
	}

	public void setLastLogoutTime2(long lastLogoutTime2) {
		this.lastLogoutTime2 = lastLogoutTime2;
	}
}
