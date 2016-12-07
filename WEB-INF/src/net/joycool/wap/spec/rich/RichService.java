package net.joycool.wap.spec.rich;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 大富翁
 * @datetime:1007-10-24
 */
public class RichService {

	public HashMap getMapMap(String condition) {
		HashMap map = new HashMap();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from rich_map where " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				RichNodeBean bean = getRichNode(rs);
				map.put(Integer.valueOf(bean.getId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return map;
	}
	
	private RichNodeBean getRichNode(ResultSet rs) throws SQLException {
		RichNodeBean bean = new RichNodeBean();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("type"));
		bean.setValue(rs.getInt("value"));
		return bean;
	}
	
	public HashMap getItemMap(String condition) {
		HashMap map = new HashMap();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from rich_item where " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				RichItemBean bean = getRichItem(rs);
				map.put(Integer.valueOf(bean.getId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return map;
	}
	
	private RichItemBean getRichItem(ResultSet rs) throws SQLException {
		RichItemBean bean = new RichItemBean();
		bean.setId(rs.getInt("id"));
		bean.setPrice(rs.getInt("price"));
		bean.setName(rs.getString("name"));
		return bean;
	}
	
	private RichUserBean getRichUser(ResultSet rs) throws SQLException {
		RichUserBean bean = new RichUserBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setRole(rs.getInt("role"));
		bean.setPosition(rs.getInt("position"));
		bean.setMoney(rs.getInt("money"));
		bean.setMoney2(rs.getInt("money2"));
		bean.setSaving(rs.getInt("saving"));
		bean.setTimeLeft(rs.getInt("time_left"));
		return bean;
	}
	public RichUserBean getRichUser(String condition) {
		RichUserBean user = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from rich_user WHERE " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				user = this.getRichUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return user;
	}
	
	public HashMap getBuildingMap(String condition) {
		HashMap map = new HashMap();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from rich_building where " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				RichBuilding bean = getRichBuilding(rs);
				map.put(Integer.valueOf(bean.getId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return map;
	}
	
	private RichBuilding getRichBuilding(ResultSet rs) throws SQLException {
		RichBuilding bean = new RichBuilding();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("type"));
		bean.setPrice(rs.getInt("price"));
		bean.setName(rs.getString("name"));
		return bean;
	}
	
	public boolean addRichLog(RichLogBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "INSERT INTO rich_log(detail,flag,`interval`,user_count,create_time) VALUES(?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getDetail());
			pstmt.setInt(2, bean.getFlag());
			pstmt.setInt(3, bean.getInterval());
			pstmt.setInt(4, bean.getUserCount());
			
			pstmt.execute();
			bean.setId(dbOp.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	
	public RichLogBean getRichLog(String condition) {
		RichLogBean bean = null;
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from rich_log where " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getRichLog(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	public List getRichLogList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from rich_log where " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getRichLog(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	private RichLogBean getRichLog(ResultSet rs) throws SQLException {
		RichLogBean bean = new RichLogBean();
		bean.setId(rs.getInt("id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setUserCount(rs.getInt("user_count"));
		bean.setDetail(rs.getString("detail"));
		bean.setInterval(rs.getInt("interval"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
}
