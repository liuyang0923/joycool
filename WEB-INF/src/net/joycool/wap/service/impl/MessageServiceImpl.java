/*
 * Created on 2005-11-21
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class MessageServiceImpl implements IMessageService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IMessageService#addMessage(net.joycool.wap.bean.MessageBean)
	 */
	public boolean addMessage(MessageBean message) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO user_message("
				+ "from_user_id, to_user_id, content, send_datetime, mark) "
				+ " VALUES(?, ?, ?, now(), ?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, message.getFromUserId());
			pstmt.setInt(2, message.getToUserId());
			pstmt.setString(3, message.getContent());
			pstmt.setInt(4, message.getMark());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();
/*
		IUserService userService = ServiceFactory.createUserService();
		INoticeService noticeService = ServiceFactory.createNoticeService();
//		UserBean fromUser = userService.getUser("id = " + message.getFromUserId());
		//zhul 2006-10-12_优化用户信息查询
		UserBean fromUser = UserInfoUtil.getUser(message.getFromUserId());
		if (fromUser != null) {
			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(fromUser.getNickName() + "给你发了一封信件");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(message.getToUserId());
			notice.setHideUrl("/user/ViewMessages.do");
			notice.setLink("/user/ViewMessages.do");
			//macq_2007-5-16_增加信件消息类型_start
			notice.setMark(NoticeBean.MESSAGE);
			//macq_2007-5-16_增加信件消息类型_end
			noticeService.addNotice(notice);
		}
*/
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IMessageService#deleteMessage(java.lang.String)
	 */
	public boolean deleteMessage(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM user_message WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IMessageService#getMessage(java.lang.String)
	 */
	public MessageBean getMessage(String condition) {
		IUserService userService = ServiceFactory.createUserService();

		MessageBean message = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, from_user_id, to_user_id, content, send_datetime, mark,flag FROM user_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				message = new MessageBean();
				message.setId(rs.getInt("id"));
				message.setFromUserId(rs.getInt("from_user_id"));
//				message.setFromUser(userService.getUser("id = "	+ message.getFromUserId()));
				//zhul 2006-10-12_优化用户信息查询
				message.setFromUser(UserInfoUtil.getUser(message.getFromUserId()));
				message.setToUserId(rs.getInt("to_user_id"));
//				message.setToUser(userService.getUser("id = "+ message.getToUserId()));
				message.setToUser(UserInfoUtil.getUser(message.getToUserId()));
				if (message.getFromUser() == null
						|| message.getToUser() == null) {
					dbOp.release();
					return null;
				}
				message.setContent(rs.getString("content"));
				message.setSendDatetime(rs.getString("send_datetime"));
				message.setMark(rs.getInt("mark"));
				message.setFlag(rs.getInt("flag"));
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
	 * @see net.joycool.wap.service.infc.IMessageService#getMessageList(java.lang.String)
	 */
	public Vector getMessageList(String condition) {
		Vector messageList = new Vector();
		IUserService userService = ServiceFactory.createUserService();

		MessageBean message = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, from_user_id, to_user_id, content, send_datetime, mark,flag FROM user_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				message = new MessageBean();
				message.setId(rs.getInt("id"));
				message.setFromUserId(rs.getInt("from_user_id"));
//				message.setFromUser(userService.getUser("id = "	+ message.getFromUserId()));
				//zhul 2006-10-12_优化用户信息查询
				message.setFromUser(UserInfoUtil.getUser(message.getFromUserId()));
				message.setToUserId(rs.getInt("to_user_id"));
//				message.setToUser(userService.getUser("id = "+ message.getToUserId()));
				message.setToUser(UserInfoUtil.getUser(message.getToUserId()));
				if (message.getFromUser() == null
						|| message.getToUser() == null) {
					continue;
				}
				message.setContent(rs.getString("content"));
				message.setSendDatetime(rs.getString("send_datetime"));
				message.setMark(rs.getInt("mark"));
				message.setFlag(rs.getInt("flag"));
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
	 * @see net.joycool.wap.service.infc.IMessageService#getMessageCount(java.lang.String)
	 */
	public int getMessageCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM user_message";
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
	 * @see net.joycool.wap.service.infc.IMessageService#updateMessage(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateMessage(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE user_message SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
}
