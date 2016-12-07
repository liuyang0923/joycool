package jc.family.photo;

import java.sql.*;
import java.util.*;

import net.joycool.wap.util.db.DbOperation;

public class FmPhotoService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 *  添加相册
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertAlbumBean(FmAlbumBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_album(fm_id,permission,name,create_user_id) VALUES(?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFid());
			pstmt.setInt(2, bean.getPermission());
			pstmt.setInt(4, bean.getCreateUid());
			pstmt.setString(3, bean.getAlbumName());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}	
	
	/**
	 *  修改相册
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterAlbumBean(FmAlbumBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update fm_album set fm_id=?,permission=?,name=?,create_user_id=?,`count`=?,create_time=now() where id="+bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFid());
			pstmt.setInt(2, bean.getPermission());
			pstmt.setInt(4, bean.getCreateUid());
			pstmt.setInt(5, bean.getPhotoNum());
			pstmt.setString(3, bean.getAlbumName());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}
	

	/**
	 *  添加相片
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertPhotoBean(FmPhotoBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_album_photo(upload_time,fm_id,album_id,read_time,comment_time,upload_user_id,photo_name,photo_url) VALUES(now(),?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFid());
			pstmt.setInt(2, bean.getAlbumId());
			pstmt.setInt(3, bean.getReadTime());
			pstmt.setInt(4, bean.getCommentTime());
			pstmt.setInt(5, bean.getUploadUid());
			pstmt.setString(6, bean.getPhotoName());
			pstmt.setString(7, bean.getUrl());
//			pstmt.setString(7, bean.getUrl());
//			pstmt.setString(8, bean.getUseUrl());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}	
	
	/**
	 *  修改相片
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterPhotoBean(FmPhotoBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update fm_album_photo set upload_time=now(),fm_id=?,album_id=?,read_time,comment_time=?,upload_user_id=?,photo_name=? where id="+bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFid());
			pstmt.setInt(2, bean.getAlbumId());
			pstmt.setInt(3, bean.getReadTime());
			pstmt.setInt(4, bean.getCommentTime());
			pstmt.setInt(5, bean.getUploadUid());
			pstmt.setString(6, bean.getPhotoName());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		// 路径单独处理
		if (bean.getUrl() != null && bean.getUrl().length() > 0) {
			upd("update fm_album_photo set photo_url="+bean.getUrl()+" where id="+bean.getId());
		}
		return true;
	}
	
	/**
	 * 添加评论
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertCommentBean(FmCommentBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_album_photo_comment(create_time,user_id,photo_id,content) VALUES(now(),?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUid());
			pstmt.setInt(2, bean.getPhotoId());
			pstmt.setString(3, bean.getContent());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}	
	
	/**
	 *  修改评论
	 * 
	 * @param bean
	 * @return
	 */
//	public boolean alterCommentBean(FmCommentBean bean) {
//		// 数据库操作类
//		DbOperation dbOp = new DbOperation(5);
//		String query = "update fm_album set fm_id=?,photo_id=?,content=?,create_time=now() where id="+bean.getId();
//		// 准备
//		if (!dbOp.prepareStatement(query)) {
//			dbOp.release();
//			return false;
//		}
//		// 传递参数
//		PreparedStatement pstmt = dbOp.getPStmt();
//		try {
//			pstmt.setInt(1, bean.getUid());
//			pstmt.setInt(2, bean.getPhotoId());
//			pstmt.setString(3, bean.getContent());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			dbOp.release();
//			return false;
//		}
//		// 执行
//		dbOp.executePstmt();
//		// 释放资源
//		dbOp.release();
//		return true;
//	}
	

	/**
	 *   相册表
	 * 
	 * @param coud
	 * @return
	 */
	public List getAlbumList (String coud) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_album where " + coud);
		try {
			while (rs.next())
				list.add(getAlbumBean(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 *   相册表
	 * 
	 * @param coud
	 * @return 单个bean
	 */
	public FmAlbumBean getAlbumBean(String coud) {
		FmAlbumBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_album where " + coud);
		try {
			if (rs.next())
				bean = getAlbumBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 *  照片表
	 * 
	 * @param coud
	 * @return
	 */
	public List getPhotoList (String coud) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_album_photo where " + coud);
		try {
			while (rs.next())
				list.add(getPhotoBean(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 获得单个相片
	 * 
	 * @param coud
	 * @return 单个bean
	 */
	public FmPhotoBean getPhotoBean (String coud) {
		FmPhotoBean photoBean = new FmPhotoBean();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_album_photo where " + coud);
		try {
			if (rs.next())
				photoBean = getPhotoBean(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return photoBean;
	}

	
	/**
	 *  回复表
	 * 
	 * @param coud
	 * @return
	 */
	public List getCommentList (String coud) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_album_photo_comment where " + coud);
		try {
			while (rs.next())
				list.add(getCommentBean(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	FmAlbumBean getAlbumBean(ResultSet rs) throws SQLException {
		FmAlbumBean bean = new FmAlbumBean();
		bean.setId(rs.getInt("id"));
		bean.setFid(rs.getInt("fm_id"));
		bean.setCreateUid(rs.getInt("create_user_id"));
		bean.setPhotoNum(rs.getInt("count"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setPermission(rs.getInt("permission"));
		bean.setAlbumName(rs.getString("name"));
		return bean;
	}
	FmPhotoBean getPhotoBean(ResultSet rs) throws SQLException {
		FmPhotoBean bean = new FmPhotoBean();
		bean.setId(rs.getInt("id"));
		bean.setFid(rs.getInt("fm_id"));
		bean.setAlbumId(rs.getInt("album_id"));
		bean.setReadTime(rs.getInt("read_time"));
		bean.setCommentTime(rs.getInt("comment_time"));
		bean.setUploadTime(rs.getInt("upload_time"));
		bean.setUploadUid(rs.getInt("upload_user_id"));
		bean.setPhotoName(rs.getString("photo_name"));
//		bean.setUseUrl(rs.getString("photo_use_url"));
		bean.setUrl(rs.getString("photo_url"));
		return bean;
	}
	FmCommentBean getCommentBean(ResultSet rs) throws SQLException {
		FmCommentBean bean = new FmCommentBean();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("user_id"));
		bean.setPhotoId(rs.getInt("photo_id"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
}
