package jc.show;

public class History {
	private int id;
	private int uid;// 付费者
	private int touid;// 收礼者
	private int iid;// item_id
	private long createtime;// 增加时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getTouid() {
		return touid;
	}

	public void setTouid(int touid) {
		this.touid = touid;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
}
