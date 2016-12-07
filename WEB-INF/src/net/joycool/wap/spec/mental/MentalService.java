package net.joycool.wap.spec.mental;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class MentalService {
	
	public MentalUser getMentalUser(String cond){
		MentalUser bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_user where " + cond);
		try {
			if (rs.next()){
				bean = getMentalUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getMentalUserList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_user where " + cond);
		try {
			while (rs.next()){
				list.add(getMentalUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 说明：返回加成后的创建时间。
	 * 如返回2月后的时间，“cond”应写“INTERVAL 2 MONTH”。
	 */
	public long getTimePastDays(String cond,int uid){
		long result = 0l;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select date_add(create_time," + cond + ") new_time from mental_user where user_id=" + uid);
		try {
			while(rs.next()){
				result = rs.getTimestamp("new_time").getTime();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}
	
	MentalUser getMentalUser(ResultSet rs) throws SQLException{
		MentalUser bean = new MentalUser();
		bean.setUserId(rs.getInt("user_id"));
		bean.setAnswer(rs.getString("answer"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		bean.setQueNow(rs.getInt("que_now"));
		return bean;
	}
	
	public MentalQuestion getQuestion(String cond){
		MentalQuestion bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_question where " + cond);
		try {
			if (rs.next()){
				bean = getQuestion(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getQuestionList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_question where " + cond);
		try {
			while (rs.next()){
				list.add(getQuestion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	MentalQuestion getQuestion(ResultSet rs) throws SQLException{
		MentalQuestion bean = new MentalQuestion();
		bean.setId(rs.getInt("id"));
		bean.setQueId(rs.getInt("que_id"));
		bean.setQuestion(rs.getString("question"));
		bean.setAnswer(rs.getString("answer"));
		bean.setAnalyse(rs.getString("analyse"));
		bean.setLevel(rs.getString("level"));
		bean.setFlag(rs.getInt("flag"));
		bean.setTitle(rs.getString("title"));
		return bean;
	}
	
	public MentalProperty getProperty(String cond){
		MentalProperty bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_property where " + cond);
		try {
			if (rs.next()){
				bean = getProperty(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getPropertyList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_property where " + cond);
		try {
			while (rs.next()){
				list.add(getProperty(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	MentalProperty getProperty(ResultSet rs) throws SQLException{
		MentalProperty bean = new MentalProperty();
		bean.setId(rs.getInt("id"));
		bean.setProName(rs.getString("pro_name"));
		bean.setDescribe(rs.getString("describe"));
		bean.setMate(rs.getString("mate"));
		return bean;
	}
}
