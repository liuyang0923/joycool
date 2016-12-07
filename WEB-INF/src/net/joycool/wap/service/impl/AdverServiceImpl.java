package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.AdverBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.service.infc.IAdverServic;
import net.joycool.wap.util.db.DbOperation;

public class AdverServiceImpl implements IAdverServic {
	public AdverBean getAdver(String condition) {

		AdverBean adverBean = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM adver";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				adverBean = new AdverBean();
				adverBean.setId(rs.getInt("id"));
				adverBean.setName(rs.getString("name"));
				adverBean.setTitle(rs.getString("title"));
				adverBean.setUrl(rs.getString("url"));
				adverBean.setGroup(rs.getInt("groupid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return adverBean;

	}

	public Vector getAdverList(String condition) {

		String sql = "SELECT * FROM adver";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		String key = condition;
		Vector adverList;
		synchronized(CacheManage.jspAdver) {
			adverList = (Vector) CacheManage.jspAdver.get(key);
			if (adverList != null) {
				return adverList;
			}
			adverList = new Vector();
	
			AdverBean adverBean = null;
	
			DbOperation dbOp = new DbOperation();
	
			if (!dbOp.init()) {
				return null;
			}
	
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
				dbOp.release();
				return null;
			}
			try {
				while (rs.next()) {
					adverBean = new AdverBean();
					adverBean.setId(rs.getInt("id"));
					adverBean.setName(rs.getString("name"));
					adverBean.setTitle(rs.getString("title"));
					adverBean.setUrl(rs.getString("url"));
					adverBean.setGroup(rs.getInt("groupid"));
					adverList.add(adverBean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
	
			CacheManage.jspAdver.put(key, adverList);
		}

		return adverList;

	}

	public boolean updateAdver(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE adver SET " + set + " WHERE " + condition;
		// System.out.println("query ="+query);
		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IUserService#deleteUserBag(java.lang.String)
	 */
	public boolean deleteAdver(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM adver WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getAdverCount(String condition) {
		int count = 0;
		return count;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ITopService#addUserTop(net.joycool.wap.bean.top.UserTopBean)
	 */
	public boolean addAdvice(String condition) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO adver(name,title,url,groupid) VALUES("
				+ condition + ")";
		// System.out.println("query ="+query);
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}
}
