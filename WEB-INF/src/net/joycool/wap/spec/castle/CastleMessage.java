package net.joycool.wap.spec.castle;

import java.util.Date;

public class CastleMessage {
	
	private int id;
	private int uid;
	private String content;
	private Date time;
	int tongId;	// 联盟id
	int type;		// 0 普通 1 运输 2 战斗 3 支援 4 支援被攻击
	String detail = "";		// 详细战报，带wml标签
	int pos;	// 默认为0，表示这个消息对应的坐标x y to pos
	
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public boolean hasDetail() {
		return type != 0;
	}
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getTongId() {
		return tongId;
	}

	public void setTongId(int tongId) {
		this.tongId = tongId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public CastleMessage(String content, int uid) {

		this.content = content;
		this.uid = uid;
	}
	
	public CastleMessage(String content, int id, Date time, int uid) {

		this.content = content;
		this.id = id;
		this.time = time;
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	
}
