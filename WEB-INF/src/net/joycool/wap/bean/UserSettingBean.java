package net.joycool.wap.bean;

import java.util.List;

import net.joycool.wap.util.StringUtil;

public class UserSettingBean {
	public static int FLAG_HIDE_LOGO = (1 << 0);	// 不显示部分logo
	public static int FLAG_HIDE_STAR = (1 << 1);	// 不显示星星
	public static int FLAG_HIDE_HAT = (1 << 2);	// 不显示聊天室帽子
	public static int FLAG_HIDE_FACE = (1 << 3);	// 不显示表情
	public static int FLAG_HIDE_SEND = (1 << 4);	// 不显示贴图
	
	public static int FLAG_XHTML_VIEW = (1 << 9);	// xhtml访问论坛
	public static int FLAG_TOPIC_DELETE = (1 << 10);	// 删除主题时不提示确认
	public static int FLAG_REPLY_DELETE = (1 << 11);	// 删除回复时不提示确认
	public static int FLAG_XHTML = (1 << 12);	// xhtml编辑
	public static int FLAG_VORDER = (1 << 13);	// 倒叙排列回复

	public static int FLAG_FRIEND_CONFIRM = (1 << 14);	// 加好友需要申请
	public static int FLAG_AUCTION_CONFIRM = (1 << 15);	// 竞价的时候需要申请
	public static int FLAG_SHOW_NOTE = (1 << 16);	// 显示好友列表中的备注
	
	int id = 0;

	int userId;

	int picMark = 0;		// 对应flag，用于各种而选一设置

	int noticeMark = 0;

	String bankPw = "";

	String updateDatetime;
	
	String shortcut = "";
	
	String shortcut2 = "";	//add by leihy 2008-12-19
	
	String bagSeq = "";

	int forumOrder = 0;		// 论坛排序，0为正，1为反
	int homeAllow = 0;		// 家园是否允许所有人看，0允许，1好友，2都不允许
	String homePassword = "";
	/**
	 * @return Returns the forumOrder.
	 */
	public int getForumOrder() {
		return forumOrder;
	}

	public void setForumOrder(int forumOrder) {
		this.forumOrder = forumOrder;
	}

	public int getHomeAllow() {
		return homeAllow;
	}

	public void setHomeAllow(int homeAllow) {
		this.homeAllow = homeAllow;
	}

	//默认的 shortcut = ",8,3,7,4," 改成了 shortcut = ",8,3,7,5,"
	public String getShortcut() {
		if(shortcut == null || shortcut.length() == 0)
			shortcut = ",5,3,78,4,";
		return shortcut;
	}
	
	

	public String getShortcut2() {
		if(shortcut2 == null || shortcut2.length() == 0)
			shortcut2 = ",5,3,78,4,";
		return shortcut2;
	}

	public void setShortcut2(String shortcut2) {
		this.shortcut2 = shortcut2;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public String getBankPw() {
		return bankPw;
	}

	public void setBankPw(String bankPw) {
		this.bankPw = bankPw;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNoticeMark() {
		return noticeMark;
	}

	public void setNoticeMark(int noticeMark) {
		this.noticeMark = noticeMark;
	}

	public int getPicMark() {
		return picMark;
	}

	public void setPicMark(int picMark) {
		this.picMark = picMark;
	}
	public boolean isFlagHideLogo() {
		return (picMark & FLAG_HIDE_LOGO) != 0;
	}
	public boolean isFlagHideStar() {
		return (picMark & FLAG_HIDE_STAR) != 0;
	}
	public boolean isFlagHideHat() {
		return (picMark & FLAG_HIDE_HAT) != 0;
	}
	public boolean isFlagHideFace() {
		return (picMark & FLAG_HIDE_FACE) != 0;
	}
	public boolean isFlagHideSend() {
		return (picMark & FLAG_HIDE_SEND) != 0;
	}
	public boolean isFlagTopicDelete() {
		return (picMark & FLAG_TOPIC_DELETE) != 0;
	}
	public boolean isFlagReplyDelete() {
		return (picMark & FLAG_REPLY_DELETE) != 0;
	}
	public boolean isFlagXhtml() {
		return (picMark & FLAG_XHTML) != 0;
	}
	public boolean isFlagVorder() {
		return (picMark & FLAG_VORDER) != 0;
	}
	public boolean isFlagAuctionConfirm() {
		return (picMark & FLAG_AUCTION_CONFIRM) != 0;
	}
	public boolean isFlagFriendConfirm() {
		return (picMark & FLAG_FRIEND_CONFIRM) != 0;
	}
	
	public boolean isFlag(int seq) {
		return (picMark & (1 << seq)) != 0;
	}
	public void setFlag(int flag, boolean set) {
		if(set)
			picMark |= (1 << flag);
		else
			picMark &= ~(1 << flag);
	}
	public void toggleFlag(int flag) {
		picMark ^= (1 << flag);
	}

	public String getUpdateDatetime() {
		return updateDatetime;
	}

	/**
	 * @param updateDatetime
	 *            要设置的 updateDatetime。
	 */
	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
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
	 * @return Returns the homePassword.
	 */
	public String getHomePassword() {
		return homePassword;
	}

	/**
	 * @param homePassword The homePassword to set.
	 */
	public void setHomePassword(String homePassword) {
		this.homePassword = homePassword;
	}

	public String getBagSeq() {
		if(bagSeq == null)
			return "";
		return bagSeq;
	}
	
	public List getBagSeqList(){
		String listStr = getBagSeq();
		
		return StringUtil.toInts(listStr);
	}

	public void setBagSeq(String bagSeq) {
		this.bagSeq = bagSeq;
	}

}
