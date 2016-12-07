package jc.game.worldcup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class WorldCupService {
	
	public WcUser getUser(String cond){
		WcUser bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_user where " + cond);
		try {
			if (rs.next()){
				bean = getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getUserList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_user where " + cond);
		try {
			while (rs.next()){
				list.add(getUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addUser(WcUser bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into wc_user(user_id,point,create_time) values (?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getPoint());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	WcUser getUser(ResultSet rs) throws SQLException{
		WcUser bean = new WcUser();
		bean.setUserId(rs.getInt("user_id"));
		bean.setPoint(rs.getInt("point"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
	
	
	public WcRank getRank(String cond){
		WcRank bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_rank where " + cond);
		try {
			if (rs.next()){
				bean = getRank(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getRankList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_rank where " + cond);
		try {
			while (rs.next()){
				list.add(getRank(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	WcRank getRank(ResultSet rs) throws SQLException{
		WcRank bean = new WcRank();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setPoint(rs.getInt("point"));
		return bean;
	}
	
	public WcBet getBet(String cond){
		WcBet bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_bet where " + cond);
		try {
			if (rs.next()){
				bean = getBet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getBetList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_bet where " + cond);
		try {
			while (rs.next()){
				list.add(getBet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addBet(WcBet bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into wc_bet(user_id,match_id,team,bet,flag,bet_time,result,`point`,odds) values (?,?,?,?,?,now(),?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getMatchId());
			pstmt.setString(3, bean.getTeam());
			pstmt.setInt(4, bean.getBet());
			pstmt.setInt(5, bean.getFlag());
			pstmt.setInt(6, bean.getResult());
			pstmt.setInt(7, bean.getPoint());
			pstmt.setInt(8, bean.getOdds());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	WcBet getBet(ResultSet rs) throws SQLException{
		WcBet bean = new WcBet();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setMatchId(rs.getInt("match_id"));
		bean.setTeam(rs.getString("team"));
		bean.setBet(rs.getInt("bet"));
		bean.setFlag(rs.getInt("flag"));
		bean.setBetTime(rs.getTimestamp("bet_time").getTime());
		bean.setResult(rs.getInt("result"));
		bean.setPoint(rs.getInt("point"));
		bean.setOdds(rs.getInt("odds"));
		return bean;
	}
	
	public WcMatch getMatch(String cond){
		WcMatch bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_match where " + cond);
		try {
			if (rs.next()){
				bean = getMatch(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getMatchList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_match where " + cond);
		try {
			while (rs.next()){
				list.add(getMatch(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addMatch(WcMatch bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into wc_match(match_time,team1,team2,win,lose,tie,score1,score2,`show`,flag) values (?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getMatchTimeStr());
			pstmt.setString(2, bean.getTeam1());
			pstmt.setString(3, bean.getTeam2());
			pstmt.setInt(4, bean.getWin());
			pstmt.setInt(5, bean.getLose());
			pstmt.setInt(6, bean.getTie());
			pstmt.setInt(7, bean.getScore1());
			pstmt.setInt(8, bean.getScore2());
			pstmt.setInt(9, bean.getShow());
			pstmt.setInt(10, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	WcMatch getMatch(ResultSet rs) throws SQLException{
		WcMatch bean = new WcMatch();
		bean.setId(rs.getInt("id"));
		bean.setMatchTime(rs.getTimestamp("match_time").getTime());
		bean.setTeam1(rs.getString("team1"));
		bean.setTeam2(rs.getString("team2"));
		bean.setWin(rs.getInt("win"));
		bean.setLose(rs.getInt("lose"));
		bean.setTie(rs.getInt("tie"));
		bean.setScore1(rs.getInt("score1"));
		bean.setScore2(rs.getInt("score2"));
		bean.setShow(rs.getInt("show"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public WcInfo getInfo(String cond){
		WcInfo bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_info where " + cond);
		try {
			if (rs.next()){
				bean = getInfo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getInfoList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wc_info where " + cond);
		try {
			while (rs.next()){
				list.add(getInfo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addInfo(WcInfo bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into wc_info(limit_time,prize_id,help_id,subject_id) values (?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getLimitTime());
			pstmt.setInt(2, bean.getPrizeId());
			pstmt.setInt(3, bean.getHelpId());
			pstmt.setInt(4, bean.getSubjectId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	WcInfo getInfo(ResultSet rs) throws SQLException{
		WcInfo bean = new WcInfo();
		bean.setId(rs.getInt("id"));
		bean.setLimitTime(rs.getInt("limit_time"));
		bean.setPrizeId(rs.getInt("prize_id"));
		bean.setHelpId(rs.getInt("help_id"));
		bean.setSubjectId(rs.getInt("subject_id"));
		return bean;
	}
}
