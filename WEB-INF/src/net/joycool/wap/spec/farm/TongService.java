package net.joycool.wap.spec.farm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 帮派
 * @datetime:1007-10-24
 */
public class TongService {
	// 读取帮派
	public TongBean getTong(String cond) {
		TongBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_tong WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getTong(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}

	private TongBean getTong(ResultSet rs) throws SQLException {
		TongBean bean = new TongBean();
		bean.setId(rs.getInt("id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setName(rs.getString("name"));
		bean.setDuty1(rs.getInt("duty1"));
		bean.setDuty2(rs.getInt("duty2"));
		bean.setDuty3(rs.getInt("duty3"));
		bean.setDuty4(rs.getInt("duty4"));
		bean.setDuty5(rs.getInt("duty5"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setRank(rs.getInt("rank"));
		bean.setCount(rs.getInt("count"));
		bean.setGroup(rs.getInt("group"));
		return bean;
	}
	
	// 读取帮派人员
	public TongUserBean getTongUser(String cond) {
		TongUserBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_tong_user WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getTongUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}

	private TongUserBean getTongUser(ResultSet rs) throws SQLException {
		TongUserBean bean = new TongUserBean();
		bean.setId(rs.getInt("id"));
		bean.setTongId(rs.getInt("tong_id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setDuty(rs.getInt("duty"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}

	public boolean addTongUser(TongUserBean bean) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_tong_user(user_id,tong_id,duty,create_time) VALUES(?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getTongId());
			pstmt.setInt(3, bean.getDuty());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
}
