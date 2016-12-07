package jc.guest.farmer;

import java.sql.*;
import java.util.*;

import net.joycool.wap.util.db.DbOperation;

public class FarmerService {
	public boolean upd(String sql) {
		DbOperation db = new DbOperation(6);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}
	
	/**
	 * 获得农夫用户信息列表
	 * 
	 * @param cond
	 * @return
	 */
	public List getFarmerBeanList (String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from farmer_user where " + cond);
		try {
			while (rs.next()) {
				list.add(getFB(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 获得单个农夫用户信息
	 * 
	 * @param cond
	 * @return
	 */
	public FarmerBean getFarmerBean (String cond) {
		FarmerBean bean = null;
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from farmer_user where " + cond);
		try {
			if (rs.next()) {
				bean = getFB(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	FarmerBean getFB (ResultSet rs) throws SQLException {
		FarmerBean bean = new FarmerBean();
		bean.setUid(rs.getInt("uid"));
		bean.setLv1(rs.getInt("lv1_num"));
		bean.setLv2(rs.getInt("lv2_num"));
		bean.setLv3(rs.getInt("lv3_num"));
		bean.setLv4(rs.getInt("lv4_num"));
		bean.setChangeLv(rs.getInt("change_lv"));
		bean.setChangeTime(rs.getLong("change_time"));
		return bean;
	}
	
}
