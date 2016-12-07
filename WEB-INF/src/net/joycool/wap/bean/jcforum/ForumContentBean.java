package net.joycool.wap.bean.jcforum;

import java.util.Date;

import net.joycool.wap.util.DateUtil;

public class ForumContentBean {
	
	
	public static int NICK_ITEM = 5141;	//匿名主题卡id
	
	public static int LOCK_ITEM = 5145;//锁帖卡
	
	public static int SPARK_ITEM = 5140;	//论坛闪耀卡
	
	public static int SPARK_BIT_LENGTH = 4;	//mark2 要移动的位数表示闪耀卡的类型
	
	//mark2 的第四位表示 是否是匿名帖子
	public static final int NICK_TOPIC = 1 << 3;	//匿名帖子
	
	public static final int PEAK_TOPIC = 1 << 0;		//置顶帖子
	
	public static final int TOP_PEAK_TOPIC = 1 << 1;	//总置顶
	public static final int NEWS_TOPIC = 1 << 2;		//新闻贴
	
	//mark2 的5~8位表示闪耀帖子
	public static String[] vipTypes = {"","闪","强","乐","酷","秀","特","灌","游",
										"潜","楼","票","原","转","","","","","","","","","","","","","","","","",""};
	
	int id;

	int forumId;

	String title;

	String content;

	int userId;

	int reply;

	int count;

	int mark1;	// 1为精华帖

	int mark2;	// 1为置顶帖
	int readonly;

	Date createTime;
	
	int type = 0;	// 0 普通帖子 1 单选投票 2 打分

	int tongId;

	// 删除日记标志
	int delMark;

	// 删除日记用户
	int dUserId;

	// 最后回复时间
	long lastReTime;

	// 图片附件
	String attach="";
	
	public void setTopPeak(){
		mark2 = mark2 | TOP_PEAK_TOPIC;
	}
	
	public void cancelTopPeak(){
		mark2 = mark2 & (~TOP_PEAK_TOPIC);
	}
	
	public boolean isTopPeak(){
		return (mark2 & TOP_PEAK_TOPIC) != 0;
	}
	
	public void setNews(){
		mark2 = mark2 | NEWS_TOPIC;
	}
	
	public void cancelNews(){
		mark2 = mark2 & (~NEWS_TOPIC);
	}
	
	public boolean isNews(){
		return (mark2 & NEWS_TOPIC) != 0;
	}
	
	public boolean isPeak(){
		return (mark2 & PEAK_TOPIC) != 0;
	}
	
	public void cancelPeak(){
		mark2 = mark2 & (~PEAK_TOPIC);
	}
	
	public void setPeak(){
		mark2 = mark2 | PEAK_TOPIC;
	}
	
	public boolean isNickTopic(){
		return (mark2 & NICK_TOPIC) != 0;
	}
	
	public int getVipType(){
		return (mark2 >> SPARK_BIT_LENGTH) & 0x1F;
	}
	
	public String getVipTypeStr() {
		return vipTypes[getVipType()];
	}
	
	public void setNickTopic(){
		mark2 = mark2 | NICK_TOPIC;
	}
	
	public void setVipType(int vipType) {
		mark2 = mark2 | (vipType << SPARK_BIT_LENGTH);
	}

	public long getLastReTime() {
		return lastReTime;
	}

	public void setLastReTime(long lastReTime) {
		this.lastReTime = lastReTime;
	}

	public int getDelMark() {
		return delMark;
	}

	public void setDelMark(int delMark) {
		this.delMark = delMark;
	}

	public int getDUserId() {
		return dUserId;
	}

	public void setDUserId(int userId) {
		dUserId = userId;
	}

	/**
	 * @return 返回 tongId。
	 */
	public int getTongId() {
		return tongId;
	}

	/**
	 * @param tongId
	 *            要设置的 tongId。
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public String getCreateDatetime() {
		return DateUtil.sformatTime(createTime);
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return Returns the forumId.
	 */
	public int getForumId() {
		return forumId;
	}

	/**
	 * @param forumId
	 *            The forumId to set.
	 */
	public void setForumId(int forumId) {
		this.forumId = forumId;
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
	 * @return Returns the mark1.
	 */
	public int getMark1() {
		return mark1;
	}

	/**
	 * @param mark1
	 *            The mark1 to set.
	 */
	public void setMark1(int mark1) {
		this.mark1 = mark1;
	}

	/**
	 * @return Returns the mark2.
	 */
	public int getMark2() {
		return mark2;
	}

	/**
	 * @param mark2
	 *            The mark2 to set.
	 */
	public void setMark2(int mark2) {
		this.mark2 = mark2;
	}

	/**
	 * @return Returns the reply.
	 */
	public int getReply() {
		return reply;
	}

	/**
	 * @param reply
	 *            The reply to set.
	 */
	public void setReply(int reply) {
		this.reply = reply;
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

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		if(attach.length() != 0 && !attach.startsWith("/"))
			this.attach = "/jcforum/" + attach;
		else
			this.attach = attach;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return Returns the readonly.
	 */
	public int getReadonly() {
		return readonly;
	}

	/**
	 * @param readonly The readonly to set.
	 */
	public void setReadonly(int readonly) {
		this.readonly = readonly;
	}

	public boolean isReadonly() {
		return readonly == 1;
	}
	
	public static long TOO_OLD = 3600000l * 24 * 180;
	// 太老的帖子不允许回复
	public boolean isTooOld() {
		return System.currentTimeMillis() - lastReTime > TOO_OLD;
	}
}
