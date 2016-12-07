package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import net.joycool.wap.bean.bank.MoneyLogBean;
import net.joycool.wap.bean.item.UserItemLogBean;
import net.joycool.wap.util.db.DbOperation;
/** 
 * @author guip
 * @explain：
 * @datetime:2007-9-18 15:23:49
 */
public class UserbagItemServiceImpl {
	public static Vector getUserBagItemList(String condition) {
		Vector userItemList = new Vector();
		UserItemLogBean bean = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from user_item_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				bean = new UserItemLogBean();
				bean.setId(rs.getInt("id"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setUserBagId(rs.getInt("userbag_id"));
				bean.setToUserId(rs.getInt("to_user_id"));
				bean.setItemId(rs.getInt("item_id"));
				bean.setType(rs.getInt("type"));
				bean.setStack(rs.getInt("stack"));
				bean.setTime(rs.getString("time"));
				userItemList.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return userItemList;
	}
	public Vector getBankLogList(String condition) {
		Vector bankLogList = new Vector();
		MoneyLogBean bean = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_bank_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				bean = new MoneyLogBean();
				bean.setId(rs.getInt("id"));
				bean.setUserId(rs.getInt("user_id"));
	            bean.setMoney(rs.getLong("money"));
	            bean.setRUserId(rs.getInt("r_user_id"));
				bean.setType(rs.getInt("type"));
				bean.setTime(rs.getString("time"));
				bankLogList.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return bankLogList;
	}
	public static int getUserbagItemCount(String condition){
		int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM user_item_log WHERE " + condition;

        ResultSet rs = dbOp.executeQuery(query);

        try {
            if (rs.next()) {
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return count;
	}
}
