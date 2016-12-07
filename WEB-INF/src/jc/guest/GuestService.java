package jc.guest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class GuestService {
	
	public GuestChat getChat(String cond){
		GuestChat bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from guest_chat where " + cond);
		try {
			if (rs.next()){
				bean = getChat(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getChatList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from guest_chat where " + cond);
		try {
			while (rs.next()){
				list.add(getChat(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addChat(GuestChat bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into guest_chat (gid1,nc_name1,gid2,nc_name2,content,create_time,del,flag) values (?,?,?,?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getGid1());
			pstmt.setString(2, bean.getNcName1());
			pstmt.setInt(3, bean.getGid2());
			pstmt.setString(4, bean.getNcName2());
			pstmt.setString(5, bean.getContent());
			pstmt.setInt(6, bean.getDel());
			pstmt.setInt(7, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	GuestChat getChat(ResultSet rs) throws SQLException{
		GuestChat bean = new GuestChat();
		bean.setId(rs.getInt("id"));
		bean.setGid1(rs.getInt("gid1"));
		bean.setNcName1(rs.getString("nc_name1"));
		bean.setGid2(rs.getInt("gid2"));
		bean.setNcName2(rs.getString("nc_name2"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public ChatAction getChatAction(String cond){
		ChatAction bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from guest_chat_action where " + cond);
		try {
			if (rs.next()){
				bean = getChatAction(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getChatActionList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from guest_chat_action where " + cond);
		try {
			while (rs.next()){
				list.add(getChatAction(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	ChatAction getChatAction(ResultSet rs) throws SQLException{
		ChatAction bean = new ChatAction();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public Guest getGuest(String cond){
		Guest bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from guest where " + cond);
		try {
			if (rs.next()){
				bean = getGuest(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getGuestList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from guest where " + cond);
		try {
			while (rs.next()){
				list.add(getGuest(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addGuest(Guest bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into guest (uid,phone,password,nick_name,age,gender,create_time) values (?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1,bean.getUid());
			pstmt.setString(2,bean.getPhone());
			pstmt.setString(3,bean.getPassword());
			pstmt.setString(4,bean.getNickName());
			pstmt.setInt(5,bean.getAge());
			pstmt.setInt(6,bean.getGender());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	public void modifyGuest(Guest bean){
		DbOperation db = new DbOperation(5);
		String query = "update guest set uid=?,phone=?,password=?,nick_name=?,age=?,gender=? where id=?";
		if(!db.prepareStatement(query)) {
			db.release();
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1,bean.getUid());
			pstmt.setString(2,bean.getPhone());
			pstmt.setString(3,bean.getPassword());
			pstmt.setString(4,bean.getNickName());
			pstmt.setInt(5,bean.getAge());
			pstmt.setInt(6,bean.getGender());
			pstmt.setInt(7,bean.getDbId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
	}
	
	Guest getGuest(ResultSet rs) throws SQLException{
		Guest bean = new Guest();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("uid"));
		bean.setPhone(rs.getString("phone"));
		bean.setPassword(rs.getString("password"));
		bean.setNickName(rs.getString("nick_name"));
		bean.setAge(rs.getInt("age"));
		bean.setGender(rs.getInt("gender"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
}