/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.wgamehall.WGameHallBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IWGameHallService;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class WGameHallServiceImpl implements IWGameHallService {

	// IFriendLevelService friendLevel =
	// ServiceFactory.createFriendLevelService();
	static IUserService service = ServiceFactory.createUserService();
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IWGameHallService#addWGameHall(net.joycool.wap.bean.wgamehall.WGameHallBean)
	 */
	public boolean addWGameHall(WGameHallBean hall) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO wgame_hall SET left_user_id=?, "
				+ "left_nickname=?, "
				+ "right_user_id=?, right_nickname=?, "
				+ "start_datetime=now(), left_status=?, "
				+ "right_status=?, mark=?, left_session_id=?, right_session_id=?, unique_mark=?, game_id=?";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, hall.getLeftUserId());
			pstmt.setString(2, hall.getLeftNickname());
			pstmt.setInt(3, hall.getRightUserId());
			pstmt.setString(4, hall.getRightNickname());
			pstmt.setInt(5, hall.getLeftStatus());
			pstmt.setInt(6, hall.getRightStatus());
			pstmt.setInt(7, hall.getMark());
			pstmt.setString(8, hall.getLeftSessionId());
			pstmt.setString(9, hall.getRightSessionId());
			pstmt.setString(10, hall.getUniqueMark());
			pstmt.setInt(11, hall.getGameId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();
		// zhul 2006-10-20 当用户PK时增加用户的PK度 start
		UserInfoUtil.addUserPKTotal(hall.getLeftUserId());
		UserInfoUtil.addUserPKTotal(hall.getRightUserId());
		// zhul 2006-10-20 当用户PK时增加用户的PK度 end
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IWGameHallService#deleteWGameHall(java.lang.String)
	 */
	public boolean deleteWGameHall(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM wgame_hall WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IWGameHallService#getWGameHall(java.lang.String)
	 */
	public WGameHallBean getWGameHall(String condition) {
		WGameHallBean hall = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM wgame_hall";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				hall = new WGameHallBean();
				hall.setId(rs.getInt("id"));
				hall.setEndDatetime(rs.getString("end_datetime"));
				hall.setLeftNickname(rs.getString("left_nickname"));
				hall.setLeftUserId(rs.getInt("left_user_id"));
				hall.setMark(rs.getInt("mark"));
				hall.setRightNickname(rs.getString("right_nickname"));
				hall.setRightUserId(rs.getInt("right_user_id"));
				hall.setStartDatetime(rs.getString("start_datetime"));
				hall.setWager(rs.getInt("wager"));
				hall.setWinUserId(rs.getInt("win_user_id"));
				hall.setLeftSessionId(rs.getString("left_session_id"));
				hall.setLeftStatus(rs.getInt("left_status"));
				hall.setRightSessionId(rs.getString("right_session_id"));
				hall.setRightStatus(rs.getInt("right_status"));
				hall.setUniqueMark(rs.getString("unique_mark"));
				hall.setResult(rs.getString("result"));
				hall.setMark(rs.getInt("mark"));
				hall.setGameId(rs.getInt("game_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return hall;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IWGameHallService#getWGameHallCount(java.lang.String)
	 */
	public int getWGameHallCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM wgame_hall WHERE "
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
	 * @see net.joycool.wap.service.infc.IWGameHallService#getWGameHallList(java.lang.String)
	 */
	public Vector getWGameHallList(String condition) {
		Vector hallList = new Vector();
		WGameHallBean hall = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM wgame_hall";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				hall = new WGameHallBean();
				hall.setId(rs.getInt("id"));
				hall.setEndDatetime(rs.getString("end_datetime"));
				hall.setLeftNickname(rs.getString("left_nickname"));
				hall.setLeftUserId(rs.getInt("left_user_id"));
				hall.setMark(rs.getInt("mark"));
				hall.setRightNickname(rs.getString("right_nickname"));
				hall.setRightUserId(rs.getInt("right_user_id"));
				hall.setStartDatetime(rs.getString("start_datetime"));
				hall.setWager(rs.getInt("wager"));
				hall.setWinUserId(rs.getInt("win_user_id"));
				hall.setLeftSessionId(rs.getString("left_session_id"));
				hall.setLeftStatus(rs.getInt("left_status"));
				hall.setRightSessionId(rs.getString("right_session_id"));
				hall.setRightStatus(rs.getInt("right_status"));
				hall.setUniqueMark(rs.getString("unique_mark"));
				hall.setResult(rs.getString("result"));
				hall.setMark(rs.getInt("mark"));
				hall.setGameId(rs.getInt("game_id"));
				hallList.add(hall);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return hallList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IWGameHallService#updateWGameHall(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateWGameHall(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE wgame_hall SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
}
