package jc.imglib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class ImgLibService {
	
	public Lib getLib(String cond){
		Lib bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from img_lib where " + cond);
		try {
			if (rs.next()){
				bean = getLibBean(rs);
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
		ResultSet rs = db.executeQuery("select * from img_lib where " + cond);
		try {
			while (rs.next()){
				list.add(getLibBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public int addNewLib(Lib bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into img_lib (user_id,title,img,create_time,modify_time,flag) values (?,?,?,now(),now(),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getImg());
			pstmt.setInt(4, bean.getFlag());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
			// 插入成功后，总数量+1
			db.executeUpdate("update img_lib_user set `count`=`count`+1 where user_id=" + bean.getUserId());
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			db.release();
		}
		return bean.getId();
	}
	
	public boolean delLib(String cond){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("delete from img_lib where " + cond);
		db.release();
		return result;
	}
	
	Lib getLibBean(ResultSet rs) throws SQLException{
		Lib bean = new Lib();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setTitle(rs.getString("title"));
		bean.setImg(rs.getString("img"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setModifyTime(rs.getTimestamp("modify_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public List getLibUserList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from img_lib_user where " + cond);
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
	
	public LibUser getLibUser(String cond){
		LibUser bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from img_lib_user where " + cond);
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
	
	public boolean addNewLibUser(LibUser bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into img_lib_user (user_id,privacy,create_time,flag,count) values (?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getPrivacy());
			pstmt.setInt(3, bean.getFlag());
			pstmt.setInt(4, bean.getCount());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	LibUser getUserBean(ResultSet rs) throws SQLException{
		LibUser bean = new LibUser();
		bean.setUserId(rs.getInt("user_id"));
		bean.setPrivacy(rs.getInt("privacy"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		bean.setCount(rs.getInt("count"));
		return bean;
	}
	
	public int getIntValue(String sql){
		int result = 0;
		DbOperation db = new DbOperation(5);
		try {
			result = db.getIntResult(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}
//	public LibLog getLog(String cond){
//		LibLog bean = null;
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from img_lib_log where " + cond);
//		try {
//			if (rs.next()){
//				bean = getLogBean(rs);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return bean;
//	}
//	
//	public List getLogList(String cond){
//		List list = new ArrayList();
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from img_lib_log where " + cond);
//		try {
//			while (rs.next()){
//				list.add(getLogBean(rs));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return list;
//	}
//	
//	public boolean addNewLog(LibLog bean){
//		DbOperation db = new DbOperation(5);
//		String query = "insert into img_lib_log (img_id,user_id,create_time,title,flag) values (?,?,now(),?,?)";
//		if(!db.prepareStatement(query)) {
//			db.release();
//			return false;
//		}
//		PreparedStatement pstmt = db.getPStmt();
//		try{
//			pstmt.setInt(1, bean.getImgId());
//			pstmt.setInt(2, bean.getUserId());
//			pstmt.setString(3, bean.getTitle());
//			pstmt.setInt(4, bean.getFlag());
//			pstmt.execute();
//		}catch(SQLException e) {
//			e.printStackTrace();
//			return false;
//		}finally{
//			db.release();
//		}
//		return true;
//	}
//	
//	public boolean updateLog(LibLog bean){
//		boolean result = false;
//		DbOperation db = new DbOperation(5);
//		result = db.executeUpdate("update img_lib_log set user_id=" + bean.getUserId() + ",create_time=now(),title='" + StringUtil.toSql(bean.getTitle()) + "',flag=" + bean.getFlag() + " where img_id=" + bean.getImgId());
//		db.release();
//		return result;
//	}
//	
//	LibLog getLogBean(ResultSet rs) throws SQLException{
//		LibLog bean = new LibLog();
//		bean.setUserId(rs.getInt("user_id"));
//		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
//		bean.setTitle(rs.getString("title"));
//		bean.setImgId(rs.getInt("img_id"));
//		bean.setFlag(rs.getInt("flag"));
//		return bean;
//	}

	public boolean addImagePool(ImgPoolBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into img_pool (md5_short,md5,user_id,create_time,file_size,file_name,catalog,flag) values (?,?,?,now(),?,?,?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setLong(1, bean.getMd5Short());
			pstmt.setString(2, bean.getMd5());
			pstmt.setInt(3, bean.getUserId());
			pstmt.setInt(4, bean.getFileSize());
			pstmt.setString(5, bean.getFileName());
			pstmt.setInt(6, bean.getCatalog());
			pstmt.setInt(7, bean.getFlag());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
		return true;
	}

	public List getImagePoolList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from img_pool where " + cond);
		try {
			while (rs.next()) {
				list.add(getImagePool(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	ImgPoolBean getImagePool(ResultSet rs) throws SQLException {
		ImgPoolBean bean = new ImgPoolBean();
		bean.setId(rs.getInt("id"));
		bean.setMd5Short(rs.getLong("md5_short"));
		bean.setMd5(rs.getString("md5"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFileSize(rs.getInt("file_size"));
		bean.setFileName(rs.getString("file_name"));
		bean.setCatalog(rs.getInt("catalog"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}

	public ImgPoolBean getImagePoolBean(String cond) {
		ImgPoolBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from img_pool where " + cond);
		try {
			if (rs.next()) {
				bean = getImagePool(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
}
