/*
 * Created on 2005-11-24
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.news.NewsAttachBean;
import net.joycool.wap.bean.news.NewsBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.INewsService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class NewsServiceImpl implements INewsService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.INewsService#getNews(java.lang.String)
	 */
	public NewsBean getNews(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_news";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		if (sql.indexOf("LIMIT") == -1) {
			sql = sql + " LIMIT 0, 1";
		}

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			NewsBean news = (NewsBean) OsCacheUtil.get(key,
					OsCacheUtil.NEWS_GROUP, OsCacheUtil.NEWS_FLUSH_PERIOD);
			if (news != null) {
				return news;
			}
		}
		// lbj_2006-08-05_缓存_end

		NewsBean news = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			if (rs.next()) {
				news = new NewsBean();
				news.setCatalogId(rs.getInt("catalog_id"));
				news.setContent(rs.getString("content"));
				news.setReleaseDate(rs.getString("release_date"));
				news.setId(rs.getInt("id"));
				news.setTitle(rs.getString("title"));
				news.setHavePic(rs.getInt("have_pic"));
				news.setType(rs.getString("type"));
				news.setHits(rs.getInt("hits"));
				news.setAttachList(getNewsAttachList("news_id = "
						+ news.getId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			if (news != null) {
				OsCacheUtil.put(key, news, OsCacheUtil.NEWS_GROUP);
			}
		}
		// lbj_2006-08-05_缓存_end

		// 返回结果
		return news;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.INewsService#getNewsList(java.lang.String)
	 */
	public Vector getNewsList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_news";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			Vector newsList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.NEWS_GROUP, OsCacheUtil.NEWS_FLUSH_PERIOD);
			if (newsList != null) {
				return newsList;
			}
		}
		// lbj_2006-08-05_缓存_end

		Vector newsList = new Vector();

		NewsBean news = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				news = new NewsBean();
				news.setCatalogId(rs.getInt("catalog_id"));
				news.setContent(rs.getString("content"));
				news.setReleaseDate(rs.getString("release_date"));
				news.setId(rs.getInt("id"));
				news.setTitle(rs.getString("title"));
				news.setHavePic(rs.getInt("have_pic"));
				news.setType(rs.getString("type"));
				news.setHits(rs.getInt("hits"));
				news.setAttachList(getNewsAttachList("news_id = "
						+ news.getId()));
				newsList.add(news);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, newsList, OsCacheUtil.NEWS_GROUP);
		}
		// lbj_2006-08-05_缓存_end

		// 返回结果
		return newsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.INewsService#updateNews(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateNews(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_news SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.INewsService#getNewsCount(java.lang.String)
	 */
	public int getNewsCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_news WHERE "
				+ condition;

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, OsCacheUtil.NEWS_GROUP,
					OsCacheUtil.NEWS_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		// lbj_2006-08-05_缓存_end

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

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.NEWS_GROUP);
		}
		// lbj_2006-08-05_缓存_end

		return count;
	}

	/**
	 * 取得附件列表。
	 * 
	 * @param condition
	 * @return
	 */
	public Vector getNewsAttachList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_news_attach";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			Vector attachList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.NEWS_GROUP, OsCacheUtil.NEWS_FLUSH_PERIOD);
			if (attachList != null) {
				return attachList;
			}
		}
		// lbj_2006-08-05_缓存_end

		Vector attachList = new Vector();

		NewsAttachBean attach = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				attach = new NewsAttachBean();
				attach.setId(rs.getInt("id"));
				attach.setNewsId(rs.getInt("news_id"));
				attach.setTitle(rs.getString("title"));
				attach.setType(rs.getString("type"));
				attach.setFileUrl(rs.getString("file_url"));
				attachList.add(attach);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, attachList, OsCacheUtil.NEWS_GROUP);
		}
		// lbj_2006-08-05_缓存_end

		// 返回结果
		return attachList;
	}
}
