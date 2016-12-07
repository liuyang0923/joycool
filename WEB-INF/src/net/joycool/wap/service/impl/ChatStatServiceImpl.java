package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import net.joycool.wap.bean.chat.ChatStatBean;
import net.joycool.wap.service.infc.IChatStatService;
import net.joycool.wap.util.db.DbOperation;

public class ChatStatServiceImpl implements IChatStatService{
	public boolean addChatStat(ChatStatBean chatStat){
//		 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String dateTime=getLastDay();
		
		String query = "INSERT INTO chat_stat (send_num,send_to_person,send_friend,no_reply,friend_num,user_count,create_datetime,user_total,chat_num,action_num,message_num,pk_num) values(?,?,?,?,?,?,?,?,?,?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
	
			pstmt.setFloat(1, chatStat.getSendNum());
			pstmt.setFloat(2, chatStat.getSendToPerson());
			pstmt.setFloat(3, chatStat.getSendFriend());
			pstmt.setFloat(4, chatStat.getNoReply());
			pstmt.setFloat(5, chatStat.getFriendNum());
			pstmt.setInt(6, chatStat.getUserCount());
			pstmt.setString(7, dateTime);
			pstmt.setInt(8, chatStat.getUserTotal());
			pstmt.setFloat(9, chatStat.getChatNum());
			pstmt.setFloat(10, chatStat.getActionNum());
			pstmt.setFloat(11, chatStat.getMessageNum());
			pstmt.setFloat(12, chatStat.getPkNum());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	public ChatStatBean getChatStat(String condition){
		ChatStatBean chatStat = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM chat_stat";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				chatStat = new ChatStatBean();
				chatStat.setId(rs.getInt("id"));
				chatStat.setCreateDateTime(rs.getString("create_datetime"));
				chatStat.setFriendNum(rs.getFloat("friend_num"));
				chatStat.setNoReply(rs.getFloat("no_reply"));
				chatStat.setSendFriend(rs.getFloat("send_friend"));
				chatStat.setSendNum(rs.getFloat("send_num"));
				chatStat.setSendToPerson(rs.getFloat("send_to_person"));
				chatStat.setUserCount(rs.getInt("user_count"));
				chatStat.setUserTotal(rs.getInt("user_total"));
				chatStat.setActionNum(rs.getFloat("action_num"));
				chatStat.setChatNum(rs.getFloat("chat_num"));
				chatStat.setPkNum(rs.getFloat("pk_num"));
				chatStat.setMessageNum(rs.getFloat("message_num"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return chatStat;
		
	}

	public Vector getChatStatList(String condition){
		Vector chatStatList = new Vector();
		ChatStatBean chatStat = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM chat_stat";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join ") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				chatStat = new ChatStatBean();
				chatStat.setId(rs.getInt("id"));
				chatStat.setCreateDateTime(rs.getString("create_datetime"));
				chatStat.setFriendNum(rs.getFloat("friend_num"));
				chatStat.setNoReply(rs.getFloat("no_reply"));
				chatStat.setSendFriend(rs.getFloat("send_friend"));
				chatStat.setSendNum(rs.getFloat("send_num"));
				chatStat.setSendToPerson(rs.getFloat("send_to_person"));
				chatStat.setUserCount(rs.getInt("user_count"));
				chatStat.setUserTotal(rs.getInt("user_total"));
				chatStat.setActionNum(rs.getFloat("action_num"));
				chatStat.setChatNum(rs.getFloat("chat_num"));
				chatStat.setPkNum(rs.getFloat("pk_num"));
				chatStat.setMessageNum(rs.getFloat("message_num"));
				chatStatList.add(chatStat);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		finally {
			dbOp.release();
		}

		return chatStatList;

		
	}

	public boolean deleteChatStat(String condition){
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM chat_stat WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean updateChatStat(String set, String condition){
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE chat_stat SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
		
	}

	public int getChatStatCount(String condition){
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(chat_stat.id) AS c_id FROM chat_stat";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join ") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
			return count;
		}

		// 释放资源
		dbOp.release();
		return count;
	}
	private String getLastDay(){
		java.util.Date d = null;;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add (Calendar.DAY_OF_YEAR,-1);
		return df.format(c.getTime());
	}
	
}
