package net.joycool.wap.bean.item;
/** 
 * @author guip
 * @explain：
 * @datetime:2007-9-18 14:39:51
 */
public class UserItemLogBean {
int id;
int userId;
int toUserId;
int itemId;
int userBagId;
int type;
int stack;
String Time;
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
 * @return 返回 itemId。
 */
public int getItemId() {
	return itemId;
}
/**
 * @param itemId 要设置的 itemId。
 */
public void setItemId(int itemId) {
	this.itemId = itemId;
}
/**
 * @return 返回 time。
 */
public String getTime() {
	return Time;
}
/**
 * @param time 要设置的 time。
 */
public void setTime(String time) {
	Time = time;
}
/**
 * @return 返回 toUserId。
 */
public int getToUserId() {
	return toUserId;
}
/**
 * @param toUserId 要设置的 toUserId。
 */
public void setToUserId(int toUserId) {
	this.toUserId = toUserId;
}
/**
 * @return 返回 type。
 */
public int getType() {
	return type;
}
/**
 * @param type 要设置的 type。
 */
public void setType(int type) {
	this.type = type;
}
/**
 * @return 返回 userBagId。
 */
public int getUserBagId() {
	return userBagId;
}
/**
 * @param userBagId 要设置的 userBagId。
 */
public void setUserBagId(int userBagId) {
	this.userBagId = userBagId;
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
/**
 * @return Returns the stack.
 */
public int getStack() {
	return stack;
}
/**
 * @param stack The stack to set.
 */
public void setStack(int stack) {
	this.stack = stack;
}
}
