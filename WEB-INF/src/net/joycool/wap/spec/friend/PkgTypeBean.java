package net.joycool.wap.spec.friend;

import java.util.*;

/**
 * @author zhouj
 * @explain： 礼包类型
 */
public class PkgTypeBean {
	int id;
	String name;	// 礼包名称
	String info;	// 详细描述
	int flag;		// 礼包参数
	int price;		// 价格
	int count;		// 礼包内可以存放的物品
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
