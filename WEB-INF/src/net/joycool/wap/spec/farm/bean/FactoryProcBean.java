package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 加工厂处理物品
 * @datetime:1007-10-24
 */
public class FactoryProcBean {
	int id;
	int userId;
	int factoryId;
	int composeId;				// 合成公式
	long createTime;			// 加入工厂的时间
	long doneTime;				// 加工完成的时间
	int status;					// 处理状态 0 未处理 1 处理完毕 2 领取完毕
	/**
	 * @return Returns the doneTime.
	 */
	public long getDoneTime() {
		return doneTime;
	}
	/**
	 * @param doneTime The doneTime to set.
	 */
	public void setDoneTime(long doneTime) {
		this.doneTime = doneTime;
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
	 * @return Returns the composeId.
	 */
	public int getComposeId() {
		return composeId;
	}
	/**
	 * @param composeId The composeId to set.
	 */
	public void setComposeId(int composeId) {
		this.composeId = composeId;
	}
	/**
	 * @return Returns the createTime.
	 */
	public long getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime The createTime to set.
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	/**
	 * @return Returns the factoryId.
	 */
	public int getFactoryId() {
		return factoryId;
	}
	/**
	 * @param factoryId The factoryId to set.
	 */
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
}
