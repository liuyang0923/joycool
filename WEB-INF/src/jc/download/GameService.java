package jc.download;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.db.DbOperation;

import jc.download.GameBean;

public class GameService {

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
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		ResultSet rs = dbOp.executeQuery(sql);

		try {
			if (rs.next()) {
				game = getGameBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			if (game != null) {
				OsCacheUtil.put(key, game, OsCacheUtil.GAME_GROUP);
			}
		}
		// lbj_2006-08-05_缓存_end

		return game;
	}
	
	public Vector getGamesList(String condition) {
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
			if (gameList != null && gameList.size() > 0) {
				return gameList;
			}
		}
		// lbj_2006-08-05_缓存_end

		Vector gamesList = new Vector();

		GameBean game = null;
		DbOperation dbOp = new DbOperation();
		if (!dbOp.init()) {
			return null;
		}

		ResultSet rs = dbOp.executeQuery(sql);

		// 将结果保存
		try {
			while (rs.next()) {
				game = getGameBean(rs);
				gamesList.add(game);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, gamesList, OsCacheUtil.GAME_GROUP);
		}
		// lbj_2006-08-05_缓存_end

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

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.GAME_GROUP);
		}
		// lbj_2006-08-05_缓存_end

		return count;
	}
	
	public int addGame(GameBean game) {
		int lastInsertId = 0;
		DbOperation db = new DbOperation();
		db.init();
		String query = "insert into jc_game (`name`,description,fit_mobile,kb,file_url,remote_url,pic_url,hits,catalog_id,create_user_id,create_datetime,update_user_id,update_datetime,provider_id,mark) values (?,?,?,?,?,?,?,?,?,?,now(),?,now(),?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, game.getName());
			pstmt.setString(2, game.getDescription());
			pstmt.setString(3, game.getFitMobile());
			pstmt.setInt(4, game.getKb());
			pstmt.setString(5, game.getFileUrl());
			pstmt.setString(6, game.getRemoteUrl());
			pstmt.setString(7, game.getPicUrl());
			pstmt.setInt(8, game.getHits());
			pstmt.setInt(9, game.getCatalogId());
			pstmt.setInt(10, game.getCreateUserId());
			pstmt.setInt(11, game.getUpdateUserId());
			pstmt.setInt(12,game.getProviderId());
			pstmt.setInt(13, game.getMark());
			db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	
	}
	
	GameBean getGameBean(ResultSet rs) throws SQLException{
		GameBean bean = new GameBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setDescription(rs.getString("description"));
		bean.setFitMobile(rs.getString("fit_mobile"));
		bean.setKb(rs.getInt("kb"));
		bean.setFileUrl(rs.getString("file_url"));
		bean.setRemoteUrl(rs.getString("remote_url"));
		bean.setPicUrl(rs.getString("pic_url"));
		bean.setHits(rs.getInt("hits"));
		bean.setCatalogId(rs.getInt("catalog_id"));
		bean.setCreateUserId(rs.getInt("create_user_id"));
		bean.setCreateDatetime(rs.getTimestamp("create_datetime").getTime());
		bean.setUpdateUserId(rs.getInt("update_user_id"));
		bean.setUpdateDatetime(rs.getTimestamp("update_datetime").getTime());
		bean.setProviderId(rs.getInt("provider_id"));
		bean.setMark(rs.getInt("mark"));
		return bean;
	}
}
