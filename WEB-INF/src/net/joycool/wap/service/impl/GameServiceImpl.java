/*
 * Created on 2005-12-8
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.game.GameBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IGameService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class GameServiceImpl implements IGameService {

	public GameBean getGame(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_game";
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
			GameBean game = (GameBean) OsCacheUtil.get(key,
					OsCacheUtil.GAME_GROUP, OsCacheUtil.GAME_FLUSH_PERIOD);
			if (game != null) {
				return game;
			}
		}
		// lbj_2006-08-05_缓存_end

		GameBean game = null;
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
				game = new GameBean();
				game.setCatalogId(rs.getInt("catalog_id"));
				game.setCreateDatetime(rs.getString("create_datetime"));
				game.setCreateUserId(rs.getInt("create_user_id"));
				game.setDescription(rs.getString("description"));
				game.setFileUrl(rs.getString("file_url"));
				game.setFitMobile(rs.getString("fit_mobile"));
				game.setHits(rs.getInt("hits"));
				game.setId(rs.getInt("id"));
				game.setKb(rs.getInt("kb"));
				game.setName(rs.getString("name"));
				game.setPicUrl(rs.getString("pic_url"));
				game.setProviderId(rs.getInt("provider_id"));
				game.setRemoteUrl(rs.getString("remote_url"));
				game.setUpdateDatetime(rs.getString("update_datetime"));
				game.setUpdateUserId(rs.getInt("update_user_id"));
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
			if (game != null) {
				OsCacheUtil.put(key, game, OsCacheUtil.GAME_GROUP);
			}
		}
		// lbj_2006-08-05_缓存_end

		// 返回结果
		return game;
	}

	public Vector getGamesList(String condition) {
		// 查询语句
		String sql = "SELECT * FROM jc_game";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			Vector gameList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.GAME_GROUP, OsCacheUtil.GAME_FLUSH_PERIOD);
			if (gameList != null) {
				return gameList;
			}
		}
		// lbj_2006-08-05_缓存_end

		Vector gamesList = new Vector();

		GameBean game = null;
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
				game = new GameBean();
				game.setCatalogId(rs.getInt("catalog_id"));
				game.setCreateDatetime(rs.getString("create_datetime"));
				game.setCreateUserId(rs.getInt("create_user_id"));
				game.setDescription(rs.getString("description"));
				game.setFileUrl(rs.getString("file_url"));
				game.setFitMobile(rs.getString("fit_mobile"));
				game.setHits(rs.getInt("hits"));
				game.setId(rs.getInt("id"));
				game.setKb(rs.getInt("kb"));
				game.setName(rs.getString("name"));
				game.setPicUrl(rs.getString("pic_url"));
				game.setProviderId(rs.getInt("provider_id"));
				game.setRemoteUrl(rs.getString("remote_url"));
				game.setUpdateDatetime(rs.getString("update_datetime"));
				game.setUpdateUserId(rs.getInt("update_user_id"));
				gamesList.add(game);
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
			OsCacheUtil.put(key, gamesList, OsCacheUtil.GAME_GROUP);
		}
		// lbj_2006-08-05_缓存_end

		// 返回结果
		return gamesList;
	}

	public boolean updateGame(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_game SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getGamesCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_game WHERE "
				+ condition;

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, OsCacheUtil.GAME_GROUP,
					OsCacheUtil.GAME_FLUSH_PERIOD);
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
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.GAME_GROUP);
		}
		// lbj_2006-08-05_缓存_end

		return count;
	}
}
