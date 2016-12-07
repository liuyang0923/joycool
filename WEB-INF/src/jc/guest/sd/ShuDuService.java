package jc.guest.sd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class ShuDuService {
	public boolean upd(String sql) {
		DbOperation db = new DbOperation(6);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 添加一场游戏
	 * @param bean
	 * @return
	 */
	public boolean insertShuDuBean(ShuDuBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(6);
		String query = "insert into shudu (uid,lvl,con_start,start_time) values (?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUid());
			pstmt.setInt(2, bean.getLvl());
			pstmt.setString(3, bean.getStart());
			pstmt.setLong(4, bean.getStartTime());
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
	
	
	/**
	 * 记录成绩
	 * @param bean
	 * @return
	 */
	public boolean alterShuDuBean(ShuDuBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(6);
		String query = "update shudu set con_start=?,con_over=?,con_middle=?,is_over=?,spend_time=? where id=" + bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getStart());
			pstmt.setString(2, bean.getOver());
			pstmt.setString(3, bean.getMiddle());
			pstmt.setInt(4, bean.getIsOver());
			pstmt.setLong(5, bean.getSpendTime());
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
	
	/**
	 * 查出数独游戏 
	 * @param cond
	 * @return
	 */
	public ShuDuBean getShuDuBean(String cond){
		ShuDuBean bean = null;
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from shudu where " + cond);
		try {
			if (rs.next()) {
				bean = getShuDuBeanFromRS(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	/**
	 * 获得一个数独用户信息
	 * 
	 * @param cond
	 * @return
	 */
	public ShuDuUser getShuDuUser(String cond){
		ShuDuUser bean = null;
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from shudu_user where " + cond);
		try {
			if (rs.next()) {
				bean = getAUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 获得用户列表
	 * 
	 * @param cond
	 * @return
	 */
	public List getShuDuUserList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from shudu_user where " + cond);
		try {
			while (rs.next()) {
				list.add(getAUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	

	ShuDuBean getShuDuBeanFromRS(ResultSet rs) throws SQLException {
		ShuDuBean bean = new ShuDuBean();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("uid"));
		bean.setLvl(rs.getInt("lvl"));
		bean.setIsOver(rs.getInt("is_over"));
		bean.setStart(rs.getString("con_start"));
		bean.setOver(rs.getString("con_over"));
		bean.setMiddle(rs.getString("con_middle"));
		bean.setStartTime(rs.getLong("start_time"));
		bean.setEndTime(rs.getLong("end_time"));
		bean.setSpendTime(rs.getLong("spend_time"));
		return bean;
	}
	
	ShuDuUser getAUser(ResultSet rs) throws SQLException {
		ShuDuUser bean = new ShuDuUser();
		bean.setUid(rs.getInt("uid"));
		bean.setLvl1(rs.getInt("lvl1_num"));
		bean.setLvl2(rs.getInt("lvl2_num"));
		bean.setLvl3(rs.getInt("lvl3_num"));
		bean.setLvl4(rs.getInt("lvl4_num"));
		return bean;
	}
}
