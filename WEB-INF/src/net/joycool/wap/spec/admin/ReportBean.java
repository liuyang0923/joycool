package net.joycool.wap.spec.admin;

public class ReportBean {
	public static String[] typesName = {"其他", "用户", "帖子", "聊天", "新建", "日记"};
	
	int id;
	int userId;
	int type;
	int status;
	
	int cid;
	int cid2;
		
	String bak;	// 保存部分举报内容，例如论坛的话保存部分内容，用户的话保存昵称
	String info;	// 用户提供的文字信息和说明
	String reason;	// 用户选择下拉框里的举报原因
	long create_time;
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getCid2() {
		return cid2;
	}
	public void setCid2(int cid2) {
		this.cid2 = cid2;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}	


}
