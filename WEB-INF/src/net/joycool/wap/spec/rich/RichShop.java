package net.joycool.wap.spec.rich;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 大富翁商店
 * @datetime:1007-10-24
 */
public class RichShop {
	
	public static int MAX_ITEM = 12;
	
	public RichItemBean[] items = new RichItemBean[MAX_ITEM];		// 0 表示没有道具
	
	public static HashMap itemMap = null;		// 所有的道具map
	
	public static int[] itemRandomRate = {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	public static int itemRandomTotal = 0;
	static {
		itemRandomTotal = RandomUtil.sumRate(itemRandomRate);
	}
	
	// 刷新商店
	public void refresh() {
		for(int i = 0;i < MAX_ITEM;i++) {
			items[i] = (RichItemBean)itemMap.get(Integer.valueOf(RandomUtil.randomRateInt(itemRandomRate, itemRandomTotal)));
		}
	}
	
	public RichItemBean getRandomItem() {
		return (RichItemBean)itemMap.get(Integer.valueOf(RandomUtil.randomRateInt(itemRandomRate, itemRandomTotal)));
	}
	
	/**
	 * 有人买了一个道具，删除这个格子里的道具
	 * 返回之前的数值
	 * @param slot
	 */
	public RichItemBean removeItem(int slot) {
		RichItemBean tmp = items[slot];
		items[slot] = null;
		return tmp;
	}

	public static RichItemBean getItem(int id) {
		return (RichItemBean)itemMap.get(Integer.valueOf(id));
	}
}
