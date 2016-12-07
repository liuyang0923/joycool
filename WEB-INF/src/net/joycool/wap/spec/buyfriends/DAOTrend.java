package net.joycool.wap.spec.buyfriends;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class DAOTrend {

	public BeanTrend getTrendById(int id) {
		BeanTrend trend = null;
		
		DbOperation db = new DbOperation(5);
		String query = "select * from trend where id = ?";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return trend;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				trend = getTrend(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return trend;
		}finally{
			db.release();
		}
		
		return trend;
	}
	
	public boolean addGameTrend(BeanTrend trend) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO game_trend(uid, nick_name, content, time, type,uid2,nick_name2,title,link) values(?,?,?,now(),?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, trend.getUid());
			pstmt.setString(2, trend.getNickName());
			pstmt.setString(3, trend.getContent());
			pstmt.setInt(4, trend.getType());
			pstmt.setInt(5, trend.getUid2());
			pstmt.setString(6, trend.getNickName2());
			pstmt.setString(7, trend.getTitle());
			pstmt.setString(8, trend.getLink());
			pstmt.execute();
			
			trend.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	/**
	 * 增加一个动态
	 * @param trend
	 * @return
	 */
	public boolean addTrend(BeanTrend trend){
		DbOperation db = new DbOperation(5);
		
		String query = "INSERT INTO trend(uid, nick_name, content, time, type,uid2,nick_name2,title,link) values(?,?,?,now(),?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, trend.getUid());
			pstmt.setString(2, trend.getNickName());
			pstmt.setString(3, trend.getContent());
			pstmt.setInt(4, trend.getType());
			pstmt.setInt(5, trend.getUid2());
			pstmt.setString(6, trend.getNickName2());
			pstmt.setString(7, trend.getTitle());
			pstmt.setString(8, trend.getLink());
			pstmt.execute();
			
			trend.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	//根据id删除一个动态
	public boolean deleteTrendById(int id){
		DbOperation db = new DbOperation(5);
		String query = "DELETE FROM trend where id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, id);
			pstmt.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 获取用户动态的id
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getTrendIdByUid(int uid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT id FROM trend WHERE uid = ? order by id desc limit ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	//获取用户动态
	public List getTrendByUid(int uid, int start, int limit){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM trend WHERE uid = ? order by id desc limit ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getTrend(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public int getCountTrendByUid(int uid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(id) as count FROM trend WHERE uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, uid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally {
			db.release();
		}
		
		return count;
	}
	
	/**
	 * 得到游戏动态
	 * @param uid
	 * @param type
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getGameTrendByType(int uid, int type, int start, int limit) {
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		
		String query = "SELECT * FROM game_trend WHERE (uid = ? or uid2 = ?) and type = ? order by id desc LIMIT ?,?";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, uid);
			pstmt.setInt(3, type);
			pstmt.setInt(4, start);
			pstmt.setInt(5, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getTrend(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}

	//获取用户的某一类的动态
	public List getTrendByType(int uid, int type, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM trend WHERE uid = ? and type = ? order by id desc LIMIT ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, type);
			pstmt.setInt(3, start);
			pstmt.setInt(4, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getTrend(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private BeanTrend getTrend(ResultSet rs) throws SQLException{
		BeanTrend trend = new BeanTrend();
		trend.setId(rs.getInt("id"));
		trend.setUid(rs.getInt("uid"));
		trend.setNickName(rs.getString("nick_name"));
		trend.setContent(rs.getString("content"));
		trend.setTime(rs.getTimestamp("time"));
		trend.setType(rs.getInt("type"));
		trend.setUid2(rs.getInt("uid2"));
		trend.setNickName2(rs.getString("nick_name2"));
		trend.setTitle(rs.getString("title"));
		trend.setLink(rs.getString("link"));
		return trend;
	}
	
	public int getCountGameTrendByType(int uid, int type) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(id) as count FROM game_trend WHERE uid = ? AND type = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, type);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally {
			db.release();
		}
		return count;
	}
	
	public int getCountTrendByType(int uid, int type) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(id) as count FROM trend WHERE uid = ? AND type = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, type);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally {
			db.release();
		}
		return count;
	}
	
	/**
	 * 获取好友动态的id
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getFriendTrendIdByUid(int uid, int start, int limit) {
		//IUserService userService = ServiceFactory.createUserService();
		List friendList = UserInfoUtil.getUserFriends(uid);//userService.getUserFriendList(uid);
		List list = new LinkedList();
		if(friendList.size() == 0) {
			return list;
		}
		
		DbOperation db = new DbOperation(5);
		StringBuilder sb = new StringBuilder("SELECT id FROM trend WHERE uid in ");
		
		sb.append('(');
		for(int i = 0;i < friendList.size(); i++) {
			String userId = (String)friendList.get(i);
			sb.append(userId);
			if((i + 1) < friendList.size()) {
				sb.append(",");
			}
		}
		
		sb.append(")");
		
		sb.append(" order by id desc limit " + start + "," + limit);
		String query = sb.toString();

		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public List getTrendList(String cond) {
		List list = new LinkedList();
		
		DbOperation db = new DbOperation(5);
		String query = "SELECT id FROM trend WHERE " + cond;

		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	//获取好友动态
	public List getFriendTrendByUid(int uid, int start, int limit) {
		//IUserService userService = ServiceFactory.createUserService();
		List friendList = UserInfoUtil.getUserFriends(uid);
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		StringBuilder sb = new StringBuilder("SELECT * FROM trend WHERE uid in ");
		
		if(friendList.size() > 0) {
			sb.append("(");
		}
		
		for(int i = 0;i < friendList.size(); i++) {
			String userId = (String)friendList.get(i);
			sb.append(userId);
			if((i + 1) < friendList.size()) {
				sb.append(",");
			}
		}
		
		if(friendList.size() > 0) {
			sb.append(")");
		}
		
		sb.append(" order by id desc limit ?,?");
		String query = sb.toString();
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, start);
			pstmt.setInt(2, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getTrend(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
		
	}
	
	public int getCountFriendTrendByUid(int uid) {
		//IUserService userService = ServiceFactory.createUserService();
		List friendList = UserInfoUtil.getUserFriends(uid);
		int count = 0;
		DbOperation db = new DbOperation(5);
		StringBuilder sb = new StringBuilder("SELECT count(id) as count FROM trend WHERE uid in (");
		for(int i = 0;i < friendList.size(); i++) {
			String userId = (String)friendList.get(i);
			sb.append(userId);
			if((i + 1) < friendList.size()) {
				sb.append(",");
			}
		}
		sb.append(")");
		String query = sb.toString();
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
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
	//获取好友某一类的动态
	public List getFriendTypeTrendByUid(int uid, int type, int start, int limit) {
		//IUserService userService = ServiceFactory.createUserService();
		List friendList = UserInfoUtil.getUserFriends(uid);
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		StringBuilder sb = new StringBuilder("SELECT * FROM trend WHERE uid in (");
		for(int i = 0;i < friendList.size(); i++) {
			String userId = (String)friendList.get(i);
			sb.append(userId);
			if((i + 1) < friendList.size()) {
				sb.append(",");
			}
			
		}
		sb.append(") and type = ? limit ?,?");
		String query = sb.toString();
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, type);
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getTrend(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public int getCountFriendTypeTrendByUid(int uid, int type) {
		//IUserService userService = ServiceFactory.createUserService();
		List friendList = UserInfoUtil.getUserFriends(uid);
		int count = 0;
		DbOperation db = new DbOperation(5);
		StringBuilder sb = new StringBuilder("SELECT count(id) as count FROM trend WHERE uid in (");
		for(int i = 0;i < friendList.size(); i++) {
			String userId = (String)friendList.get(i);
			sb.append(userId);
			if((i + 1) < friendList.size()) {
				sb.append(",");
			}
		}
		sb.append(") and type = ?");
		String query = sb.toString();
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, type);
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
