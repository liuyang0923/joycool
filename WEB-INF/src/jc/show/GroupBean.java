package jc.show;


/**
 * 酷秀组合
 * @author maning
 */
public class GroupBean {
	int id;
	String name;
	String itemIds;	// ID列表.以英文逗号分隔.
	int uid;
	int due;	// 过期天数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemIds() {
		return itemIds;
	}
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getDue() {
		return due;
	}
	public void setDue(int due) {
		this.due = due;
	}
}
