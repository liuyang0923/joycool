package net.joycool.wap.spec.buyfriends;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class DAOMaster {

	
	/**
	 * 初始化自身的数据
	 * @param master
	 */
	public boolean addMaster(BeanMaster master) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO master(uid, nick_name, money, price, slave_count,ransom_time) values(?,?,?,?,?, date_sub(now(),interval 3 day))";
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, master.getUid());
			pstmt.setString(2, master.getNickName());
			pstmt.setInt(3, master.getMoney());
			pstmt.setInt(4, master.getPrice());
			pstmt.setInt(5, master.getSlaveCount());
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
	 * 取得该用户的信息
	 * @param uid
	 * @return
	 */
	public BeanMaster getMasterByUid(int uid) {
		BeanMaster master = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM master where uid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return null;
		}
		try {
			db.getPStmt().setInt(1, uid);
			ResultSet rs = db.getPStmt().executeQuery();
			if (rs.next()) {
				master = getMaster(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return master;
	}
	
	private BeanMaster getMaster(ResultSet rs) throws SQLException{
		BeanMaster master = new BeanMaster();
		master.setUid(rs.getInt("uid"));
		master.setNickName(rs.getString("nick_name"));
		master.setMoney(rs.getInt("money"));
		master.setPrice(rs.getInt("price"));
		master.setSlaveCount(rs.getInt("slave_count"));
		master.setRansomTime(rs.getTimestamp("ransom_time"));
		master.setSalaryTime(rs.getTimestamp("salary_time").getTime());
		return master;
	}
	
	/**
	 * 增加用户的资产
	 * @param uid
	 * @param moneyOffSet 增加的资产多少
	 * @param slaveLock 是否奴隶数减1
	 */
	public boolean increaseMoneyByUid(int uid, int moneyOffSet, boolean slaveLock) {
		DbOperation db = new DbOperation(5);
		StringBuilder query = new StringBuilder("UPDATE master SET money = money + ? ");		
		if(slaveLock) {
			query.append(", slave_count = slave_count - 1 ");
		}
		query.append("WHERE uid = ?");
		if (!db.prepareStatement(query.toString())) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, moneyOffSet);
			pstmt.setInt(2, uid);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	public boolean ransom(int uid, int priceOffSet, int moneyOffSet) {
		DbOperation db = new DbOperation(5);
		//interval 1 day => interval 5 minute
		String query = "UPDATE master SET price = price + ?, money = money - ?, ransom_time = date_add(now(),interval 1 day) WHERE uid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		
		try {
			
			pstmt.setInt(1, priceOffSet);
			pstmt.setInt(2, moneyOffSet);
			pstmt.setInt(3, uid);
			
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	/**
	 * 增加用户的身价
	 */
	public boolean increasePriceByUid(int uid, int priceOffSet) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE master SET price = price + ? WHERE uid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		
		try {
			
			pstmt.setInt(1, priceOffSet);
			pstmt.setInt(2, uid);
			
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	/**
	 * 减少用户的金钱
	 * @param uid
	 * @param moneyOffSet
	 * @param slaveLock 是否奴隶加1
	 */
	public boolean decreaseMoneyByUid(int uid, int moneyOffSet, boolean slaveLock) {
		DbOperation db = new DbOperation(5);		
		StringBuilder query = new StringBuilder("UPDATE master SET money = money - ? ");
		if(slaveLock) {
			query.append(", slave_count = slave_count + 1 ");
		}
		query.append("WHERE uid = ?");
		if (!db.prepareStatement(query.toString())) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, moneyOffSet);
			pstmt.setInt(2, uid);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	/**
	 * 取得我能购买的奴隶，仅仅从我的好友里面显示
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getMastersICanBuyOfMyFriend(int uid, int money, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT m.uid as uid, m.nick_name as nick_name, m.price as price FROM user_friend as uf, master as m WHERE uf.user_id = ? AND uf.friend_id = m.uid and m.price <= ? limit ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, money);
			pstmt.setInt(3, start);
			pstmt.setInt(4, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getMasterOfMyFriend(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public int getCountMastersICanBuyOfMyFriend(int uid, int money) {
		int count = 0;/*
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(m.uid) as count FROM user_friend as uf, master as m WHERE uf.user_id = ? AND uf.friend_id = m.uid and m.price <= ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, uid);
			pstmt.setInt(2, money);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			db.release();
		}*/
		return count;
	}
	
	/**
	 * 取得我的所有好友的奴隶价格
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getMastersOfMyFriend(int uid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT m.uid as uid, m.nick_name as nick_name, m.price as price FROM user_friend as uf, master as m WHERE uf.user_id = ? AND uf.friend_id = m.uid limit ?,?";
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
				list.add(getMasterOfMyFriend(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private BeanMaster getMasterOfMyFriend(ResultSet rs) throws SQLException{
		BeanMaster master = new BeanMaster();
		master.setUid(rs.getInt("uid"));
		master.setNickName(rs.getString("nick_name"));
		master.setPrice(rs.getInt("price"));
		return master;
	}
	
	public int getCountMasterOfMyFriend(int uid) {
		IUserService userService = ServiceFactory.createUserService();
		return userService.getFriendCount("user_id = " + uid);
	}
	
	public boolean decreasePricePercentByUid(int uid, float percent) {
		DbOperation db = new DbOperation(5);
		String query = new String("UPDATE master SET price = price * (1 - ?) where uid = ?");
		if (!db.prepareStatement(query.toString())) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setFloat(1, percent);
			pstmt.setInt(2, uid);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	
//	public boolean ransom(int moneyOffSet, int priceOffSet){
//		
//		return true;
//	}
	
	
	
}
