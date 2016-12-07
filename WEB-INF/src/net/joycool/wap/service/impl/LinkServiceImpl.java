package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.LinkBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.ILinkService;
import net.joycool.wap.util.db.DbOperation;

public class LinkServiceImpl implements ILinkService {

	public boolean addLink(LinkBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_link(type_id, module_id, sub_module_id, url, name) "
				+ " VALUES(?, ?, ?, ?, ?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTypeId());
			pstmt.setInt(2, bean.getModuleId());
			pstmt.setInt(3, bean.getSubModuleId());
			pstmt.setString(4, bean.getUrl());
			pstmt.setString(5, bean.getDesc());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return false;
	}

	public boolean updateLink(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_link SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteLink(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_link WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public LinkBean getLink(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_link";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (sql.indexOf("LIMIT") == -1) {
			sql = sql + " LIMIT 0, 1";
		}

		String key = sql;
		LinkBean bean = (LinkBean) OsCacheUtil.get(key, OsCacheUtil.LINK_GROUP,
				OsCacheUtil.Link_FLUSH_PERIOD);
		if (bean != null) {
			return bean;
		}

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
				bean = new LinkBean();
				bean.setId(rs.getInt("id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setModuleId(rs.getInt("module_id"));
				bean.setSubModuleId(rs.getInt("sub_module_id"));
				bean.setUrl(rs.getString("url"));
				bean.setDesc(rs.getString("name"));

				OsCacheUtil.put(key, bean, OsCacheUtil.RING_GROUP);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// 返回结果
		return bean;
	}

	public List getLinkList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_link";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (condition.indexOf("join") != -1)
			sql = condition;

		// mcq_2006-09-13_缓存_start
		// 判断是否是用缓存
		String key = sql;
		List ret = (List) OsCacheUtil.get(key, OsCacheUtil.LINK_LIST_GROUP,
				OsCacheUtil.LINK_LIST_FLUSH_PERIOD);
		if (ret != null) {
			return ret;
		}
		// mcq_2006-09-13_缓存_end

		ret = new Vector();

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
				LinkBean bean = new LinkBean();
				bean.setId(rs.getInt("id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setModuleId(rs.getInt("module_id"));
				bean.setSubModuleId(rs.getInt("sub_module_id"));
				bean.setUrl(rs.getString("url"));
				bean.setDesc(rs.getString("name"));

				ret.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		OsCacheUtil.put(key, ret, OsCacheUtil.LINK_LIST_GROUP);

		// 返回结果
		return ret;
	}

}
