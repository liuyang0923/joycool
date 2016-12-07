package net.joycool.wap.util;

import java.util.HashMap;

// 用于计数，根据id给一个数
public class CountMap extends HashMap {

	private static final long serialVersionUID = -8942789969014155105L;

	public synchronized void resetCount() {
		clear();
	}
	
	public  int[] getCount(int id) {
		Integer key = new Integer(id);
		synchronized(this) {
			int[] c = (int[])get(key);
			if(c == null) {
				c = new int[1];
				put(key, c);
			}
			return c;
		}
	}

}
