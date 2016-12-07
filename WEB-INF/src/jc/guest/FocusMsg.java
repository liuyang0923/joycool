package jc.guest;

import net.joycool.wap.util.StringUtil;

/**
 * 验证关注信息日志
 */
public class FocusMsg {
	int id;
	int leftUid;	// 发出验证的人
	int rightUid;	// 验证人
	String content;	// 内容
	int readed;		// 0:未读,1:已读
	long createTime;
	
	public FocusMsg(){
		
	}
	
	public FocusMsg(int uid1,int uid2,String str){
		this.leftUid = uid1;
		this.rightUid = uid2;
		this.content = str;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLeftUid() {
		return leftUid;
	}
	public void setLeftUid(int leftUid) {
		this.leftUid = leftUid;
	}
	public int getRightUid() {
		return rightUid;
	}
	public void setRightUid(int rightUid) {
		this.rightUid = rightUid;
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
	public int getReaded() {
		return readed;
	}
	public void setReaded(int readed) {
		this.readed = readed;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
