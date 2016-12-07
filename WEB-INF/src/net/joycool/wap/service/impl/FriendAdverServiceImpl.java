/**
 * 
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.friendadver.FriendAdverBean;
import net.joycool.wap.bean.friendadver.FriendAdverMessageBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.util.db.DbOperation;

public class FriendAdverServiceImpl implements IFriendAdverService {

	public int getFriendAdverCacheCount(String condition) {
		// 构建查询语句
		String query = "SELECT count(jc_friend_adver.id) AS c_id FROM jc_friend_adver";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key,
					OsCacheUtil.FRIEND_ADVER_CACHE_GROUP,
					OsCacheUtil.FRIEND_ADVER_CACHE_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

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

		dbOp.release();

		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count),
					OsCacheUtil.FRIEND_ADVER_CACHE_GROUP);
		}
		return count;
	}

	public Vector getFriendAdverCacheList(String condition) {
		String query = "SELECT * FROM jc_friend_adver";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		String key = query;
		Vector friendAdverList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_ADVER_CACHE_GROUP,
				OsCacheUtil.FRIEND_ADVER_CACHE_FLUSH_PERIOD);
		if (friendAdverList != null) {
			return friendAdverList;
		}

		friendAdverList = getFriendAdverList2(condition);

		OsCacheUtil.put(key, friendAdverList, OsCacheUtil.FRIEND_ADVER_CACHE_GROUP);

		return friendAdverList;
	}
	
	public Vector getFriendAdverList2(String condition) {
		String query = "SELECT * FROM jc_friend_adver";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		Vector friendAdverList = new Vector();
		FriendAdverBean adver = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);

		try {
			while (rs.next()) {
				adver = new FriendAdverBean();
				adver.setId(rs.getInt("jc_friend_adver.id"));
				adver.setUserId(rs.getInt("jc_friend_adver.user_id"));
				adver.setTitle(rs.getString("jc_friend_adver.title"));
				adver.setSex(rs.getInt("jc_friend_adver.sex"));
				adver.setAge(rs.getInt("jc_friend_adver.age"));
				adver.setArea(rs.getInt("jc_friend_adver.area"));
				adver.setRemark(rs.getString("jc_friend_adver.remark"));
				adver.setAttachment(rs.getString("jc_friend_adver.attachment"));
				adver.setCreateDatetime(rs
						.getString("jc_friend_adver.create_datetime"));
				adver.setHits(rs.getInt("jc_friend_adver.hits"));
				adver.setCityno(rs.getInt("jc_friend_adver.cityno"));
				friendAdverList.add(adver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return friendAdverList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverService#addFriendAdver(net.joycool.wap.bean.friendadver.FriendAdverBean)
	 */
	public boolean addFriendAdver(FriendAdverBean friendAdver) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_friend_adver(user_id, title,sex,age,area,remark,attachment,create_datetime,hits,cityno,gender) VALUES(?, ?, ?, ?, ?,?,?, now(), 0,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, friendAdver.getUserId());
			pstmt.setString(2, friendAdver.getTitle());
			pstmt.setInt(3, friendAdver.getSex());
			pstmt.setInt(4, friendAdver.getAge());
			pstmt.setInt(5, friendAdver.getArea());
			pstmt.setString(6, friendAdver.getRemark());
			pstmt.setString(7, friendAdver.getAttachment());
			pstmt.setInt(8, friendAdver.getCityno());
			pstmt.setInt(9, friendAdver.getGender());

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
	 * @see net.joycool.wap.service.infc.IFriendAdverService#getFriendAdver(java.lang.String)
	 */
	public FriendAdverBean getFriendAdver(String condition) {
		// TODO Auto-generated method stub
		FriendAdverBean adver = null;

		// 构建查询语句
		String query = "SELECT * FROM jc_friend_adver";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// liuyi 2006-11-30 交友优化 start
		String key = condition;
		adver = (FriendAdverBean) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_ADV_GROUP,
				OsCacheUtil.FRIEND_ADV_FLUSH_PERIOD);
		if (adver == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			// 查询
			ResultSet rs = dbOp.executeQuery(query);

			try {
				// 结果不为空
				if (rs.next()) {
					adver = new FriendAdverBean();
					adver.setId(rs.getInt("id"));
					adver.setUserId(rs.getInt("user_id"));
					adver.setTitle(rs.getString("title"));
					adver.setSex(rs.getInt("sex"));
					adver.setAge(rs.getInt("age"));
					adver.setArea(rs.getInt("area"));
					adver.setRemark(rs.getString("remark"));
					adver.setAttachment(rs.getString("attachment"));
					adver.setCreateDatetime(rs.getString("create_datetime"));
					adver.setHits(rs.getInt("hits"));
					adver.setCityno(rs.getInt("cityno"));
					
					OsCacheUtil.put(key, adver, OsCacheUtil.FRIEND_ADV_GROUP);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 释放资源
			dbOp.release();
		}

		// liuyi 2006-11-30 交友优化 end

		return adver;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverService#getFriendAdverList(java.lang.String)
	 */
	public Vector getFriendAdverList(String condition) {
		// TODO Auto-generated method stub
		Vector friendAdverList = null;
		FriendAdverBean adver = null;

		//liuyi 2006-11-30 交友优化 start
		// 构建查询语句
		String query = "SELECT * FROM jc_friend_adver";
		if (condition != null) {
			if (condition.toLowerCase().indexOf("join ") != -1) {
				query = query + " " + condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}

		String key = query;
		friendAdverList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_ADV_LIST_GROUP,
				OsCacheUtil.FRIEND_ADV_LIST_FLUSH_PERIOD);

		if (friendAdverList == null) {
			friendAdverList = new Vector();

			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			// 查询
			ResultSet rs = dbOp.executeQuery(query);

			try {
				// 结果不为空
				while (rs.next()) {
					adver = new FriendAdverBean();
					adver.setId(rs.getInt("jc_friend_adver.id"));
					adver.setUserId(rs.getInt("jc_friend_adver.user_id"));
					adver.setTitle(rs.getString("jc_friend_adver.title"));
					adver.setSex(rs.getInt("jc_friend_adver.sex"));
					adver.setAge(rs.getInt("jc_friend_adver.age"));
					adver.setArea(rs.getInt("jc_friend_adver.area"));
					adver.setRemark(rs.getString("jc_friend_adver.remark"));
					adver.setAttachment(rs
							.getString("jc_friend_adver.attachment"));
					adver.setCreateDatetime(rs
							.getString("jc_friend_adver.create_datetime"));
					adver.setHits(rs.getInt("jc_friend_adver.hits"));
					adver.setCityno(rs.getInt("jc_friend_adver.cityno"));
					friendAdverList.add(adver);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 释放资源
			dbOp.release();

			OsCacheUtil.put(key, friendAdverList,
					OsCacheUtil.FRIEND_ADV_LIST_GROUP);
		}
		//liuyi 2006-11-30 交友优化 end

		return friendAdverList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverService#deleteFriendAdver(java.lang.String)
	 */
	public boolean deleteFriendAdver(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_friend_adver WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IFriendAdverService#updateFriendAdver(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateFriendAdver(String set, String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_friend_adver SET " + set + " WHERE "
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
	 * @see net.joycool.wap.service.infc.IFriendAdverService#getFriendAdverCount(java.lang.String)
	 */
	public int getFriendAdverCount(String condition) {
		// TODO Auto-generated method stub
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(jc_friend_adver.id) AS c_id FROM jc_friend_adver";
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

	public List getFriendAdverMessageList(String condition) {

		List friendAdverMessageList = null;
		FriendAdverMessageBean adverMessage = null;

		String query = "SELECT * FROM jc_friend_adver_message";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		friendAdverMessageList = new ArrayList();

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);

		try {
			while (rs.next()) {
				adverMessage = new FriendAdverMessageBean();
				adverMessage.setId(rs.getInt("id"));
				adverMessage.setUserId(rs.getInt("user_id"));
				adverMessage.setFriendAdverId(rs.getInt("friend_adver_id"));
				adverMessage.setUserNickname(rs.getString("user_nickname"));
				adverMessage.setContent(rs.getString("content"));
				adverMessage.setAttachment(rs.getString("attachment"));
				adverMessage.setCreateDatetime(rs.getString("create_datetime"));
				friendAdverMessageList.add(adverMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();

		return friendAdverMessageList;

	}
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
}
