/*
 * Created on 2005-12-24
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.forum.ForumMessageBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IForumMessageService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class ForumMessageServiceImpl implements IForumMessageService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.sp.service.infc.IForumMessageService#addForumMessage(net.joycool.sp.bean.ForumMessageBean)
	 */
	public boolean addForumMessage(ForumMessageBean message) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO sp_forum_message(title, content, attachment, "
				+ "parent_id, user_id, create_datetime, forum_id, user_nickname) VALUES(?, ?, ?, ?, ?, now(), ?, ?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, message.getTitle());
			pstmt.setString(2, message.getContent());
			pstmt.setString(3, message.getAttachment());
			pstmt.setInt(4, message.getParentId());
			pstmt.setInt(5, message.getUserId());
			pstmt.setInt(6, message.getForumId());
			pstmt.setString(7, message.getUserNickname());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.sp.service.infc.IForumMessageService#deleteForumMessage(java.lang.String)
	 */
	public boolean deleteForumMessage(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM sp_forum_message WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.sp.service.infc.IForumMessageService#getForumMessage(java.lang.String)
	 */
	public ForumMessageBean getForumMessage(String condition) {
		ForumMessageBean message = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM sp_forum_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				message = new ForumMessageBean();
				message.setAttachment(rs.getString("attachment"));
				message.setContent(rs.getString("content"));
				message.setForumId(rs.getInt("forum_id"));
				message.setUserNickname(rs.getString("user_nickname"));
				message.setCreateDatetime(rs.getString("create_datetime"));
				message.setHits(rs.getInt("hits"));
				message.setId(rs.getInt("id"));
				message.setParentId(rs.getInt("parent_id"));
				message.setTitle(rs.getString("title"));
				message.setUserId(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.sp.service.infc.IForumMessageService#getForumMessageList(java.lang.String)
	 */
	public Vector getForumMessageList(String condition) {
		Vector messageList = new Vector();
		ForumMessageBean message = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM sp_forum_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				message = new ForumMessageBean();
				message.setAttachment(rs.getString("attachment"));
				message.setContent(rs.getString("content"));
				message.setForumId(rs.getInt("forum_id"));
				message.setUserNickname(rs.getString("user_nickname"));
				message.setCreateDatetime(rs.getString("create_datetime"));
				message.setHits(rs.getInt("hits"));
				message.setId(rs.getInt("id"));
				message.setParentId(rs.getInt("parent_id"));
				message.setTitle(rs.getString("title"));
				message.setUserId(rs.getInt("user_id"));
				messageList.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return messageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.sp.service.infc.IForumMessageService#getForumMessageCount(java.lang.String)
	 */
	public int getForumMessageCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM sp_forum_message";
		if (condition != null) {
			query += " WHERE " + condition;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.sp.service.infc.IForumMessageService#updateForumMessage(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateForumMessage(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE sp_forum_message SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IForumMessageService#getForumMessageId(java.lang.String)
	 */
	public int[] getForumMessageId(String order) {

		int count = this.getForumMessageCount(null);
		int[] orderId = new int[count];

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id FROM sp_forum_message where forum_id=14 and parent_id=0  order by "
				+ order + " desc";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			int i = 0;
			while (rs.next()) {
				orderId[i] = rs.getInt("id");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return orderId;
	}

	public int getNewForumMessageCount(String condition) {
		int count = 0;
		Integer counts = (Integer) OsCacheUtil.get(condition,
				OsCacheUtil.NEW_PIC_GROUP, OsCacheUtil.NEW_PIC_FLUSH_PERIOD);
		if (counts == null) {

			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			// 构建查询语句
			String query = "SELECT count(id) AS c_id FROM sp_forum_message where to_days(now())-to_days(create_datetime)=0 ";
			if (condition != null) {
				query += " and " + condition;
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
			OsCacheUtil.put(condition, new Integer(count),
					OsCacheUtil.NEW_PIC_GROUP);
		} else
			count = counts.intValue();
		return count;
	}
}
