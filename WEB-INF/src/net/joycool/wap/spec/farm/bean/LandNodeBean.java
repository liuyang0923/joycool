package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 地图结点
 * @datetime:1007-10-24
 */
public class LandNodeBean {
	public static String[] names = {"平地","草地","森林","沙漠","山地","河流","峭壁"};
	int flag;
	LandItemBean item = null;		// 长出来的物品，没有则null
	int count = 1;		// 物品数量
	
	// 次数减少1
	public void decCount() {
		count--;
		if(count <= 0)
			decCountAll();
	}
	
	// 次数减少到0
	public void decCountAll() {
		count = 0;
		item = null;
	}
	
	// 是否长出来了……
	public boolean hasItem() {
		return item != null;
	}
	
	public String getName() {
		return names[flag];
	}
	
	/**
	 * @return Returns the flag.
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag The flag to set.
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return Returns the item.
	 */
	public LandItemBean getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(LandItemBean item) {
		this.item = item;
	}
}
