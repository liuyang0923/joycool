package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.service.infc.IModuleService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class ModuleServiceImpl implements IModuleService{
	private static String table = "jc_module";

	public boolean addModule(ModuleBean moduleBean) {
			// TODO Auto-generated method stub
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();

			String query = "INSERT INTO " + table + "(name,image,url_pattern,priority,return_url,entry_url,pos_name,short_name,flag) values(?,?,?,?,?,?,?,?,?)";

			// 准备
			if (!dbOp.prepareStatement(query)) {
				dbOp.release();
				return false;
			}
			// 传递参数
			PreparedStatement pstmt = dbOp.getPStmt();
			try {
				pstmt.setString(1, moduleBean.getName());
				pstmt.setString(2, moduleBean.getImage());
				pstmt.setString(3, moduleBean.getUrlPattern());
				pstmt.setInt(4, moduleBean.getPriority());
				pstmt.setString(5, moduleBean.getReturnUrl());
				pstmt.setString(6, moduleBean.getEntryUrl());
				pstmt.setString(7, moduleBean.getPosName());
				pstmt.setString(8, moduleBean.getShortName());
				pstmt.setInt(9, moduleBean.getFlag());
				// 执行
				dbOp.executePstmt();
				moduleBean.setId(dbOp.getLastInsertId());
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				// 释放资源
				dbOp.release();
			}

			return true;
	}

	public ModuleBean getModule(String condition) {
		// TODO Auto-generated method stub
		ModuleBean module = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM " + table + " ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs!=null && rs.next()) {
				module = new ModuleBean();
				module.setId(rs.getInt("id"));
				module.setName(rs.getString("name"));
				module.setImage(rs.getString("image"));
				module.setUrlPattern(rs.getString("url_pattern"));
				module.setPriority(rs.getInt("priority"));
				module.setReturnUrl(rs.getString("return_url"));
				
				module.setEntryUrl(rs.getString("entry_url"));
				module.setFlag(rs.getInt("flag"));
				
				module.setPosName(rs.getString("pos_name"));
				module.setShortName(rs.getString("short_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return module;
	}

	public List getModuleList(String condition) {
		List ret = new ArrayList();
		ModuleBean module = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM " + table + " ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				module = new ModuleBean();
				module.setId(rs.getInt("id"));
				module.setName(rs.getString("name"));
				module.setImage(rs.getString("image"));
				module.setUrlPattern(rs.getString("url_pattern"));
				module.setPriority(rs.getInt("priority"));
				module.setReturnUrl(rs.getString("return_url"));
				
				module.setEntryUrl(rs.getString("entry_url"));
				module.setFlag(rs.getInt("flag"));
				
				module.setPosName(rs.getString("pos_name"));
				module.setShortName(rs.getString("short_name"));
				ret.add(module);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return ret;
	}

	public boolean deleteModule(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM " + table + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean updateModule(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE " + table + " SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getModuleCount(String condition) {
		// TODO Auto-generated method stub
		int ret = 0;
		
		String query = "SELECT count(id) as c_id FROM " + table + " ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		
		ret = SqlUtil.getIntResult(query, Constants.DBShortName);
		
		return ret;
	}

}
