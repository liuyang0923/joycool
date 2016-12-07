package jc.exam.bag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class ExamService {
	
	public ExamType getType(String cond){
		ExamType bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_type where " + cond);
		try {
			if (rs.next()){
				bean = getType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getTypeList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_type where " + cond);
		try {
			while (rs.next()){
				list.add(getType(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public LinkedHashMap getTypeMap(String cond){
		LinkedHashMap map = new LinkedHashMap();
		ExamType type = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_type where " + cond);
		try {
			while (rs.next()){
				type = getType(rs);
				map.put(new Integer(type.getId()),type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	ExamType getType(ResultSet rs) throws SQLException{
		ExamType bean = new ExamType();
		bean.setId(rs.getInt("id"));
		bean.setTypeName(rs.getString("type_name"));
		bean.setHypo(rs.getInt("hypo"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public ExamBag getBag(String cond){
		ExamBag bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_bag where " + cond);
		try {
			if (rs.next()){
				bean = getBag(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getBagList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_bag where " + cond);
		try {
			while (rs.next()){
				list.add(getBag(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addNewBag(ExamBag bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into exam_bag (user_id,que_type,title,content,create_time,del,flag) values (?,?,?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getQueType());
			pstmt.setString(3, bean.getTitle());
			pstmt.setString(4, bean.getContent());
			pstmt.setInt(5, bean.getDel());
			pstmt.setInt(6, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean modifyBag(ExamBag bean){
		DbOperation db = new DbOperation(5);
		String query = "update exam_bag set title=?,content=?,que_type=?,del=?,flag=? where id=" + bean.getId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getQueType());
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
	
	ExamBag getBag(ResultSet rs) throws SQLException{
		ExamBag bean = new ExamBag();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setQueType(rs.getInt("que_type"));
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public ExamNote getNote(String cond){
		ExamNote bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_note where " + cond);
		try {
			if (rs.next()){
				bean = getNote(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getNoteList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_note where " + cond);
		try {
			while (rs.next()){
				list.add(getNote(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addNewNote(ExamNote bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into exam_note (user_id,bag_id,content,create_time,del,flag) values (?,?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getBagId());
			pstmt.setString(3, bean.getContent());
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
	
	ExamNote getNote(ResultSet rs) throws SQLException{
		ExamNote bean = new ExamNote();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setBagId(rs.getInt("bag_id"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public ExamLib getLib(String cond){
		ExamLib bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_lib where " + cond);
		try {
			if (rs.next()){
				bean = getLib(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getLibList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from exam_lib where " + cond);
		try {
			while (rs.next()){
				list.add(getLib(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addNewLib(ExamLib bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into exam_lib (title,content,create_time,type,del,flag) values (?,?,now(),?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getType());
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
	
	public boolean modifyLib(ExamLib bean){
		DbOperation db = new DbOperation(5);
		String query = "update exam_lib set title=?,content=?,type=?,del=?,flag=? where id=" + bean.getId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getType());
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
	
	ExamLib getLib(ResultSet rs) throws SQLException{
		ExamLib bean = new ExamLib();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setType(rs.getInt("type"));
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	// 统计信息
	
	public List getStatList(){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select `type` type,flag,count(id) stat from exam_lib  where del = 0 group by flag ,`type`");
		try {
			while (rs.next()){
				list.add(getStat(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	ExamStat getStat(ResultSet rs) throws SQLException{
		ExamStat bean = new ExamStat();
		bean.setType(rs.getInt("type"));
		bean.setFlag(rs.getInt("flag"));
		bean.setStat(rs.getInt("stat"));
		return bean;
	}
}
