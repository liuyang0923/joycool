package jc.credit;

public class UserContacts {
	int userId;
	String contacts;
	String idCard;
	String address;
	String trueName;
	int privateLevel;
	int point;
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public int getPrivateLevel() {
		return privateLevel;
	}
	public void setPrivateLevel(int privateLevel) {
		this.privateLevel = privateLevel;
	}
}
