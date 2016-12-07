package net.joycool.wap.spec.buyfriends;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class DAOVisit {

	public boolean addVisit(BeanVisit visit) {
		DbOperation db = new DbOperation(5);		
		String query = "INSERT INTO visit(from_uid, from_nickname, to_uid, to_nickName, visit_time) values(?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, visit.getFromUid());
			pstmt.setString(2, visit.getFromNickName());
			pstmt.setInt(3, visit.getToUid());
			pstmt.setString(4, visit.getToNickName());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	public boolean deleteVisit(int fromUid, int toUid) {
		DbOperation db = new DbOperation(5);
		String query = "delete from visit where from_uid = ? and to_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, fromUid);
			pstmt.setInt(2, toUid);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	public List getVisitByToUid(int toUid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM visit where to_uid = ? order by visit_time desc limit ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, toUid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getVisit(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private BeanVisit getVisit(ResultSet rs) throws SQLException{
		BeanVisit visit = new BeanVisit();
		visit.setId(rs.getInt("id"));
		visit.setFromUid(rs.getInt("from_uid"));
		visit.setFromNickName(rs.getString("from_nickname"));
		visit.setToUid(rs.getInt("to_uid"));
		visit.setToNickName(rs.getString("to_nickname"));
		visit.setVisitTime(rs.getTimestamp("visit_time"));
		return visit;
	}
	
	public int getCountVisitByToUid(int toUid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(*) as count FROM visit where to_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, toUid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			db.release();
		}
		return count;
	}
}
