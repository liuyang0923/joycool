package jc.guest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class GuestHallService {
	
	public GuestUserInfo getUserInfo(String cond){
		GuestUserInfo bean = null;
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from guest_user_info where " + cond);
		try {
			if (rs.next()){
				bean = getUserBean(rs);
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
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from guest_user_info where " + cond);
		try {
			while (rs.next()){
				list.add(getUserBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	
	public int addUser(GuestUserInfo bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(6);
		String query = "insert into guest_user_info(user_name,password,age,gender,mobile,flag,create_time,point,money,focus,level,buid,award,my_title,title_now,special,last_time) values (?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getUserName());
			pstmt.setString(2, bean.getPassword());
			pstmt.setInt(3, bean.getAge());
			pstmt.setInt(4,bean.getGender());
			pstmt.setString(5, bean.getMobile());
			pstmt.setInt(6, bean.getFlag());
			pstmt.setInt(7, bean.getPoint());
			pstmt.setInt(8, bean.getMoney());
			pstmt.setInt(9, bean.getFocus());
			pstmt.setInt(10, bean.getLevel());
			pstmt.setInt(11, bean.getBuid());
			pstmt.setInt(12, bean.getAward());
			pstmt.setString(13, bean.getMyTitle());
			pstmt.setInt(14, bean.getTitleNow());
			pstmt.setInt(15, bean.getSpecial());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastInsertId;
		}finally{
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	public int addUser(String nickName,String password,int flag){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(6);
		String query = "insert into guest_user_info(user_name,password,flag,create_time,point,money,focus,level,buid,award,my_title,title_now,special,last_time) values (?,?,?,now(),0,0,0,0,0,0,'',0,0,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, nickName);
			pstmt.setString(2, password);
			pstmt.setInt(3, flag);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastInsertId;
		}finally{
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	GuestUserInfo getUserBean(ResultSet rs) throws SQLException{
		GuestUserInfo bean = new GuestUserInfo();
		bean.setId(rs.getInt("id"));
		bean.setUserName(rs.getString("user_name"));
		bean.setPassword(rs.getString("password"));
		bean.setAge(rs.getInt("age"));
		bean.setGender(rs.getInt("gender"));
		bean.setMobile(rs.getString("mobile"));
		bean.setFlag(rs.getInt("flag"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setPoint(rs.getInt("point"));
		bean.setMoney(rs.getInt("money"));
		bean.setFocus(rs.getInt("focus"));
		bean.setLevel(rs.getInt("level"));
		bean.setBuid(rs.getInt("buid"));
		bean.setAward(rs.getInt("award"));
		bean.setMyTitle(rs.getString("my_title"));
		bean.setTitleNow(rs.getInt("title_now"));
		bean.setSpecial(rs.getInt("special"));
		return bean;
	}
	
	public UserFocus getUserFocus(String cond){
		UserFocus bean = null;
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from user_focus where " + cond);
		try {
			if (rs.next()){
				bean = getFocusBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getUserFocusList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from user_focus where " + cond);
		try {
			while (rs.next()){
				list.add(getFocusBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addFocus(UserFocus bean){
		DbOperation db = new DbOperation(6);
		String query = "insert into user_focus(left_uid,right_uid,create_time,flag) values (?,?,now(),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getLeftUid());
			pstmt.setInt(2, bean.getRightUid());
			pstmt.setInt(3, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	UserFocus getFocusBean(ResultSet rs) throws SQLException{
		UserFocus bean = new UserFocus();
		bean.setId(rs.getInt("id"));
		bean.setLeftUid(rs.getInt("left_uid"));
		bean.setRightUid(rs.getInt("right_uid"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	
	
	public FocusMsg getFocusMsg(String cond){
		FocusMsg bean = null;
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from focus_msg where " + cond);
		try {
			if (rs.next()){
				bean = getFocusMsgBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getFocusMsgList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select * from focus_msg where " + cond);
		try {
			while (rs.next()){
				list.add(getFocusMsgBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addFocusMsg(FocusMsg bean){
		DbOperation db = new DbOperation(6);
		String query = "insert into focus_msg(left_uid,right_uid,content,readed,create_time) values (?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getLeftUid());
			pstmt.setInt(2, bean.getRightUid());
			pstmt.setString(3, bean.getContent());
			pstmt.setInt(4, bean.getReaded());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	FocusMsg getFocusMsgBean(ResultSet rs) throws SQLException{
		FocusMsg bean = new FocusMsg();
		bean.setId(rs.getInt("id"));
		bean.setLeftUid(rs.getInt("left_uid"));
		bean.setRightUid(rs.getInt("right_uid"));
		bean.setContent(rs.getString("content"));
		bean.setReaded(rs.getInt("readed"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
}
