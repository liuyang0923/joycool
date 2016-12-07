package net.joycool.wap.action.floor;
import java.sql.Timestamp;
/** 
 * @author guip
 * @explain：
 * @datetime:2007-8-29 10:24:31
 */
public class FloorTopBean {
int userId;
int count;
int id;
int floorId;
Timestamp createTime;
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
 * @return 返回 floorId。
 */
public int getFloorId() {
	return floorId;
}
/**
 * @param floorId 要设置的 floorId。
 */
public void setFloorId(int floorId) {
	this.floorId = floorId;
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
