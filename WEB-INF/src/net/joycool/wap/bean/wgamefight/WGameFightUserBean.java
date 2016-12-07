package net.joycool.wap.bean.wgamefight;
/** 
 * @author guip
 * @explain：
 * @datetime:2007-10-23 11:41:14
 */
public class WGameFightUserBean {
int id;
int userId;
int groupId;
String content;
int mark;
/**
 * @return 返回 mark。
 */
public int getMark() {
	return mark;
}
/**
 * @param mark 要设置的 mark。
 */
public void setMark(int mark) {
	this.mark = mark;
}
/**
 * @return 返回 content。
 */
public String getContent() {
	return content;
}
/**
 * @param content 要设置的 content。
 */
public void setContent(String content) {
	this.content = content;
}
/**
 * @return 返回 groupId。
 */
public int getGroupId() {
	return groupId;
}
/**
 * @param groupId 要设置的 groupId。
 */
public void setGroupId(int groupId) {
	this.groupId = groupId;
}
/**
 * @return 返回 id。
 */
public int getId() {
	return id;
}
/**
 * @param id 要设置的 id。
 */
public void setId(int id) {
	this.id = id;
}
/**
 * @return 返回 userId。
 */
public int getUserId() {
	return userId;
}
/**
 * @param userId 要设置的 userId。
 */
public void setUserId(int userId) {
	this.userId = userId;
}
}
