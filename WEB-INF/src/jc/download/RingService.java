package jc.download;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import jc.download.PringBean;
import jc.download.PringFileBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.db.DbOperation;

public class RingService {

	public PringBean getPring(String condition) {

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
		DbOperation dbOp = new DbOperation();
		if (!dbOp.init()) {
			return null;
		}

		ResultSet rs = dbOp.executeQuery(sql);

		try {
			if (rs.next()) {
				pring = getRingBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			if (pring != null) {
				OsCacheUtil.put(key, pring, OsCacheUtil.RING_GROUP);
			}
		}
		// mcq_2006-09-13_缓存_end

		return pring;
	}
	
	public Vector getPringsList(String condition) {
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
		try {
			while (rs.next()) {
				pring = getRingBean(rs);
				pringsList.add(pring);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, pringsList, OsCacheUtil.RING_GROUP);
		}
		// mcq_2006-09-13_缓存_end

		return pringsList;
	}
	
	public int addRing(PringBean ring) {
		int lastInsertId = 0;
		DbOperation db = new DbOperation();
		db.init();
		String query = "insert into pring (catalog_id,`name`,singer,type_id,file,remote_url,user_id,create_datetime,download_sum) values (?,?,?,?,?,?,?,now(),?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, ring.getCatalogId());
			pstmt.setString(2, ring.getName());
			pstmt.setString(3, ring.getSinger());
			pstmt.setInt(4, ring.getTypeId());
			pstmt.setString(5, ring.getFile());
			pstmt.setString(6, ring.getRemoteUrl());
			pstmt.setInt(7, ring.getUserId());
			pstmt.setInt(8, ring.getDownloadSum());
			db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	PringBean getRingBean(ResultSet rs) throws SQLException{
		PringBean bean = new PringBean();
		bean.setId(rs.getInt("id"));
		bean.setCatalogId(rs.getInt("catalog_id"));
		bean.setName(rs.getString("name"));
		bean.setSinger(rs.getString("singer"));
		bean.setTypeId(rs.getInt("type_id"));
		bean.setFile(rs.getString("file"));
		bean.setRemoteUrl(rs.getString("remote_url"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setCreateDatetime(rs.getTimestamp("create_datetime").getTime());
		bean.setDownloadSum(rs.getInt("download_sum"));
		return bean;
	}
	
	public PringFileBean getPringFile(String condition) {
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

		PringFileBean pringFile = null;

		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		ResultSet rs = dbOp.executeQuery(sql);
		try {
			if (rs.next()) {
				pringFile =getRingFileBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			if (pringFile != null) {
				OsCacheUtil.put(key, pringFile, OsCacheUtil.RING_GROUP);
			}
		}
		// mcq_2006-09-13_缓存_end

		return pringFile;
	}
	
	public int addRingFile(PringFileBean ringFile) {
		int lastInsertId = 0;
		DbOperation db = new DbOperation();
		db.init();
		String query = "insert into pring_file (pring_id,size,file_type,file) values (?,?,?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, ringFile.getPringId());
			pstmt.setInt(2, ringFile.getSize());
			pstmt.setString(3, ringFile.getFileType());
			pstmt.setString(4, ringFile.getFile());
			db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	PringFileBean getRingFileBean(ResultSet rs) throws SQLException{
		PringFileBean bean = new PringFileBean();
		bean.setId(rs.getInt("id"));
		bean.setPringId(rs.getInt("pring_id"));
		bean.setSize(rs.getInt("size"));
		bean.setFileType(rs.getString("file_type"));
		bean.setFile(rs.getString("file"));
		return bean;
	}
}
