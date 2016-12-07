/**
 * 
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.friendadver.FriendAdverMessageBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IFriendAdverMessageService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhul 2006-06-20 处理交友中心模块中用户评论信息的db类
 */
public class FriendAdverMessageServiceImpl implements
		IFriendAdverMessageService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverMessageService#addFriendAdverMessage(net.joycool.wap.bean.friendadver.FriendAdverMessageBean)
	 */
	public boolean addFriendAdverMessage(
			FriendAdverMessageBean friendAdverMessage) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_friend_adver_message(friend_adver_id,user_id,user_nickname,content,attachment,create_datetime) VALUES(?, ?, ?, ?, ?, now())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, friendAdverMessage.getFriendAdverId());
			pstmt.setInt(2, friendAdverMessage.getUserId());
			pstmt.setString(3, friendAdverMessage.getUserNickname());
			pstmt.setString(4, friendAdverMessage.getContent());
			pstmt.setString(5, friendAdverMessage.getAttachment());

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
	 * @see net.joycool.wap.service.infc.IFriendAdverMessageService#getFriendAdverMessage(java.lang.String)
	 */
	public FriendAdverMessageBean getFriendAdverMessage(String condition) {
		// TODO Auto-generated method stub
		FriendAdverMessageBean adverMessage = null;

		// liuyi 2006-11-30 交友优化 start
		// 构建查询语句
		String query = "SELECT * FROM jc_friend_adver_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		String key = condition;
		adverMessage = (FriendAdverMessageBean) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_ADV_MESSAGE_GROUP,
				OsCacheUtil.FRIEND_ADV_MESSAGE_LIST_FLUSH_PERIOD);

		if (adverMessage == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			// 查询
			ResultSet rs = dbOp.executeQuery(query);

			try {
				// 结果不为空
				if (rs.next()) {
					adverMessage = new FriendAdverMessageBean();
					adverMessage.setId(rs.getInt("id"));
					adverMessage.setUserId(rs.getInt("user_id"));
					adverMessage.setFriendAdverId(rs.getInt("friend_adver_id"));
					adverMessage.setUserNickname(rs.getString("user_nickname"));
					adverMessage.setContent(rs.getString("content"));
					adverMessage.setAttachment(rs.getString("attachment"));
					adverMessage.setCreateDatetime(rs
							.getString("create_datetime"));

					OsCacheUtil.put(key, adverMessage,
							OsCacheUtil.FRIEND_ADV_MESSAGE_GROUP);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 释放资源
			dbOp.release();
		}
		// liuyi 2006-11-30 交友优化 end

		return adverMessage;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverMessageService#getFriendAdverMessageList(java.lang.String)
	 */
	public Vector getFriendAdverMessageList(String condition) {
		// TODO Auto-generated method stub
		Vector friendAdverMessageList = null;
		FriendAdverMessageBean adverMessage = null;

		// liuyi 2006-11-30 交友优化 start
		// 构建查询语句
		String query = "SELECT * FROM jc_friend_adver_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		String key = condition;
		friendAdverMessageList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_ADV_MESSAGE_LIST_GROUP,
				OsCacheUtil.FRIEND_ADV_MESSAGE_LIST_FLUSH_PERIOD);
		if (friendAdverMessageList == null) {
			friendAdverMessageList = new Vector();
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			// 查询
			ResultSet rs = dbOp.executeQuery(query);

			try {
				// 结果不为空
				while (rs.next()) {
					adverMessage = new FriendAdverMessageBean();
					adverMessage.setId(rs.getInt("id"));
					adverMessage.setUserId(rs.getInt("user_id"));
					adverMessage.setFriendAdverId(rs.getInt("friend_adver_id"));
					adverMessage.setUserNickname(rs.getString("user_nickname"));
					adverMessage.setContent(rs.getString("content"));
					adverMessage.setAttachment(rs.getString("attachment"));
					adverMessage.setCreateDatetime(rs
							.getString("create_datetime"));
					friendAdverMessageList.add(adverMessage);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 释放资源
			dbOp.release();
			
			OsCacheUtil.put(key, friendAdverMessageList,
					OsCacheUtil.FRIEND_ADV_MESSAGE_LIST_GROUP);
		}
		// liuyi 2006-11-30 交友优化 end

		return friendAdverMessageList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverMessageService#deleteFriendAdverMessage(java.lang.String)
	 */
	public boolean deleteFriendAdverMessage(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_friend_adver_message WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverMessageService#updateFriendAdverMessage(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateFriendAdverMessage(String set, String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverMessageService#getFriendAdverMessageCount(java.lang.String)
	 */
	public int getFriendAdverMessageCount(String condition) {
		// TODO Auto-generated method stub
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM jc_friend_adver_message";
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

}
