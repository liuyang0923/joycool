package jc.guest;

import net.joycool.wap.util.StringUtil;

public class GuestChat {
	int id;
	int gid1;
	String ncName1;
	int gid2;
	String ncName2;
	String content;
	long createTime;
	int del;
	int flag;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNcName1() {
		return ncName1;
	}
	public String getNcNameWml1() {
		return StringUtil.toWml(ncName1);
	}
	public void setNcName1(String ncName1) {
		this.ncName1 = ncName1;
	}
	public String getNcName2() {
		return ncName2;
	}
	public String getNcNameWml2() {
		return StringUtil.toWml(ncName2);
	}
	public void setNcName2(String ncName2) {
		this.ncName2 = ncName2;
	}
	public int getGid1() {
		return gid1;
	}
	public void setGid1(int gid1) {
		this.gid1 = gid1;
	}
	public int getGid2() {
		return gid2;
	}
	public void setGid2(int gid2) {
		this.gid2 = gid2;
	}
	public String getContent() {
		return content;
	}
	public String getContentWml() {
		return StringUtil.toWml(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getDel() {
		return del;
	}
	public void setDel(int del) {
		this.del = del;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}