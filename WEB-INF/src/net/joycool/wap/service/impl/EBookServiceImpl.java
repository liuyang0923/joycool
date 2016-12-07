/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class EBookServiceImpl implements IEBookService {
	static ICacheMap ebookCache = CacheManage.ebook;
	
	public EBookBean getEBook(String condition) {

		String sql = "SELECT * FROM pebook";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		String key = sql;
		synchronized(ebookCache) {
			EBookBean ebook = (EBookBean) ebookCache.get(key);
			if (ebook != null) {
				return ebook;
			}
	
			DbOperation dbOp = new DbOperation(true);
	
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {

				dbOp.release();
				return null;
			}
	
			try {
				if (rs.next()) {
					ebook = new EBookBean();
					ebook.setAuthor(rs.getString("author"));
					ebook.setCatalogId(rs.getInt("catalog_id"));
					ebook.setCreateDateTime(rs.getString("create_datetime"));
					ebook.setCreateUserId(rs.getInt("create_user_id"));
					ebook.setDescription(rs.getString("description"));
					ebook.setDownloadSum(rs.getInt("download_sum"));
					ebook.setFileUrl(rs.getString("file_url"));
					ebook.setId(rs.getInt("id"));
					ebook.setKb(rs.getInt("kb"));
					ebook.setName(rs.getString("name"));
					ebook.setPicUrl(rs.getString("pic_url"));
					ebook.setUpdateDateTime(rs.getString("update_datetime"));
					ebook.setUpdateUserId(rs.getInt("update_user_id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
			if (ebook != null) {
				ebookCache.put(key, ebook);
			}
			return ebook;
		}
	}

	public Vector getEBooksList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM pebook";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		String key = sql;
		synchronized(ebookCache) {
			Vector ebookList = (Vector) ebookCache.get(key);
			if (ebookList != null) {
				return ebookList;
			}
	
			Vector ebooksList = new Vector();
	
			EBookBean ebook = null;
	
			DbOperation dbOp = new DbOperation(true);
	
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
	
				dbOp.release();
				return null;
			}
	
			// 将结果保存
			try {
				while (rs.next()) {
					ebook = new EBookBean();
					ebook.setAuthor(rs.getString("author"));
					ebook.setCatalogId(rs.getInt("catalog_id"));
					// ebook.setCreateDateTime(rs.getString("create_datetime"));
					ebook.setCreateUserId(rs.getInt("create_user_id"));
					ebook.setDescription(rs.getString("description"));
					ebook.setDownloadSum(rs.getInt("download_sum"));
					ebook.setFileUrl(rs.getString("file_url"));
					ebook.setId(rs.getInt("id"));
					ebook.setKb(rs.getInt("kb"));
					ebook.setName(rs.getString("name"));
					ebook.setPicUrl(rs.getString("pic_url"));
					// ebook.setUpdateDateTime(rs.getString("update_datetime"));
					ebook.setUpdateUserId(rs.getInt("update_user_id"));
					ebooksList.add(ebook);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			dbOp.release();
	
			ebookCache.put(key, ebooksList);
	
			return ebooksList;
		}
	}

	public boolean updateEBook(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE pebook SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getEBooksCount(String condition) {

		String query = "SELECT count(id) as c_id FROM pebook WHERE "
				+ condition;

		String key = query;
		synchronized(ebookCache) {
			Integer c = (Integer) ebookCache.get(key);
			if (c != null) {
				return c.intValue();
			}
			int count = 0;

			DbOperation dbOp = new DbOperation(true);
	
			ResultSet rs = dbOp.executeQuery(query);
	
			try {
				if (rs.next()) {
					count = rs.getInt("c_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			dbOp.release();

			ebookCache.put(key, new Integer(count));

			return count;
		}
	}

}
