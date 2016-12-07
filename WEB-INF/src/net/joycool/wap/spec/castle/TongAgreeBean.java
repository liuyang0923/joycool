package net.joycool.wap.spec.castle;


public class TongAgreeBean {
	public final static int AGREE_ALLY = 1;
	public final static int AGREE_NAP = 2;
	public final static int AGREE_WAR = 3;
	
	public static String[] typeNames = {"", "同盟", "不侵略", "宣战"};
	
	int id;
	int tongId;		// 结盟双方的id，前者为发起方
	int tongId2;
	int type;		// 1 联盟 2 不侵略 3 宣战
	public TongAgreeBean() {
		
	}
	public TongAgreeBean(int tongId, int tongId2, int type) {
		this.tongId = tongId;
		this.tongId2 = tongId2;
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTongId() {
		return tongId;
	}
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
	public int getTongId2() {
		return tongId2;
	}
	public void setTongId2(int tongId2) {
		this.tongId2 = tongId2;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeNames[type];
	}
}
