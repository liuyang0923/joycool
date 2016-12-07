package net.joycool.wap.bean;

import java.sql.Timestamp;

/** 
 * @author guip
 * @explain：
 * @datetime:2007-8-27 16:33:43
 */
public class FloorBean {

	/** 
	 * @author guip
	 * @explain：
	 * @datetime:2007-8-27 16:33:43
	 * @param args
	 * @return void
	 */
int id;
int userId;
String content;
int prize;
int number;
int floor;
Timestamp createTime;
int count;
int mark ; 
int nowPrize;
/**
 * @return 返回 nowPrize。
 */
public int getNowPrize() {
	return nowPrize;
}
/**
 * @param nowPrize 要设置的 nowPrize。
 */
public void setNowPrize(int nowPrize) {
	this.nowPrize = nowPrize;
}
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
 * @return 返回 count。
 */
public int getCount() {
	return count;
}
/**
 * @param count 要设置的 count。
 */
public void setCount(int count) {
	this.count = count;
}
/**
 * @return 返回 createTime。
 */
public Timestamp getCreateTime() {
	return createTime;
}
/**
 * @param createTime 要设置的 createTime。
 */
public void setCreateTime(Timestamp createTime) {
	this.createTime = createTime;
}
/**
 * @return 返回 floor。
 */
public int getFloor() {
	return floor;
}
/**
 * @param floor 要设置的 floor。
 */
public void setFloor(int floor) {
	this.floor = floor;
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
 * @return 返回 number。
 */
public int getNumber() {
	return number;
}
/**
 * @param number 要设置的 number。
 */
public void setNumber(int number) {
	this.number = number;
}
/**
 * @return 返回 prize。
 */
public int getPrize() {
	return prize;
}
/**
 * @param prize 要设置的 prize。
 */
public void setPrize(int prize) {
	this.prize = prize;
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
