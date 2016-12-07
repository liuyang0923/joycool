/*
 * Created on 2006-8-15
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.video.VideoBean;
import net.joycool.wap.bean.video.VideoFileBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IVideoService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author MCQ
 * 
 */
public class VideoServiceImpl implements IVideoService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#getVideo(java.lang.String)
	 */
	public VideoBean getVideo(String condition) {
		VideoBean video = null;

		// 构建查询语句
		String query = "SELECT * from jc_video";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			video = (VideoBean) OsCacheUtil.get(key, OsCacheUtil.VIDEO_GROUP,
					OsCacheUtil.VIDEO_FLUSH_PERIOD);
			if (video != null) {
				return video;
			}
		}
		// ly_2006-09-13_缓存_end

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				video = this.getVideo(rs);
				// ly_2006-09-13_缓存_start
				// 判断是否是用缓存
				if (OsCacheUtil.USE_CACHE) {
					String key = query;
					if (video != null) {
						OsCacheUtil.put(key, video, OsCacheUtil.VIDEO_GROUP);
					}
				}
				// ly_2006-09-13_缓存_end
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return video;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#getVideoCount(java.lang.String)
	 */
	public int getVideoCount(String condition) {
		int count = 0;

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_video WHERE "
				+ condition;

		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, OsCacheUtil.VIDEO_GROUP,
					OsCacheUtil.VIDEO_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		// ly_2006-09-13_缓存_end

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

		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.VIDEO_GROUP);
		}
		// ly_2006-09-13_缓存_end
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#getVideoFile(java.lang.String)
	 */
	public VideoFileBean getVideoFile(String condition) {
		VideoFileBean videoFile = null;

		// 构建查询语句
		String query = "SELECT * from jc_video_file";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			videoFile = (VideoFileBean) OsCacheUtil.get(key,
					OsCacheUtil.VIDEO_GROUP, OsCacheUtil.VIDEO_FLUSH_PERIOD);
			if (videoFile != null) {
				return videoFile;
			}
		}
		// ly_2006-09-13_缓存_end

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				videoFile = this.getVideoFile(rs);
				// ly_2006-09-13_缓存_start
				// 判断是否是用缓存
				if (OsCacheUtil.USE_CACHE) {
					String key = query;
					if (videoFile != null) {
						OsCacheUtil
								.put(key, videoFile, OsCacheUtil.VIDEO_GROUP);
					}
				}
				// ly_2006-09-13_缓存_end
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return videoFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#getVideoFileCount(java.lang.String)
	 */
	public int getVideoFileCount(String condition) {
		int count = 0;
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_video_file WHERE "
				+ condition;
		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, OsCacheUtil.VIDEO_GROUP,
					OsCacheUtil.VIDEO_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		// ly_2006-09-13_缓存_end
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
		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.VIDEO_GROUP);
		}
		// ly_2006-09-13_缓存_end
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#getVideoFileList(java.lang.String)
	 */
	public Vector getVideoFileList(String condition) {
		VideoFileBean videoFile = null;
		Vector videoFileList = new Vector();

		// 构建查询语句
		String query = "SELECT * from jc_video_file";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector list = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.VIDEO_GROUP, OsCacheUtil.VIDEO_FLUSH_PERIOD);
			if (list != null) {
				return list;
			}
		}
		// ly_2006-09-13_缓存_end

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				videoFile = this.getVideoFile(rs);
				videoFileList.add(videoFile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// ly_2006-09-13_缓存_start
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, videoFileList, OsCacheUtil.VIDEO_GROUP);
		}
		// ly_2006-09-13_缓存_end

		return videoFileList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#getVideoList(java.lang.String)
	 */
	public Vector getVideoList(String condition) {
		VideoBean video = null;
		Vector videoList = new Vector();

		// 构建查询语句
		String query = "SELECT * from jc_video";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// ly_2006-09-13_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector list = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.VIDEO_GROUP, OsCacheUtil.VIDEO_FLUSH_PERIOD);
			if (list != null) {
				return list;
			}
		}
		// ly_2006-09-13_缓存_end

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				video = this.getVideo(rs);
				videoList.add(video);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// ly_2006-09-13_缓存_start
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, videoList, OsCacheUtil.VIDEO_GROUP);
		}
		// ly_2006-09-13_缓存_end
		return videoList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#updateVideo(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateVideo(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_video SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IVideoService#updateVideoFile(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateVideoFile(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_video_file SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private VideoBean getVideo(ResultSet rs) throws SQLException {
		VideoBean video = new VideoBean();
		video.setId(rs.getInt("id"));
		video.setCatalogId(rs.getInt("catalog_id"));
		video.setName(rs.getString("name"));
		video.setIntroduction(rs.getString("introduction"));
		video.setCreateDatetime(rs.getString("create_datetime"));
		video.setUpdateDatetime(rs.getString("update_datetime"));
		video.setDownloadSum(rs.getInt("download_sum"));
		return video;
	}

	private VideoFileBean getVideoFile(ResultSet rs) throws SQLException {
		VideoFileBean videoFile = new VideoFileBean();
		videoFile.setId(rs.getInt("id"));
		videoFile.setVideoId(rs.getInt("video_id"));
		videoFile.setFile(rs.getString("file"));
		videoFile.setFileType(rs.getString("file_type"));
		videoFile.setSize(rs.getInt("size"));
		return videoFile;
	}
}
