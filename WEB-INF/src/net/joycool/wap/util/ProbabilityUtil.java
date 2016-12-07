package net.joycool.wap.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

public class ProbabilityUtil {

	private static LinkedHashMap catalogMap = null;

	private static TreeMap catalogProbabilityMap = null;
	
	//liuyi 2006-12-01 程序优化 start
	static{
		init();
	}
	
	public static void init(){
		catalogProbabilityMap = new TreeMap();
		LinkedHashMap map = getCatalogMap();
		int area = 0;
		Set set = map.keySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Integer key = (Integer) itr.next();
			int probability=((Integer)map.get(key)).intValue();
			area += probability;
			if (probability != 0) {
				catalogProbabilityMap.put(new Integer(area), key);
			}
		}
		catalogProbabilityMap.put(new Integer(Constants.RANDOM_BASE),
				new Integer(area));
	}

	/**
	 * 初始化对应模块的骚扰系数 macq_2006-10-23_
	 * 
	 * @return
	 */
	public static LinkedHashMap getCatalogMap() {
		if (catalogMap != null) {
			return catalogMap;
		}
		catalogMap = new LinkedHashMap();
		catalogMap.put(new Integer(Constants.POSITION_CHAT), new Integer(10));
		catalogMap.put(new Integer(Constants.POSITION_USER), new Integer(10));
		catalogMap.put(new Integer(Constants.POSITION_WGAME), new Integer(7));
		catalogMap.put(new Integer(Constants.POSITION_FORUM), new Integer(6));
		catalogMap.put(new Integer(Constants.POSITION_TOP), new Integer(5));
		catalogMap.put(new Integer(Constants.POSITION_JOB), new Integer(3));
		catalogMap.put(new Integer(Constants.POSITION_EBOOK), new Integer(2));
		catalogMap.put(new Integer(Constants.POSITION_IMAGE), new Integer(2));
		catalogMap.put(new Integer(Constants.POSITION_NEWS), new Integer(2));
		catalogMap.put(new Integer(Constants.POSITION_GAME), new Integer(2));
		catalogMap.put(new Integer(Constants.POSITION_RING), new Integer(2));
		catalogMap.put(new Integer(Constants.POSITION_VIDEO), new Integer(2));
		return catalogMap;
	}

	/**
	 * 获取对应模块的骚扰系数
	 * 
	 * @param map
	 * @return
	 */
	public static TreeMap getCatalogProbabilityMap() {
		return catalogProbabilityMap;
	}
	//liuyi 2006-12-01 程序优化 end
}
