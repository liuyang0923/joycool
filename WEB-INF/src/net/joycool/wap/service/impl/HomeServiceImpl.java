/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园接口实现 
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.home.HomeBean;
import net.joycool.wap.bean.home.HomeDiaryBean;
import net.joycool.wap.bean.home.HomeDiaryCat;
import net.joycool.wap.bean.home.HomeDiaryReviewBean;
//import net.joycool.wap.bean.home.HomeEnounce;
import net.joycool.wap.bean.home.HomeHitsBean;
import net.joycool.wap.bean.home.HomeImageBean;
import net.joycool.wap.bean.home.HomeImageTypeBean;
import net.joycool.wap.bean.home.HomeNeighborBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.bean.home.HomePhotoCat;
import net.joycool.wap.bean.home.HomePhotoReviewBean;
//import net.joycool.wap.bean.home.HomePlayer;
//import net.joycool.wap.bean.home.HomePlayerRank;
//import net.joycool.wap.bean.home.HomePlayerVote;
import net.joycool.wap.bean.home.HomeReviewBean;
import net.joycool.wap.bean.home.HomeTypeBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.home.HomeUserImageBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;
//import net.joycool.wap.util.SqlUtil;

public class HomeServiceImpl implements IHomeService {

	/*
	 * zhul_2006-09-14 获取用户图片及用户相关信息(non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IHomeService#getPhotoList(java.lang.String)
	 */
	public Vector getPhotoList(String condition) {

		Vector photoList = new Vector();
		HomePhotoBean homePhoto = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT p.id,p.user_id,p.title,p.attach,p.hits,p.mark,p.create_datetime,u.nickname,u.gender,u.age from jc_home_photo as p inner join user_info as u  on p.user_id=u.id ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homePhoto = new HomePhotoBean();
				homePhoto.setId(rs.getInt("p.id"));
				homePhoto.setUserId(rs.getInt("p.user_id"));
				homePhoto.setTitle(rs.getString("p.title"));
				homePhoto.setAttach(rs.getString("p.attach"));
				homePhoto.setHits(rs.getInt("p.hits"));
				homePhoto.setMark(rs.getInt("p.mark"));
				homePhoto.setCreateDatetime(rs.getString("p.create_datetime"));
				homePhoto.getUser().setNickName(rs.getString("u.nickname"));
				homePhoto.getUser().setGender(rs.getInt("u.gender"));
				homePhoto.getUser().setAge(rs.getInt("u.age"));
				photoList.add(homePhoto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return photoList;
	}

	public HomeUserBean getHomeUser(String condition) {
		HomeUserBean homeUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeUser = this.getHomeUse(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeUser;
	}

	public Vector getHomeUserList(String condition) {
		Vector HomeUserList = new Vector();
		HomeUserBean homeUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeUser = this.getHomeUse(rs);
				HomeUserList.add(homeUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return HomeUserList;
	}

	public boolean addHomeUser(HomeUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_user(user_id,name,mobile,city,constellation,height,weight,work,personality,marriage,aim,friend_condition,create_datetime,photo_count,diary_count,last_modify_time,gender,age,type_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,now(),?,?,1)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getMobile());
			pstmt.setString(4, bean.getCity());
			pstmt.setInt(5, bean.getConstellation());
			pstmt.setInt(6, bean.getHeight());
			pstmt.setInt(7, bean.getWeight());
			pstmt.setString(8, bean.getWork());
			pstmt.setInt(9, bean.getPersonality());
			pstmt.setInt(10, bean.getMarriage());
			pstmt.setInt(11, bean.getAim());
			pstmt.setString(12, bean.getFriendCondition());
			pstmt.setInt(13, bean.getPhotoCount());
			pstmt.setInt(14, bean.getDiaryCount());
			pstmt.setInt(15, bean.getGender());
			pstmt.setInt(16, bean.getAge());
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

	public boolean deleteHomeUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_user SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public HomeImageBean getHomeImage(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_image";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			HomeImageBean homeImage = (HomeImageBean) OsCacheUtil.get(key,
					OsCacheUtil.HOME_FITMENT_CACHE_GROUP,
					OsCacheUtil.HOME_FITMENT_FLUSH_PERIOD);
			if (homeImage != null) {
				return homeImage;
			}
		}
		HomeImageBean homeImage = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeImage = this.getHomeImage(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, homeImage, OsCacheUtil.HOME_FITMENT_CACHE_GROUP);
		}
		return homeImage;
	}

	public Vector getHomeImageList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_image";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector homeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.HOME_FITMENT_CACHE_GROUP,
					OsCacheUtil.HOME_FITMENT_FLUSH_PERIOD);
			if (homeList != null) {
				return homeList;
			}
		}
		Vector homeImageList = new Vector();
		HomeImageBean homeImage = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeImage = this.getHomeImage(rs);
				homeImageList.add(homeImage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, homeImageList, OsCacheUtil.HOME_FITMENT_CACHE_GROUP);
		}
		return homeImageList;
	}

	public boolean addHomeImage(HomeImageBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_image(name,file,price,type_id,mark) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getFile());
			pstmt.setInt(3, bean.getPrice());
			pstmt.setInt(4, bean.getTypeId());
			pstmt.setInt(5, bean.getMark());
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

	public boolean deleteHomeImage(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_image WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeImage(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_image SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeImageCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_image WHERE "
				+ condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, 
					OsCacheUtil.HOME_FITMENT_CACHE_GROUP,
					OsCacheUtil.HOME_FITMENT_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.HOME_FITMENT_CACHE_GROUP);
		}
		return count;
	}

	public HomeImageTypeBean getHomeImageType(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_image_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			HomeImageTypeBean homeImageType = (HomeImageTypeBean) OsCacheUtil.get(key,
					OsCacheUtil.HOME_FITMENT_TYPE_CACHE_GROUP,
					OsCacheUtil.HOME_FITMENT_TYPE_FLUSH_PERIOD);
			if (homeImageType != null) {
				return homeImageType;
			}
		}
		HomeImageTypeBean homeImageType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeImageType = this.getHomeImageType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (homeImageType != null) {
				OsCacheUtil.put(key, homeImageType, OsCacheUtil.HOME_FITMENT_TYPE_CACHE_GROUP);
			}
		}
		return homeImageType;
	}

	public Vector getHomeImageTypeList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_image_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector homeImageTypeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.HOME_FITMENT_TYPE_CACHE_GROUP,
					OsCacheUtil.HOME_FITMENT_TYPE_FLUSH_PERIOD);
			if (homeImageTypeList != null) {
				return homeImageTypeList;
			}
		}
		Vector homeImageTypeList = new Vector();
		HomeImageTypeBean homeImageType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeImageType = this.getHomeImageType(rs);
				homeImageTypeList.add(homeImageType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, homeImageTypeList, OsCacheUtil.HOME_FITMENT_TYPE_CACHE_GROUP);
		}
		return homeImageTypeList;
	}

	public boolean addHomeImageType(HomeImageTypeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_image_type(name,type) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getType());
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

	public boolean deleteHomeImageType(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_image_type WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeImageType(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_image_type SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeImageTypeCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_image_type WHERE "
				+ condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, 
					OsCacheUtil.HOME_FITMENT_TYPE_CACHE_GROUP,
					OsCacheUtil.HOME_FITMENT_TYPE_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.HOME_FITMENT_TYPE_CACHE_GROUP);
		}
		return count;
	}

	public HomeNeighborBean getHomeNeighbor(String condition) {
		HomeNeighborBean homeNeighbor = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_neighbor";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeNeighbor = this.getHomeNeighbor(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeNeighbor;
	}

	public Vector getHomeNeighborList(String condition) {
		Vector HomeNeighborList = new Vector();
		HomeNeighborBean homeNeighbor = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_neighbor";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeNeighbor = this.getHomeNeighbor(rs);
				HomeNeighborList.add(homeNeighbor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return HomeNeighborList;
	}

	public boolean deleteHomeNeighbor(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_neighbor WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeNeighbor(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_neighbor SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeNeighborCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_neighbor WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public HomePhotoBean getHomePhoto(String condition) {
		HomePhotoBean homePhoto = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_photo a";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homePhoto = this.getHomePhoto(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homePhoto;
	}

	public Vector getHomePhotoList(String condition) {
		Vector homePhotoList = new Vector();
		HomePhotoBean homePhoto = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_photo a";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homePhoto = this.getHomePhoto(rs);
				homePhotoList.add(homePhoto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return homePhotoList;
	}

	// mcq_2006-10-10_乐客照片缓存_start
	public Vector getHomePhotoTopList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_photo a";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector homePhotoList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.HOME_PHOTO_GROUP,
					OsCacheUtil.HOME_PHOTO_FLUSH_PERIOD);
			if (homePhotoList != null) {
				return homePhotoList;
			}
		}

		Vector homePhotoList = new Vector();
		HomePhotoBean homePhoto = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homePhoto = this.getHomePhoto(rs);
				homePhotoList.add(homePhoto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, homePhotoList, OsCacheUtil.HOME_PHOTO_GROUP);
		}
		return homePhotoList;
	}
	// 带jc_home_photo_top的缓存查询
	public Vector getHomePhotoTopList2(String condition) {

		String query = "SELECT * from jc_home_photo_top b,jc_home_photo a where b.photo_id=a.id " + condition;

		String key = query;
		Vector homePhotoList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.HOME_PHOTO_GROUP,
				OsCacheUtil.HOME_PHOTO_FLUSH_PERIOD);
		if (homePhotoList != null) {
			return homePhotoList;
		}

		homePhotoList = new Vector();
		HomePhotoBean homePhoto = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				homePhoto = this.getHomePhoto(rs);
				homePhotoList.add(homePhoto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		
		key = query;
		OsCacheUtil.put(key, homePhotoList, OsCacheUtil.HOME_PHOTO_GROUP);

		return homePhotoList;
	}

	// mcq_2006-10-10_乐客照片缓存_end

	public boolean addHomePhoto(HomePhotoBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_photo(user_id,title,attach,create_datetime,review_count,cat_id) VALUES(?,?,?,now(),?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getAttach());
			pstmt.setInt(4, bean.getReviewCount());
			pstmt.setInt(5, bean.getCatId());
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
	public boolean deleteHomePhoto(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_photo WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomePhoto(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_photo SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomePhotoCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_photo WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public int getHomePhotoTopCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_photo WHERE "
				+ condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key,
					OsCacheUtil.HOME_PHOTO_GROUP,
					OsCacheUtil.HOME_PHOTO_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count),
					OsCacheUtil.HOME_PHOTO_GROUP);
		}
		return count;
	}

	public HomePhotoReviewBean getHomePhotoReview(String condition) {
		HomePhotoReviewBean homePhotoReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_photo_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homePhotoReview = this.getHomePhotoReview(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homePhotoReview;
	}

	public Vector getHomePhotoReviewList(String condition) {
		Vector homePhotoReviewList = new Vector();
		HomePhotoReviewBean homePhotoReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_photo_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homePhotoReview = this.getHomePhotoReview(rs);
				homePhotoReviewList.add(homePhotoReview);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homePhotoReviewList;
	}

	public boolean addHomePhotoReview(HomePhotoReviewBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_photo_review(photo_id,review_user_id,review,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getPhotoId());
			pstmt.setInt(2, bean.getReviewUserId());
			pstmt.setString(3, bean.getReview());
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

	public boolean deleteHomePhotoReview(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_photo_review WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomePhotoReview(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_photo_review SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomePhotoReviewCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_photo_review WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public HomeReviewBean getHomeReview(String condition) {
		HomeReviewBean homeReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeReview = this.getHomeReview(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeReview;
	}

	public Vector getHomeReviewList(String condition) {
		Vector homeReviewList = new Vector();
		HomeReviewBean homeReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeReview = this.getHomeReview(rs);
				homeReviewList.add(homeReview);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeReviewList;
	}

	public boolean addHomeNeighbor(HomeNeighborBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_neighbor(user_id,neighbor_id,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getNeighborId());

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

	public boolean addHomeReview(HomeReviewBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_review(user_id,review_user_id,review,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getReviewUserId());
			pstmt.setString(3, bean.getReview());
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

	public boolean deleteHomeReview(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_review WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeReview(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_review SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeReviewCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_review WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public HomeUserImageBean getHomeUserImage(String condition) {
		HomeUserImageBean homeUserImage = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_user_image";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeUserImage = this.getHomeUserImage(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeUserImage;
	}

	public Vector getHomeUserImageList(String condition) {
		Vector homeUserImageList = new Vector();
		HomeUserImageBean homeUserImage = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_user_image";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeUserImage = this.getHomeUserImage(rs);
				homeUserImageList.add(homeUserImage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeUserImageList;
	}

	public boolean addHomeUserImage(HomeUserImageBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_user_image(user_id,image_id,type_id,create_datetime,home_id) VALUES(?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getImageId());
			pstmt.setInt(3, bean.getTypeId());
			pstmt.setInt(4, bean.getHomeId());
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

	public boolean deleteHomeUserImage(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_user_image WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeUserImage(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_user_image SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeUserImageCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_user_image WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public HomeDiaryBean getHomeDiary(String condition) {
		HomeDiaryBean homeDiary = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_diary a";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeDiary = this.getHomeDiary(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeDiary;
	}

	public Vector getHomeDiaryList(String condition) {
		Vector homeDiaryList = new Vector();
		HomeDiaryBean homeDiary = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_diary a";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeDiary = this.getHomeDiary(rs);
				homeDiaryList.add(homeDiary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeDiaryList;
	}

	public Vector getHomeDiaryTopList(String condition) {
		String query = "SELECT * from jc_home_diary a";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		String key = query;
		Vector homeDiaryList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.HOME_DIARY_GROUP,
				OsCacheUtil.HOME_DIARY_FLUSH_PERIOD);
		if (homeDiaryList != null) {
			return homeDiaryList;
		}

		homeDiaryList = new Vector();
		HomeDiaryBean homeDiary = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeDiary = this.getHomeDiary(rs);
				homeDiaryList.add(homeDiary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		key = query;
		OsCacheUtil.put(key, homeDiaryList, OsCacheUtil.HOME_DIARY_GROUP);

		return homeDiaryList;
	}
	
	public Vector getHomeDiaryTopList2(String condition) {
		String query = "SELECT * from jc_home_diary_top b,jc_home_diary a where b.diary_id=a.id " + condition;
		String key = query;
		Vector homeDiaryList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.HOME_DIARY_GROUP,
				OsCacheUtil.HOME_DIARY_FLUSH_PERIOD);
		if (homeDiaryList != null) {
			return homeDiaryList;
		}

		homeDiaryList = new Vector();
		HomeDiaryBean homeDiary = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeDiary = this.getHomeDiary(rs);
				homeDiaryList.add(homeDiary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		key = query;
		OsCacheUtil.put(key, homeDiaryList, OsCacheUtil.HOME_DIARY_GROUP);

		return homeDiaryList;
	}

	public boolean addHomeDiary(HomeDiaryBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_diary(user_id,title,content,create_datetime,review_count,cat_id) VALUES(?,?,?,now(),?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getTitel());
			pstmt.setString(3, bean.getContent());
			// wucx 2006-10-11
			// 添加浏览两的树值
			pstmt.setInt(4, bean.getReviewCount());
			pstmt.setInt(5, bean.getCatId());
			// wucx 2006-10-11
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

	public boolean deleteHomeDiary(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "update jc_home_diary set del=1 WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeDiary(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_diary SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeDiaryCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_diary WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// mcq_2006-10-10_乐客日记缓存_start
	public int getHomeDiaryTopCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_diary WHERE "
				+ condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key,
					OsCacheUtil.HOME_DIARY_GROUP,
					OsCacheUtil.HOME_DIARY_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count),
					OsCacheUtil.HOME_DIARY_GROUP);
		}
		return count;
	}

	// mcq_2006-10-10_乐客日记缓存_end

	public HomeDiaryReviewBean getHomeDiaryReview(String condition) {
		HomeDiaryReviewBean homeDiaryReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_diary_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				homeDiaryReview = this.getHomeDiaryReview(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeDiaryReview;
	}

	public Vector getHomeDiaryReviewList(String condition) {
		Vector homeDiaryReviewList = new Vector();
		HomeDiaryReviewBean homeDiaryReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_diary_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeDiaryReview = this.getHomeDiaryReview(rs);
				homeDiaryReviewList.add(homeDiaryReview);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeDiaryReviewList;
	}

	public boolean addHomeDiaryReview(HomeDiaryReviewBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_home_diary_review(diary_id,review_user_id,review,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getDiaryId());
			pstmt.setInt(2, bean.getReviewUserId());
			pstmt.setString(3, bean.getReview());
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

	public boolean deleteHomeDiaryReview(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "update jc_home_diary_review set del=1 WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeDiaryReview(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_diary_review SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeDiaryReviewCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_diary_review WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public HomeHitsBean getHomeHits(String condition) {
		HomeHitsBean homeHits = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_hits";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeHits = this.getHomeHits(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeHits;
	}

	public Vector getHomeHitsList(String condition) {
		Vector homeHitsList = new Vector();
		HomeHitsBean homeHits = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_home_hits";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeHits = this.getHomeHits(rs);
				homeHitsList.add(homeHits);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return homeHitsList;
	}

	/**
	 * 注意:此方法插入日期为昨天时间
	 */
	public boolean addHomeHits(HomeHitsBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 注意:此sql插入日期为昨天时间
		String query = "INSERT INTO jc_home_hits(user_id,hits,create_datetime) VALUES(?,?,DATE_SUB(now(),  INTERVAL 1 day))";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getHits());
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

	public boolean deleteHomeHits(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_hits WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeHits(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_hits SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeHitsCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_hits WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private HomeUserBean getHomeUse(ResultSet rs) throws SQLException {
		HomeUserBean homeUser = new HomeUserBean();
		homeUser.setId(rs.getInt("id"));
		homeUser.setUserId(rs.getInt("user_id"));
		homeUser.setName(rs.getString("name"));
		homeUser.setMobile(rs.getString("mobile"));
		homeUser.setCity(rs.getString("city"));
		homeUser.setConstellation(rs.getInt("constellation"));
		homeUser.setHeight(rs.getInt("height"));
		homeUser.setWeight(rs.getInt("weight"));
		homeUser.setWork(rs.getString("work"));
		homeUser.setPersonality(rs.getInt("personality"));
		homeUser.setMarriage(rs.getInt("marriage"));
		homeUser.setAim(rs.getInt("aim"));
		homeUser.setFriendCondition(rs.getString("friend_condition"));
		homeUser.setHits(rs.getInt("hits"));
		homeUser.setTotalHits(rs.getInt("total_hits"));
		homeUser.setMark(rs.getInt("mark"));
		homeUser.setCreateDatetime(rs.getString("create_datetime"));
		homeUser.setPhotoCount(rs.getInt("photo_count"));
		homeUser.setDiaryCount(rs.getInt("diary_count"));
		homeUser.setReviewCount(rs.getInt("review_count"));
		homeUser.setLastModifyTime(rs.getString("last_modify_time"));
		homeUser.setGender(rs.getInt("gender"));
		homeUser.setAge(rs.getInt("age"));
		homeUser.setTypeId(rs.getInt("type_id"));
		homeUser.setNotice(rs.getInt("notice"));
		homeUser.setAllow(rs.getInt("allow"));
		homeUser.setPassword(rs.getString("password"));
		homeUser.setRecommended(rs.getInt("recommended"));
		homeUser.setDiaryCatCount(rs.getInt("diary_cat_count"));
		homeUser.setPhotoCatCount(rs.getInt("photo_cat_count"));
		homeUser.setDiaryDefCount(rs.getInt("diary_def_count"));
		homeUser.setPhotoDefCount(rs.getInt("photo_def_count"));
		return homeUser;
	}

	private HomeImageBean getHomeImage(ResultSet rs) throws SQLException {
		HomeImageBean homeImage = new HomeImageBean();
		homeImage.setId(rs.getInt("id"));
		homeImage.setName(rs.getString("name"));
		homeImage.setFile(rs.getString("file"));
		homeImage.setPrice(rs.getInt("price"));
		homeImage.setTypeId(rs.getInt("type_id"));
		homeImage.setMark(rs.getInt("mark"));
		return homeImage;
	}

	private HomeImageTypeBean getHomeImageType(ResultSet rs)
			throws SQLException {
		HomeImageTypeBean homeImageType = new HomeImageTypeBean();
		homeImageType.setId(rs.getInt("id"));
		homeImageType.setName(rs.getString("name"));
		homeImageType.setType(rs.getString("type"));
		return homeImageType;
	}

	private HomeNeighborBean getHomeNeighbor(ResultSet rs) throws SQLException {
		HomeNeighborBean homeNeighbor = new HomeNeighborBean();
		homeNeighbor.setId(rs.getInt("id"));
		homeNeighbor.setUserId(rs.getInt("user_id"));
		homeNeighbor.setNeighborId(rs.getInt("neighbor_id"));
		homeNeighbor.setCreateDatetime(rs.getString("create_datetime"));
		return homeNeighbor;
	}

	private HomePhotoBean getHomePhoto(ResultSet rs) throws SQLException {
		HomePhotoBean homePhoto = new HomePhotoBean();
		homePhoto.setId(rs.getInt("a.id"));
		homePhoto.setUserId(rs.getInt("a.user_id"));
		homePhoto.setTitle(rs.getString("a.title"));
		homePhoto.setAttach(rs.getString("a.attach"));
		homePhoto.setDailyHits(rs.getInt("a.daily_hits"));
		homePhoto.setHits(rs.getInt("a.hits"));
		homePhoto.setMark(rs.getInt("a.mark"));
		homePhoto.setReviewCount(rs.getInt("a.review_count"));
		homePhoto.setCreateDatetime(rs.getString("a.create_datetime"));
		homePhoto.setCatId(rs.getInt("a.cat_id"));
		return homePhoto;
	}

	private HomePhotoReviewBean getHomePhotoReview(ResultSet rs)
			throws SQLException {
		HomePhotoReviewBean homePhotoReview = new HomePhotoReviewBean();
		homePhotoReview.setId(rs.getInt("id"));
		homePhotoReview.setPhotoId(rs.getInt("photo_id"));
		homePhotoReview.setReviewUserId(rs.getInt("review_user_id"));
		homePhotoReview.setReview(rs.getString("review"));
		homePhotoReview.setCreateDatetime(rs.getString("create_datetime"));
		return homePhotoReview;
	}

	private HomeReviewBean getHomeReview(ResultSet rs) throws SQLException {
		HomeReviewBean homeReview = new HomeReviewBean();
		homeReview.setId(rs.getInt("id"));
		homeReview.setUserId(rs.getInt("user_id"));
		homeReview.setReviewUserId(rs.getInt("review_user_id"));
		homeReview.setReview(rs.getString("review"));
		homeReview.setCreateTime(rs.getTimestamp("create_datetime").getTime());
		return homeReview;
	}

	private HomeUserImageBean getHomeUserImage(ResultSet rs)
			throws SQLException {
		HomeUserImageBean homeUserImage = new HomeUserImageBean();
		homeUserImage.setId(rs.getInt("id"));
		homeUserImage.setUserId(rs.getInt("user_id"));
		homeUserImage.setImageId(rs.getInt("image_id"));
		homeUserImage.setTypeId(rs.getInt("type_id"));
		homeUserImage.setHomeId(rs.getInt("home_id"));
		homeUserImage.setCreateDatetime(rs.getString("create_datetime"));
		return homeUserImage;
	}

	private HomeDiaryBean getHomeDiary(ResultSet rs) throws SQLException {
		HomeDiaryBean homeDiary = new HomeDiaryBean();
		homeDiary.setId(rs.getInt("a.id"));
		homeDiary.setUserId(rs.getInt("a.user_id"));
		homeDiary.setTitel(rs.getString("a.title"));
		homeDiary.setContent(rs.getString("a.content"));
		homeDiary.setDailyHits(rs.getInt("a.daily_hits"));
		homeDiary.setHits(rs.getInt("a.hits"));
		homeDiary.setMark(rs.getInt("a.mark"));
		homeDiary.setReviewCount(rs.getInt("a.review_count"));
		homeDiary.setCreateTime(rs.getTimestamp("a.create_datetime").getTime());
		//TODO add 日记薄
		homeDiary.setCatId(rs.getInt("cat_id"));
		homeDiary.setDel(rs.getInt("del"));
		return homeDiary;
	}

	private HomeDiaryReviewBean getHomeDiaryReview(ResultSet rs)
			throws SQLException {
		HomeDiaryReviewBean homeDiaryReview = new HomeDiaryReviewBean();
		homeDiaryReview.setId(rs.getInt("id"));
		homeDiaryReview.setDiaryId(rs.getInt("diary_id"));
		homeDiaryReview.setReviewUserId(rs.getInt("review_user_id"));
		homeDiaryReview.setReview(rs.getString("review"));
		homeDiaryReview.setCreateTime(rs.getTimestamp("create_datetime").getTime());
		return homeDiaryReview;
	}

	private HomeHitsBean getHomeHits(ResultSet rs) throws SQLException {
		HomeHitsBean homeHits = new HomeHitsBean();
		homeHits.setId(rs.getInt("id"));
		homeHits.setUserId(rs.getInt("user_id"));
		homeHits.setHits(rs.getInt("hits"));
		homeHits.setCreateDatetime(rs.getString("create_datetime"));
		return homeHits;
	}

	/*
	 * zhul 2006-09-20 用户家园人气排名
	 * 
	 * @see net.joycool.wap.service.infc.IHomeService#getHomeOrder()
	 */
	public ArrayList getHomeOrder() {

		ArrayList homeOrder = new ArrayList();
		HomeHitsBean homeHit = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "select user_id,hit_sum from (select user_id ,sum(hits) as hit_sum from (select user_id , hits from jc_home_hits) as a  group by a.user_id) as b order by b.hit_sum desc";
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				homeHit = new HomeHitsBean();
				homeHit.setUserId(rs.getInt("user_id"));
				homeHit.setHits(rs.getInt("hit_sum"));
				homeOrder.add(homeHit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return homeOrder;
	}

	// wucx2006-10-10
	public int getHomeUserDiaryCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT diary_count as c_id FROM jc_home_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public int getHomeUserPhotoCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT photo_count as c_id FROM jc_home_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	// wucx2006-10-10
	// wucx2006-10-16START
	public String getHomeNeighborID(String condition) {
		ArrayList neighborId = new ArrayList();
		String idShow = null;
		int neighbor = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "select neighbor_id from jc_home_neighbor ";

		// 数据库操作类

		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				neighbor = rs.getInt("neighbor_id");
				if (neighbor != 0)
					neighborId.add(neighbor + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		if (neighborId.size() > 0) {
			idShow = neighborId.toString();
			idShow = idShow.substring(1, idShow.length() - 1);
		}
		return idShow;
	}

	// wucx2006-10-16 END
	public HomeTypeBean getHomeType(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			HomeTypeBean homeType = (HomeTypeBean) OsCacheUtil.get(key,
					OsCacheUtil.HOME_ROOM_CACHE_GROUP,
					OsCacheUtil.HOME_ROOM_FLUSH_PERIOD);
			if (homeType != null) {
				return homeType;
			}
		}
		HomeTypeBean homeType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				homeType = this.getHomeType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (homeType != null) {
				OsCacheUtil.put(key, homeType, OsCacheUtil.HOME_ROOM_CACHE_GROUP);
			}
		}
		return homeType;
	}

	public Vector getHomeTypeList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector homeTypeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.HOME_ROOM_CACHE_GROUP,
					OsCacheUtil.HOME_ROOM_FLUSH_PERIOD);
			if (homeTypeList != null) {
				return homeTypeList;
			}
		}
		Vector homeTypeList = new Vector();
		HomeTypeBean home = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				home = this.getHomeType(rs);
				homeTypeList.add(home);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil
					.put(key, homeTypeList, OsCacheUtil.HOME_ROOM_CACHE_GROUP);
		}
		return homeTypeList;
	}

	/**
	 * 注意:此方法插入日期为昨天时间
	 */
	public boolean addHomeType(HomeTypeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 注意:此sql插入日期为昨天时间
		String query = "INSERT INTO jc_home_type(name,rooms_ids,goods,pic,money) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getRoomIds());
			pstmt.setInt(3, bean.getGoods());
			pstmt.setString(4, bean.getPic());
			pstmt.setInt(5, bean.getMoney());
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

	public boolean deleteHomeType(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home_type WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHomeType(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_type SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeTypeCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home_type WHERE "
				+ condition;
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key,
					OsCacheUtil.HOME_ROOM_CACHE_GROUP,
					OsCacheUtil.HOME_ROOM_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count),
					OsCacheUtil.HOME_ROOM_CACHE_GROUP);
		}
		return count;
	}

	public HomeBean getHome(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			HomeBean home = (HomeBean) OsCacheUtil.get(key,
					OsCacheUtil.HOME_FACE_CACHE_GROUP,
					OsCacheUtil.HOME_FACE_FLUSH_PERIOD);
			if (home != null) {
				return home;
			}
		}
		HomeBean home = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				home = this.getHome(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (home != null) {
				OsCacheUtil.put(key, home, OsCacheUtil.HOME_FACE_CACHE_GROUP);
			}
		}
		return home;
	}

	public Vector getHomeList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_home";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector homeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.HOME_FACE_CACHE_GROUP,
					OsCacheUtil.HOME_FACE_FLUSH_PERIOD);
			if (homeList != null) {
				return homeList;
			}
		}
		Vector homeList = new Vector();
		HomeBean home = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				home = this.getHome(rs);
				homeList.add(home);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil
					.put(key, homeList, OsCacheUtil.HOME_FACE_CACHE_GROUP);
		}
		return homeList;
	}

	/**
	 * 注意:此方法插入日期为昨天时间
	 */
	public boolean addHome(HomeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 注意:此sql插入日期为昨天时间
		String query = "INSERT INTO jc_home(name,type_id) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getTypeId());
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

	public boolean deleteHome(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_home WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateHome(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getHomeCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_home WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private HomeBean getHome(ResultSet rs) throws SQLException {
		HomeBean home = new HomeBean();
		home.setId(rs.getInt("id"));
		home.setName(rs.getString("name"));
		home.setTypeId(rs.getInt("type_id"));
		return home;
	}

	private HomeTypeBean getHomeType(ResultSet rs) throws SQLException {
		HomeTypeBean homeType = new HomeTypeBean();
		homeType.setId(rs.getInt("id"));
		homeType.setRoomIds(rs.getString("room_ids"));
		homeType.setGoods(rs.getInt("goods"));
		homeType.setPic(rs.getString("pic"));
		homeType.setName(rs.getString("name"));
		homeType.setMoney(rs.getInt("money"));
		return homeType;
	}
	
	
	//public boolean updateHomeDiaryCat
	
	public HomeDiaryCat getHomeDiaryCat(int id){
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from jc_home_diary_cat where id = " + id;
		
		ResultSet rs = dbOp.executeQuery(query);
		HomeDiaryCat bean = null;
		try{
			if(rs.next()) {
				bean =  new HomeDiaryCat();
				bean.setId(rs.getInt("id"));
				bean.setCatName(rs.getString("cat_name"));
				bean.setCount(rs.getInt("count"));
				bean.setUid(rs.getInt("uid"));
				bean.setPrivacy(rs.getInt("privacy"));
				bean.setDef(rs.getInt("def"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			dbOp.release();
		}
		
		return bean;
		
	}
	
	public HomeDiaryCat getHomeDiaryCat(String cond){
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from jc_home_diary_cat where " + cond;
		
		ResultSet rs = dbOp.executeQuery(query);
		HomeDiaryCat bean = null;
		try{
			if(rs.next()) {
				bean =  new HomeDiaryCat();
				bean.setId(rs.getInt("id"));
				bean.setCatName(rs.getString("cat_name"));
				bean.setCount(rs.getInt("count"));
				bean.setUid(rs.getInt("uid"));
				bean.setPrivacy(rs.getInt("privacy"));
				bean.setDef(rs.getInt("def"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			dbOp.release();
		}
		return bean;
	}
	
	public HomePhotoCat getHomePhotoCat(int id){
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from jc_home_photo_cat where id = " + id;
		
		ResultSet rs = dbOp.executeQuery(query);
		HomePhotoCat bean = null;
		try{
			if(rs.next()) {
				bean =  new HomePhotoCat();
				bean.setId(rs.getInt("id"));
				bean.setCatName(rs.getString("cat_name"));
				bean.setCount(rs.getInt("count"));
				bean.setUid(rs.getInt("uid"));
				bean.setPrivacy(rs.getInt("privacy"));
				bean.setDef(rs.getInt("def"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			dbOp.release();
		}
		
		return bean;
		
	}
	
	public HomePhotoCat getHomePhotoCat(String cond){
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select * from jc_home_photo_cat where " + cond;
		
		ResultSet rs = dbOp.executeQuery(query);
		HomePhotoCat bean = null;
		try{
			if(rs.next()) {
				bean =  new HomePhotoCat();
				bean.setId(rs.getInt("id"));
				bean.setCatName(rs.getString("cat_name"));
				bean.setCount(rs.getInt("count"));
				bean.setUid(rs.getInt("uid"));
				bean.setPrivacy(rs.getInt("privacy"));
				bean.setDef(rs.getInt("def"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			dbOp.release();
		}
		return bean;
	}
	
	public List getHomeDiaryCatList(int uid, int privacy){
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		List list = new ArrayList();
		String query = "select * from jc_home_diary_cat where uid = " + uid;
		if(privacy > HomeDiaryCat.PRIVACY_SELF) {
			query += " and privacy >= " + privacy;
		}
		ResultSet rs = dbOp.executeQuery(query);
		
		try{
			while(rs.next()) {
				HomeDiaryCat bean = new HomeDiaryCat();
				bean.setId(rs.getInt("id"));
				bean.setCatName(rs.getString("cat_name"));
				bean.setCount(rs.getInt("count"));
				bean.setUid(rs.getInt("uid"));
				bean.setPrivacy(rs.getInt("privacy"));
				bean.setDef(rs.getInt("def"));
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			dbOp.release();
		}
		return list;
	}
	
	public List getHomePhotoCatList(int uid, int privacy){
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		List list = new ArrayList();
		String query = "select * from jc_home_photo_cat where uid = " + uid;
		if(privacy > HomePhotoCat.PRIVACY_SELF) {
			query += " and privacy >= " + privacy;
		}
		ResultSet rs = dbOp.executeQuery(query);
		
		try{
			while(rs.next()) {
				HomePhotoCat bean = new HomePhotoCat();
				bean.setId(rs.getInt("id"));
				bean.setCatName(rs.getString("cat_name"));
				bean.setCount(rs.getInt("count"));
				bean.setUid(rs.getInt("uid"));
				bean.setPrivacy(rs.getInt("privacy"));
				bean.setDef(rs.getInt("def"));
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			dbOp.release();
		}
		return list;
	}
	
	
	public boolean updateHomeDiaryCat(HomeDiaryCat bean) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_diary_cat SET privacy = " + bean.getPrivacy() + ", cat_name = '" + StringUtil.toSql(bean.getCatName()) + "' WHERE id = " + bean.getId() + " and uid = " + bean.getUid();
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}
	
	public boolean updateHomePhotoCat(HomePhotoCat bean) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_home_photo_cat SET privacy = " + bean.getPrivacy() + ", cat_name = '" + StringUtil.toSql(bean.getCatName()) + "' WHERE id = " + bean.getId() + " and uid = " + bean.getUid();
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}
	
	public boolean addHomePhotoCat(HomePhotoCat bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		
		String query = "INSERT INTO jc_home_photo_cat(cat_name,uid,count,privacy,def) VALUES(?,?,0,9,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getCatName());
			pstmt.setInt(2, bean.getUid());
			pstmt.setInt(3, bean.getDef());
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
	
	
	public boolean addHomeDiaryCat(HomeDiaryCat bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		
		String query = "INSERT INTO jc_home_diary_cat(cat_name,uid,count,privacy,def) VALUES(?,?,0,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getCatName());
			pstmt.setInt(2, bean.getUid());
			pstmt.setInt(3, bean.getPrivacy());
			pstmt.setInt(4, bean.getDef());
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
	
	public boolean deleteHomeDiaryCat(int id, int uid) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "delete from jc_home_diary_cat WHERE id = " + id + " and uid = " + uid;
		String query2 = "update jc_home_diary set del = 1 where user_id = " + uid + " and cat_id = " + id;
		String query3 = "update jc_home_user set diary_cat_count=diary_cat_count-1 where user_id=" + uid;
		// 执行更新
		result = dbOp.executeUpdate(query);
		result = dbOp.executeUpdate(query2);
		result = dbOp.executeUpdate(query3);
		// 释放资源
		dbOp.release();
		return result;
	}
	
	public boolean deleteHomePhotoCat(int id, int uid) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "delete from jc_home_photo_cat WHERE id = " + id + " and uid = " + uid;
		String query2 = "delete from jc_home_photo  where user_id = " + uid + " and cat_id = " + id;
		String query3 = "update jc_home_user set photo_cat_count=photo_cat_count-1 where user_id=" + uid;
//		String query2 = "update jc_home_photo set cat_id = 0 where user_id = " + uid + " and cat_id = " + id;
		// 执行更新
		result = dbOp.executeUpdate(query);
		result = dbOp.executeUpdate(query2);
		result = dbOp.executeUpdate(query3);
		// 释放资源
		dbOp.release();
		return result;
	}
	
//	// 查找一个用户宣言
//	public HomeEnounce getEnounce(String cond){
//		HomeEnounce bean = null;
//		DbOperation db = new DbOperation();
//		db.init();
//		ResultSet rs = db.executeQuery("select * from jc_home_enounce where " + cond);
//		try {
//			if (rs.next()){
//				bean = getEnounce(rs);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return bean;
//	}
//	
//	// 查找用户宣言List
//	public List getEnounceList(String cond){
//		List list = new ArrayList();
//		DbOperation db = new DbOperation();
//		db.init();
//		ResultSet rs = db.executeQuery("select * from jc_home_enounce where " + cond);
//		try {
//			while (rs.next()){
//				list.add(getEnounce(rs));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return list;
//	}
//	
//	// 写入用户宣言
//	public boolean addNewEnounce(HomeEnounce bean){
//		DbOperation db = new DbOperation();
//		db.init();
//		String query = "insert into jc_home_enounce (user_id,content,create_time,del,flag,pic) values (?,?,now(),?,?,?)";
//		if(!db.prepareStatement(query)) {
//			db.release();
//			return false;
//		}
//		PreparedStatement pstmt = db.getPStmt();
//		try{
//			pstmt.setInt(1, bean.getUserId());
//			pstmt.setString(2, bean.getContent());
//			pstmt.setInt(3, bean.getDel());
//			pstmt.setInt(4, bean.getFlag());
//			pstmt.setInt(5, bean.getPic());
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
//	HomeEnounce getEnounce(ResultSet rs) throws SQLException{
//		HomeEnounce bean = new HomeEnounce();
//		bean.setId(rs.getInt("id"));
//		bean.setUserId(rs.getInt("user_id"));
//		bean.setContent(rs.getString("content"));
//		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
//		bean.setDel(rs.getInt("del"));
//		bean.setFlag(rs.getInt("flag"));
//		bean.setPic(rs.getInt("pic"));
//		return bean;
//	}
//	
//	// 查找一个参赛用户
//	public HomePlayer getPlayer(String cond){
//		HomePlayer bean = null;
//		DbOperation db = new DbOperation();
//		db.init();
//		ResultSet rs = db.executeQuery("select * from jc_home_player where " + cond);
//		try {
//			if (rs.next()){
//				bean = getPlayer(rs);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return bean;
//	}
//	
//	// 写入一个参赛用户
//	public boolean addNewPlayer(HomePlayer bean){
//		DbOperation db = new DbOperation();
//		db.init();
//		String query = "insert into jc_home_player (user_id,create_time,del,flag,vote_count) values (?,now(),?,?,?)";
//		if(!db.prepareStatement(query)) {
//			db.release();
//			return false;
//		}
//		PreparedStatement pstmt = db.getPStmt();
//		try{
//			pstmt.setInt(1, bean.getUserId());
//			pstmt.setInt(2, bean.getDel());
//			pstmt.setInt(3, bean.getFlag());
//			pstmt.setInt(4, bean.getVoteCount());
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
//	// 投票(fromUid给toUid投了一票)
//	public boolean vote(HomePlayerVote bean){
//		boolean result = false;
//		// toUid是否真的参赛
//		if (getPlayer(" user_id = " + bean.getToUid()) != null) {
//			// 投
//			result = votePlayer(bean);
//			// 投票总数+1(注意这里,我写死了数据库的名子)
//			result = SqlUtil.executeUpdate("update mcoolwap.jc_home_player set vote_count=vote_count+1 where user_id=" + bean.getToUid());
//		}
//		return result;
//	}
//	
//	HomePlayer getPlayer(ResultSet rs) throws SQLException{
//		HomePlayer bean = new HomePlayer();
//		bean.setUserId(rs.getInt("user_id"));
//		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
//		bean.setDel(rs.getInt("del"));
//		bean.setFlag(rs.getInt("flag"));
//		bean.setVoteCount(rs.getInt("vote_count"));
//		return bean;
//	}
//	
//	// 写入一个投票记录
//	public boolean votePlayer(HomePlayerVote bean){
//		DbOperation db = new DbOperation();
//		db.init();
//		String query = "insert into jc_home_player_vote (from_uid,to_uid,vote_time,del,flag) values (?,?,now(),?,?)";
//		if(!db.prepareStatement(query)) {
//			db.release();
//			return false;
//		}
//		PreparedStatement pstmt = db.getPStmt();
//		try{
//			pstmt.setInt(1, bean.getFromUid());
//			pstmt.setInt(2, bean.getToUid());
//			pstmt.setInt(3, bean.getDel());
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
//	HomePlayerVote getPlayerVote(ResultSet rs) throws SQLException{
//		HomePlayerVote bean = new HomePlayerVote();
//		bean.setId(rs.getInt("id"));
//		bean.setFromUid(rs.getInt("from_uid"));
//		bean.setToUid(rs.getInt("to_uid"));
//		bean.setVoteTime(rs.getTimestamp("vote_time").getTime());
//		bean.setDel(rs.getInt("del"));
//		bean.setFlag(rs.getInt("flag"));
//		return bean;
//	}
//	
//	// 取得一个排名
//	public HomePlayerRank getRank(String cond){
//		HomePlayerRank bean = null;
//		DbOperation db = new DbOperation();
//		db.init();
//		ResultSet rs = db.executeQuery("select * from jc_home_player_rank where " + cond);
//		try {
//			if (rs.next()){
//				bean = getRank(rs);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return bean;
//	}
//	
//	// 取得用户排名列表
//	public List getRankList(String cond){
//		List list = new ArrayList();
//		DbOperation db = new DbOperation();
//		db.init();
//		ResultSet rs = db.executeQuery("select * from jc_home_player_rank where " + cond);
//		try {
//			while (rs.next()){
//				list.add(getRank(rs));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return list;
//	}
//	
//	HomePlayerRank getRank(ResultSet rs) throws SQLException{
//		HomePlayerRank bean = new HomePlayerRank();
//		bean.setId(rs.getInt("id"));
//		bean.setUserId(rs.getInt("user_id"));
//		bean.setVoteCount(rs.getInt("vote_count"));
//		return bean;
//	}
}
