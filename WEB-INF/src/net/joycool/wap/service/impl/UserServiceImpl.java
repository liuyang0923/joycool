/*
 * Created on 2005-7-26
 *
 */
package net.joycool.wap.service.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.ActionRecordBean;
import net.joycool.wap.bean.BlackListUserBean;
import net.joycool.wap.bean.CartBean;
import net.joycool.wap.bean.CrownBean;
import net.joycool.wap.bean.OnlineBean;
import net.joycool.wap.bean.RankActionBean;
import net.joycool.wap.bean.RankBean;
import net.joycool.wap.bean.ShortcutBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserFriendBean;
import net.joycool.wap.bean.UserHonorBean;
import net.joycool.wap.bean.UserMoneyLogBean;
import net.joycool.wap.bean.UserNoteBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.item.ComposeBean;
import net.joycool.wap.bean.item.ShowBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.Integer2;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class UserServiceImpl implements IUserService {
	static ICacheMap jsFriendsCache = CacheManage.jyFriends;
	static ICacheMap userFriendCache = CacheManage.userFriend;
	static ICacheMap badGuyCache = CacheManage.badGuy;
	static ICacheMap stuffCache = CacheManage.stuff;
	
	public boolean addUserSetting(UserSettingBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO user_setting(user_id,pic_mark,notice_mark,bank_pw,update_datetime,shortcut) VALUES(?,?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getPicMark());
			pstmt.setInt(3, bean.getNoticeMark());
			pstmt.setString(4, bean.getBankPw());
			pstmt.setString(5, bean.getShortcut());
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
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getUserSetting(java.lang.String)
	 */
	public UserSettingBean getUserSetting(String condition) {
		UserSettingBean userSetting = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from user_setting";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				userSetting = this.getUserSetting(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userSetting;
	}

	private UserSettingBean getUserSetting(ResultSet rs) throws SQLException {
		UserSettingBean userSetting = new UserSettingBean();
		userSetting.setId(rs.getInt("id"));
		userSetting.setUserId(rs.getInt("user_id"));
		userSetting.setPicMark(rs.getInt("pic_mark"));
		userSetting.setNoticeMark(rs.getInt("notice_mark"));
		userSetting.setBankPw(rs.getString("bank_pw"));
		userSetting.setShortcut(rs.getString("shortcut"));
		userSetting.setShortcut2(rs.getString("shortcut2"));
		userSetting.setBagSeq(rs.getString("bag_seq"));
		userSetting.setUpdateDatetime(rs.getString("update_datetime"));
		userSetting.setForumOrder(rs.getInt("forum_order"));
		userSetting.setHomeAllow(rs.getInt("home_allow"));
		userSetting.setHomePassword(rs.getString("home_password"));
		return userSetting;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#updateUserSetting(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateUserSetting(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE user_setting SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getMoneyLogCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM user_money_log WHERE "
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

	public boolean addMoneyLog(UserMoneyLogBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO user_money_log(from_id,to_id,money,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFromId());
			pstmt.setInt(2, bean.getToId());
			pstmt.setInt(3, bean.getMoney());
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

	public UserMoneyLogBean getMoneyLog(String condition) {
		UserMoneyLogBean userMoneyLog = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from user_money_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				userMoneyLog = this.getUserMoneyLog(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userMoneyLog;
	}

	public List getMoneyLogList(String condition) {
		List userMoneyLogList = new ArrayList();
		UserMoneyLogBean userMoneyLog = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from user_money_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				userMoneyLog = this.getUserMoneyLog(rs);
				userMoneyLogList.add(userMoneyLog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userMoneyLogList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#addUserBag(net.joycool.wap.bean.UserBagBean)
	 */
	public static long MAX_DUE = 3600000l * 24 * 1000;		// 物品最大过期时间，1000天
	public boolean addUserBag(UserBagBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO user_bag(user_id,product_id,type_id,time,mark,create_datetime,end_datetime,creator_id) VALUES(?,?,?,?,?,now(),?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getProductId());
			pstmt.setInt(3, bean.getTypeId());
			pstmt.setInt(4, bean.getTime());
			pstmt.setInt(5, bean.getMark());
			if(bean.getEndTime() == 0)
				bean.setEndTime(System.currentTimeMillis() + MAX_DUE);
			pstmt.setTimestamp(6, new Timestamp(bean.getEndTime()));
			pstmt.setInt(7, bean.getCreatorId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#deleteUserBag(java.lang.String)
	 */
	public boolean deleteUserBag(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM user_bag WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getDummyType(java.lang.String)
	 */
	public UserBagBean getUserBag(String condition) {
		UserBagBean userBag = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from user_bag";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				userBag = this.getUserBag(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userBag;
	}

	public HashMap getUserBagMap(String condition) {
		HashMap userBagMap = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT product_id,count(id) from user_bag where "
				+ condition + " group by product_id order by product_id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			userBagMap = new HashMap();
			while (rs.next()) {
				int item = rs.getInt(1);
				int count = rs.getInt(2);
				userBagMap.put(Integer.valueOf(item), Integer.valueOf(count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return userBagMap;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getUserBagCount(java.lang.String)
	 */
	public int getUserBagCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM user_bag WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getUserBagList(java.lang.String)
	 */
	public Vector getUserBagList(String condition) {
		Vector userBagList = new Vector();
		UserBagBean userBag = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from user_bag";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				userBag = this.getUserBag(rs);
				userBagList.add(userBag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userBagList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#updateUserBag(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateUserBag(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE user_bag SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private UserBagBean getUserBag(ResultSet rs) throws SQLException {
		UserBagBean userBag = new UserBagBean();
		userBag.setId(rs.getInt("id"));
		userBag.setUserId(rs.getInt("user_id"));
		userBag.setCreatorId(rs.getInt("creator_id"));
		userBag.setProductId(rs.getInt("product_id"));
		userBag.setTypeId(rs.getInt("type_id"));
		userBag.setTime(rs.getInt("time"));
		userBag.setMark(rs.getInt("mark"));
		userBag.setUseTime(rs.getTimestamp("use_datetime").getTime());
		userBag.setEndTime(rs.getTimestamp("end_datetime").getTime());
		return userBag;
	}

	private UserMoneyLogBean getUserMoneyLog(ResultSet rs) throws SQLException {
		UserMoneyLogBean userMoneyLog = new UserMoneyLogBean();
		userMoneyLog.setId(rs.getInt("id"));
		userMoneyLog.setFromId(rs.getInt("from_id"));
		userMoneyLog.setToId(rs.getInt("to_id"));
		userMoneyLog.setMoney(rs.getInt("money"));
		userMoneyLog.setCreateDatetime(rs.getString("create_datetime"));
		return userMoneyLog;
	}

	public static HashMap wsTypeList;

	public boolean addUser(UserBean user) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO user_info("
				+ "password, mobile, nickname, gender, "
				+ "self_introduction, age, placeno, cityname, cityno, create_datetime, id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, now(),?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, user.getPassword());
			pstmt.setString(2, user.getMobile());
			pstmt.setString(3, user.getNickName());
			pstmt.setInt(4, user.getGender());
			pstmt.setString(5, user.getSelfIntroduction());
			pstmt.setInt(6, user.getAge());
			pstmt.setInt(7, user.getPlaceno());
			pstmt.setString(8, user.getCityname());
			pstmt.setInt(9, user.getCityno());
			pstmt.setInt(10, user.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();

		// 输出LOG
		LogUtil.logUser(user.getMobile() + ":" + user.getNickName() + ":"
				+ user.getCityno() + ":" + user.getCityname());

		return true;
	}

	/*
	 * @see net.mcool.www.service.infc.IUserService#getUser(java.lang.String)
	 */
	public UserBean getUser(String condition) {
		UserBean user = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// liuyi 2007-01-22 用户信息添加 start
		// 构建查询语句
		// 李北金_2006-06-20_查询优化_start
		String query = "SELECT * FROM user_info";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join ") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}
		// 李北金_2006-06-20_查询优化_end
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// liuyi 2007-01-22 用户信息添加 end
		// 释放资源
		dbOp.release();

		return user;
	}

	/**
	 * 获取用户最后一次登录用户信息
	 */
	public UserBean getLastLoginUser(String mobile) {
		UserBean user = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// liuyi 2007-01-22 用户信息添加 start
		// 构建查询语句
		String query = "SELECT a.* FROM user_info a  left join user_status b on a.id=b.user_id where mobile='"
				+ mobile + "' order by b.last_login_time desc limit 1";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// liuyi 2007-01-22 用户信息添加 end
		// 释放资源
		dbOp.release();

		return user;
	}

	/**
	 * 根据mid获取用户最后一次登录用户信息
	 */
	public UserBean getLastLoginUserByMid(String mid) {
		UserBean user = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// liuyi 2007-01-22 用户信息添加 start
		// 构建查询语句
		String query = "SELECT a.* FROM user_info a  left join user_status b on a.id=b.user_id where mid='"
				+ mid + "' order by b.last_login_time desc limit 1";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// liuyi 2007-01-22 用户信息添加 end
		// 释放资源
		dbOp.release();

		return user;
	}

	public UserBean getUser(ResultSet rs) throws SQLException {
		UserBean user = new UserBean();
		user.setGender(rs.getInt("gender"));
		user.setId(rs.getInt("id"));
		user.setMobile(rs.getString("mobile"));
		user.setPassword(rs.getString("password"));
		user.setNickName(rs.getString("nickname"));
		user.setSelfIntroduction(rs.getString("self_introduction"));
		user.setAge(rs.getInt("age"));
		user.setCityname(rs.getString("cityname"));
		user.setCityno(rs.getInt("cityno"));
		user.setPlaceno(rs.getInt("placeno"));
		user.setHome(rs.getInt("home"));
		user.setFriend(rs.getInt("friend"));
		user.setCreateDatetime(rs.getTimestamp("create_datetime"));
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mcool.www.service.infc.IUserService#getUserList(java.lang.String)
	 */
	/*
	 * public Vector getUserList(String condition) { Vector userList = new
	 * Vector(); UserBean user = null; // 数据库操作类 DbOperation dbOp = new
	 * DbOperation(); dbOp.init(); // 构建查询语句 // 李北金_2006-06-20_查询优化_start String
	 * query = "SELECT " + "user_info.id, user_info.user_name,
	 * user_info.password, user_info.gender, " + "user_info.nickname,
	 * user_info.mobile, user_info.self_introduction, " + "user_info.age,
	 * user_info.placeno, user_info.cityname, user_info.cityno FROM user_info"; //
	 * 李北金_2006-06-20_查询优化_end if (condition != null) { if
	 * (condition.toLowerCase().indexOf("join ") != -1) { query = query + " " +
	 * condition; } else { query = query + " WHERE " + condition; } } // 查询
	 * ResultSet rs = dbOp.executeQuery(query);
	 * 
	 * try { // 结果不为空 while (rs.next()) { user = new UserBean();
	 * user.setGender(rs.getInt("gender")); user.setId(rs.getInt("id"));
	 * user.setMobile(rs.getString("mobile"));
	 * user.setPassword(rs.getString("password"));
	 * user.setUserName(rs.getString("user_name"));
	 * user.setNickName(rs.getString("nickname"));
	 * user.setSelfIntroduction(rs.getString("self_introduction"));
	 * user.setAge(rs.getInt("age"));
	 * user.setCityname(rs.getString("cityname"));
	 * user.setCityno(rs.getInt("cityno"));
	 * user.setPlaceno(rs.getInt("placeno")); userList.add(user); } } catch
	 * (SQLException e) { e.printStackTrace(); } // 释放资源 dbOp.release();
	 * 
	 * return userList; }
	 */
	/**
	 * zhul_2006-10-12_优化查询 用户信息从内存中获得
	 */
	public Vector getUserList(String condition) {

		Vector userList = new Vector();
		UserBean user = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT user_info.id FROM user_info ";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join") != -1) {
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
				user = UserInfoUtil.getUser(rs.getInt("user_info.id"));
				if (user != null)
					userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return userList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mcool.www.service.infc.IUserService#updateUser(java.lang.String)
	 */
	public boolean updateUser(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE user_info SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#addFriend(int, int)
	 */
	public boolean addFriend(int userId, int friendId) {
		if (userId == friendId) {
			return false;
		}

		DbOperation dbOp = new DbOperation(true);

		String query = "INSERT INTO user_friend("
				+ "user_id, friend_id,create_datetime,update_datetime) VALUES(?, ?,now(),now())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, friendId);
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		dbOp.executePstmt();

		dbOp.release();
		// zhul 2006-10-17 添加好友，更新缓存
		ArrayList userFriends = (ArrayList) UserInfoUtil.userFriendsCache.sgt(
				Integer.valueOf(userId));
		if (userFriends != null)
			userFriends.add(friendId + "");
		userFriendCache.srm(new Integer2(userId, friendId));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#deleteFriend(int, int)
	 */
	public boolean deleteFriend(int userId, int friendId) {
		boolean result;

		// liuyi 2006-10-31 删除好友关系前判断是否不只是普通好友 start
		UserFriendBean userFriend = this.getUserFriend(userId, friendId);
		if (userFriend != null && userFriend.getMark() != 0) {
			return false;
		}
		// liuyi 2006-10-31 删除好友关系前判断是否不只是普通好友 end

		// 数据库操作类
		DbOperation dbOp = new DbOperation(true);

		String query = "DELETE FROM user_friend WHERE user_id = " + userId
				+ " AND friend_id = " + friendId;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		// zhul 2006-10-17 删除好友，更新缓存
		ArrayList userFriends = (ArrayList) UserInfoUtil.userFriendsCache.srm(
				Integer.valueOf(userId));
		if (userFriends != null)
			userFriends.remove(friendId + "");

		userFriendCache.spt(new Integer2(userId, friendId), nullUserFriend);
		return result;
	}

	/*
	 * liuyi 2006-10-31 获取某个用户的金兰id (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getJyFriendIds(int)
	 */
	public List getJyFriendIds(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(jsFriendsCache) {
			List ret = (List) jsFriendsCache.get(key);
			if (ret == null) {
				String sql = "select friend_id from user_friend where mark=1 and user_id="
						+ userId;
				ret = SqlUtil.getIntList(sql, Constants.DBShortName);
				if (ret == null) {
					ret = new ArrayList();
				}
				jsFriendsCache.put(key, ret);
			}
			return ret;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getFriends(java.lang.String)
	 */
	public Vector getFriends(String condition) {
		// liuyi 2006-09-30 sql优化 start
		String newCondition = "right join (SELECT friend_id FROM user_friend WHERE "
				+ condition + ") b on (user_info.id=b.friend_id) order by id";
		// liuyi 2006-09-30 sql优化 end
		return getUserList(newCondition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#isUserFriend(int, int)
	 */
	public boolean isUserFriend(int selfId, int userId) {
		boolean result = false;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT id FROM user_friend WHERE user_id = " + selfId
				+ " AND friend_id = " + userId;

		// 执行更新
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#addFriend(int, int)
	 */
	public boolean addBadGuy(int userId, int friendId) {
		DbOperation dbOp = new DbOperation(true);

		String query = "INSERT INTO user_blacklist("
				+ "user_id, badguy_id) VALUES(?, ?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, friendId);
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		dbOp.executePstmt();
		dbOp.release();
		badGuyCache.spt(new Integer2(userId, friendId), isBadGuy);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#deleteFriend(int, int)
	 */
	public boolean deleteBadGuy(int userId, int friendId) {
		SqlUtil.executeUpdate("DELETE FROM user_blacklist WHERE user_id = " + userId
				+ " AND badguy_id = " + friendId);
		badGuyCache.spt(new Integer2(userId, friendId), notBadGuy);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getFriends(java.lang.String)
	 */
	public Vector getBadGuys(String condition) {
		// liuyi 2006-09-30 sql优化 start
		String newCondition = "right join (SELECT badguy_id FROM user_blacklist WHERE "
				+ condition + ") b on (user_info.id=b.badguy_id) order by id";
		// liuyi 2006-09-30 sql优化 end
		return getUserList(newCondition);
	}

	static Integer isBadGuy = new Integer(0);
	static Integer notBadGuy = new Integer(1);;	// 不要用final，用地址判断相等的，可能有问题
	// 判断是否在黑名单
	public boolean isUserBadGuy(int selfId, int userId) {
		
		Integer2 key = new Integer2(selfId, userId);
		synchronized(badGuyCache) {
			Integer is = (Integer)badGuyCache.get(key);
			if(is == null) {
				boolean result = false;
				DbOperation dbOp = new DbOperation(true);
				String query = "SELECT id FROM user_blacklist WHERE user_id = "
						+ selfId + " AND badguy_id = " + userId;

				ResultSet rs = dbOp.executeQuery(query);

				try {
					if (rs.next()) {
						is = isBadGuy;
					} else {
						is = notBadGuy;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dbOp.release();
				badGuyCache.put(key, is);
			}
			return is == isBadGuy;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getCartCount(java.lang.String)
	 */
	public int getCartCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM cart WHERE " + condition;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getCartList(java.lang.String)
	 */
	public Vector getCartList(String condition) {
		Vector cartList = new Vector();
		CartBean cart = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return cartList;
		}

		// 查询语句
		String sql = "SELECT * FROM cart";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return cartList;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				cart = new CartBean();
				cart.setId(rs.getInt("id"));
				cart.setUserId(rs.getInt("user_id"));
				cart.setTitle(rs.getString("title"));
				cart.setUrl(rs.getString("url"));
				cartList.add(cart);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return cartList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#deleteCart(java.lang.String)
	 */
	public boolean deleteCart(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM cart WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#addCart(net.joycool.wap.bean.CartBean)
	 */
	public boolean addCart(CartBean cart) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO cart("
				+ "user_id, title, url) VALUES(?, ?, ?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, cart.getUserId());
			pstmt.setString(2, cart.getTitle());
			pstmt.setString(3, cart.getUrl());
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
	 * @see net.joycool.wap.service.infc.IUserService#addOnlineUser(net.joycool.wap.bean.wgamepk.OnlineUserBean)
	 */
	public boolean addOnlineUser(OnlineBean online) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_online_user set user_id=?, position_id=?, sub_id=?, status=?, session_id=?,tong_id=?";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, online.getUserId());
			pstmt.setInt(2, online.getPositionId());
			pstmt.setInt(3, online.getSubId());
			pstmt.setInt(4, online.getStatus());
			pstmt.setString(5, online.getSessionId());
			pstmt.setInt(6, online.getTongId());
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
	 * @see net.joycool.wap.service.infc.IUserService#deleteOnlineUser(java.lang.String)
	 */
	public boolean deleteOnlineUser(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_online_user WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getOnlineUserCount(java.lang.String)
	 */
	public int getOnlineUserCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_online_user WHERE "
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

	public int getUserCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(user_info.id) as c_id FROM user_info";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join ") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}
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

	public int getFriendCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM user_friend WHERE "
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#updateOnlineUser(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateOnlineUser(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_online_user SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// zhul-2006-06-06 start
	// 将原WGameServiceImpl下的所有关于UserStatus的方法移到UserServiceImpl
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#addUserStatusBean(net.joycool.wap.bean.UserStatusBean)
	 *      zhul-2006-06-06 修改sql语句，增加字段
	 */
	public boolean addUserStatus(UserStatusBean status) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO user_status("
				+ "user_id, point, rank, game_point, login_count, last_login_time, last_logout_time, total_online_time) VALUES(?, ?, ?, ?, ?, NOW(),NOW(),0)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, status.getUserId());
			pstmt.setInt(2, status.getPoint());
			pstmt.setInt(3, status.getRank());
			pstmt.setInt(4, status.getGamePoint());
			pstmt.setInt(5, status.getLoginCount());

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
	 * @see net.joycool.wap.service.infc.IUserService#getUserStatus(java.lang.String)
	 *      zhul-2006-06-06 修改获取结果部分，因为有新增加的属性
	 */
	public UserStatusBean getUserStatus(String condition) {
		UserStatusBean status = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM user_status";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				status = new UserStatusBean();
				status.setUserId(rs.getInt("user_id"));
				status.setPoint(rs.getInt("point"));
				status.setRank(rs.getInt("rank"));
				status.setGamePoint(rs.getInt("game_point"));
				status.setLoginCount(rs.getInt("login_count"));
				status.setLastLoginTime(rs.getString("last_login_time"));
				status.setLastLogoutTime(rs.getString("last_logout_time"));
				
				status.setLastLoginTime2(rs.getTimestamp("last_login_time").getTime());
				status.setLastLogoutTime2(rs.getTimestamp("last_logout_time").getTime());
				status.setTotalOnlineTime(rs.getLong("total_online_time"));
				status.setImagePathId(rs.getInt("image_path_id"));
				status.setSpirit(rs.getInt("spirit"));
				status.setPk(rs.getInt("pk"));
				status.setSocial(rs.getInt("social"));
				status.setMark(rs.getInt("mark"));
				status.setCharitarian(rs.getInt("charitarian"));
				// macq_2006-12-13_增加用户行囊_start
				status.setUserBag(rs.getInt("user_bag"));
				// macq_2006-12-13_增加用户行囊_end
				// mcq_2006-12-25_帮会判断字段_start
				status.setTong(rs.getInt("tong"));
				// mcq_2006-12-25_帮会判断字段_end
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getUserStatusList(java.lang.String)
	 */
	public Vector getUserStatusList(String condition, boolean getUser) {
		Vector list = new Vector();
		UserStatusBean status = null;

		DbOperation dbOp = new DbOperation(true);

		String query = "SELECT * FROM user_status";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				status = new UserStatusBean();
				status.setUserId(rs.getInt("user_id"));
				if (getUser) {
					// status.setUser(userService.getUser("id = " +
					// status.getUserId()));
					// zhul 2006-10-12_优化用户信息查询
					status.setUser(UserInfoUtil.getUser(status.getUserId()));
				}
				status.setGamePoint(rs.getInt("game_point"));
				status.setPoint(rs.getInt("point"));
				status.setRank(rs.getInt("rank"));
				status.setImagePathId(rs.getInt("image_path_id"));
				status.setSpirit(rs.getInt("spirit"));
				status.setPk(rs.getInt("pk"));
				status.setSocial(rs.getInt("social"));
				status.setMark(rs.getInt("mark"));
				status.setCharitarian(rs.getInt("charitarian"));
				// macq_2006-12-13_增加用户行囊_start
				status.setUserBag(rs.getInt("user_bag"));
				// macq_2006-12-13_增加用户行囊_end
				// mcq_2006-12-25_帮会判断字段_start
				status.setTong(rs.getInt("tong"));
				// mcq_2006-12-25_帮会判断字段_end
				list.add(status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#updateUserStatusBean(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateUserStatus(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE user_status SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getUserStatusCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM user_status";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
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

	// zhul-2006-06-06 end
	// 将原WGameServiceImpl下的所有关于UserStatus的方法移到UserServiceImpl

	// mqc_start_增加获取jcrank表方法 时间 2006-6-7
	public Vector getRankList(String condition) {
		Vector rankList = new Vector();
		RankBean rank = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return rankList;
		}

		// 查询语句
		String sql = "SELECT * FROM jc_rank";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return rankList;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				rank = new RankBean();
				rank.setId(rs.getInt("id"));
				rank.setRankId(rs.getInt("rank_id"));
				rank.setMaleName(rs.getString("male_name"));
				rank.setFemaleName(rs.getString("female_name"));
				rank.setNeedPoint(rs.getInt("need_point"));
				rankList.add(rank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return rankList;
	}

	// mqc_end

	// mqc_start_增加通过条件获取jc_rank_action记录的方法 时间 2006-6-7
	public Vector getRankActionList(String condition) {
		Vector actionList = new Vector();
		RankActionBean action = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return actionList;
		}

		// 查询语句
		String sql = "SELECT * FROM jc_rank_action";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return actionList;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				action = new RankActionBean();
				action.setId(rs.getInt("id"));
				action.setRankId(rs.getInt("rank_id"));
				action.setActionName(rs.getString("action_name"));
				action.setNeedGamePoint(rs.getInt("need_game_point"));
				action.setSendMessage(rs.getString("send_message"));
				action.setReceiveMessage(rs.getString("receive_message"));
				actionList.add(action);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return actionList;
	}

	// mqc_end

	// mqc_start_增加通过条件获取jc_rank_action记录的方法 时间 2006-6-8
	public RankActionBean getRankAction(String condition) {
		RankActionBean action = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return action;
		}

		// 查询语句
		String sql = "SELECT * FROM jc_rank_action";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return action;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				action = new RankActionBean();
				action.setId(rs.getInt("id"));
				action.setRankId(rs.getInt("rank_id"));
				action.setActionName(rs.getString("action_name"));
				action.setNeedGamePoint(rs.getInt("need_game_point"));
				action.setSendMessage(rs.getString("send_message"));
				action.setReceiveMessage(rs.getString("receive_message"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return action;
	}

	// mqc_end

	// zt_start_获取jc_online_user的列表 时间 2006-6-9
	/**
	 * user_info a user_status b jc_online_user c 查询条件：使用表别名加上表的字段来查询。 eg:
	 * c.position_id <>0 AND c.position_id <>1
	 */
	public Vector getOnlineUserList(String condition) {
		Vector userList = new Vector();
		UserBean user = null;
		UserStatusBean userStatus = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT a.id,a.nickname,c.position_id FROM user_info AS a  JOIN jc_online_user AS c ON a.id = c.user_id";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				user = new UserBean();
				userStatus = new UserStatusBean();
				user.setId(rs.getInt("a.id"));
				user.setNickName(rs.getString("a.nickname"));
				user.setPositionId(rs.getInt("c.position_id"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return userList;
	}

	/**
	 * zhul 2006-10-12 获取所有在线用户的id列表
	 * 
	 * @return
	 */
	public ArrayList getOnlineUserIdList() {
		ArrayList array = new ArrayList();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT user_id FROM  jc_online_user ";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				array.add(rs.getInt("user_id") + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return array;
	}

	// zhul_add new code_2006-06-23_start
	public OnlineBean getOnlineUser(String condition) {

		OnlineBean onlineBean = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id,user_id,position_id,sub_id,status,session_id FROM  jc_online_user ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				onlineBean = new OnlineBean();
				onlineBean.setId(rs.getInt("id"));
				onlineBean.setPositionId(rs.getInt("position_id"));
				onlineBean.setSubId(rs.getInt("sub_id"));
				onlineBean.setUserId(rs.getInt("user_id"));
				onlineBean.setStatus(rs.getInt("status"));
				onlineBean.setSessionId(rs.getString("session_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return onlineBean;
	}

	// zhul_add new code_2006-06-23_end
	// zt_end

	// mqc_start_添加用户发送动作记录 时间 2006-6-9
	public boolean addActionRecord(ActionRecordBean actionRecord) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_action_record("
				+ "from_id, to_id,action_id,mark,action_datetime) VALUES(?,?,?,?,now())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, actionRecord.getFromId());
			pstmt.setInt(2, actionRecord.getToId());
			pstmt.setInt(3, actionRecord.getActionId());
			pstmt.setInt(4, 0);
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

	// mqc_end

	// mqc_start_添加用户发送动作记录 时间 2006-6-9
	public Vector getActionRecordList(String condition) {
		Vector recordList = new Vector();
		ActionRecordBean actionRecord = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * from jc_action_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				actionRecord = new ActionRecordBean();
				actionRecord.setId(rs.getInt("id"));
				actionRecord.setFromId(rs.getInt("from_id"));
				actionRecord.setToId(rs.getInt("to_id"));
				actionRecord.setActionId(rs.getInt("action_id"));
				actionRecord.setMark(rs.getInt("mark"));
				actionRecord.setActionDatetime(rs.getString("action_datetime"));
				recordList.add(actionRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return recordList;
	}

	// mqc_end

	// mqc_start_通过条件返回用户动作记录数量 时间 2006-6-9
	public int getActionRecordCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_action_record "
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

	// mqc_end

	// mqc_start_通过条件返回等级信息数量 时间 2006-6-9
	public int getRankActionCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_rank_action "
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

	// mqc_end

	// fanys -start 2006-06-15
	/**
	 * 获取动作列表 根据用户已经发送动作次数多少和动作等级来排序
	 */
	public Vector getRankActionListOrderByAction(String strsql) {
		Vector actionList = new Vector();
		RankActionBean action = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return actionList;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(strsql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return actionList;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				action = new RankActionBean();
				action.setId(rs.getInt("id"));
				action.setRankId(rs.getInt("rank_id"));
				action.setActionName(rs.getString("action_name"));
				action.setNeedGamePoint(rs.getInt("need_game_point"));
				action.setSendMessage(rs.getString("send_message"));
				action.setReceiveMessage(rs.getString("receive_message"));
				actionList.add(action);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return actionList;
	}

	// fanys--end

	// zhangyi_start_在用户表中查询所有城市 时间 2006-6-20
	/**
	 * 在用户表中查询所有城市
	 */
	public Vector getCityFromUserInfor() {
		Vector cityNameList = new Vector();

		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return cityNameList;
		}
		// 查询
		ResultSet rs = dbOp
				.executeQuery("SELECT distinct cityname FROM user_info");

		// 将结果保存
		try {
			while (rs.next()) {
				String cityName = rs.getString("cityname");
				if (cityName != null) {
					cityNameList.add(cityName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 返回结果
		return cityNameList;
	}

	// zhangyi_end_在用户表中查询所有城市 时间 2006-6-20

	// zhangyi_start_每天一个用户的登录次数 时间 2006-6-30
	public int getDaysLoginUserCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_days_login_user";
		if (condition != null) {
			query = query + " WHERE " + condition + " AND date = NOW()";
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
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

	// zhangyi_end_每天一个用户的登录次数 时间 2006-6-30

	// zhangyi_start_每天一个用户表添加 时间 2006-6-30
	public boolean addDaysLoginUse(int userId) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_days_login_user("
				+ "user_id,date,create_datetime) VALUES(?, NOW(), NOW())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, userId);
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

	// zhangyi_end_每天一个用户表添加 时间 2006-6-30

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#delDaysLoginUse(java.lang.String)
	 */
	public boolean delDaysLoginUse(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_days_login_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	// mcq_2006-9-4_查询用户最后登录ID_start
	public Vector getUserLogoutIdList(String sql) {
		Vector userList = new Vector();
		UserBean user = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		// 李北金_2006-06-20_查询优化_start
		String query = sql;
		// 李北金_2006-06-20_查询优化_end
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				user = new UserBean();
				user.setGender(rs.getInt("gender"));
				user.setId(rs.getInt("id"));
				user.setMobile(rs.getString("mobile"));
				user.setPassword(rs.getString("password"));
				user.setNickName(rs.getString("nickname"));
				user.setSelfIntroduction(rs.getString("self_introduction"));
				user.setAge(rs.getInt("age"));
				user.setCityname(rs.getString("cityname"));
				user.setCityno(rs.getInt("cityno"));
				user.setPlaceno(rs.getInt("placeno"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return userList;
	}

	// mcq_2006-9-4_查询用户最后登录ID_end
	// fanys_2006-09-07_王冠_start
	public CrownBean getCrown(String condition) {
		CrownBean crown = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from jc_crown where ";

		if (null != condition)
			query = query + condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				crown = getCrown(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return crown;
	}

	private CrownBean getCrown(ResultSet rs) throws SQLException {
		CrownBean crown = null;
		crown = new CrownBean();
		crown.setId(rs.getInt("id"));
		crown.setImage(rs.getString("image"));
		crown.setImageId(rs.getInt("image_id"));
		crown.setImagePathId(rs.getInt("image_path_id"));
		return crown;
	}

	public boolean addCrown(CrownBean crown) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_crown("
				+ "image_id,image,image_path_id ) VALUES(?, ?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, crown.getImageId());
			pstmt.setString(2, crown.getImage());
			pstmt.setInt(3, crown.getImagePathId());
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

	public boolean deleteCrown(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_crown WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateCrown(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_crown SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public Vector getCrownList(String condition) {
		Vector vecCrown = new Vector();
		CrownBean crown = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from jc_crown where ";

		if (null != condition)
			query = query + condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				crown = getCrown(rs);
				vecCrown.add(crown);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return vecCrown;
	}

	public int getCrownCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_crown WHERE "
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

	// fanys_2006-09-07_王冠_end
	static UserFriendBean nullUserFriend = new UserFriendBean();
	/**
	 * liuyi 2006-10-28 根据id获取UserFriendBean
	 * 
	 * @param userId
	 * @return
	 */
	public UserFriendBean getUserFriend(int userId, int friendId) {
		UserFriendBean bean = null;

		Integer2 key = new Integer2(userId, friendId);
		synchronized(userFriendCache) {
			bean = (UserFriendBean) userFriendCache.get(key);
			if (bean == null) {
				String condition = "user_id=" + userId + " and friend_id="
						+ friendId;
				bean = getUserFriend(condition);
				if (bean != null) {
					userFriendCache.put(key, bean);
					return bean;
				} else {
					userFriendCache.put(key, nullUserFriend);
					return null;
				}
			}
			if(bean == nullUserFriend)
				return null;
			return bean;
		}
	}

	/**
	 * liuyi 2006-10-28 根据条件获取UserFriendBean
	 * 
	 * @param condition
	 * @return
	 */
	public UserFriendBean getUserFriend(String condition) {
		UserFriendBean userFriend = null;

		DbOperation dbOp = new DbOperation();
		try {
			dbOp.init();
			String query = "select * from user_friend where ";

			if (null != condition) {
				query = query + condition;
			}
			ResultSet rs = dbOp.executeQuery(query);
			if (rs != null && rs.next()) {
				userFriend = this.getUserFriend(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		return userFriend;
	}
	
	public void flushUserFriend(int fromId, int toId) {
		userFriendCache.srm(new Integer2(fromId, toId));
		userFriendCache.srm(new Integer2(toId, fromId));
	}
	// 刷新单方向的好友数据
	public void flushUserFriendSingle(int fromId, int toId) {
		userFriendCache.srm(new Integer2(fromId, toId));
	}

	// mcq_2006-9-25_获取用户好友列表_start
	public Vector getUserFriendList(String condition) {
		Vector userFriendList = new Vector();
		UserFriendBean userFriend = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from user_friend where ";

		if (null != condition)
			query = query + condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				userFriend = this.getUserFriend(rs);
				userFriendList.add(userFriend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return userFriendList;
	}

	// mcq_2006-9-25_获取用户好友列表_end

	/**
	 * zhul 2006-10-13 获取用户好友列表
	 */
	public ArrayList getUserFriendList(int userId) {

		ArrayList userFriends = new ArrayList();

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select friend_id from user_friend where user_id="
				+ userId;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				userFriends.add(rs.getInt("friend_id") + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return userFriends;
	}

	private UserFriendBean getUserFriend(ResultSet rs) throws SQLException {
		UserFriendBean userFriend = new UserFriendBean();
		userFriend.setId(rs.getInt("id"));
		userFriend.setUserId(rs.getInt("user_id"));
		userFriend.setFriendId(rs.getInt("friend_id"));
		userFriend.setMark(rs.getInt("mark"));
		userFriend.setLevelValue(rs.getInt("level_value"));
		userFriend.setCreateDatetime(rs.getString("create_datetime"));
		userFriend.setUpdateDatetime(rs.getString("update_datetime"));
		return userFriend;
	}

	/*
	 * liuyi 2006-10-29 更新好友关系表 (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#updateFriend(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateFriend(String set, String condition) {
		boolean result = false;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		try {
			dbOp.init();

			// 构建更新语句
			String query = "UPDATE user_friend SET " + set + " WHERE "
					+ condition;

			// 执行更新
			result = dbOp.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("UPDATE user_friend SET " + set + " WHERE "
					+ condition);
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return result;
	}

	/**
	 * liuyi 2006-10-31 判断有无该两个用户的好友记录，有则更新友好度
	 * 
	 * @param fromId
	 * @param toId
	 * @return
	 */
	public boolean addOrupdateFriendLevel(int fromId, int toId) {
		boolean result = false;
		UserFriendBean userFriend = getUserFriend(fromId, toId);
		if (userFriend == null) {
			return false;
		} else {
			// 更新友好度
			result = updateFriend(
					" level_value=level_value+1,update_datetime=now() ",
					"user_id=" + fromId + " and friend_id=" + toId);
			// 清除友好度缓存
			flushUserFriend(fromId, toId);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#updateMid(net.joycool.wap.bean.UserBean,
	 *      java.lang.String)
	 */
	public void updateMid(UserBean user, String mid) {
//		if (mid == null || user == null || mid.length() < 10
//				|| user.getMid().equals(mid))
//			return;
//
//		DbOperation dbOp = new DbOperation();
//		dbOp.init();
//		dbOp.executeUpdate("update user_info set mid='" + StringUtil.toSql(mid) + "' where id="
//				+ user.getId());
//		dbOp.release();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getItemComposeList(java.lang.String)
	 */
	static final String itemComposeKey = "itemCompose";
	public HashMap getItemComposeMap(String condition) {
		String sql = "select * from item_compose where " + condition;
		HashMap map = (HashMap) stuffCache.get(itemComposeKey);
		if (map == null) {
			map = new HashMap();

			DbOperation dbOp = new DbOperation(4);

			ResultSet rs = dbOp.executeQuery(sql);
			try {
				while (rs.next()) {
					ComposeBean bean = getItemCompose(rs);
					map.put(Integer.valueOf(bean.getItemId()), bean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			stuffCache.spt(itemComposeKey, map);
		}
		return map;
	}

	private ComposeBean getItemCompose(ResultSet rs) throws SQLException {
		ComposeBean bean = new ComposeBean();
		bean.setId(rs.getInt("id"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setRank(rs.getInt("rank"));
		bean.setFlag(rs.getInt("flag"));
		bean.setProduct(rs.getString("product"));
		bean.setMaterial(rs.getString("material"));
		bean.setFail(rs.getInt("fail"));
		bean.setPrice(rs.getInt("price"));
		return bean;
	}

	static final String itemShowKey = "itemShow";
	public HashMap getItemShowMap(String condition) {
		String sql = "select * from item_show where " + condition;
		
		HashMap map = (HashMap) stuffCache.get(itemShowKey);
		if (map == null) {
			map = new HashMap();

			DbOperation dbOp = new DbOperation(4);
			ResultSet rs = dbOp.executeQuery(sql);
			try {
				while (rs.next()) {
					ShowBean bean = getItemShow(rs);
					map.put(Integer.valueOf(bean.getItemId()), bean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			stuffCache.spt(itemShowKey, map);
		}
		return map;
	}

	private ShowBean getItemShow(ResultSet rs) throws SQLException {
		ShowBean bean = new ShowBean();
		bean.setId(rs.getInt("id"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setName(rs.getString("name"));
		bean.setIntro(rs.getString("intro"));
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#getUserHonor(java.lang.String)
	 */
	public UserHonorBean getUserHonor(String condition) {
		UserHonorBean bean = new UserHonorBean();

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * FROM user_honor";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 1";

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				bean.setId(rs.getInt("id"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setHonor(rs.getInt("honor"));
				bean.setPlace(rs.getInt("place"));
				bean.setLastWeek(rs.getInt("last_week"));
				bean.calcHonorRank();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();

		return bean;
	}

	public boolean updateUserHonor(String set, String condition) {
		boolean result;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "UPDATE user_honor SET " + set + " WHERE " + condition;
		result = dbOp.executeUpdate(query);
		dbOp.release();
		return result;
	}

	/**
	 * 每周计算荣誉排名，并把上周积分保存
	 */
	public void calcHonorPlace() {
		UserHonorBean bean = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		dbOp.executeUpdate("delete from user_honor where last_week+honor=0");
		
		SqlUtil.createRownum(dbOp, "user_honor order by honor desc");
		dbOp.executeUpdate("update user_honor a," + SqlUtil.ROWNUM_TABLE
				+ " b set a.place=b.rownum where a.id=b.id");
		dbOp.executeUpdate("update user_honor set last_week=honor,honor=floor(honor * 0.8)");

		dbOp.release();
	}

	/**
	 * @param 增加一条这个用户的荣誉记录
	 */
	public void addUserHonor(UserHonorBean bean) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO user_honor(user_id,honor,create_datetime) VALUES(?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getHonor());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return;
		}
		dbOp.executePstmt();
		dbOp.release();
	}

	public List getShortcutList() {
		List list = new ArrayList();

		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp
				.executeQuery("select * from shortcut order by seq,id");
		try {
			while (rs.next()) {
				list.add(getShortcut(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return list;
	}

	private ShortcutBean getShortcut(ResultSet rs) throws SQLException {
		ShortcutBean bean = new ShortcutBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setUrl(rs.getString("url"));
		bean.setShortName(rs.getString("short_name"));
		bean.setHide(rs.getInt("hide"));
		return bean;
	}

	public boolean addBlackListUser(BlackListUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO blacklist_user(user_id,create_datetime) VALUES(?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
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

	public BlackListUserBean getBlackListUser(String condition) {
		BlackListUserBean blackListUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from blacklist_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				blackListUser = this.getBlackListUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return blackListUser;
	}

	public List getBlackListUserList(String condition) {
		List blackListUserList = new ArrayList();
		BlackListUserBean blackListUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from blacklist_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				blackListUser = this.getBlackListUser(rs);
				blackListUserList.add(blackListUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return blackListUserList;
	}

	public boolean deleteBlackListUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM blacklist_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateBlackListUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE blacklist_user SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getBlackListUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM blacklist_user WHERE "
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

	private BlackListUserBean getBlackListUser(ResultSet rs)
			throws SQLException {
		BlackListUserBean blackListUser = new BlackListUserBean();
		blackListUser.setId(rs.getInt("id"));
		blackListUser.setUserId(rs.getInt("user_id"));
		blackListUser.setCreateDatetime(rs.getTimestamp("create_datetime"));
		return blackListUser;
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IUserService#addItemLog(int, int, int, int)
	 */
	public void addItemLog(int userId, int toUserId, int userBagId, int itemId, int type) {
		addItemLog(userId, toUserId, userBagId, itemId, 1, type);
	}
	public void addItemLog(int userId, int toUserId, int userBagId, int itemId, int stack, int type) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT delayed INTO user_item_log(user_id,to_user_id,item_id,userbag_id,stack,type,time) VALUES(?,?,?,?,?,?,now())";

		if (dbOp.prepareStatement(query)) {
			PreparedStatement pstmt = dbOp.getPStmt();
			try {
				pstmt.setInt(1, userId);
				pstmt.setInt(2, toUserId);
				pstmt.setInt(3, itemId);
				pstmt.setInt(4, userBagId);
				pstmt.setInt(5, stack);
				pstmt.setInt(6, type);
				dbOp.executePstmt();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		dbOp.release();
	}

	public List getUserNoteList(String condition) {
		List list = new ArrayList();

		DbOperation dbOp = new DbOperation();
		if (dbOp.init()) {

			ResultSet rs = dbOp
					.executeQuery("select * from user_note where " + condition + " order by id");
			try {
				while (rs.next()) {
					list.add(getUserNote(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

		}
		return list;
	}

	private UserNoteBean getUserNote(ResultSet rs) throws SQLException {
		UserNoteBean bean = new UserNoteBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setToUserId(rs.getInt("to_user_id"));
		bean.setShortNote(rs.getString("short_note"));
		bean.setNote(rs.getString("note"));
		return bean;
	}
	public boolean addUserNote(UserNoteBean note) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO user_note("
				+ "user_id, to_user_id, short_note, note) VALUES(?, ?, ?, ?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, note.getUserId());
			pstmt.setInt(2, note.getToUserId());
			pstmt.setString(3, note.getShortNote());
			pstmt.setString(4, note.getNote());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		dbOp.executePstmt();
		note.setId(SqlUtil.getLastInsertId(dbOp));
		dbOp.release();

		return true;
	}

	public boolean updateUserNote(String set, String condition) {
		boolean result;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "UPDATE user_note SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public void removeUserNote(String condition) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "delete from user_note WHERE " + condition;

		dbOp.executeUpdate(query);

		dbOp.release();
	}

	public boolean addUserInterval(int type, int userId, int refresh) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO user_interval("
				+ "type, user_id, refresh) VALUES(?, ?, ?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, type);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, refresh);
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		dbOp.executePstmt();
		dbOp.release();

		return true;
	}
}
