package jc.guest.wall;

import net.joycool.wap.util.StringUtil;

public class WallBean {
	int id;
	String title;		// 标题
	int uid;			
	String shortCont;	// 简版
	String content;		// 原文
	long createTime;
	String createTimeStr;
	int visible;		// 是否显示。0:隐藏,1:显示
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public String getTitleWml() {
		return StringUtil.toWml(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getShortCont() {
		return shortCont;
	}
	public String getShortContWml() {
		return StringUtil.toWml(shortCont);
	}
	public void setShortCont(String shortCont) {
		this.shortCont = shortCont;
	}
	public String getContent() {
		return content;
	}
	public String getContentWml() {
		return StringUtil.toWml(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	/**
	 * 获得当前显示状态的字符串。
	 * @return
	 */
	public String getVisibleStr(){
		return visible==0?"隐藏":"显示";
	}
	/**
	 * 更改显示状态。
	 * @return<br/>返回改变后的状态。
	 */
	public int changeVisible(){
		this.visible = (this.visible==0?1:0);
		return this.visible;
	}
}
