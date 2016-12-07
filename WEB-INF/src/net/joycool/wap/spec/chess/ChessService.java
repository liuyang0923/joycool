package net.joycool.wap.spec.chess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class ChessService {
	
	public List getUserHistories(int userId, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.chess_his where user_id1 ="+userId+" union select * from mcoolgame.chess_his where user_id2 = "+userId+" order by id desc limit " + start + "," + limit);
		try{
			while(rs.next()) {
				list.add(getChessHistory2(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	
	public List getAllHistories(int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.chess_his order by id desc limit " + start + "," + limit);
		try{
			while(rs.next()) {
				list.add(getChessHistory2(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	// 获取简单的历史记录信息
	public ChessBean getChessHistory2(ResultSet rs) throws SQLException {
		ChessBean bean = new ChessBean();
		bean.setId(rs.getInt("id"));
		bean.winSide = rs.getInt("win_side");
		bean.userId1 = rs.getInt("user_id1");
		bean.userId2 = rs.getInt("user_id2");
		bean.moveCount = rs.getInt("move_count");
		bean.startTime = rs.getTimestamp("start_time").getTime();
		return bean;
	}
	
	public List getRecords() {
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		
		try{
			ResultSet rs = db.executeQuery("SELECT * from mcoolgame.chess order by id");
			
			while(rs.next()) {
				list.add(getRecord(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public ChessBean getRecord(int id) {
		ChessBean bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.chess where id = " + id);
		try{
			if(rs.next())
				bean = getRecord(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return bean;
	}
	
	public ChessBean getRecord(ResultSet rs) throws SQLException {
		ChessBean bean = new ChessBean();
		bean.setId(rs.getInt("id"));
		bean.winSide = rs.getInt("win_side");
		bean.userId1 = rs.getInt("user_id1");
		bean.userId2 = rs.getInt("user_id2");
		bean.status = rs.getInt("status");
		bean.startTime = rs.getTimestamp("start_time").getTime();
		bean.endTime = rs.getTimestamp("end_time").getTime();
		bean.flag = rs.getInt("flag");
		bean.point = rs.getInt("point");
		bean.history = StringUtil.toIntss(rs.getString("history"));
		return bean;
	}
	// 历史战局
	public ChessBean getChessHistory(String cond) {
		ChessBean bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.chess_his where " + cond);
		try{
			if(rs.next())
				bean = getChessHistory(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return bean;
	}
	public ChessBean getChessHistory(ResultSet rs) throws SQLException {
		ChessBean bean = new ChessBean();
		bean.setId(rs.getInt("id"));
		bean.winSide = rs.getInt("win_side");
		bean.userId1 = rs.getInt("user_id1");
		bean.userId2 = rs.getInt("user_id2");
		bean.startTime = rs.getTimestamp("start_time").getTime();
		bean.endTime = rs.getTimestamp("end_time").getTime();
		bean.moveCount = rs.getInt("move_count");
		bean.history = StringUtil.toIntss(rs.getString("history"));
		bean.history2 = java.util.Arrays.asList(rs.getString("history2").split("-"));
		return bean;
	}
	
	public boolean updateRecord(int id, String history) {
		DbOperation db = new DbOperation(4);
		
		String query = "update mcoolgame.chess set history = ? where id = ?";
		
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		
		try{
			pstmt.setString(1, history);
			pstmt.setInt(2, id);
			pstmt.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	public boolean updateRecord(ChessBean bean) {
		DbOperation db = new DbOperation(4);
		
		String query = "update mcoolgame.chess set user_id1 = ?, user_id2 = ?, status = ?, start_time = ?, end_time = ?, flag = ?, history = ?,win_side=? where id = ?";
		
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		
		try{
			pstmt.setInt(1, bean.getUserId1());
			pstmt.setInt(2, bean.getUserId2());
			pstmt.setInt(3, bean.getStatus());
			pstmt.setTimestamp(4, new Timestamp(bean.startTime));
			pstmt.setTimestamp(5, new Timestamp(bean.endTime));
			pstmt.setInt(6, bean.flag);
			pstmt.setString(7, StringUtil.intsToString(bean.history));
			pstmt.setInt(8, bean.winSide);
			pstmt.setInt(9, bean.getId());
			pstmt.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	
	public boolean addHistory(ChessBean bean) {
		DbOperation db = new DbOperation(4);
		
		String query = "insert into mcoolgame.chess_his(user_id1, user_id2, start_time, end_time, move_count, history,win_side,history2) values(?,?,?,?,?,?,?,?)";
		
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		
		try{
			pstmt.setInt(1, bean.getUserId1());
			pstmt.setInt(2, bean.getUserId2());
			pstmt.setTimestamp(3, new Timestamp(bean.startTime));
			pstmt.setTimestamp(4, new Timestamp(bean.endTime));
			pstmt.setInt(5, bean.getMoveCount());
			pstmt.setString(6, StringUtil.intsToString(bean.history));
			pstmt.setInt(7, bean.winSide);
			pstmt.setString(8, bean.concatHistory2());
			pstmt.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	public ChessUserBean getChessUser(String cond) {
		ChessUserBean bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.chess_user where " + cond);
		try{
			if(rs.next())
				bean = getChessUser(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return bean;
	}
	
	public ChessUserBean getChessUser(ResultSet rs) throws SQLException {
		ChessUserBean bean = new ChessUserBean();
		bean.setUserId(rs.getInt("user_id"));
		bean.setWin(rs.getInt("win"));
		bean.setLose(rs.getInt("lose"));
		bean.setDraw(rs.getInt("draw"));
		bean.setFlee(rs.getInt("flee"));
		bean.setPoint(rs.getInt("point"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}

	public boolean addChessUser(ChessUserBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "INSERT INTO mcoolgame.chess_user(user_id,point,create_time) VALUES(?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getPoint());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	
	
	public List getChessUserStat(int start, int limit) {
		DbOperation db = new DbOperation(4);
		List list = new ArrayList();
		String query = "select * from mcoolgame.chess_stat order by id asc limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			
			while(rs.next()) {
				Object[] obj = new Object[3];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = new Integer(rs.getInt("point"));
				
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	//用户当前的排名
	public int getChessCurStat(int uid) {
		DbOperation db = new DbOperation(4);
		
		String query = "select id from mcoolgame.chess_stat where uid = " + uid;
		
		try{
			return db.getIntResult(query);
		}catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}finally{
			db.release();
		}
	}
	
}
