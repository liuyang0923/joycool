/*
 * Created on 2005-11-30
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.bean.image.ImageFileBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class ImageServiceImpl implements IImageService {
	static ICacheMap imageCache = CacheManage.image;
	
	public ImageBean getImage(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_image";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		String key = sql;
		synchronized(imageCache) {
			ImageBean image = (ImageBean) imageCache.get(key);
			if (image != null) {
				return image;
			}
	
			DbOperation dbOp = new DbOperation();
			if (!dbOp.init()) {
				return null;
			}
	
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
				dbOp.release();
				return null;
			}
	
			try {
				if (rs.next()) {
					image = new ImageBean();
					image.setCatalogId(rs.getInt("catalog_id"));
					image.setCode(rs.getString("code"));
					image.setHits(rs.getInt("hits"));
					image.setId(rs.getInt("id"));
					image.setName(rs.getString("name"));
					image.setFile128128(getImageFile(" code='"
							+ rs.getString("code") + "' AND spec='128X128'"));
					image.setFile7070(getImageFile(" code='" + rs.getString("code")
							+ "' AND spec='70X70'"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
	
			if (image != null) {
				imageCache.put(key, image);
			}
	
			return image;
		}
	}

	public Vector getImagesList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_image";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		String key = sql;
		synchronized(imageCache) {
			Vector imageList = (Vector) imageCache.get(key);
			if (imageList != null) {
				return imageList;
			}
	
			Vector imagesList = new Vector();
	
			ImageBean image = null;
			DbOperation dbOp = new DbOperation();
			if (!dbOp.init()) {
				return null;
			}
	
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
				dbOp.release();
				return null;
			}
	
			try {
				while (rs.next()) {
					image = new ImageBean();
					image.setCatalogId(rs.getInt("catalog_id"));
					image.setCode(rs.getString("code"));
					image.setHits(rs.getInt("hits"));
					image.setId(rs.getInt("id"));
					image.setName(rs.getString("name"));
					image.setFile128128(getImageFile(" code='"
							+ rs.getString("code") + "' AND spec='128X128'"));
					image.setFile7070(getImageFile(" code='" + rs.getString("code")
							+ "' AND spec='70X70'"));
					imagesList.add(image);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
	
			imageCache.put(key, imagesList);
			return imagesList;
		}
	}

	public boolean updateImage(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_image SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getImagesCount(String condition) {
		String query = "SELECT count(id) as c_id FROM jc_image WHERE "
				+ condition;

		String key = query;
		synchronized(imageCache) {
			Integer c = (Integer) imageCache.get(key);
			if (c != null) {
				return c.intValue();
			}
	
			int count = 0;
	
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
	
			dbOp.release();
			
			imageCache.put(key, new Integer(count));
			return count;
		}
	}

	private ImageFileBean getImageFile(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_image_file";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		String key = sql;
		synchronized(imageCache) {
			ImageFileBean file = (ImageFileBean) imageCache.get(key);
			if (file != null) {
				return file;
			}

			DbOperation dbOp = new DbOperation();
			if (!dbOp.init()) {
				return null;
			}
	
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
				// 释放资源
				dbOp.release();
				return null;
			}
	
			try {
				if (rs.next()) {
					file = new ImageFileBean();
					file.setCode(rs.getString("code"));
					file.setFileUrl(rs.getString("file_url"));
					file.setId(rs.getInt("id"));
					file.setSize(rs.getInt("size"));
					file.setSpec(rs.getString("spec"));
					file.setType(rs.getString("type"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
	
			if (file != null) {
				imageCache.put(key, file);
			}
	
			return file;
		}
	}
}
