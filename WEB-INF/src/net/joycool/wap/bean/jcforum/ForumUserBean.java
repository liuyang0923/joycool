package net.joycool.wap.bean.jcforum;

import net.joycool.wap.util.DateUtil;

public class ForumUserBean {
	public static int MAX_RANK = 18;
	
	public static int SIGNATURE_ITEM = 5070;
	
	int mark;	// 暂时没有用
	int userId;
	int exp;			// 经验值
	int rank;			// 级别，0表示0级，升级所需要的经验值：10 40 90 160 250...
	int point;			// 积分，暂时没有用
	int threadCount;	// 发贴数
	int replyCount;		// 回复数
	int mForumId;		// 某个版面的版主
	String info;		// 此人的额外信息，显示在信息页
	long vip;			//论坛vip
	String signature;	//个性签名
	
	public boolean isVip(){
		return vip > System.currentTimeMillis();
	}
	
	public String getVipLeft(){
		int left = (int)((vip - System.currentTimeMillis()) / 1000);
		return DateUtil.formatTimeInterval(left);
	}
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public long getVip() {
		return vip;
	}
	public void setVip(long vip) {
		this.vip = vip;
	}
	/**
	 * @return Returns the info.
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * @param info The info to set.
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * @return Returns the mForumId.
	 */
	public int getMForumId() {
		return mForumId;
	}
	/**
	 * @param forumId The mForumId to set.
	 */
	public void setMForumId(int forumId) {
		mForumId = forumId;
	}
	/**
	 * @return Returns the exp.
	 */
	public int getExp() {
		return exp;
	}
	/**
	 * @param exp The exp to set.
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}
	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}
	/**
	 * @param mark The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}
	/**
	 * @return Returns the point.
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point The point to set.
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
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * @return Returns the replyCount.
	 */
	public int getReplyCount() {
		return replyCount;
	}
	/**
	 * @param replyCount The replyCount to set.
	 */
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	/**
	 * @return Returns the threadCount.
	 */
	public int getThreadCount() {
		return threadCount;
	}
	/**
	 * @param threadCount The threadCount to set.
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
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

	public String getRankName() {
		if(!isVip())
			return getRankName(rank);
		else 
			return "<img src=\"/jcforum/img/"+getRankImg(rank)+".gif\" alt=\""+ getRankImg(rank) + "\"/>";
	}

	public static String[] rankNames = {"", "樱桃", "葡萄", "草莓", "杨梅", "荔枝", "桔子", "杨桃", "苹果", "芒果", "蟠桃", "柚子", "菠萝", "椰子", "西瓜", "菠萝蜜", "恶魔果", "天使果", "果神"};
	
	public static String[] rankImgs = {"yingtao","yingtao","putao","caomei","yangmei","lizhi","juzi","yangtao","pingguo","mangguo","pantao","youzi","boluo","yezi","xiguan","boluomi","emoguo", "tianshiguo", "guoshen"};
	
	public static String getRankImg(int rank) {
		if(rank < 0 || rank > MAX_RANK)
			rank = 0;
		return rankImgs[rank];
	}
	
	public static String getRankName(int rank) {
		if(rank < 0 || rank > MAX_RANK)
			rank = 0;
		return rankNames[rank];
	}
	
	public static int[] rankExps = {10, 40, 90, 160, 250, 360, 490, 640, 810, 1000, 1500, 2500, 5000, 10000, 20000, 35000, 60000, 100000};
	
	public void addExp(int add) {
		exp += add;
		if(rank < MAX_RANK && exp >= rankExps[rank])
			rank++;
	}
	public void addThreadCount() {
		addExp(3);
		threadCount++;
	}
	public void addReplyCount() {
		addExp(1);
		replyCount++;
	}
	public void decExp(int dec) {
		if(exp <= dec)
			exp = 0;
		else
			exp -= dec;
	}
}
