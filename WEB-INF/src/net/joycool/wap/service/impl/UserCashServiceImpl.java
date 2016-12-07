/*
 * 2006-10-8
 * WUCX
 * 乐酷现金流追踪
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.jcadmin.UserCashBean;
import net.joycool.wap.service.infc.IUserCashService;
import net.joycool.wap.util.db.DbOperation;

public class UserCashServiceImpl implements IUserCashService{
	
	//添加现金增减信息
	public boolean addUserCash(UserCashBean usercash){
		//liuyi 2006-11-20 取消用户现金流记录  start
		if(true)return false;
		//liuyi 2006-11-20 取消用户现金流记录  end
		
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO user_cash(user_id, type, reason, create_datetime) VALUES(?, ?, ?, now())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, usercash.getUserId());
			pstmt.setInt(2, usercash.getType());
			pstmt.setString(3, usercash.getReason());
	
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

	//得到某个记录
	public UserCashBean getUserCash(String condition){
		UserCashBean usercash=null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM user_cash ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				usercash = getUserCash(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return usercash;
		
	}
//得到一组记录
	public Vector getUserCashList(String condition){
		Vector usercashvector=new Vector();
		UserCashBean usercash = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM user_cash ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				usercash = getUserCash(rs);
				usercashvector.add(usercash);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return usercashvector;
	}
//得到现金流个数
	public int getUserCashCount(String condition){
		int count=0;
		
		return count;
		
	}
	//结果赋值
	private UserCashBean getUserCash(ResultSet rs)throws SQLException
	{
		UserCashBean usercash = new UserCashBean();
		usercash.setId(rs.getInt("id"));
		usercash.setUserId(rs.getInt("user_id"));
		usercash.setCreateDatetime(rs.getString("create_datetime"));
		usercash.setType(rs.getInt("type"));
		usercash.setReason(rs.getString("reason"));
		return usercash;
		
	}
	//更新现金流
	public boolean updateUserCash(String set,String condition)
	{
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE user_cash SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
	//删除现金流信息
	public boolean deleteUserCash(String condition)
	{
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM user_cash WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
	
}