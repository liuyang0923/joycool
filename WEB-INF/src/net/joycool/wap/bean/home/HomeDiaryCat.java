package net.joycool.wap.bean.home;

public class HomeDiaryCat {

	public static final int PRIVACY_SELF = 0;
	public static final int PRIVACY_FRIEND = 5;
	public static final int PRIVACY_ALL = 9;
	
	int id;
	String catName;
	int uid;
	int count;
	int privacy;
	int def;
	
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrivacy() {
		return privacy;
	}
	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}
	
	public String getPrivacyStr() {
		if(this.privacy == PRIVACY_SELF) {
			return "隐藏";
		}else if(this.privacy == PRIVACY_FRIEND){
			return "好友可见";
		}else if(this.privacy == PRIVACY_ALL) {
			return "所有可见";
		}
		
		return null;
	}
	public boolean canRead(boolean friend, boolean self) {
		return self || friend && privacy == PRIVACY_FRIEND || privacy == PRIVACY_ALL;
	}
}
