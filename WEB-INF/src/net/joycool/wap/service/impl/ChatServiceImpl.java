package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.user.UserBagAction;
import net.joycool.wap.bean.chat.ChatSpeakerBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomCompainBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.chat.JCRoomContentCountBean;
import net.joycool.wap.bean.chat.JCRoomForbidBean;
import net.joycool.wap.bean.chat.JCRoomOnlineBean;
import net.joycool.wap.bean.chat.RoomAfficheBean;
import net.joycool.wap.bean.chat.RoomApplyBean;
import net.joycool.wap.bean.chat.RoomGrantBean;
import net.joycool.wap.bean.chat.RoomHallInviteBean;
import net.joycool.wap.bean.chat.RoomInviteBean;
import net.joycool.wap.bean.chat.RoomInviteRankBean;
import net.joycool.wap.bean.chat.RoomInviteResourceBean;
import net.joycool.wap.bean.chat.RoomInviteStatBean;
import net.joycool.wap.bean.chat.RoomManagerBean;
import net.joycool.wap.bean.chat.RoomPaymentBean;
import net.joycool.wap.bean.chat.RoomRateBean;
import net.joycool.wap.bean.chat.RoomUserBean;
import net.joycool.wap.bean.chat.RoomVoteBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RoomCacheUtil;
import net.joycool.wap.util.SequenceUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 * 
 */
public class ChatServiceImpl implements IChatService {
	// 通过条件在online表中删除登陆用户
	// IFriendLevelService friendLevel =
	// ServiceFactory.createFriendLevelService();
	public static ICacheMap roomManagerCache = CacheManage.roomManager;
	
	static IUserService service = ServiceFactory.createUserService();

	public void deleteOnlineUser(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "delete FROM jc_room_online where " + condition;

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件更新online表中登陆用户
	public void updateOnlineUser(String set, String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "UPDATE jc_room_online SET " + set + " where "
				+ condition;

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件向online表中插入登陆用户
	public void addOnlineUser(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "insert into jc_room_online (room_id,user_id,enter_datetime) values("
				+ condition + ")";

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件取得聊天室名称
	public JCRoomBean getRoomName(String condition) {
		JCRoomBean room = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT * FROM jc_room where " + condition;

		ResultSet rs = dbOp.executeQuery(query);

		try {
			while (rs.next()) {
				room = this.getJCRoom(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return room;
	}

	// 获得jcroomforbin中所有记录
	public Vector getForBID() {
		Vector forbidList = new Vector();
		JCRoomForbidBean forbid = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_forbid";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				forbid = this.getJCRoomForbid(rs);
				forbidList.add(forbid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return forbidList;
	}

	// 通过条件检查jcroomforbin中是否存在登陆的userid
	public boolean getForBID(String condition) {
		boolean flag = false;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT * FROM jc_room_forbid where " + condition;

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return flag;
	}

	// 通过条件删除jcroomforbin中存在的登陆userid
	public void deltetForBID(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "delete FROM jc_room_forbid where " + condition;

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件在jcroomconent表中删除聊天记录
	public boolean deleteContent(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "delete FROM jc_room_content where " + condition;

		// 执行操作
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// 通过条件添加登陆userid到jcroomforbin中
	public void addForBID(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "insert into jc_room_forbid (user_id) values("
				+ condition + ")";

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件向jcroomcontent表中插入登陆用户
	//
	public JCRoomContentBean addContent(JCRoomContentBean bean) {
		if (bean == null)
			return null;

		int id = SequenceUtil.getSeq("roomContent");
		if (id < 1) {
			return null;
		}
		bean.setId(id);
		String time = DateUtil
				.formatDate(new Date(), DateUtil.normalTimeFormat);
		bean.setSendDateTime(time);

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String condition = id + "," + bean.getFromId() + "," + bean.getToId()
				+ ",'" + StringUtil.toSql(bean.getFromNickName()) + "','" + StringUtil.toSql(bean.getToNickName())
				+ "','" + StringUtil.toSql(bean.getContent()) + "','" + StringUtil.toSql(bean.getAttach())
				+ "',now()," + bean.getIsPrivate() + "," + bean.getRoomId()
				+ "," + bean.getSecRoomId() + "," + bean.getMark();
		// 构建查询语句
		String query = "insert into jc_room_content (id,from_id,to_id,from_nickname,to_nickname,content,attach,send_datetime,is_private,room_id,sec_room_id,mark) values("
				+ condition + ")";

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		// mcq_2006-9-16_增加聊天缓存记录_start
		RoomCacheUtil.addRoomContent(bean);
		// mcq_2006-9-16_增加聊天缓存记录_end

		// mcq_2006-9-16_增加公聊id缓存记录_start
		if (bean.getIsPrivate() == 0 && bean.getRoomId() >= 0
				&& bean.getMark() != 2) {
			if ((bean.getRoomId() == bean.getSecRoomId() && bean.getSecRoomId() == -1)
					|| bean.getRoomId() != bean.getSecRoomId()
					&& bean.getSecRoomId() == -1) {
				RoomCacheUtil.addRoomContentId(bean.getRoomId(), bean);
			} else {
				RoomCacheUtil.addRoomContentId(bean.getRoomId(), bean);
				// fanys 2006-09-16 start
				if (bean.getSecRoomId() != bean.getRoomId())
					RoomCacheUtil.addRoomContentId(bean.getSecRoomId(), bean);
				// fanys 2006-09-16 end
			}
		} else if (bean.getIsPrivate() == 0 && bean.getSecRoomId() >= 0
				&& bean.getMark() != 2) {
			RoomCacheUtil.addRoomContentId(bean.getSecRoomId(), bean);
		}
		// mcq_2006-9-16_增加公聊缓存记录_start

		// liuyi 2006-09-16 私聊加缓存 start
		RoomCacheUtil.addPrivateContentID(id, bean.getFromId());
		RoomCacheUtil.addPrivateContentID(id, bean.getToId());
		RoomCacheUtil.addTwoUserContentID(id, bean.getFromId(), bean.getToId());
		// liuyi 2006-09-16 私聊加缓存 end

		// wucx 用户有好度2006－10－19 start

		UserBagAction.changeUserFriend(bean.getFromId(), bean.getToId(), 1);
		UserInfoUtil.updateUserSocial(bean.getFromId(), 1);

		// wucx 用户有好度2006－10－19 end
		return bean;
	}

	// 通过条件取得聊天室人数列表
	public Vector getOnlineList(String condition) {
		Vector onlineList = new Vector();
		JCRoomOnlineBean online = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		// fanys 2006-06-23 start
		String query = "SELECT * FROM jc_room_online where " + condition;
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join") > 0)
				query = condition;
		}
		// fanys 2006-06-23 end
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				online = this.getJCRoomOnline(rs);
				onlineList.add(online);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return onlineList;
	}

	// 通过条件取得聊天室人数列表
	public JCRoomOnlineBean getOnlineUser(String condition) {
		JCRoomOnlineBean online = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_online where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				online = this.getJCRoomOnline(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return online;
	}

	// 通过条件获得聊天记录
	public JCRoomContentBean getContent(String condition) {
		JCRoomContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_content where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				content = this.getJCRoomConetnt(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return content;

	}

	// 获得所有的聊天记录
	public Vector getContent() {
		Vector messageList = new Vector();
		JCRoomContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_content";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				content = this.getJCRoomConetnt(rs);
				messageList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return messageList;
	}

	// 通过条件获取聊天记录数量
	public int getMessageCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room_content where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// 取得所有公聊记录
	public Vector getMessageList(String condition) {
		Vector MessageList = new Vector();
		JCRoomContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_content where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				content = this.getJCRoomConetnt(rs);
				MessageList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return MessageList;
	}

	// 通过条件取得聊天室人数
	public int getOnlineCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_room_online where "
				+ condition;

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// 通过条件添加投诉记录
	public void addJCRoomCompain(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "insert into jc_room_compain (left_user_id,right_user_id,content,mark,enter_datetime) values("
				+ condition + ")";

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件更新投诉记录
	public void updateJCRoomCompain(String set, String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "UPDATE jc_room_compain SET " + set + " where "
				+ condition;

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 获得所有的投诉记录
	public Vector getJCRoomCompainList() {
		Vector compainList = new Vector();
		JCRoomCompainBean compain = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_compain order by mark desc ,enter_datetime desc";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				compain = this.getJCRoomCompain(rs);
				compainList.add(compain);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return compainList;
	}

	// 通过条件获得相关投诉记录
	public JCRoomCompainBean getJCRoomCompain(String condition) {
		JCRoomCompainBean compain = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_compain where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				compain = this.getJCRoomCompain(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return compain;
	}

	// 通过条件删除相关投诉记录
	public void delJCRoomCompain(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "delete FROM jc_room_compain where " + condition;

		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// 通过条件显示相关聊天记录
	public Vector getContentList(String condition) {
		Vector MessageList = new Vector();
		JCRoomContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_content where " + condition
				+ " order by id desc";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				content = this.getJCRoomConetnt(rs);
				MessageList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return MessageList;
	}

	// mcq_2006-6-27_增加自建聊天室方法—_start
	public int getJCRoomCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room  where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return count;
	}

	public boolean addJCRoom(JCRoomBean jcRoom) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		int payDays = jcRoom.getPayDays();
		if (jcRoom.getPayWay() == 0) {
			payDays = Constants.EXPIRE_DAYS;
		}
		String query = "INSERT INTO jc_room set name=?, creator_id=?,  max_online_count=?, pay_way=?, create_datetime=now(),expire_datetime=DATE_ADD(now(), INTERVAL ? DAY),thumbnail=?,grant_mode=?,status=?,current_online_count=?";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, jcRoom.getName());
			pstmt.setInt(2, jcRoom.getCreatorId());
			pstmt.setInt(3, jcRoom.getMaxOnlineCount());
			pstmt.setInt(4, jcRoom.getPayWay());
			pstmt.setInt(5, payDays);
			pstmt.setString(6, jcRoom.getThumbnail());
			pstmt.setInt(7, jcRoom.getGrantMode());
			pstmt.setInt(8, 1);
			pstmt.setInt(9, 0);

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

	public boolean updateJCRoom(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteJCRoom(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public Vector getJCRoomList(String condition) {
		Vector roomList = new Vector();
		JCRoomBean room = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT jc_room.id,jc_room.name,jc_room.creator_id,jc_room.max_online_count,jc_room.pay_way,jc_room.create_datetime,jc_room.expire_datetime,jc_room.thumbnail,jc_room.grant_mode,jc_room.status,jc_room.current_online_count,jc_room.mark,jc_room.description,jc_room.top FROM jc_room  ";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " where " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				room = this.getJCRoom(rs);
				roomList.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomList;
	}

	public JCRoomBean getJCRoom(String condition) {

		JCRoomBean room = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT jc_room.id,jc_room.name,jc_room.creator_id,jc_room.max_online_count,jc_room.pay_way,jc_room.create_datetime,jc_room.expire_datetime,jc_room.thumbnail,jc_room.grant_mode,jc_room.status,jc_room.current_online_count,jc_room.mark,jc_room.description,jc_room.top FROM jc_room  ";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " where " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				room = this.getJCRoom(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return room;
	}

	public JCRoomBean getJCRoomSpec(String condition) {
		JCRoomBean room = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id,name,creator_id,max_online_count,pay_way,create_datetime,expire_datetime,thumbnail,grant_mode,status,current_online_count,mark,TO_DAYS(expire_datetime)-TO_DAYS(NOW()) as payDays FROM jc_room where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				room = this.getJCRoomSpec(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return room;
	}

	public int getJCRoomPaymentCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room_payment  where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return count;
	}

	public boolean addJCRoomPayment(RoomPaymentBean roomPayment) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_payment set user_id=?, room_id=?,  pay_type=?, money=?, pay_datetime=now(),remark=?";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomPayment.getUserId());
			pstmt.setInt(2, roomPayment.getRoomId());
			pstmt.setInt(3, roomPayment.getPayType());
			pstmt.setInt(4, roomPayment.getMoney());
			pstmt.setString(5, roomPayment.getRemark());

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

	public boolean updateJCRoomPayment(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_payment SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteJCRoomPayment(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room_payment WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public Vector getJCRoomPaymentList(String condition) {
		Vector roomPaymentList = new Vector();
		RoomPaymentBean roomPayment = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_payment";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomPayment = this.getRoomPayment(rs);
				roomPaymentList.add(roomPayment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomPaymentList;
	}

	public RoomPaymentBean getJCRoomPayment(String condition) {
		RoomPaymentBean roomPayment = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_payment";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomPayment = this.getRoomPayment(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomPayment;
	}

	public int getJCRoomManagerCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room_manager  where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return count;
	}

	public boolean addJCRoomManager(RoomManagerBean roomManager) {

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_manager set user_id=?, room_id=?, mark=?, grant_datetime=now()";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomManager.getUserId());
			pstmt.setInt(2, roomManager.getRoomId());
			pstmt.setInt(3, roomManager.getMark());
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

	public boolean updateJCRoomManager(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_manager SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteJCRoomManager(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room_manager WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// 对查询一组聊天室管理员增加了缓存
	public Vector getJCRoomManagerList(String condition) {
		Vector roomManagerList = null;
		RoomManagerBean roomManager = null;

		// 构建查询语句
		String query = "SELECT * FROM jc_room_manager";
		if (condition != null)
			query = query + " where " + condition;

		roomManagerList = (Vector) OsCacheUtil.get(query,
				OsCacheUtil.ROOM_MANAGERS_GROUP,
				OsCacheUtil.ROOM_MANAGERS_FLUSH_PERIOD);

		if (roomManagerList == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			roomManagerList = new Vector();

			// 查询

			ResultSet rs = dbOp.executeQuery(query);

			try {
				// 结果不为空
				while (rs.next()) {
					roomManager = this.getRoomManager(rs);
					if (roomManager != null)
						roomManagerList.add(roomManager);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 释放资源
			dbOp.release();
		}

		if (roomManagerList != null)

			OsCacheUtil.put(query, roomManagerList,
					OsCacheUtil.ROOM_MANAGERS_GROUP);
		else
			roomManagerList = new Vector();

		return roomManagerList;
	}

	static RoomManagerBean nullRoomManager = new RoomManagerBean();
	public RoomManagerBean getJCRoomManager(String condition) {
		RoomManagerBean roomManager = null;

		// 构建查询语句

		String query = "SELECT * FROM jc_room_manager";
		if (condition != null)
			query = query + " where " + condition;
		String key = condition;
		synchronized(roomManagerCache) {
			roomManager = (RoomManagerBean) roomManagerCache.get(key);
			if (roomManager == null) {
	
				DbOperation dbOp = new DbOperation(true);
	
				ResultSet rs = dbOp.executeQuery(query);
	
				try {
					// 结果不为空
					if (rs.next()) {
						roomManager = this.getRoomManager(rs);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (roomManager != null)
					roomManagerCache.put(key, roomManager);
				else {
					roomManagerCache.put(key, nullRoomManager);
				}

				dbOp.release();
			}
		}
		if (roomManager == nullRoomManager)
			return null;
		return roomManager;
	}

	public int getJCRoomUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room_user  where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return count;
	}

	public boolean addJCRoomUser(RoomUserBean roomUser) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_user set user_id=?, room_id=?,  manager_id=?, apply_datetime=now(),status=?";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomUser.getUserId());
			pstmt.setInt(2, roomUser.getRoomId());
			pstmt.setInt(3, roomUser.getManagerId());
			pstmt.setInt(4, roomUser.getStatus());

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

	public boolean updateJCRoomUser(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_user SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteJCRoomUser(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room_user WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public Vector getJCRoomUserList(String condition) {
		Vector roomUserList = new Vector();
		RoomUserBean roomUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_user  ";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomUser = this.getRoomUserBean(rs);
				roomUserList.add(roomUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomUserList;
	}

	public RoomUserBean getJCRoomUser(String condition) {
		RoomUserBean roomUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_user";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomUser = this.getRoomUserBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomUser;
	}

	public int getJCRoomGrantCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room_grant  where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return count;
	}

	public boolean addJCRoomGrant(RoomGrantBean roomGrant) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_grant set user_id=?, room_id=?,  manager_id=?, grant_datetime=now(),grant_type=?";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomGrant.getUserId());
			pstmt.setInt(2, roomGrant.getRoomId());
			pstmt.setInt(3, roomGrant.getManagerId());
			pstmt.setInt(4, roomGrant.getGrantType());

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

	public boolean updateJCRoomGrant(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_grant SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteJCRoomGrant(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room_grant WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public Vector getJCRoomGrantList(String condition) {
		Vector roomGrantList = new Vector();
		RoomGrantBean roomGrant = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_grant";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomGrant = this.getRoomGrant(rs);
				roomGrantList.add(roomGrant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomGrantList;
	}

	public RoomGrantBean getJCRoomGrant(String condition) {
		RoomGrantBean roomGrant = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_grant";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomGrant = this.getRoomGrant(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomGrant;
	}

	public int getJCRoomAfficheCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as id FROM jc_room_affiche  where "
				+ condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return count;
	}

	public boolean addJCRoomAffiche(RoomAfficheBean roomAffiche) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_affiche set user_id=?, room_id=?, content=?, create_datetime=now()";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomAffiche.getUserId());
			pstmt.setInt(2, roomAffiche.getRoomId());
			pstmt.setString(3, roomAffiche.getContent());

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

	public boolean updateJCRoomAffiche(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_affiche SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteJCRoomAffiche(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room_affiche WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public Vector getJCRoomAfficheList(String condition) {
		Vector roomAfficheList = new Vector();
		RoomAfficheBean roomAffiche = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_affiche";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomAffiche = this.getRoomAffiche(rs);
				roomAfficheList.add(roomAffiche);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomAfficheList;
	}

	public RoomAfficheBean getJCRoomAffiche(String condition) {
		RoomAfficheBean roomAffiche = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_affiche";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomAffiche = this.getRoomAffiche(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomAffiche;
	}

	// mcq_2006-6-27_增加自建聊天室方法—_end

	// 李北金_2006-6-27_查询优化，主要是避免使用or，非特殊情况请勿使用
	public Vector getMessageList0(String query) {
		Vector MessageList = new Vector();
		JCRoomContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				content = this.getJCRoomConetnt(rs);
				MessageList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return MessageList;
	}

	// 李北金_2006-6-27_查询优化

	// 李北金_2006-6-27_查询优化，主要是避免使用or，非特殊情况请勿使用
	public int getMessageCount0(String query) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// 李北金_2006-6-27_查询优化

	private JCRoomCompainBean getJCRoomCompain(ResultSet rs)
			throws SQLException {
		JCRoomCompainBean compain = new JCRoomCompainBean();
		compain.setId(rs.getInt("id"));
		compain.setLeftUserId(rs.getInt("left_user_id"));
		compain.setRightUserId(rs.getInt("right_user_id"));
		compain.setContent(rs.getString("content"));
		compain.setMark(rs.getInt("mark"));
		compain.setEnterDateTime(rs.getString("enter_datetime"));
		return compain;
	}

	private JCRoomContentBean getJCRoomConetnt(ResultSet rs)
			throws SQLException {
		JCRoomContentBean content = new JCRoomContentBean();
		content.setId(rs.getInt("id"));
		content.setFromId(rs.getInt("from_id"));
		content.setToId(rs.getInt("to_id"));
		content.setFromNickName(rs.getString("from_nickname"));
		content.setToNickName(rs.getString("to_nickname"));
		content.setContent(rs.getString("content"));
		content.setAttach(rs.getString("attach"));
		content.setSendDateTime(rs.getString("send_datetime"));
		content.setIsPrivate(rs.getInt("is_private"));
		content.setRoomId(rs.getInt("room_id"));
		// fanys 2006-06-26 start
		content.setMark(rs.getInt("mark"));
		// fanys 2006-06-26 end
		return content;
	}

	private JCRoomBean getJCRoom(ResultSet rs) throws SQLException {
		JCRoomBean room = new JCRoomBean();
		room.setId(rs.getInt("jc_room.id"));
		room.setName(rs.getString("jc_room.name"));
		// fanys 2006-06-26 start
		room.setCreatorId(rs.getInt("jc_room.creator_id"));
		room.setMaxOnlineCount(rs.getInt("jc_room.max_online_count"));
		room.setPayWay(rs.getInt("jc_room.pay_way"));
		// room.setIntroduce(rs.getString("introduce"));
		room.setCreateDatetime(rs.getString("jc_room.create_datetime"));
		room.setExpireDatetime(rs.getString("jc_room.expire_datetime"));
		room.setThumbnail(rs.getString("jc_room.thumbnail"));
		room.setGrantMode(rs.getInt("jc_room.grant_mode"));
		room.setStatus(rs.getInt("jc_room.status"));
		room.setCurrentOnlineCount(rs.getInt("jc_room.current_online_count"));
		room.setMark(rs.getInt("jc_room.mark"));
		room.setDescription(rs.getString("jc_room.description"));
		room.setTop(rs.getInt("jc_room.top"));
		// fanys 2006-06-26 end
		return room;
	}

	private JCRoomBean getJCRoomSpec(ResultSet rs) throws SQLException {
		JCRoomBean room = new JCRoomBean();
		room.setId(rs.getInt("id"));
		room.setName(rs.getString("name"));
		// fanys 2006-06-26 start
		room.setCreatorId(rs.getInt("creator_id"));
		room.setMaxOnlineCount(rs.getInt("max_online_count"));
		room.setPayWay(rs.getInt("pay_way"));
		// add by zhangyi 2006-06-28 start
		room.setPayDays(rs.getInt("payDays"));
		// add by zhangyi 2006-06-28 end
		// room.setIntroduce(rs.getString("introduce"));
		room.setCreateDatetime(rs.getString("create_datetime"));
		room.setExpireDatetime(rs.getString("expire_datetime"));
		room.setThumbnail(rs.getString("thumbnail"));
		room.setGrantMode(rs.getInt("grant_mode"));
		room.setStatus(rs.getInt("status"));
		room.setCurrentOnlineCount(rs.getInt("current_online_count"));
		// fanys 2006-06-26 end
		return room;
	}

	private JCRoomOnlineBean getJCRoomOnline(ResultSet rs) throws SQLException {
		JCRoomOnlineBean online = new JCRoomOnlineBean();
		online.setId(rs.getInt("id"));
		online.setRoomId(rs.getInt("room_id"));
		online.setUserId(rs.getInt("user_id"));
		online.setEnterDateTime(rs.getString("enter_datetime"));
		return online;
	}

	private JCRoomForbidBean getJCRoomForbid(ResultSet rs) throws SQLException {
		JCRoomForbidBean bid = new JCRoomForbidBean();
		bid.setId(rs.getInt("id"));
		bid.setUserId(rs.getInt("user_id"));
		return bid;
	}

	private RoomPaymentBean getRoomPayment(ResultSet rs) throws SQLException {
		RoomPaymentBean roomPayment = new RoomPaymentBean();
		roomPayment.setId(rs.getInt("id"));
		roomPayment.setUserId(rs.getInt("user_id"));
		roomPayment.setRoomId(rs.getInt("room_id"));
		roomPayment.setPayType(rs.getInt("pay_type"));
		roomPayment.setMoney(rs.getInt("money"));
		roomPayment.setPayDatetime(rs.getString("pay_datetime"));
		roomPayment.setRemark(rs.getString("remark"));
		return roomPayment;
	}

	private RoomManagerBean getRoomManager(ResultSet rs) throws SQLException {
		RoomManagerBean roomManager = new RoomManagerBean();
		roomManager.setId(rs.getInt("id"));
		roomManager.setUserId(rs.getInt("user_id"));
		roomManager.setRoomId(rs.getInt("room_id"));
		roomManager.setMark(rs.getInt("mark"));
		roomManager.setGrantDatetime(rs.getString("grant_datetime"));
		return roomManager;
	}

	private RoomUserBean getRoomUserBean(ResultSet rs) throws SQLException {
		RoomUserBean roomUser = new RoomUserBean();
		roomUser.setId(rs.getInt("id"));
		roomUser.setUserId(rs.getInt("user_id"));
		roomUser.setRoomId(rs.getInt("room_id"));
		roomUser.setManagerId(rs.getInt("manager_id"));
		roomUser.setApplyDatetime(rs.getString("apply_datetime"));
		roomUser.setGrantDatetime(rs.getString("grant_datetime"));
		roomUser.setStatus(rs.getInt("status"));
		return roomUser;
	}

	private RoomGrantBean getRoomGrant(ResultSet rs) throws SQLException {
		RoomGrantBean roomGrant = new RoomGrantBean();
		roomGrant.setId(rs.getInt("id"));
		roomGrant.setUserId(rs.getInt("user_id"));
		roomGrant.setRoomId(rs.getInt("room_id"));
		roomGrant.setManagerId(rs.getInt("manager_id"));
		roomGrant.setGrantDatetime(rs.getString("grant_datetime"));
		roomGrant.setGrantType(rs.getInt("grant_type"));
		return roomGrant;
	}

	private RoomAfficheBean getRoomAffiche(ResultSet rs) throws SQLException {
		RoomAfficheBean roomAffiche = new RoomAfficheBean();
		roomAffiche.setId(rs.getInt("id"));
		roomAffiche.setUserId(rs.getInt("user_id"));
		roomAffiche.setRoomId(rs.getInt("room_id"));
		roomAffiche.setContent(rs.getString("content"));
		roomAffiche.setCreateDatetime(rs.getString("create_datetime"));
		return roomAffiche;
	}

	private RoomHallInviteBean getRoomHallInvite(ResultSet rs)
			throws SQLException {
		RoomHallInviteBean roomHallInvite = new RoomHallInviteBean();
		roomHallInvite.setId(rs.getInt("id"));
		roomHallInvite.setUserId(rs.getInt("user_id"));
		roomHallInvite.setToId(rs.getInt("to_id"));
		roomHallInvite.setCreateDatetime(rs.getString("create_datetime"));
		return roomHallInvite;
	}

	/**
	 * fanys 2006-08-15
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private RoomInviteBean getRoomInvite(ResultSet rs) throws SQLException {
		RoomInviteBean roomInvite = new RoomInviteBean();
		roomInvite.setId(rs.getInt("id"));
		roomInvite.setUserId(rs.getInt("user_id"));
		roomInvite.setMobile(rs.getString("mobile"));
		roomInvite.setName(rs.getString("name"));
		roomInvite.setContent(rs.getString("content"));
		roomInvite.setSendDatetime(rs.getString("send_datetime"));
		roomInvite.setMark(rs.getInt("mark"));
		roomInvite.setSendMark(rs.getInt("send_mark"));
		roomInvite.setSuccessMark(rs.getInt("success_mark"));
		roomInvite.setInviteeId(rs.getInt("invitee_id"));
		roomInvite.setLoginDateTime(rs.getString("login_datetime"));
		roomInvite.setNewUserMark(rs.getInt("new_user_mark"));
		return roomInvite;
	}

	/**
	 * fanys 2006-08-15
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public RoomInviteResourceBean getRoomInviteResource(ResultSet rs)
			throws SQLException {
		RoomInviteResourceBean resource = new RoomInviteResourceBean();
		resource.setId(rs.getInt("id"));
		resource.setImage(rs.getString("image"));
		return resource;
	}

	public RoomInviteRankBean getRoomInviteRank(ResultSet rs)
			throws SQLException {
		RoomInviteRankBean rank = new RoomInviteRankBean();
		rank.setId(rs.getInt("id"));
		rank.setResourceId(rs.getInt("resource_id"));
		rank.setUserId(rs.getInt("user_id"));
		rank.setCount(rs.getInt("count"));
		rank.setCreateDateTime(rs.getString("create_datetime"));
		rank.setNickName(rs.getString("nickname"));
		return rank;
	}

	// zhul_2006-07-06_增加统计聊天室公聊消息方法_start
	public boolean addJCRoomContentCount(JCRoomContentCountBean roomContentCount) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_content_count set room_id=?, count=0 ";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomContentCount.getRoomId());

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

	public boolean updateJCRoomContentCount(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_content_count SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;

	}

	public boolean deleteJCRoomContentCount(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_room_content_count WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;

	}

	public Vector getJCRoomContentCountList(String condition) {
		Vector roomContentCountList = new Vector();
		JCRoomContentCountBean roomContentCount = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_content_count ";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				roomContentCount = new JCRoomContentCountBean();
				roomContentCount.setId(rs.getInt("id"));
				roomContentCount.setRoomId(rs.getInt("room_id"));
				roomContentCount.setCount(rs.getInt("count"));

				roomContentCountList.add(roomContentCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return roomContentCountList;

	}

	public JCRoomContentCountBean getJCRoomContentCount(String condition) {
		JCRoomContentCountBean roomContentCount = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_room_content_count ";
		if (condition != null)
			query = query + " where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				roomContentCount = new JCRoomContentCountBean();
				roomContentCount.setId(rs.getInt("id"));
				roomContentCount.setRoomId(rs.getInt("room_id"));
				roomContentCount.setCount(rs.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return roomContentCount;

	}

	// zhul_2006-07-06_增加统计聊天室公聊消息方法_end

	// mcq_2006-7-9_申请自建聊天室方法_start
	public RoomApplyBean getRoomApply(String condition) {
		RoomApplyBean roomApply = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_apply";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				roomApply = new RoomApplyBean();
				roomApply.setId(rs.getInt("id"));
				roomApply.setUserId(rs.getInt("user_id"));
				roomApply.setRoomName(rs.getString("room_name"));
				roomApply.setRoomSubject(rs.getString("room_subject"));
				roomApply.setRoomEnounce(rs.getString("room_enounce"));
				roomApply.setVoteCount(rs.getInt("vote_count"));
				roomApply.setApplyDatetime(rs.getString("apply_datetime"));
				roomApply.setMark(rs.getInt("mark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomApply;
	}

	public Vector getRoomApplyList(String condition) {
		Vector RoomApplylist = new Vector();
		RoomApplyBean roomApply = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_apply";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				roomApply = new RoomApplyBean();
				roomApply.setId(rs.getInt("id"));
				roomApply.setUserId(rs.getInt("user_id"));
				roomApply.setRoomName(rs.getString("room_name"));
				roomApply.setRoomSubject(rs.getString("room_subject"));
				roomApply.setRoomEnounce(rs.getString("room_enounce"));
				roomApply.setVoteCount(rs.getInt("vote_count"));
				roomApply.setApplyDatetime(rs.getString("apply_datetime"));
				roomApply.setMark(rs.getInt("mark"));
				RoomApplylist.add(roomApply);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return RoomApplylist;
	}

	public int getRoomApplyCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_room_apply WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public boolean addRoomApply(RoomApplyBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_apply("
				+ "user_id, room_name, room_subject, room_enounce,"
				+ "apply_datetime, mark) VALUES(?, ?, ?, ?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getRoomName());
			pstmt.setString(3, bean.getRoomSubject());
			pstmt.setString(4, bean.getRoomEnounce());
			pstmt.setInt(5, bean.getMark());
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

	public boolean delRoomApply(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_room_apply WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateRoomApply(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_room_apply SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	// mcq_2006-7-9_申请自建聊天室方法_end

	// mcq_2006-7-9_自建聊天室投票方法_start
	public RoomVoteBean getRoomVote(String condition) {
		RoomVoteBean roomVote = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_vote";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				roomVote = new RoomVoteBean();
				roomVote.setId(rs.getInt("id"));
				roomVote.setUserId(rs.getInt("user_id"));
				roomVote.setApplyId(rs.getInt("apply_id"));
				roomVote.setVoteDatetime(rs.getString("vote_datetime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomVote;
	}

	public Vector getRoomVoteList(String condition) {
		Vector RoomVoteList = new Vector();
		RoomVoteBean roomVote = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_vote";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				roomVote = new RoomVoteBean();
				roomVote.setId(rs.getInt("id"));
				roomVote.setUserId(rs.getInt("user_id"));
				roomVote.setApplyId(rs.getInt("apply_id"));
				roomVote.setVoteDatetime(rs.getString("vote_datetime"));
				RoomVoteList.add(roomVote);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return RoomVoteList;
	}

	public int getRoomVoteCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_room_vote WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public boolean addRoomVote(RoomVoteBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_vote("
				+ "user_id, apply_id, vote_datetime) VALUES(?, ?, now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getApplyId());
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

	public boolean delRoomVote(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_room_vote WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateRoomVote(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_room_vote SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	// mcq_2006-7-9_自建聊天室投票方法_end

	// mcq_2006-7-12_自建聊天室邀请次数方法_start
	public RoomHallInviteBean getRoomHallInvite(String condition) {
		RoomHallInviteBean roomHallInvite = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_hall_invite";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				roomHallInvite = this.getRoomHallInvite(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomHallInvite;
	}

	public Vector getRoomHallInviteList(String condition) {
		Vector roomHallInviteList = new Vector();
		RoomHallInviteBean roomHallInvite = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_hall_invite";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				roomHallInvite = this.getRoomHallInvite(rs);
				roomHallInviteList.add(roomHallInvite);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomHallInviteList;
	}

	public boolean addRoomHallInvite(RoomHallInviteBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_hall_invite(user_id,to_id,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getToId());
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

	public boolean delRoomHallInvite(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_room_hall_invite WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateRoomHallInvite(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_room_hall_invite SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getRoomHallInviteCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_room_hall_invite WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// mcq_2006-7-12_自建聊天室邀请次数方法_end

	// mcq_2006-7-12_自建聊天室通过push邀请次数方法_start
	public RoomInviteBean getRoomInvite(String condition) {
		RoomInviteBean roomInvite = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_invite";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				roomInvite = this.getRoomInvite(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomInvite;
	}

	public Vector getRoomInviteList(String condition) {
		Vector roomInviteList = new Vector();
		RoomInviteBean roomInvite = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_invite";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				roomInvite = this.getRoomInvite(rs);
				roomInviteList.add(roomInvite);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return roomInviteList;
	}

	public boolean addRoomInvite(RoomInviteBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_invite(user_id,mobile,name,content,new_user_mark,send_datetime,mark,send_mark,success_mark,invitee_id) "
				+ "VALUES(?,?,?,?,?,now(),0,1,1,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getMobile());
			pstmt.setString(3, bean.getName());
			pstmt.setString(4, bean.getContent());
			pstmt.setInt(5, bean.getNewUserMark());
			pstmt.setInt(6, bean.getInviteeId());
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
	// 直接插入邀请成功记录
	public boolean addRoomInviteOk(RoomInviteBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_invite(user_id,mobile,name,content,new_user_mark,send_datetime,login_datetime,mark,send_mark,success_mark,invitee_id) "
				+ "VALUES(?,?,?,?,?,now(),now(),0,1,1,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getMobile());
			pstmt.setString(3, bean.getName());
			pstmt.setString(4, bean.getContent());
			pstmt.setInt(5, bean.getNewUserMark());
			pstmt.setInt(6, bean.getInviteeId());
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

	public boolean delRoomInvite(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_room_invite WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateRoomInvite(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_room_invite SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getRoomInviteCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = null;
		if (condition.indexOf("temp") < 0)
			query = "SELECT count(id) as c_id FROM jc_room_invite WHERE "
					+ condition;
		else
			query = condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// mcq_2006-7-12_自建聊天室通过push邀请次数方法_end

	public int getMaxRoomInviteId(String condition) {
		int maxId = 0;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "SELECT max(id) as maxId FROM jc_room_invite WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				maxId = rs.getInt("maxId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return maxId;
	}

	public Vector getRoomInviteResourceList(String condition) {
		Vector list = new Vector();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		RoomInviteResourceBean resource = null;
		String query = "SELECT * FROM jc_room_invite_resource WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				resource = this.getRoomInviteResource(rs);
				list.add(resource);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}

	public Vector getRoomInviteRankList(String condition) {
		Vector list = new Vector();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		RoomInviteRankBean rank = null;
		String query = "SELECT a.*,b.nickname FROM jc_room_invite_rank  as a  join user_info as b on a.user_id=b.id WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				rank = this.getRoomInviteRank(rs);
				list.add(rank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}

	/**
	 * fanys 2006-08-15
	 */
	public RoomInviteResourceBean getRoomInviteResource(String condition) {
		RoomInviteResourceBean resource = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "SELECT * FROM jc_room_invite_resource WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				resource = this.getRoomInviteResource(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return resource;
	}

	public Vector getCurRoomInviteRankList(String condition) {
		Vector list = new Vector();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		RoomInviteRankBean rank = null;
		String query = "select a.id,a.nickname, count(*) as count from user_info as a join jc_room_invite as  b on a.id=b.user_id where ";
		if (condition != null)
			query = query + condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				rank = new RoomInviteRankBean();
				rank.setUserId(rs.getInt("id"));
				rank.setNickName(rs.getString("nickname"));
				rank.setCount(rs.getInt("count"));
				list.add(rank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}

	public boolean addRoomInviteRankBean(RoomInviteRankBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_invite_rank(user_id,count,resource_id,create_datetime) "
				+ "" + "VALUES(?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getCount());
			pstmt.setInt(3, bean.getResourceId());
			pstmt.setString(4, bean.getCreateDateTime());
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

	public boolean addRoomInviteStat(RoomInviteStatBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_invite_stat(stat_datetime,invite_count,accept_new_count,reply_count,reply_new_count,reach_limit_count) "
				+ "VALUES(?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getStatDatetime());
			pstmt.setInt(2, bean.getInviteCount());
			pstmt.setInt(3, bean.getAcceptNewCount());
			pstmt.setInt(4, bean.getReplyCount());
			pstmt.setInt(5, bean.getReplyNewCount());
			pstmt.setInt(6, bean.getReachLimitCount());
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

	public void deleteRoomInviteStat(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "delete from jc_room_invite_stat where  " + condition;
		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	public boolean updateRoomInviteStat(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_invite_stat SET " + set + " WHERE "
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
	 * @see net.joycool.wap.service.infc.IChatService#addRoomRate(net.joycool.wap.bean.chat.RoomRateBean)
	 */
	public boolean addRoomRate(RoomRateBean roomRate) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_room_rate(room_id,room_name, room_rate) values(?,?,?) ";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, roomRate.getRoomId());
			pstmt.setString(2, roomRate.getName());
			pstmt.setInt(3, roomRate.getRate());
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#deleteRoomRate(java.lang.String)
	 */
	public boolean deleteRoomRate(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_room_rate WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#getRoomRate(java.lang.String)
	 */
	public RoomRateBean getRoomRate(String condition) {

		RoomRateBean room = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, room_id, room_name, room_rate FROM jc_room_rate  ";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " where " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				room = new RoomRateBean();
				room.setId(rs.getInt("id"));
				room.setRoomId(rs.getInt("room_id"));
				room.setName(rs.getString("room_name"));
				room.setRate(rs.getInt("room_rate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return room;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#getRoomRateCount(java.lang.String)
	 */
	public int getRoomRateCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#getRoomRateList(java.lang.String)
	 */
	public ArrayList getRoomRateList(String condition) {

		ArrayList roomList = new ArrayList();
		RoomRateBean room = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, room_id, room_name, room_rate FROM jc_room_rate  ";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " where " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				room = new RoomRateBean();
				room.setId(rs.getInt("id"));
				room.setRoomId(rs.getInt("room_id"));
				room.setName(rs.getString("room_name"));
				room.setRate(rs.getInt("room_rate"));
				roomList.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return roomList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#updateRoomRate(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateRoomRate(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_rate SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * mcq_2006-09-5_获取满足特定条件全部聊天室记录id_end 参数:sql语句
	 */
	public Vector getRoomIdCountList(String condition) {
		Vector roomIdCountList = new Vector();
		int count = 0;
		// JCRoomContentBean roomContent = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = condition;

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				// roomContent = new JCRoomContentBean();
				count = rs.getInt("id");
				roomIdCountList.add(new Integer(count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return roomIdCountList;

	}

	/**
	 * mcq_2006-09-5_更新聊天室记录_start
	 */
	public boolean updateRoomContent(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_room_content SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// mcq_2006-09-5_更新聊天室记录_end

	/*
	 * (non-Javadoc) zhul_2006-09-05 获取聊天室在线人数
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#getRoomOnlineNum(java.lang.String)
	 */
	public int getRoomOnlineNum(String roomId) {

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT current_online_count FROM jc_room where id="
				+ roomId;

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				return rs.getInt("current_online_count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return 0;
	}

	public RoomInviteRankBean getRoomInviteRank(String condition) {

		RoomInviteRankBean rank = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_room_invite_rank";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				rank = new RoomInviteRankBean();
				rank.setId(rs.getInt("id"));
				rank.setResourceId(rs.getInt("resource_id"));
				rank.setUserId(rs.getInt("user_id"));
				rank.setCount(rs.getInt("count"));
				rank.setCreateDateTime(rs.getString("create_datetime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return rank;
	}

	/*
	 * zhul 2006-09-16 获取所有聊天室ID
	 * 
	 * @see net.joycool.wap.service.infc.IChatService#getAllRoomId()
	 */
	public int[] getAllRoomId() {

		int rooms = this.getJCRoomCount("0=0");
		int[] room = new int[rooms];
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id FROM jc_room ";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			int i = 0;
			while (rs.next()) {
				room[i] = rs.getInt("id");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return room;
	}

	public boolean addChatSpeaker(ChatSpeakerBean bean) {
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into chat_speaker set uid = " + bean.getUid() + ", name='" + bean.getName() + "',create_time = now(), mark = " + bean.getMark() + ", content = '" + bean.getContent() + "'";
		dbOp.executeUpdate(query);
		bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateChatSpeaker(int id) {
		DbOperation dbOp = new DbOperation(5);
		String query = "update chat_speaker set mark = 1 where id = " + id;
		dbOp.executeUpdate(query);
		dbOp.release();
		return true;
	}
	
	private ChatSpeakerBean getChatSpeaker(ResultSet rs) throws SQLException{
		ChatSpeakerBean bean = new ChatSpeakerBean();
		bean.setId(rs.getInt("id"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time"));
		bean.setUid(rs.getInt("uid"));
		bean.setName(rs.getString("name"));
		bean.setMark(rs.getInt("mark"));
		
		return bean;
	}
	
	public void getChatSpeaker(String condition) {
		DbOperation dbOp = new DbOperation(5);
		
		String query = "select * from chat_speaker";
		
		if(condition != null) {
			query += " where " + condition;
		}
		
		ResultSet rs = dbOp.executeQuery(query);
		
		try{
			synchronized(JCRoomChatAction.speakerList){
				while(rs.next()) {
					ChatSpeakerBean bean = getChatSpeaker(rs);
					JCRoomChatAction.speakerList.addLast(bean);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			dbOp.release();
		}
	}
	
}
