package jc.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain:聊天日志
 */
public class SimpleChatLog2 {

	static int LOG_DEFAULT_SIZE = 200;
	static String LOG_DEFAULT_TABLE = "chat_";
	public static HashMap group = new HashMap();

	public static SimpleChatLog2 getChatLog(int id, String name) {
		String tablename = LOG_DEFAULT_TABLE + name;
		Integer key = Integer.valueOf(id);
		synchronized (group) {
			HashMap chatlogMap = (HashMap) group.get(tablename);
			if (chatlogMap == null) {
				chatlogMap = new HashMap();
				SimpleChatLog2 cl = new SimpleChatLog2(id, name);
				cl.getChatList();
				chatlogMap.put(key, cl);
				group.put(tablename, chatlogMap);
				return cl;
			}
			SimpleChatLog2 cl = (SimpleChatLog2) chatlogMap.get(key);
			if (cl == null) {
				cl = new SimpleChatLog2(id, name);
				cl.getChatList();
				chatlogMap.put(key, cl);
			}
			return cl;
		}
	}
	
	public static SimpleChatLog2 getChatLog() {
		SimpleChatLog2 cl = new SimpleChatLog2();
		return cl;
	}

	public static SimpleChatLog2 getChatLog(Object key, String name) {
		int keyint = ((Integer) key).intValue();
		return getChatLog(keyint, name);
	}

	int key;
	LinkedList chatList = new LinkedList();
	int maxSize = LOG_DEFAULT_SIZE;
	String table = LOG_DEFAULT_TABLE;
	int chatTotal; // 总发过的聊天记录数量
	boolean tempChat;	// 是否是临时的，临时聊天不保存到数据库

	private SimpleChatLog2(int key, String table) {
		this.key = key;
		this.table = LOG_DEFAULT_TABLE + table;
		tempChat = false;
	}
	// 临时聊天，不保存也没有key
	private SimpleChatLog2() {
		tempChat = true;
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

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * @param limit
	 *            限制返回前几条log
	 */
	public String getChatString(int start, int limit) {
		StringBuilder sb = new StringBuilder(256);
		Iterator iter = chatList.iterator();
		while (iter.hasNext()) {
			ChatContent chat = (ChatContent) iter.next();
			if (start <= 0) {
				sb.append(chat);
				sb.append("<br/>");
				limit--;
				if (limit <= 0)
					break;
			} else {
				start--;
			}
		}
		return sb.toString();
	}
	// 带连接的
	public String getChatString2(int uid,int start, int limit, String link) {
		StringBuilder sb = new StringBuilder(256);
		Iterator iter = chatList.iterator();
		while (iter.hasNext()) {
			ChatContent chat = (ChatContent) iter.next();
			if (start <= 0) {
				if(chat.userid == uid)
					sb.append(chat.toString());
				else
					sb.append(chat.toString2(link));
				sb.append("<br/>");
				limit--;
				if (limit <= 0)
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
		if (limit > chatList.size())
			limit = chatList.size();

		int i = 0;
		StringBuilder sb = new StringBuilder();
		ListIterator iter = chatList.listIterator(limit);
		while (iter.hasPrevious()) {
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
		if (c > maxSize)
			return maxSize;
		return c;
	}

	public int getUnreadTotal(Object read) {
		if (read == null || !(read instanceof Integer))
			return chatList.size();
		return getUnreadTotal(((Integer) read).intValue());
	}

	// 和上面函数的区别是，如果新来，显示未读为0
	public int getUnreadTotal2(Object read) {
		if (read == null || !(read instanceof Integer))
			return 0;
		return getUnreadTotal(((Integer) read).intValue());
	}

	/**
	 * 添加log
	 * 
	 * @param content
	 *            添加的内容
	 */
	public void add(int userid, String username, String content) {
		if(content.length() == 0)
			return;
		if(content.length() > 100)
			content = content.substring(0, 100);
		synchronized (chatList) {
			chatTotal++;
			ChatContent chatContent = new ChatContent(key, userid, username, content, System.currentTimeMillis());
			
			if (key != 0 && !tempChat)
				addChat(chatContent);
			
			// 添加到内存数据
			chatContent.toWml();	// 内存中保存towml后的数据，减少数据处理次数
			chatList.addFirst(chatContent);
			// 只保留最近的log
			if (chatList.size() > maxSize) {
				chatList.removeLast();
			}
			
		}
	}

	public boolean addChat(ChatContent chatContent) {
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into " + table + "(key_id,userid,username,content,chat_time)values(?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, chatContent.keyid);
			pstmt.setInt(2, chatContent.userid);
			pstmt.setString(3, chatContent.username);
			pstmt.setString(4, chatContent.content);
			pstmt.execute();
			// chatContent.id = dbOp.getLastInsertId();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}

	public List getChatList() {
		if(tempChat)
			return null;

		DbOperation dbOp = new DbOperation(5);
		// String query = "CREATE TABLE `"
		// + table
		// +
		// "` (`id` int(10) unsigned NOT NULL AUTO_INCREMENT,`key_id` int(10) unsigned NOT NULL,`userid` int(10) unsigned NOT NULL,`username` varchar(15) NOT NULL,`content` varchar(100) NOT NULL,`chat_time` datetime NOT NULL,  PRIMARY KEY (`id`),KEY `Index_2` (`key_id`)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		// dbOp.executeUpdate(query);
		String query = "SELECT id,key_id,userid,username,content,chat_time from " + table + " WHERE `key_id`='" + key
				+ "' order by id desc limit " + maxSize;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				ChatContent chatContent = new ChatContent();
				chatContent.id = rs.getInt(1);
				chatContent.keyid = rs.getInt(2);
				chatContent.userid = rs.getInt(3);
				chatContent.username = rs.getString(4);
				chatContent.content = rs.getString(5);
				chatContent.time = rs.getTimestamp(6).getTime();
				chatContent.toWml();
				chatList.addLast(chatContent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return chatList;
	}

	public static class ChatContent {
		int id;
		int keyid;
		int userid;
		String username;
		String content;
		long time;

		public ChatContent() {
		}

		public ChatContent(int key, int userid, String username, String content, long time) {
			this.keyid = key;
			this.userid = userid;
			this.username = username;
			this.content = content;
			this.time = time;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(content.length() + 32);
			sb.append(username);
			sb.append(':');
			sb.append(content);
			sb.append('(');
			sb.append(DateUtil.formatTime(time));
			sb.append(')');
			return sb.toString();
		}
		
		public String toString2(String link) {
			StringBuilder sb = new StringBuilder(content.length() + 48);
			sb.append("<a href=\"");
			sb.append(link);
			sb.append("&amp;uid=");
			sb.append(userid);
			sb.append("\">");
			sb.append(username);
			sb.append("</a>:");
			sb.append(content);
			sb.append('(');
			sb.append(DateUtil.formatTime(time));
			sb.append(')');
			return sb.toString();
		}
		
		public void toWml() {
			username = StringUtil.toWml(username);
			content = StringUtil.toWml(content);
		}
	}
}
