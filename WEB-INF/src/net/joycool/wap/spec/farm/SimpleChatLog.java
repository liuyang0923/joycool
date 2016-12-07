package net.joycool.wap.spec.farm;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 聊天日志
 */
public class SimpleChatLog {
	static int LOG_DEFAULT_SIZE = 200;
	public static HashMap group = new HashMap();
	public static SimpleChatLog getChatLog(int id) {
		Integer key = Integer.valueOf(id);
		synchronized(group) {
			SimpleChatLog cl = (SimpleChatLog)group.get(key);
			if(cl == null) {
				cl = new SimpleChatLog();
				cl.setKey(key.toString());
				cl.getChatList();
				group.put(key, cl);
			}
			return cl;
		}
	}
	public static SimpleChatLog getChatLog(Object key) {
		synchronized(group) {
			SimpleChatLog cl = (SimpleChatLog)group.get(key);
			if(cl == null) {
				cl = new SimpleChatLog();
				cl.setKey(key.toString());
				cl.getChatList();
				group.put(key, cl);
			}
			return cl;
		}
	}

	String key;
	LinkedList chatList = new LinkedList();
	int maxSize = LOG_DEFAULT_SIZE;
	int chatTotal;		// 总发过的聊天记录数量
	
	public SimpleChatLog() {
	}
	
	public SimpleChatLog(int limit) {
		maxSize = limit;
	}
	
	/**
	 * @param limit 限制返回前几条log
	 */
	public String getChatString(int start, int limit) {
		StringBuilder sb = new StringBuilder(256);
		Iterator iter = chatList.iterator();
		while(iter.hasNext()) {
			String chat = (String)iter.next();
			if(start <= 0) {
				sb.append(chat);
				sb.append("<br/>");
				limit--;
				if(limit <= 0)
					break;
			} else {
				start--;
			}
		}
		return sb.toString();
	}
	
	public String toString() {
		return getChatString(0, 10);
	}
	
	// 返回前几条，但是顺序相反
	public String getLogStringR(int limit) {
		if(limit > chatList.size())
			limit = chatList.size();
		
		int i = 0;
		StringBuilder sb = new StringBuilder();
		ListIterator iter = chatList.listIterator(limit);
		while(iter.hasPrevious()) {
			i++;
			sb.append(i);
			sb.append(".");
			sb.append(iter.previous());
			sb.append("<br/>");
		}
		return sb.toString();
	}
	
	public int getUnreadTotal(int read) {
		int c = chatTotal - read;
		if(c > maxSize)
			return maxSize;
		return c;
	}
	public int getUnreadTotal(Object read) {
		if(read == null || !(read instanceof Integer))
			return chatList.size();
		return getUnreadTotal(((Integer)read).intValue());
	}
	// 和上面函数的区别是，如果新来，显示未读为0
	public int getUnreadTotal2(Object read) {
		if(read == null || !(read instanceof Integer))
			return 0;
		return getUnreadTotal(((Integer)read).intValue());
	}
	
	/**
	 * 添加log
	 * @param content 添加的内容
	 */
	public void add(String content) {
		chatTotal++;
		synchronized (chatList) {
			chatList.addFirst(content);
			// 只保留最近的log
			if (chatList.size() > maxSize) {
				chatList.removeLast();
			}
			if(key != null) 
				addChat(key, content);
		}
	}

	public void clear() {
		chatList.clear();
	}
	public int size() {
		return chatList.size();
	}

	public int getChatTotal() {
		return chatTotal;
	}

	public void setChatTotal(int chatTotal) {
		this.chatTotal = chatTotal;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public boolean addChat(String key, String content) {
		DbOperation dbOp = new DbOperation(3);
		String query = "INSERT INTO simple_chat(`key`,content,time) VALUES(?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, key);
			pstmt.setString(2, content);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	public List getChatList() {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from simple_chat WHERE `key`='" + key + "' order by id desc limit " + maxSize;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				chatList.addLast(rs.getString("content"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
}
