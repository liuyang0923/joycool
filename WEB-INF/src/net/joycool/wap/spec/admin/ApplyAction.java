package net.joycool.wap.spec.admin;

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * 玩家申诉，提交的相关信息
 *	只有管理员可以看到
 */
public class ApplyAction extends CustomAction{
	
	public ApplyAction(HttpServletRequest request) {
		super(request);
	}

	public void addApply(int type, String content) {
		UserBean loginUser = getLoginUser();
		
		UserApplyBean bean = new UserApplyBean();
		if(loginUser != null)
			bean.setUserId(loginUser.getId());
		bean.setContent(content);
		bean.setType(type);
		bean.setToId(getParameterInt("toId"));
		addApply(bean);
	}

	public static boolean addApply(UserApplyBean bean) {
		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT INTO user_apply set user_id=?,type=?,content=?,to_id=?,status=0,bak='',create_time=now()";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getType());
			pstmt.setString(3, bean.getContent());
			pstmt.setInt(4, bean.getToId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
//		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	// 解决问题
	public static boolean solveApply(int id, int operator, String bak) {
		DbOperation dbOp = new DbOperation(2);
		String query = "UPDATE user_apply set operator=?,status=1,bak=?,solve_time=now() where id=" + id;

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, operator);
			pstmt.setString(2, bak);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	
	// 获得投诉的简单信息列表
	public static List getUserApplyList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(2);

		String query = "SELECT * from user_apply WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getUserApply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	public static UserApplyBean getUserApply(String cond) {
		UserApplyBean bean = null;
		DbOperation dbOp = new DbOperation(2);

		String query = "SELECT * from user_apply WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getUserApply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	public static UserApplyBean getUserApply(ResultSet rs) throws SQLException {
		UserApplyBean bean = new UserApplyBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setToId(rs.getInt("to_id"));
		bean.setType(rs.getInt("type"));
		bean.setStatus(rs.getInt("status"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setSolveTime(rs.getTimestamp("solve_time").getTime());
		return bean;
	}
	
	public static UserApplyBean getLatestUserApply(int userId) {
		UserApplyBean apply = getUserApply("user_id=" + userId + " order by id desc limit 1");

		if(apply != null && apply.getStatus() == 0)
			return apply;
		return null;
	}
}
