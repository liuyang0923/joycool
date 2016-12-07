package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.top.MoneyTopBean;
import net.joycool.wap.bean.top.UserTopBean;
import net.joycool.wap.service.infc.ITopService;
import net.joycool.wap.util.db.DbOperation;

public class TopServiceImpl implements ITopService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#getTopCount(java.lang.String)
	 */
	public int getMoneyTopCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_money_top WHERE "
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
	 * @see net.joycool.wap.service.infc.ITopService#addTop(net.joycool.wap.bean.top.TopBean)
	 */
	public boolean addMoneyTop(MoneyTopBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_money_top(user_id,game_point,bank_store,bank_load,money_total,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getGamePoint());
			pstmt.setLong(3, bean.getBankLoad());
			pstmt.setLong(4, bean.getBankStore());
			pstmt.setLong(5, bean.getMoneyTotal());
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
	 * @see net.joycool.wap.service.infc.ITopService#delTop(java.lang.String)
	 */
	public boolean delMoneyTop(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_money_top WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#getTop(java.lang.String)
	 */
	public MoneyTopBean getMoneyTop(String condition) {
		MoneyTopBean top = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_top";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				top = this.getMoneyTop(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return top;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#getTopList(java.lang.String)
	 */
	public Vector getMoneyTopList(String condition) {
		Vector topList = new Vector();
		MoneyTopBean top = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_top";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				top = this.getMoneyTop(rs);
				topList.add(top);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return topList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#updateTop(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateMoneyTop(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_money_top SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private MoneyTopBean getMoneyTop(ResultSet rs) throws SQLException {
		MoneyTopBean top = new MoneyTopBean();
		top.setId(rs.getInt("id"));
		top.setUserId(rs.getInt("user_id"));
		top.setGamePoint(rs.getInt("game_point"));
		top.setBankStore(rs.getLong("bank_store"));
		top.setBankLoad(rs.getLong("bank_load"));
		top.setMoneyTotal(rs.getLong("money_total"));
		top.setCreateDatetime(rs.getString("create_datetime"));
		return top;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#addUserTop(net.joycool.wap.bean.top.UserTopBean)
	 */
	public boolean addUserTop(UserTopBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_user_top(priority,user_id,mark,create_datetime,image_id) VALUES(?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getPriority());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getMark());
			pstmt.setInt(4, bean.getImageId());
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
	 * @see net.joycool.wap.service.infc.ITopService#delUserTop(java.lang.String)
	 */
	public boolean delUserTop(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_user_top WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#getUserTop(java.lang.String)
	 */
	public UserTopBean getUserTop(String condition) {
		UserTopBean userTop = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_user_top";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				userTop = this.getUserTop(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userTop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#getUserTopCount(java.lang.String)
	 */
	public int getUserTopCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_user_top WHERE "
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
	 * @see net.joycool.wap.service.infc.ITopService#getUserTopList(java.lang.String)
	 */
	public Vector getUserTopList(String condition) {
		Vector userTopList = new Vector();
		UserTopBean userTop = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_user_top";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				userTop = this.getUserTop(rs);
				userTopList.add(userTop);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userTopList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#updateUserTop(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateUserTop(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_user_top SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private UserTopBean getUserTop(ResultSet rs) throws SQLException {
		UserTopBean userTop = new UserTopBean();
		userTop.setId(rs.getInt("id"));
		userTop.setPriority(rs.getInt("priority"));
		userTop.setUserId(rs.getInt("user_id"));
		userTop.setMark(rs.getInt("mark"));
		userTop.setCreateDatetime(rs.getString("create_datetime"));
		userTop.setImageId(rs.getInt("image_id"));
		return userTop;
	}

	public long getUserLoadTotal(String sql) {
		long total = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = sql;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				total = rs.getInt("total_money");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return total;
	}
}
