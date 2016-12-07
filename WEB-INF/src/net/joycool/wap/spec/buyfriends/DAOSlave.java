package net.joycool.wap.spec.buyfriends;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class DAOSlave {

	
	public boolean updateSlaveFrozenTime(int slaveUid, long frozenTime) {
		DbOperation db = new DbOperation(5);
		String query = "update slave set frozen_time = ? where slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, slaveUid);
			pstmt.setLong(2, frozenTime);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean deleteTempSlave(){
		DbOperation db = new DbOperation(5);
		String query = "DELETE FROM slave where rank = 0 and date_add(buy_time, interval 24 hour) >= now()";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	
	public BeanFlag getBeanFlag(int slaveUid) {
		BeanFlag flag = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM pea_pun WHERE slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return flag;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, slaveUid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				flag = new BeanFlag();
				flag.masterUid = rs.getInt("master_uid");
				flag.slaveUid = slaveUid;
				flag.money = rs.getInt("money");
				flag.endTime = rs.getLong("end_time");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return flag;
		}finally{
			db.release();
		}
		
		return flag;
	}
	
	public boolean updatePunish(BeanFlag bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE pea_pun SET punish = 1, punish_type = ?, money = ?, end_time = ? WHERE slave_uid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		
		try {
			
			pstmt.setInt(1, bean.punishType);
			pstmt.setInt(2, bean.money);
			pstmt.setLong(3, bean.endTime);
			pstmt.setInt(4, bean.slaveUid);
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
	 * 删除惩罚记录
	 * @param bean
	 * @return
	 */
	public boolean deletePunish(int slaveUid) {
		DbOperation db = new DbOperation(5);
		String query = "DELETE FROM pea_pun WHERE slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, slaveUid);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public List getAllPunish() {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM pea_pun WHERE end_time <= " + System.currentTimeMillis();
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				BeanFlag bean = new BeanFlag();
				
				bean.masterUid = rs.getInt("master_uid");
				bean.slaveUid = rs.getInt("slave_uid");
				bean.appease = rs.getInt("appease") == 1 ? true:false;
				bean.punish = rs.getInt("punish") == 1 ? true : false;
				bean.money = rs.getInt("money");
				bean.endTime = rs.getLong("end_time");
				
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	/**
	 * 是否被惩罚过
	 * @param masterUid
	 * @param slaveUid
	 * @return
	 */
	public boolean isPunish(int masterUid, int slaveUid) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM pea_pun WHERE master_uid = ? and slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, masterUid);
			pstmt.setInt(2, slaveUid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getInt("punish") == 1)
					return true;
				else 
					return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return false;
	}
	
	public boolean isPunish(int slaveUid) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM pea_pun WHERE slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, slaveUid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getInt("punish") == 1)
					return true;
				else 
					return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return false;
	}
	
	
	/**
	 * 是否安抚过
	 * @param masterUid
	 * @param slaveUid
	 * @return
	 */
	public boolean isAppease(int masterUid, int slaveUid) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM pea_pun WHERE master_uid = ? and slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, masterUid);
			pstmt.setInt(2, slaveUid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getInt("appease") == 1)
					return true;
				else 
					return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return false;
	}
	
	/**
	 * 增加一个安抚操作
	 * @param bean
	 * @return
	 */
	public boolean addPunish(BeanFlag bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO pea_pun(master_uid, slave_uid, appease, punish, punish_type, money, end_time) VALUES(?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.masterUid);
			pstmt.setInt(2, bean.slaveUid);
			pstmt.setInt(3, 1);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);
			pstmt.setLong(7, 0);
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
	 * 增加一个奴隶
	 * @param slave
	 * @return
	 */
	public boolean addSlave(BeanSlave slave) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO slave(master_uid, master_nickname, slave_uid, slave_nickname, slave_alias, slave_type, old_master, buy_time, buy_price, rank) VALUES(?,?,?,?,?,?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, slave.getMasterUid());
			pstmt.setString(2, slave.getMasterNickName());
			pstmt.setInt(3, slave.getSlaveUid());
			pstmt.setString(4, slave.getSlaveNickName());
			pstmt.setString(5, slave.getSlaveAlias());
			pstmt.setInt(6, slave.getSlaveType());
			pstmt.setInt(7, slave.getOldMasterUid());
			pstmt.setInt(8, slave.getBuyPrice());
			pstmt.setInt(9, slave.getRank());
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
	 * 删除masterUid的一个奴隶
	 * @param masterUid
	 * @param slaveUid
	 * @return
	 */
	public boolean deleteSlave(int masterUid, int slaveUid) {
		DbOperation db = new DbOperation(5);
		String query = "DELETE FROM slave where master_uid = ? and slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, masterUid);
			pstmt.setInt(2, slaveUid);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public List getSlavesUidByUid(int masterUid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT slave_uid FROM slave WHERE master_uid = ? LIMIT ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, masterUid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Integer(rs.getInt("slave_uid")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	/**
	 * 得到我的所有奴隶
	 * @param masterUid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getSlavesByUid(int masterUid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select slave_uid from slave where master_uid = ? order by buy_time desc limit ?, ?";
		//String query2 = "SELECT s.id as id, s.slave_uid as slave_uid, s.slave_nickname as slave_nickname, s.slave_type as slave_type, s.slave_alias as slave_alias, s.info as info, s.rank as rank, m.money as money, m.price as price FROM slave as s, master as m WHERE s.master_uid = ? AND s.slave_uid = m.uid LIMIT ?,?";
		if(!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, masterUid);
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new Integer(rs.getInt("slave_uid")));
				//list.add(getSlave(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private BeanSlave getSlave(ResultSet rs) throws SQLException{
		
		BeanSlave slave = new BeanSlave();
		slave.setId(rs.getInt("id"));
		slave.setSlaveUid(rs.getInt("slave_uid"));
		slave.setSlaveNickName(rs.getString("slave_nickname"));
		slave.setSlaveType(rs.getInt("slave_type"));
		slave.setSlaveAlias(rs.getString("slave_alias"));
		slave.setMoney(rs.getInt("money"));
		slave.setPrice(rs.getInt("price"));
		slave.setOldMasterUid(rs.getInt("old_master"));
		slave.setRank(rs.getInt("rank"));
		return slave;
	}
	
	public int getSlavesCountByUid(int masterUid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(id) as count FROM slave WHERE master_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, masterUid);
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
	
	public BeanSlave getSlaveBySlaveUid(int slaveUid) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * from slave where slave_uid = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return null;
		}
		BeanSlave slave = null;
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, slaveUid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				slave = new BeanSlave();
				slave.setId(rs.getInt("id"));
				slave.setBuyTime(rs.getTimestamp("buy_time"));
				slave.setOldMasterUid(rs.getInt("old_master"));
				slave.setMasterNickName(rs.getString("master_nickname"));
				slave.setMasterUid(rs.getInt("master_uid"));
				slave.setSlaveAlias(rs.getString("slave_alias"));
				slave.setSlaveNickName(rs.getString("slave_nickname"));
				slave.setSlaveType(rs.getInt("slave_type"));
				slave.setSlaveUid(rs.getInt("slave_uid"));
				slave.setBuyPrice(rs.getInt("buy_price"));
				slave.setRank(rs.getInt("rank"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return slave;
		}finally{
			db.release();
		}
		return slave;
	}
	
}
