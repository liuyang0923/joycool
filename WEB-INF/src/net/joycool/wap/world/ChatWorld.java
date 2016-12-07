package net.joycool.wap.world;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @explain：新版本聊天室
 * @datetime:2007-7-11 9:47:37
 * @param args
 * @return void
 */
public class ChatWorld {
	// 所有聊天室缓存map
	public static Hashtable chatWorldMap = new Hashtable();

	// 聊天室信息最大容量
	public static int MAX_SIZE = 50;

	// 获取所有聊天室
	public static Hashtable getCacheMap() {
		return chatWorldMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取一个指定聊天室信息列表
	 * @datetime:2007-7-11 11:02:27
	 * @param key
	 * @param group
	 * @return
	 * @return LinkedList
	 */
	public static LinkedList get(String key, String group) {
		if (key == null || group == null) {
			return null;
		}
		Hashtable ht = getCacheMap(group);
		LinkedList chatWorkdList = (LinkedList) ht.get(key);
		if (chatWorkdList == null) {
			chatWorkdList = new LinkedList();
			getChatList(group + key, chatWorkdList);
			ht.put(key, chatWorkdList);
		}
		return chatWorkdList;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取一组聊天室
	 * @datetime:2007-7-11 10:59:16
	 * @param group
	 * @return
	 * @return Hashtable
	 */
	static byte[] lock = new byte[0];

	private static Hashtable getCacheMap(String group) {
		Hashtable ht = (Hashtable) chatWorldMap.get(group);
		if (ht == null) {
			synchronized (lock) {
				ht = (Hashtable) chatWorldMap.get(group);
				if (ht == null) {
					ht = new Hashtable();
					chatWorldMap.put(group, ht);
				}
			}
		}
		return ht;
	}

	/**
	 * 
	 * @author macq
	 * @explain：向一个聊天室插入聊天记录
	 * @datetime:2007-7-11 10:58:38
	 * @param key
	 * @param bean
	 * @param group
	 * @return void
	 */
	public static void put(String key, ChatWorldBean bean, String group) {
		if (key == null || bean == null || group == null) {
			return;
		}
		Hashtable ht = getCacheMap(group);
		LinkedList chatWorkdList = (LinkedList) ht.get(key);
		if (chatWorkdList == null)
			synchronized (ht) {
				chatWorkdList = (LinkedList) ht.get(key);
				if (chatWorkdList == null) {
					chatWorkdList = new LinkedList();
					getChatList(group + key, chatWorkdList);
					ht.put(key, chatWorkdList);
				}
			}
		synchronized (chatWorkdList) {
			// 判断场景log是否大于50条
			if (chatWorkdList.size() >= MAX_SIZE) {
				chatWorkdList.removeLast();
			}
		}
		// 添加聊天内容
		chatWorkdList.addFirst(bean);
		addChat(group + key, bean);
	}

	/**
	 * 
	 * @author macq
	 * @explain：清空一组聊天室缓存缓存。
	 * @datetime:2007-7-11 10:57:54
	 * @param group
	 * @return void
	 */
	public static void flushGroup(String group) {
		if (group == null) {
			return;
		}
		try {
			getCacheMap(group).clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：清空所有缓存。
	 * @datetime:2007-7-11 10:57:48
	 * @return void
	 */
	public static void flushAll() {
		Hashtable cacheMap = getCacheMap();
		Iterator it = cacheMap.keySet().iterator();
		while (it.hasNext()) {
			String group = (String) it.next();
			flushGroup(group);
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：清除一个缓存组的一个聊天室
	 * @datetime:2007-7-11 10:56:47
	 * @param group
	 * @param key
	 * @return void
	 */
	public static void flushGroup(String group, String key) {
		if (group == null || key == null) {
			return;
		}
		getCacheMap(group).remove(key);
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取一个缓存组中所有聊天室
	 * @datetime:2007-7-11 10:56:07
	 * @param group
	 * @return
	 * @return Hashtable
	 */
	public static Hashtable getGroup(String group) {
		if (group == null)
			return null;
		return getCacheMap(group);
	}
	
	public static boolean addChat(String key, ChatWorldBean bean) {
		DbOperation dbOp = new DbOperation(3);
		String query = "INSERT INTO tong_chat(`key`,user_id,content,time) VALUES(?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, key);
			pstmt.setInt(2, bean.getUserId());
			pstmt.setString(3, bean.getContent());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	public static void getChatList(String key, List ret) {
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from tong_chat WHERE `key`='" + key + "' order by id desc limit " + MAX_SIZE;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				ChatWorldBean bean = new ChatWorldBean();
				bean.setContent(rs.getString("content"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setCreateDatetime(DateUtil.formatTime(rs.getTimestamp("time").getTime()));
				ret.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
	}
}
