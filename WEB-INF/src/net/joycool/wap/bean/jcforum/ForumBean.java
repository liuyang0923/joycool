package net.joycool.wap.bean.jcforum;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.joycool.wap.util.StringUtil;

public class ForumBean {
	int id;

	String title;

	int totalCount;		// 总帖子
	int primeCount;		// 精华总数
	int todayCount;		// 今日总帖

	String description;

	String userId;
	String rule;

	int mark;

	int tongId;

	String badUser = "";

	Set userIdSet = new HashSet();

	Set badUserSet = new HashSet();

	int type = 0;
	int primeCat;		// 分类精华的根目录id

	public int getPrimeCat() {
		return primeCat;
	}

	public void setPrimeCat(int primeCat) {
		this.primeCat = primeCat;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the todayCount.
	 */
	public int getTodayCount() {
		return todayCount;
	}

	/**
	 * @param todayCount
	 *            The todayCount to set.
	 */
	public void setTodayCount(int todayCount) {
		this.todayCount = todayCount;
	}

	/**
	 * @return Returns the totalCount.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            The totalCount to set.
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the tongId.
	 */
	public int getTongId() {
		return tongId;
	}

	/**
	 * @param tongId
	 *            The tongId to set.
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}

	public String getBadUser() {
		return badUser;
	}

	public void setBadUser(String badUser) {
		this.badUser = badUser;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 转换String到Set
	 * @datetime:2007-5-24 7:02:16
	 * @return
	 * @return Set
	 */
	public Set getBadUserSet() {
		String[] badUserArray = badUser.split("_");
		for (int i = 0; i < badUserArray.length; i++) {
			String string = badUserArray[i];
			if (StringUtil.toInt(string) != -1) {
				badUserSet.add(new Integer(string));
			}
		}
		return badUserSet;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 转换String到Set
	 * @datetime:2007-5-24 7:02:16
	 * @return
	 * @return Set
	 */
	public Set getUserIdSet() {
		String[] userIdArray = userId.split("_");
		for (int i = 0; i < userIdArray.length; i++) {
			String string = userIdArray[i];
			if (StringUtil.toInt(string) != -1) {
				userIdSet.add(new Integer(string));
			}
		}
		return userIdSet;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 转换Set到特殊字符串
	 * @datetime:2007-5-24 7:01:53
	 * @return
	 * @return Sting
	 */
	public String setToString(Set set) {
		StringBuffer sb = new StringBuffer();
		Iterator it = set.iterator();
		int size = set.size();
		int count = 0;
		while (it.hasNext()) {
			Integer userId = (Integer) it.next();
			sb.append(userId.toString());
			if (count + 1 < size) {
				sb.append("_");
			}
			count++;
		}
		return sb.toString();
	}

	public int getPrimeCount() {
		return primeCount;
	}

	public void setPrimeCount(int primeCount) {
		this.primeCount = primeCount;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
}
