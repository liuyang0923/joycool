/*
 *作者:mcq
 *日期:2006-6-26
 *功能:聊天室基础bean 
 */
package net.joycool.wap.bean.chat;

public class RoomUserBean {
	int id;

	int roomId;

	int userId;

	int ManagerId;

	String grantDatetime;

	int status;

	String applyDatetime;
/**
 * @return Returns the applyDatetime.
 */
public String getApplyDatetime() {
	return applyDatetime;
}
/**
 * @param applyDatetime The applyDatetime to set.
 */
public void setApplyDatetime(String applyDatetime) {
	this.applyDatetime = applyDatetime;
}
/**
 * @return Returns the grantDatetime.
 */
public String getGrantDatetime() {
	return grantDatetime;
}
/**
 * @param grantDatetime The grantDatetime to set.
 */
public void setGrantDatetime(String grantDatetime) {
	this.grantDatetime = grantDatetime;
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
 * @return Returns the managerId.
 */
public int getManagerId() {
	return ManagerId;
}
/**
 * @param managerId The managerId to set.
 */
public void setManagerId(int managerId) {
	ManagerId = managerId;
}
/**
 * @return Returns the roomId.
 */
public int getRoomId() {
	return roomId;
}
/**
 * @param roomId The roomId to set.
 */
public void setRoomId(int roomId) {
	this.roomId = roomId;
}
/**
 * @return Returns the status.
 */
public int getStatus() {
	return status;
}
/**
 * @param status The status to set.
 */
public void setStatus(int status) {
	this.status = status;
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
}
