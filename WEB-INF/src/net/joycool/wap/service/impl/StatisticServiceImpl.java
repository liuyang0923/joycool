package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.joycool.wap.service.infc.IStatisticService;
import net.joycool.wap.util.db.DbOperation;

public class StatisticServiceImpl implements IStatisticService {

	public int getUserNumber(String date,String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select count(id) as c_id  from user_info where create_datetime >"+date;
		if (condition != null) {
			query = query  +" and "+ condition;
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
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}
	public int getMobileNumber(String date,String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select  count(DISTINCT mobile) as c_id from user_info WHERE id IN(SELECT user_id from user_status WHERE last_login_time>"+date+") ";
		if (condition != null) {
			query = query  +" and "+ condition;
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
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}
	public int getNewUserNumber(String date,String cond) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query ="select  count(*) as c_id from user_info WHERE create_datetime> "+date +" AND id IN(SELECT user_id from user_status WHERE "+cond+")";
		//if (condition != null) {
		//	query = query  +" and "+ condition;
		//}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}
	public int getWapUserNumber(String date,String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select count(id) as c_id  from log_login where create_datetime >"+date;
		if (condition != null) {
			query = query  +" and "+ condition;
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
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}
	public int getWapMobileNumber(String date,String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select  count(DISTINCT mobile) as c_id from log_login WHERE last_login_datetime>"+date;
		if (condition != null) {
			query = query  +" and "+ condition;
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
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}
	public int getWapNewUserNumber(String date,String cond) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query ="select  count(*) as c_id from log_login WHERE create_datetime> "+date +" AND "+cond;
		//if (condition != null) {
		//	query = query  +" and "+ condition;
		//}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}
}