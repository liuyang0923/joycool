package net.joycool.wap.spec.rich;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouj
 * @explain： 大富翁，建筑物（一排街道，或者商业用地）
 * @datetime:1007-10-24
 */
public class RichBuilding {
	
	int id;
	int type;		// 0 住宅 1 商业用地（多格子是一起）
	int price;		// 价格
	String name;	// 建筑名称
	public List nodeList = new ArrayList();	// 这个建筑对应的结点列表
	
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the price.
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price The price to set.
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	public int getRent(int level) {
		return HouseBean.rentPrices[level] * price;
	}
	public void addLevel(int value) {
		for(int i = 0;i < nodeList.size();i++) {
			RichNodeBean node = (RichNodeBean)nodeList.get(i);
			if(!node.house.noOwner())
				node.house.addLevel(value);
		}
		
	}
	/**
	 * 连锁租金，一排街道都算
	 * @return
	 */
	public int getRents(RichUserBean richUser) {
		int rent = 0;
		for(int i = 0;i < nodeList.size();i++) {
			RichNodeBean node = (RichNodeBean)nodeList.get(i);
			HouseBean house = node.getHouse();
			if(house.getOwner() == richUser)
				rent += getRent(house.getLevel());
		}
		return rent;
		
	}
	
//	public void addBusiness(RichUserBean user, int type) {
//		for(int i = 0;i < nodeList.size();i++) {
//			RichNodeBean node = (RichNodeBean)nodeList.get(i);
//			node.addBusiness(user, type);
//		}
//	}
//	public void addBusiness(RichUserBean user) {
//		for(int i = 0;i < nodeList.size();i++) {
//			RichNodeBean node = (RichNodeBean)nodeList.get(i);
//			node.addBusiness(user);
//		}
//	}
}
