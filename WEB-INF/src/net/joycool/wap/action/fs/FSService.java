package net.joycool.wap.action.fs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @datetime 2006-12-12 下午05:02:22
 * @explain 北京浮生记数据库接口实现
 */
public class FSService {

	public FSUserBean getFSUser(String condition) {
		FSUserBean fsUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from fs_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				fsUser = this.getFSUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return fsUser;
	}
	
	public FSTopBean getHighscore(String condition) {
		FSTopBean bean = null;
		String query = "SELECT * from fs_top";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next())
				bean = getTop(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return bean;
	}

	/**
	 * 该实现启用缓存请注意，更新时间为12小时
	 */
	public Vector getFSTopList(String condition) {
		String query = "SELECT * from fs_top";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		//缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector list = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.BJFS_USER_TOP_COUNT_GROUP, 60);
			if (list != null) {
				return list;
			}
		}
		Vector topList = new Vector();
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				topList.add(getTop(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		//缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, topList, OsCacheUtil.BJFS_USER_TOP_COUNT_GROUP);
		}
		return topList;
	}

	public boolean addFSUser(FSUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO fs_user(user_id) VALUES(?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
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
	
	public boolean addHighscore(String set) {
		boolean result;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "insert into fs_top SET " + set;
		result = dbOp.executeUpdate(query);
		dbOp.release();
		return result;
	}
	
	public boolean saveFSUser(FSUserBean bean) {
		FSSceneBean scene = bean.getScene();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		
		if(bean.getDate() < 3 || bean.isGameOver()) {	// 如果3天内，或者游戏结束，清空之前的游戏保存
			String query = "update fs_user set day=0,type=" + bean.getType() + " where id=" + bean.getId();
			dbOp.executeUpdate(query);
		} else {
			String query = "update fs_user set money=?,saving=?,debt=?,day=?,city=?,health=?,credit=?,special_event=?,black_event=?,bag=?,goods=?,scene_goods=?,products=?,type=? where id=" + bean.getId();
			// 准备
			if (!dbOp.prepareStatement(query)) {
				dbOp.release();
				return false;
			}
			PreparedStatement pstmt = dbOp.getPStmt();
			try {
				pstmt.setInt(1, bean.getMoney());
				pstmt.setInt(2, bean.getSaving());
				pstmt.setInt(3, bean.getDebt());
				pstmt.setInt(4, bean.getDate());
				pstmt.setInt(5, bean.getSceneId());
				pstmt.setInt(6, bean.getHealth());
				pstmt.setInt(7, bean.getHonor());
				pstmt.setInt(8, scene.getSpecialEvent());
				pstmt.setInt(9, scene.getBlackEvent());
				pstmt.setInt(10, bean.getUserBag());
				pstmt.setString(11, bean.saveGoods());
				pstmt.setString(12, bean.saveSceneGoods());
				pstmt.setString(13, bean.saveProducts());
				pstmt.setInt(14, bean.getType());
			} catch (SQLException e) {
				e.printStackTrace();
				dbOp.release();
				return false;
			}
			dbOp.executePstmt();
		}
		dbOp.release();
		return true;
	}

	public boolean delFSUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM fs_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFSTop(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE fs_top SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFSUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM fs_user WHERE "
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
	
	public FSEventBean getFSEvent(String condition) {
		FSEventBean fsEvent = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fs_event";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				fsEvent = this.getFSEvent(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return fsEvent;
	}

	public Vector getFSEventList(String condition) {
		Vector fsEventList = new Vector();
		FSEventBean fsEvent = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fs_event";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				fsEvent = this.getFSEvent(rs);
				fsEventList.add(fsEvent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return fsEventList;
	}

	public boolean addFSEvent(FSEventBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO fs_event(product_id,description,price_change) VALUES(?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getProductId());
			pstmt.setString(2, bean.getDescription());
			pstmt.setFloat(3, bean.getPriceChange());
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

	public boolean delFSEvent(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "DELETE FROM fs_event WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFSEvent(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "UPDATE fs_event SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFSEventCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT count(id) as c_id FROM fs_event WHERE "
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
	
	
	
	
	public FSProductBean getFSProduct(String condition) {
		FSProductBean fsProduct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fs_product";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				fsProduct = this.getFSProduct(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return fsProduct;
	}

	public Vector getFSProductList(String condition) {
		Vector fsProductList = new Vector();
		FSProductBean fsProduct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT * from fs_product";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				fsProduct = this.getFSProduct(rs);
				fsProductList.add(fsProduct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return fsProductList;
	}

	public boolean addFSProduct(FSProductBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO fs_product(name,price_base,price_change,probability) VALUES(?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getPriceBase());
			pstmt.setInt(3, bean.getPriceChange());
			pstmt.setInt(4, bean.getProbability());
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

	public boolean delFSProduct(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "DELETE FROM fs_product WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFSProduct(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "UPDATE fs_product SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFSProductCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "SELECT count(id) as c_id FROM fs_product WHERE "
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

	private FSUserBean getFSUser(ResultSet rs) throws SQLException {
		FSUserBean fsUser = new FSUserBean();
		fsUser.setId(rs.getInt("id"));
		fsUser.setUserId(rs.getInt("user_id"));

		int date = rs.getInt("day");
		if(date > 0) {	//	从数据库载入上次保存的数据
			fsUser.setSceneId(rs.getInt("city"));
			fsUser.setDate(date);
			fsUser.setHealth(rs.getInt("health"));
			fsUser.setMoney(rs.getInt("money"));
			fsUser.setSaving(rs.getInt("saving"));
			fsUser.setDebt(rs.getInt("debt"));
			fsUser.setHonor(rs.getInt("credit"));
			fsUser.setUserBag(rs.getInt("bag"));
			fsUser.setGameStatus(FSUserBean.STATUS_PLAY);
			FSSceneBean scene = fsUser.getScene();
			scene.setSpecialEvent(rs.getInt("special_event"));
			scene.setBlackEvent(rs.getInt("black_event"));
			fsUser.loadGoods(rs.getString("goods"));
			fsUser.loadSceneGoods(rs.getString("scene_goods"));
			fsUser.loadProducts(rs.getString("products"));
			fsUser.setType(rs.getInt("type"));
		} else {	// 初始化
			fsUser.setType(rs.getInt("type"));
			fsUser.reset();
		}
		return fsUser;
	}
	
	private FSTopBean getTop(ResultSet rs) throws SQLException {
		FSTopBean bean = new FSTopBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setHighScore(rs.getInt("high_score"));
		bean.setRecentHighScore(rs.getInt("recent_high_score"));
		bean.setType(rs.getInt("type"));
		return bean;
	}

	private FSSceneBean getFSScene(ResultSet rs) throws SQLException {
		FSSceneBean fsScene = new FSSceneBean();
		fsScene.setId(rs.getInt("id"));
		fsScene.setDescription(rs.getString("description"));
		fsScene.setPicture(rs.getString("picture"));
		return fsScene;
	}

	private FSProductBean getFSProduct(ResultSet rs) throws SQLException {
		FSProductBean fsProduct = new FSProductBean();
		fsProduct.setId(rs.getInt("id"));
		fsProduct.setName(rs.getString("name"));
		fsProduct.setPriceBase(rs.getInt("price_base"));
		fsProduct.setPriceChange(rs.getInt("price_change"));
		fsProduct.setProbability(rs.getInt("probability"));
		return fsProduct;
	}

	private FSEventBean getFSEvent(ResultSet rs) throws SQLException {
		FSEventBean fsEvent = new FSEventBean();
		fsEvent.setId(rs.getInt("id"));
		fsEvent.setDescription(rs.getString("description"));
		fsEvent.setProductId(rs.getInt("product_id"));
		fsEvent.setPriceChange(rs.getFloat("price_change"));
		return fsEvent;
	}
}
