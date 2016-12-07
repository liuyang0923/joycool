package net.joycool.wap.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

public class SimpleGameLog {
	static int LOG_DEFAULT_SIZE = 50;
	
	LinkedList log = new LinkedList();
	int maxSize = LOG_DEFAULT_SIZE;
	
	public SimpleGameLog() {
	}
	
	public SimpleGameLog(int limit) {
		maxSize = limit;
	}
	
	/**
	 * @param limit 限制返回前几条log
	 */
	public String getLogString(int limit) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		Iterator iter = log.iterator();
		while(iter.hasNext() && limit > 0) {
			i++;
			sb.append(i);
			sb.append(".");
			sb.append(iter.next());
			sb.append("<br/>");
			limit--;
		}
		return sb.toString();
	}
	// 用于分页显示
	public String getLogString(int start, int limit) {
		int i = start;
		StringBuilder sb = new StringBuilder();
		Iterator iter = log.listIterator(start);
		while(iter.hasNext() && limit > i) {
			i++;
			sb.append(i);
			sb.append(".");
			sb.append(iter.next());
			sb.append("<br/>");
		}
		return sb.toString();
	}
	public String toString() {
		return getLogString(10);
	}
	
	// 返回前几条，但是顺序相反
	public String getLogStringR(int limit) {
		if(limit > log.size())
			limit = log.size();
		
		int i = 0;
		StringBuilder sb = new StringBuilder();
		ListIterator iter = log.listIterator(limit);
		while(iter.hasPrevious()) {
			i++;
			sb.append(i);
			sb.append(".");
			sb.append(iter.previous());
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	/**
	 * 添加log
	 * @param content 添加的内容
	 */
	public void add(String content) {
		synchronized (log) {
			log.addFirst(content);
			// 只保留最近的log
			if (log.size() > maxSize) {
				log.removeLast();
			}
		}
	}

	public void clear() {
		log.clear();
	}
	public int size() {
		return log.size();
	}
}
