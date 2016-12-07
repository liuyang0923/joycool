package net.joycool.wap.action.job.fish;

import java.util.Date;


/**
 * @author bomb
 *
 */
public class GlobalEventBean {
	int id;
	int areaId;			// 出现区域
	String beginDesc;	// 出现时的描述
	String endDesc;		// 结束时的描述
	int randChange;		// 改变该区域的几率
	long duration;		// 持续时间
	long createTime = 0;	// 出现时间
	boolean started = false;	// 该事件是否已经开始
	
	/**
	 * @return Returns the areaId.
	 */
	public int getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId The areaId to set.
	 */
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return Returns the beginDesc.
	 */
	public String getBeginDesc() {
		return beginDesc;
	}
	/**
	 * @param beginDesc The beginDesc to set.
	 */
	public void setBeginDesc(String beginDesc) {
		this.beginDesc = beginDesc;
	}
	/**
	 * @return Returns the endDesc.
	 */
	public String getEndDesc() {
		return endDesc;
	}
	/**
	 * @param endDesc The endDesc to set.
	 */
	public void setEndDesc(String endDesc) {
		this.endDesc = endDesc;
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
	 * @return Returns the randChange.
	 */
	public int getRandChange() {
		return randChange;
	}
	/**
	 * @param randChange The randChange to set.
	 */
	public void setRandChange(int randChange) {
		this.randChange = randChange;
	}

	/**
	 * 重置开始时间
	 */
	public void reset() {
		createTime = new Date().getTime();
		started = true;
	}
	
	/**
	 * cur : 传入当前判断时间
	 * @return 判断事件是否过期
	 */
	public boolean isOver(long cur) {
		return cur - createTime > duration;
	}
	/**
	 * @return Returns the duration.
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * @param duration The duration to set.
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	/**
	 * @return Returns the started.
	 */
	public boolean isStarted() {
		return started;
	}
	/**
	 * @param started The started to set.
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}
}
