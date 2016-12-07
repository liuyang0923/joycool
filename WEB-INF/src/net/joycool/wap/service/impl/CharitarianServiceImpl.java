package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.charitarian.CharitarianBean;
import net.joycool.wap.bean.charitarian.CharitarianHistoryBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.util.db.DbOperation;

public class CharitarianServiceImpl implements ICharitarianService {

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianHistoryCacheCount(java.lang.String)
	 */
	public int getCharitarianHistoryCacheCount(String condition) {
		// 构建更新语句
		String query = condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key,
					OsCacheUtil.CHARITARIAN_CACHE_GROUP,
					OsCacheUtil.CHARITARIAN_CACHE_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}	
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

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
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count),
					OsCacheUtil.CHARITARIAN_CACHE_GROUP);
		}
		return count;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianHistoryCacheList(java.lang.String)
	 */
	public Vector getCharitarianHistoryCacheList(String condition) {
		// 构建查询语句
		String query = condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector charitarianHistoryList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.CHARITARIAN_CACHE_GROUP,
					OsCacheUtil.CHARITARIAN_CACHE_FLUSH_PERIOD);
			if (charitarianHistoryList != null) {
				return charitarianHistoryList;
			}
		}
		int userId=0;
		Vector charitarianHistoryList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				userId = rs.getInt("char_id");
				charitarianHistoryList.add(new Integer(userId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil
					.put(key, charitarianHistoryList, OsCacheUtil.CHARITARIAN_CACHE_GROUP);
		}
		return charitarianHistoryList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#addCharitarian(net.joycool.wap.bean.charitarian.CharitarianBean)
	 */
	public boolean addCharitarian(CharitarianBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_charitarian(user_id,money,count,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setLong(2, bean.getMoney());
			pstmt.setInt(3, bean.getCount());
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
	 * @see net.joycool.wap.service.infc.ICharitarianService#addCharitarianeHistory(net.joycool.wap.bean.charitarian.CharitarianHistoryBean)
	 */
	public boolean addCharitarianHistory(CharitarianHistoryBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_charitarian_history(charitarian_id,receive_id,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getCharitarianId());
			pstmt.setInt(2, bean.getReceiveId());
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
	 * @see net.joycool.wap.service.infc.ICharitarianService#delCharitarian(java.lang.String)
	 */
	public boolean delCharitarian(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_charitarian WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#delCharitarianeHistory(java.lang.String)
	 */
	public boolean delCharitarianHistory(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_charitarian_history WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarian(java.lang.String)
	 */
	public CharitarianBean getCharitarian(String condition) {
		CharitarianBean charitarian = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_charitarian";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				charitarian = this.getCharitarian(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return charitarian;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianCount(java.lang.String)
	 */
	public int getCharitarianCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_charitarian WHERE "
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
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianeHistory(java.lang.String)
	 */
	public CharitarianHistoryBean getCharitarianHistory(String condition) {
		CharitarianHistoryBean charitarianHistory = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_charitarian_history";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				charitarianHistory = this.getCharitarianHistory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return charitarianHistory;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianeHistoryCount(java.lang.String)
	 */
	public int getCharitarianHistoryCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_charitarian_history WHERE "
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
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianeHistoryList(java.lang.String)
	 */
	public Vector getCharitarianHistoryList(String condition) {
		Vector charitarianHistoryList = new Vector();
		CharitarianHistoryBean charitarianHistory = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_charitarian_history";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				charitarianHistory = this.getCharitarianHistory(rs);
				charitarianHistoryList.add(charitarianHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return charitarianHistoryList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#getCharitarianList(java.lang.String)
	 */
	public Vector getCharitarianList(String condition) {
		Vector charitarianeList = new Vector();
		CharitarianBean charitarian = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_charitarian";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				charitarian = this.getCharitarian(rs);
				charitarianeList.add(charitarian);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return charitarianeList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#updateCharitarian(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateCharitarian(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_charitarian SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.ICharitarianService#updateCharitarianeHistory(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateCharitarianHistory(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_charitarian_history SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private CharitarianBean getCharitarian(ResultSet rs) throws SQLException {
		CharitarianBean charitarian = new CharitarianBean();
		charitarian.setId(rs.getInt("id"));
		charitarian.setUserId(rs.getInt("user_id"));
		charitarian.setMoney(rs.getLong("money"));
		charitarian.setCount(rs.getInt("count"));
		charitarian.setCreateDatetime(rs.getString("create_datetime"));
		return charitarian;
	}

	private CharitarianHistoryBean getCharitarianHistory(ResultSet rs) throws SQLException {
		CharitarianHistoryBean charitarianHistory = new CharitarianHistoryBean();
		charitarianHistory.setId(rs.getInt("id"));
		charitarianHistory.setCharitarianId(rs.getInt("charitarian_id"));
		charitarianHistory.setReceiveId(rs.getInt("receive_id"));
		charitarianHistory.setCreateDatetime(rs.getString("create_datetime"));
		return charitarianHistory;
	}
}
