package jc.family.game.yard;

/**
 * 物品
 * 
 * @author qiuranke
 * 
 */
public class YardItemBean {

	int id;
	int fmid;
	int itemId;
	int number;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFmid() {
		return fmid;
	}

	public void setFmid(int fmid) {
		this.fmid = fmid;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itmeId) {
		this.itemId = itmeId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void addNumber(int i) {
		number += i;
		if (number < 0)
			number = 0;
	}

	public int getItemType() {
		YardItemProtoBean yP = YardAction.getItmeProto(itemId);
		return yP != null ? yP.getType() : 0;
	}

	public String getItemNameWml() {
		YardItemProtoBean yP = YardAction.getItmeProto(itemId);
		return yP != null ? yP.getNameWml() : "";
	}

	public String getItemName() {
		YardItemProtoBean yP = YardAction.getItmeProto(itemId);
		return yP != null ? yP.getName() : "";
	}

	public int getItemPrice(){
		YardItemProtoBean yP = YardAction.getItmeProto(itemId);
		return yP.getPrice();
	}
	
	public int getItemRank(){
		YardItemProtoBean yP = YardAction.getItmeProto(itemId);
		return yP.getRank();
	}
}
