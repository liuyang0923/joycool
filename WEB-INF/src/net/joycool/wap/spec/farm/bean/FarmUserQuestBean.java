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
 * @explain： 任务实例
 * @datetime:1007-10-24
 */
public class FarmUserQuestBean {
	
	int id;
	int questId;
	int npcId;		// 从哪个npc接的任务
	int userId;
	long startTime;
	long doneTime;
	int status = 0;		 // 0 已接 1 已完成

	public boolean isFinished() {		// 已经完成
		return status == 1;
	}
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
	 * @return Returns the flag.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param flag The flag to set.
	 */
	public void setStatus(int status) {
		this.status = status;
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
	 * @return Returns the questId.
	 */
	public int getQuestId() {
		return questId;
	}
	/**
	 * @param questId The questId to set.
	 */
	public void setQuestId(int questId) {
		this.questId = questId;
	}
	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
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
	 * @return Returns the npcId.
	 */
	public int getNpcId() {
		return npcId;
	}
	/**
	 * @param npcId The npcId to set.
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	
}
