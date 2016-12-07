package jc.family.game.vs.term;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.joycool.wap.util.db.DbOperation;

public class TermService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertTermBean(TermBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_term(name,game_type,fmids,create_time,info)values(?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getGameType());
			pstmt.setString(3, bean.getFmids());
			pstmt.setString(4, bean.getInfo());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertTermMatchBean(TermMatchBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_term_match(challenge_id,start_time,term_id,fma,fmb,wager)values(?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getChallengeId());
			pstmt.setTimestamp(2, new java.sql.Timestamp(bean.getStartTime()));
			pstmt.setInt(3, bean.getTermId());
			pstmt.setInt(4, bean.getFmidA());
			pstmt.setInt(5, bean.getFmidB());
			pstmt.setInt(6, bean.getWager());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	public List getTermMatchBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term_match where " + cond);
		try {
			while (rs.next()) {
				list.add(getTermMatchBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public TermMatchBean getTermMatchBean(String cond) {
		TermMatchBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term_match where " + cond);
		try {
			if (rs.next()) {
				bean = getTermMatchBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	private TermMatchBean getTermMatchBean(ResultSet rs) throws SQLException {
		TermMatchBean bean = new TermMatchBean();
		bean.setId(rs.getInt("id"));
		bean.setTermId(rs.getInt("term_id"));
		bean.setChallengeId(rs.getInt("challenge_id"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setFmidA(rs.getInt("fma"));
		bean.setFmidB(rs.getInt("fmb"));
		bean.setWager(rs.getInt("wager"));
		bean.setState(rs.getInt("state"));
		return bean;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	public List getTermBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term where " + cond);
		try {
			while (rs.next()) {
				list.add(getTermBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public TermBean getTermBean(String cond) {
		TermBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_term where " + cond);
		try {
			if (rs.next()) {
				bean = getTermBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	private TermBean getTermBean(ResultSet rs) throws SQLException {
		TermBean bean = new TermBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setGameType(rs.getInt("game_type"));
		bean.setFmids(rs.getString("fmids"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setInfo(rs.getString("info"));
		bean.setState(rs.getInt("state"));
		return bean;
	}
}
