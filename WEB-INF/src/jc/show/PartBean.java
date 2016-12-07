package jc.show;

public class PartBean {
	int id;
	int lvlLayer;
	int lvlShow;
	String name;
	String bak;
	int index;	// 数组索引

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLvlLayer() {
		return lvlLayer;
	}

	public void setLvlLayer(int lvlLayer) {
		this.lvlLayer = lvlLayer;
	}

	public int getLvlShow() {
		return lvlShow;
	}

	public void setLvlShow(int lvlShow) {
		this.lvlShow = lvlShow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}
}
