package net.joycool.wap.action.job.punch;


/**
 * @author bomb
 *
 */
public class PunchUserBean {
	long actionTime;
	int room = 0;
	int[] num = {0, 0};	// 打的小强个数和老鼠个数
	/**
	 * @return Returns the actionTime.
	 */
	public long getActionTime() {
		return actionTime;
	}
	/**
	 * @param actionTime The actionTime to set.
	 */
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}
	/**
	 * @return Returns the room.
	 */
	public int getRoom() {
		return room;
	}
	/**
	 * @param room The room to set.
	 */
	public void setRoom(int room) {
		this.room = room;
	}
	/**
	 * @return Returns the num.
	 */
	public int[] getNum() {
		return num;
	}
	/**
	 * @param num The num to set.
	 */
	public void addNum(int type) {
		num[type]++;
	}
}
