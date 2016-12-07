package net.joycool.wap.spec.garden;

public class GardenUserSeedBean {

	int id;
	int uid;
	int seedId;
	int count;
	int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public GardenUserSeedBean() {
	}

	public GardenUserSeedBean(int uid, int seedId) {
		this.uid = uid;
		this.seedId = seedId;
	}
	
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
	public int getSeedId() {
		return seedId;
	}
	public void setSeedId(int seedId) {
		this.seedId = seedId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
