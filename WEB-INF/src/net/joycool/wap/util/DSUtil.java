package net.joycool.wap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// data structure util
public class DSUtil {
	// 获取子列表，从start开始，到end - 1结束
	public static List sublist(List list, int start, int end) {
		int length = end - start;
		List list2 = new ArrayList(length);
		if(length == 0 || start >= list.size())
			return list2;
		Iterator iter = list.iterator();
		int i = 0;
		while(iter.hasNext() && i < start) {
			iter.next();
			i++;
		}
		while(iter.hasNext() && i < end) {
			list2.add(iter.next());
			i++;
		}
		return list2;
	}
}
