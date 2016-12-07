package jc.family.game.snow;

public class SnowGameToolsBean {

	private int id;// 唯一标识
	private int fmId;// 拥有它的家族
	private int tid;// 道具类型id
	// private int type;// 道具类别:1.投的2.清扫的
	private int usedTime;// 已用次数
	private static int countId;// 

	public SnowGameToolsBean(int usedTime) {
		SnowGameToolsBean.countId = SnowGameToolsBean.countId + 1;
		this.usedTime = usedTime;
	}

	public SnowGameToolsBean() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFmId() {
		return fmId;
	}

	public void setFmId(int fmId) {
		this.fmId = fmId;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	/*
	 * public int getType() { return type; }
	 * 
	 * public void setType(int type) { this.type = type; }
	 */

	public int getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(int usedTime) {
		this.usedTime = usedTime;
	}

	public static int getCountId() {
		return countId;
	}

	public static void setCountId(int countId) {
		SnowGameToolsBean.countId = countId;
	}

}
