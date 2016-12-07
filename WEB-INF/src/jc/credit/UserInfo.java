package jc.credit;

public class UserInfo {
	int userId;
	String mobile;
	int point;
	int intactPoint;
	int phonePoint;
	int idCardPoint;
	int netPoint;
	int conPoint;
	int playerPoint;
	int playerCount;
	long createTime;
	long modifyTime;
	int flag;
	int totalPoint;
	String users;
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public int getPlayerCount() {
		return playerCount;
	}
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
	public int getTotalPoint(){
		return intactPoint + phonePoint + idCardPoint + netPoint + conPoint + (playerCount != 0 ? playerPoint / playerCount : 0);
	}
	public long getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}
	public int getIntactPoint() {
		return intactPoint;
	}
	public void setIntactPoint(int intactPoint) {
		this.intactPoint = intactPoint;
	}
	public int getPhonePoint() {
		return phonePoint;
	}
	public void setPhonePoint(int phonePoint) {
		this.phonePoint = phonePoint;
	}
	public int getIdCardPoint() {
		return idCardPoint;
	}
	public void setIdCardPoint(int idCardPoint) {
		this.idCardPoint = idCardPoint;
	}
	public int getNetPoint() {
		return netPoint;
	}
	public void setNetPoint(int netPoint) {
		this.netPoint = netPoint;
	}
	public int getConPoint() {
		return conPoint;
	}
	public void setConPoint(int conPoint) {
		this.conPoint = conPoint;
	}
	public int getPlayerPoint() {
		return playerPoint;
	}
	public void setPlayerPoint(int playerPoint) {
		this.playerPoint = playerPoint;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
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
