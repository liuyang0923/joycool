package jc.guest;

/**
 * 用户关注
 */
public class UserFocus {
	int id;
	int leftUid;
	int rightUid;
	long createTime;
	int flag;	
	
	public UserFocus(){
	
	}
	
	public UserFocus(int leftUid,int rightUid){
		this.leftUid = leftUid;
		this.rightUid = rightUid;
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
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
