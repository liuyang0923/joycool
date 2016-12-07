/*
 * Created on 2006-2-16
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.ring.PringBean;
import net.joycool.wap.bean.ring.PringFileBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IRingService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author MCQ
 * 
 */
public class RingServiceImpl implements IRingService {

	public PringBean getPring(String condition) {

		// 查询语句
		String sql = "SELECT * FROM pring";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (sql.indexOf("LIMIT") == -1) {
			sql = sql + " LIMIT 0, 1";
		}

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			PringBean pring = (PringBean) OsCacheUtil.get(key,
					OsCacheUtil.RING_GROUP, OsCacheUtil.RING_FLUSH_PERIOD);
			if (pring != null) {
				return pring;
			}
		}
		// mcq_2006-09-13_缓存_end

		PringBean pring = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			if (rs.next()) {
				pring = new PringBean();
				pring.setId(rs.getInt("id"));
				pring.setCatalog_id(rs.getInt("catalog_id"));
				pring.setName(rs.getString("name"));
				pring.setSinger(rs.getString("singer"));
				pring.setType_id(rs.getInt("Type_id"));
				pring.setFile(rs.getString("file"));
				pring.setRemote_url(rs.getString("remote_url"));
				pring.setUser_id(rs.getInt("user_id"));
				pring.setCreate_datetime(rs.getString("create_datetime"));
				pring.setDownload_sum(rs.getInt("download_sum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			if (pring != null) {
				OsCacheUtil.put(key, pring, OsCacheUtil.RING_GROUP);
			}
		}
		// mcq_2006-09-13_缓存_end

		// 返回结果
		return pring;
	}

	public Vector getPringsList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM pring";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (condition.indexOf("join") != -1)
			sql = condition;

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			Vector pringsList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.RING_GROUP, OsCacheUtil.RING_FLUSH_PERIOD);
			if (pringsList != null) {
				return pringsList;
			}
		}
		// mcq_2006-09-13_缓存_end

		Vector pringsList = new Vector();

		PringBean pring = null;

		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				pring = new PringBean();
				pring.setId(rs.getInt("id"));
				pring.setCatalog_id(rs.getInt("catalog_id"));
				pring.setName(rs.getString("name"));
				pring.setSinger(rs.getString("singer"));
				pring.setType_id(rs.getInt("Type_id"));
				pring.setFile(rs.getString("file"));
				pring.setRemote_url(rs.getString("remote_url"));
				pring.setUser_id(rs.getInt("user_id"));
				pring.setCreate_datetime(rs.getString("create_datetime"));
				pring.setDownload_sum(rs.getInt("download_sum"));
				pringsList.add(pring);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, pringsList, OsCacheUtil.RING_GROUP);
		}
		// mcq_2006-09-13_缓存_end

		// 返回结果
		return pringsList;
	}

	public boolean updatePring(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE pring SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getPringsCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pring WHERE " + condition;

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, OsCacheUtil.RING_GROUP,
					OsCacheUtil.RING_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		// mcq_2006-09-13_缓存_end

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

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.RING_GROUP);
		}
		// mcq_2006-09-13_缓存_end

		return count;
	}

	public PringFileBean getPring_file(String condition) {
		// 查询语句
		String sql = "SELECT * FROM pring_file";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (sql.indexOf("LIMIT") == -1) {
			sql = sql + " LIMIT 0, 1";
		}

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			PringFileBean pring_file = (PringFileBean) OsCacheUtil.get(key,
					OsCacheUtil.RING_GROUP, OsCacheUtil.RING_FLUSH_PERIOD);
			if (pring_file != null) {
				return pring_file;
			}
		}
		// mcq_2006-09-13_缓存_end

		PringFileBean pring_file = null;

		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			if (rs.next()) {
				pring_file = new PringFileBean();
				pring_file.setId(rs.getInt("id"));
				pring_file.setPring_id(rs.getInt("pring_id"));
				pring_file.setSize(rs.getInt("size"));
				pring_file.setFile_type(rs.getString("file_type"));
				pring_file.setFile(rs.getString("file"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			if (pring_file != null) {
				OsCacheUtil.put(key, pring_file, OsCacheUtil.RING_GROUP);
			}
		}
		// mcq_2006-09-13_缓存_end

		// 返回结果
		return pring_file;
	}

	public Vector getPring_filesList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM pring_file";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			Vector pring_filesList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.RING_GROUP, OsCacheUtil.RING_FLUSH_PERIOD);
			if (pring_filesList != null) {
				return pring_filesList;
			}
		}
		// mcq_2006-09-13_缓存_end

		Vector pring_filesList = new Vector();

		PringFileBean pring_file = null;

		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				pring_file = new PringFileBean();
				pring_file.setId(rs.getInt("id"));
				pring_file.setPring_id(rs.getInt("pring_id"));
				pring_file.setSize(rs.getInt("size"));
				pring_file.setFile_type(rs.getString("file_type"));
				pring_file.setFile(rs.getString("file"));
				pring_filesList.add(pring_file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, pring_filesList, OsCacheUtil.RING_GROUP);
		}
		// mcq_2006-09-13_缓存_end

		// 返回结果
		return pring_filesList;
	}

	public boolean updatePring_file(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE pring_file SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getPring_filesCount(String condition) {

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pring_file WHERE "
				+ condition;

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, OsCacheUtil.RING_GROUP,
					OsCacheUtil.RING_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		// mcq_2006-09-13_缓存_end

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		if (condition.indexOf("temp") != -1) {
			query = condition;
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

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.RING_GROUP);
		}
		// mcq_2006-09-13_缓存_end

		return count;
	}
}
