package net.joycool.wap.spec.mentalpic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class MentalPicService {

	public MentalPicQuestion getQuestion(String cond){
		MentalPicQuestion bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_pic_question where " + cond);
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
		ResultSet rs = db.executeQuery("select * from mental_pic_question where " + cond);
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
	
	public int addQuestion(MentalPicQuestion bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(4);
		String query = "insert into mental_pic_question (title,content,content_pic,del,flag,create_time) values (?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setString(3, bean.getContentPic());
			pstmt.setInt(4, bean.getDel());
			pstmt.setInt(5, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			lastInsertId = SqlUtil.getLastInsertId(db, "mental_pic_question");
			db.release();
		}
		return lastInsertId;
	}
	
	public boolean modifyQuestion(MentalPicQuestion bean){
		DbOperation db = new DbOperation(4);
		String query = "update mental_pic_question set title=?,content=?,content_pic=?,del=?,flag=? where id=?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setString(3, bean.getContentPic());
			pstmt.setInt(4, bean.getDel());
			pstmt.setInt(5, bean.getFlag());
			pstmt.setInt(6, bean.getId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MentalPicQuestion getQuestion(ResultSet rs) throws SQLException{
		MentalPicQuestion bean = new MentalPicQuestion();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setContentPic(rs.getString("content_pic"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public MentalPicOption getOption(String cond){
		MentalPicOption bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_pic_option where " + cond);
		try {
			if (rs.next()){
				bean = getOption(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getOptionList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_pic_option where " + cond);
		try {
			while (rs.next()){
				list.add(getOption(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addOption(MentalPicOption bean){
		DbOperation db = new DbOperation(4);
		String query = "insert into mental_pic_option (que_id,`option`,analyse,del,flag) values (?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getQueId());
			pstmt.setString(2, bean.getOption());
			pstmt.setString(3, bean.getAnalyse());
			pstmt.setInt(4, bean.getDel());
			pstmt.setInt(5, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean modifyOption(MentalPicOption bean){
		DbOperation db = new DbOperation(4);
		String query = "update mental_pic_option set que_id=?,`option`=?,analyse=?,del=?,flag=? where id=?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getQueId());
			pstmt.setString(2, bean.getOption());
			pstmt.setString(3, bean.getAnalyse());
			pstmt.setInt(4, bean.getDel());
			pstmt.setInt(5, bean.getFlag());
			pstmt.setInt(6, bean.getId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MentalPicOption getOption(ResultSet rs) throws SQLException{
		MentalPicOption bean = new MentalPicOption();
		bean.setId(rs.getInt("id"));
		bean.setQueId(rs.getInt("que_id"));
		bean.setOption(rs.getString("option"));
		bean.setAnalyse(rs.getString("analyse"));
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public MentalPicUser getMentalPicUser(String cond){
		MentalPicUser bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_pic_user where " + cond);
		try {
			if (rs.next()){
				bean = getMentalPicUser(rs);
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
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from mental_pic_user where " + cond);
		try {
			while (rs.next()){
				list.add(getMentalPicUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addUser(MentalPicUser bean){
		DbOperation db = new DbOperation(4);
		String query = "insert into mental_pic_user (user_id,que_id,create_time,flag,selected) values (?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getQueId());
			pstmt.setInt(3, bean.getFlag());
			pstmt.setInt(4, bean.getSelected());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean modifyUser(MentalPicUser bean){
		DbOperation db = new DbOperation(4);
		String query = "update mental_pic_user set create_time=now(),flag=?,selected=? where user_id=" + bean.getUserId() + " and que_id=" + bean.getQueId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getFlag());
			pstmt.setInt(2, bean.getSelected());
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
	 * 说明：返回完成后的创建时间。
	 * 如返回7天后的时间，“cond”应写“INTERVAL 7 DAY”。
	 * queId:题目的ID。
	 */
	public long getTimePastDays(String cond,MentalPicUser user){
		long result = 0l;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select date_add(create_time," + cond + ") new_time from mental_pic_user where user_id=" + user.getUserId() + " and que_id=" + user.getQueId());
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
	
	MentalPicUser getMentalPicUser(ResultSet rs) throws SQLException{
		MentalPicUser bean = new MentalPicUser();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setQueId(rs.getInt("que_id"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		bean.setSelected(rs.getInt("selected"));
		return bean;
	}
}