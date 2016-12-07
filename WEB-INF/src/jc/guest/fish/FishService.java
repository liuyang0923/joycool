package jc.guest.fish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.action.job.fish.AreaBean;
import net.joycool.wap.action.job.fish.FishBean;
import net.joycool.wap.action.job.fish.FishUserBean;
import net.joycool.wap.action.job.fish.GlobalEventBean;
import net.joycool.wap.action.job.fish.PullBean;
import net.joycool.wap.action.job.fish.PullEventBean;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @datetime 2007-4-23 10:10
 * @explain 钓鱼的数据库操作
 */
public class FishService {

	public FishUserBean getUser(String condition) {
		FishUserBean user = null;
		DbOperation dbOp = new DbOperation(6);
		String query = "SELECT * from fish_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return user;
	}

	private FishUserBean getUser(ResultSet rs) throws SQLException {
		FishUserBean user = new FishUserBean();
		user.setId(rs.getInt("id"));
		return user;
	}

	public boolean addUser(FishUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(6);
		String query = "INSERT INTO fish_user(user_id) VALUES(?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		dbOp.executePstmt();

		bean.setId(dbOp.getLastInsertId());
		
		dbOp.release();
		return true;
	}

	public boolean updateUser(String set, String condition) {
		boolean result;
		DbOperation dbOp = new DbOperation(6);
		String query = "UPDATE fish_user SET " + set + " WHERE " + condition;
		result = dbOp.executeUpdate(query);
		dbOp.release();
		return result;
	}

	public AreaBean getArea(ResultSet rs) throws SQLException {
		AreaBean bean = new AreaBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setRand(rs.getInt("rand"));
		bean.setImage(rs.getString("image"));
		return bean;
	}
	
	public List getAreaList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fish_area";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				list.add(getArea(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}
	public FishBean getFish(ResultSet rs) throws SQLException {
		FishBean bean = new FishBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setPrice(rs.getInt("price"));
		bean.setAreaId(rs.getInt("area_id"));
		bean.setPullId(rs.getInt("pull_id"));
		bean.setImage(rs.getString("image"));
		return bean;
	}
	
	public List getFishList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fish_fish";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				list.add(getFish(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}
	
	public PullBean getPull(ResultSet rs) throws SQLException {
		PullBean bean = new PullBean();
		bean.setId(rs.getInt("id"));
		bean.setPattern(rs.getString("pattern"));
		bean.setPullMode(rs.getString("pull_mode"));
		bean.setImage(rs.getString("image"));
		return bean;
	}
	
	public List getPullList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fish_pull";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				list.add(getPull(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}
	
	public PullEventBean getPullEvent(ResultSet rs) throws SQLException {
		PullEventBean bean = new PullEventBean();
		bean.setId(rs.getInt("id"));
		bean.setAreaId(rs.getInt("area_id"));
		bean.setDesc(rs.getString("desc"));
		bean.setLog(rs.getString("log"));
		bean.setExp(rs.getInt("exp"));
		bean.setMoney(rs.getInt("money"));
		bean.setImage(rs.getString("image"));
		return bean;
	}
	
	public List getPullEventList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fish_pull_event";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				PullEventBean bean = getPullEvent(rs);
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}

	public GlobalEventBean getGlobalEvent(ResultSet rs) throws SQLException {
		GlobalEventBean bean = new GlobalEventBean();
		bean.setId(rs.getInt("id"));
		bean.setAreaId(rs.getInt("area_id"));
		bean.setBeginDesc(rs.getString("begin_desc"));
		bean.setEndDesc(rs.getString("end_desc"));
		bean.setRandChange(rs.getInt("rand_change"));
		bean.setDuration(rs.getInt("duration") * 1000);	// 转换为long的毫秒
		return bean;
	}
	
	public List getGlobalEventList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fish_global_event";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				GlobalEventBean bean = getGlobalEvent(rs);
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return list;
	}
	
}
