package jc.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class UserService2 {
	
	public UserBean2 getUserBean2(String cond){
		UserBean2 bean = null;
		DbOperation db = new DbOperation(0);
		ResultSet rs = db.executeQuery("select * from user_info2 where " + cond);
		try {
			if (rs.next()){
				bean = getUserBean2(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public List getUserBean2List(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(0);
		ResultSet rs = db.executeQuery("select * from user_info2 where " + cond);
		try {
			while (rs.next()){
				list.add(getUserBean2(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addNewUser(UserBean2 bean){
		int lastId = 0;
		DbOperation db = new DbOperation(0);
		String query = "insert into user_info2 (mobile,`password`,nick_name,gender,age,checked,create_time) values (?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getMobile());
			pstmt.setString(2, bean.getPassword());
			pstmt.setString(3, bean.getNickName());
			pstmt.setInt(4, bean.getGender());
			pstmt.setInt(5, bean.getAge());
			pstmt.setInt(6, bean.getChecked());
			pstmt.execute();
			lastId = db.getLastInsertId();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			db.release();
		}
		return lastId;
	}
	
	public void checkedUser(int uid){
		DbOperation db = new DbOperation(0);
		db.executeUpdate("update user_info2 set checked=1 where id=" + uid);
		db.release();
	}
	
	public void checkedUser(String mobile){
		DbOperation db = new DbOperation(0);
		db.executeUpdate("update user_info2 set checked=1 where mobile='" + StringUtil.toSql(mobile) + "'");
		db.release();
	}
	
	UserBean2 getUserBean2(ResultSet rs) throws SQLException{
		UserBean2 bean = new UserBean2();
		bean.setId(rs.getInt("id"));
		bean.setMobile(rs.getString("mobile"));
		bean.setPassword(rs.getString("password"));
		bean.setNickName(rs.getString("nick_name"));
		bean.setGender(rs.getInt("gender"));
		bean.setAge(rs.getInt("age"));
		bean.setChecked(rs.getInt("checked"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
}
