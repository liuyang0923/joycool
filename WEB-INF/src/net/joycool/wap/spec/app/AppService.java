package net.joycool.wap.spec.app;

import java.sql.*;

import java.util.*;

import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.SqlUtil;

public class AppService {
	// 获取插件列表
	public List getAppList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from app where " + cond);
		try{
			while(rs.next()) {
				list.add(getApp(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	public AppBean getApp(String cond) {
		AppBean app = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from app where " + cond);
		try{
			if(rs.next()) {
				app = getApp(rs);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return app;
	}
	public AppBean getApp(ResultSet rs) throws SQLException {
		AppBean bean = new AppBean();
		bean.setId(rs.getInt("id"));
		bean.name = rs.getString("name");
		bean.name2 = rs.getString("name2");
		bean.shortName = rs.getString("short_name");
		bean.info = rs.getString("info");
		bean.setDir(rs.getString("dir"));
		bean.url = rs.getString("url");
		bean.index = rs.getString("index");
		bean.offline = rs.getString("offline");
		bean.flag = rs.getInt("flag");
		bean.count = rs.getInt("count");
		bean.type = rs.getInt("type");
		bean.createTime = rs.getTimestamp("create_time").getTime();
		
		bean.replyCount = rs.getInt("reply_count");
		bean.scoreCount = rs.getInt("score_count");
		bean.score = rs.getInt("score");
		bean.activeCount = rs.getInt("active_count");
		bean.favorCount = rs.getInt("favor_count");
		
		bean.author = rs.getString("author");
		bean.contact = rs.getString("contact");
		bean.email = rs.getString("email");
		bean.apiKey = rs.getString("api_key");
		bean.secretKey = rs.getString("secret_key");
		return bean;
	}
	// 获取插件类型列表
	public List getTypeList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from app_type where " + cond);
		try{
			while(rs.next()) {
				list.add(getType(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	public AppTypeBean getType(ResultSet rs) throws SQLException {
		AppTypeBean bean = new AppTypeBean();
		bean.setId(rs.getInt("id"));
		bean.name = rs.getString("name");
		bean.info = rs.getString("info");
		bean.count = rs.getInt("count");
		bean.parentId = rs.getInt("parent_id");
		bean.seq = rs.getInt("seq");
		return bean;
	}
//	 获取用户安装的插件列表
	public List getAppUserList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from app_add where " + cond);
		try{
			while(rs.next()) {
				list.add(getAppUser(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	public AppAddBean getAppUser(ResultSet rs) throws SQLException {
		AppAddBean bean = new AppAddBean();
		bean.setId(rs.getInt("id"));
		bean.appId = rs.getInt("app_id");
		bean.userId = rs.getInt("user_id");
		bean.flag = rs.getInt("flag");
		bean.createTime = rs.getTimestamp("create_time").getTime();
		return bean;
	}
	
	// 从用户插件列表中删除一个插件
	public boolean delUserApp(AppAddBean bean){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("delete from app_add where user_id =" + bean.getUserId() + " and app_id = " + bean.getAppId());
		db.release();
		return result;
	}
	public boolean addAppUser(AppAddBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO app_add(user_id, app_id,flag,create_time) values(?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getAppId());
			pstmt.setInt(3, bean.getFlag());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
			
			// 增加插件安装人数
			db.executeUpdate("update app set `count`=`count`+1 where id=" + bean.getAppId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean addApp(AppBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO app(name, info, dir,url,`index`,offline,flag,`count`,author,api_key,secret_key,email,contact,name2,short_name,type,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setString(3, bean.getDir());
			pstmt.setString(4, bean.getUrl());
			pstmt.setString(5, bean.getIndex());
			pstmt.setString(6, bean.getOffline());
			pstmt.setInt(7, bean.getFlag());
			pstmt.setInt(8, bean.getCount());
			
			pstmt.setString(9, bean.getAuthor());
			pstmt.setString(10, bean.getApiKey());
			pstmt.setString(11, bean.getSecretKey());
			pstmt.setString(12, bean.getEmail());
			pstmt.setString(13, bean.getContact());
			pstmt.setString(14, bean.getName2());
			pstmt.setString(15, bean.getShortName());
			pstmt.setInt(16, bean.getType());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean updateApp(AppBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "update app set name=?,info=?,dir=?,url=?,`index`=?,offline=?,flag=?,`count`=?,author=?,api_key=?,secret_key=?,email=?,contact=?,name2=?,short_name=?,type=? where id=" + bean.getId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setString(3, bean.getDir());
			pstmt.setString(4, bean.getUrl());
			pstmt.setString(5, bean.getIndex());
			pstmt.setString(6, bean.getOffline());
			pstmt.setInt(7, bean.getFlag());
			pstmt.setInt(8, bean.getCount());
			pstmt.setString(9, bean.getAuthor());
			
			pstmt.setString(10, bean.getApiKey());
			pstmt.setString(11, bean.getSecretKey());
			pstmt.setString(12, bean.getEmail());
			pstmt.setString(13, bean.getContact());
			pstmt.setString(14, bean.getName2());
			pstmt.setString(15, bean.getShortName());
			pstmt.setInt(16, bean.getType());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	// 获取评论列表
	public List getAppReplyList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from app_reply where " + cond);
		try{
			while(rs.next()) {
				list.add(getAppReply(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	
	public AppReplyBean getAppReply(ResultSet rs) throws SQLException {
		AppReplyBean bean = new AppReplyBean();
		bean.setId(rs.getInt("id"));
		bean.userId = (rs.getInt("user_id"));
		bean.appId = (rs.getInt("app_id"));
		bean.content = rs.getString("content");
		bean.createTime = rs.getTimestamp("create_time").getTime();
		return bean;
	}
	
	// 写入评论
	public boolean addReply(AppReplyBean bean) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO app_reply(user_id,app_id,content,create_time) values(?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getAppId());
			pstmt.setString(3, StringUtil.toSql(bean.getContent()));
			pstmt.execute();
			bean.setId(db.getLastInsertId());
			result = SqlUtil.executeUpdate("update app set reply_count = reply_count + 1 where id = " + bean.getAppId() , 5);
		}catch(SQLException e) {
			e.printStackTrace();
			return result;
		}finally{
			db.release();
		}
		return result;
	}

	// 删除评论
	public boolean delReplyById(AppReplyBean bean){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("delete from app_reply where id = " + bean.getId());
		result = db.executeUpdate("update app set reply_count = reply_count - 1 where id = " + bean.getAppId() + " and reply_count > 1");
		db.release();
		return result;
	}
	
	// 获取评分列表
	public List getAppScoreList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from app_score where " + cond);
		try{
			while(rs.next()) {
				list.add(getAppScore(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	
	public AppScoreBean getAppScore(ResultSet rs) throws SQLException {
		AppScoreBean bean = new AppScoreBean();
		bean.setId(rs.getInt("id"));
		bean.userId = rs.getInt("user_id");
		bean.appId = rs.getInt("app_id");
		bean.score = rs.getInt("score");
		bean.content = rs.getString("content");
		bean.createTime = rs.getTimestamp("create_time").getTime();
		return bean;
	}

	// 写入评分
	public boolean addScore(AppScoreBean bean) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO app_score(user_id,app_id,score,content,create_time) values(?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getAppId());
			pstmt.setInt(3, bean.getScore());
			pstmt.setString(4, StringUtil.toSql(bean.getContent()));
			pstmt.execute();
			bean.setId(db.getLastInsertId());
			result = SqlUtil.executeUpdate("update app set score = score + " +  bean.getScore() + ",score_count = score_count + 1 where id = " + bean.getAppId() , 5);
		}catch(SQLException e) {
			e.printStackTrace();
			return result;
		}finally{
			db.release();
		}
		return result;
	}
	
	// 删除评分
	public boolean delScoreById(AppScoreBean bean){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("delete from app_score where id = " + bean.getId());
		result = db.executeUpdate("update app set score_count = score_count - 1,score = score - " + bean.getScore() + " where id = " + bean.getAppId() + " and score_count > 1 and score - " + bean.getScore() + " > 0");
		db.release();
		return result;
	}
}