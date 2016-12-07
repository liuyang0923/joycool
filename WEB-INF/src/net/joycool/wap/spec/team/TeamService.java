package net.joycool.wap.spec.team;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 帮派
 * @datetime:1007-10-24
 */
public class TeamService {
	public TeamBean getTeam(String cond) {
		TeamBean bean = null;
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from team WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getTeam(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}

	private TeamBean getTeam(ResultSet rs) throws SQLException {
		TeamBean bean = new TeamBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setCount(rs.getInt("count"));
		bean.setFlag(rs.getInt("flag"));
		bean.setClass1(rs.getInt("class1"));
		bean.setClass2(rs.getInt("class2"));
		bean.setDuty0(rs.getInt("duty0"));
		return bean;
	}
	
	public List getUserList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from team_user WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTeamUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	private TeamUserBean getTeamUser(ResultSet rs) throws SQLException {
		TeamUserBean bean = new TeamUserBean();
		bean.setId(rs.getInt("id"));
		bean.setTeamId(rs.getInt("team_id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setDuty(rs.getInt("duty"));
		bean.setFlag(rs.getInt("flag"));
		bean.setName(rs.getString("name"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setReadId(rs.getInt("read_id"));
		return bean;
	}
		
	public List getChatList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from team_chat WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getChat(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	private TeamChatBean getChat(ResultSet rs) throws SQLException {
		TeamChatBean bean = new TeamChatBean();
		bean.setId(rs.getInt("id"));
		bean.setFromId(rs.getInt("from_id"));
		bean.setTeamId(rs.getInt("team_id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setFromName(rs.getString("from_name"));
		bean.setTime(rs.getTimestamp("time").getTime());
		bean.setContent(rs.getString("content"));
		return bean;
	}

	public boolean addChat(TeamChatBean bean) {
		DbOperation dbOp = new DbOperation(3);
		String query = "INSERT INTO team_chat(team_id,from_id,from_name,content,flag,time) VALUES(?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTeamId());
			pstmt.setInt(2, bean.getFromId());
			pstmt.setString(3, bean.getFromName());
			pstmt.setString(4, bean.getContent());
			pstmt.setInt(5, bean.getFlag());
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
	
	public boolean addUser(TeamUserBean bean) {
		DbOperation dbOp = new DbOperation(3);
		String query = "INSERT INTO team_user(team_id,name,duty,flag,user_id,create_time) VALUES(?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTeamId());
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getDuty());
			pstmt.setInt(4, bean.getFlag());
			pstmt.setInt(5, bean.getUserId());
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

	public boolean addTeam(TeamBean bean) {
		DbOperation dbOp = new DbOperation(3);
		String query = "INSERT INTO team(name,info,duty0,flag,`count`,create_time) VALUES(?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getDuty0());
			pstmt.setInt(4, bean.getFlag());
			pstmt.setInt(5, bean.getCount());
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

	public void updateTeamCount(TeamBean bean) {
		SqlUtil.executeUpdate("update team set count=" + bean.getCount() + " where id=" + bean.getId(), 3);
	}
	
	// 动作列表
	public List getActList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from team_act WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTeamAct(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	private TeamActBean getTeamAct(ResultSet rs) throws SQLException {
		TeamActBean bean = new TeamActBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setName2(rs.getString("name2"));
		bean.setContent(rs.getString("content"));
		bean.setContent2(rs.getString("content2"));
		return bean;
	}
	
	public List getTeamClassList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from team_class WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTeamClass(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	private TeamClassBean getTeamClass(ResultSet rs) throws SQLException {
		TeamClassBean bean = new TeamClassBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setParent(rs.getInt("parent"));
		return bean;
	}
}
