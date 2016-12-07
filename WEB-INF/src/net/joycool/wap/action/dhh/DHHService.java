package net.joycool.wap.action.dhh;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author Liq
 * @datetime 2007-4-17 
 * @explain 大航海数据库接口实现
 */
public class DHHService {

	public DhhShipBean getShip(String condition)
	{
		DhhShipBean DhhShip = null;

		// 构建查询语句
		String query = "SELECT * from dhh_ship";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				DhhShip = new DhhShipBean();
				DhhShip.setId(rs.getInt("id"));
				DhhShip.setName(rs.getString("name"));
				DhhShip.setPrice(rs.getInt("price"));
				DhhShip.setSpeed(rs.getInt("speed"));
				DhhShip.setVolume(rs.getInt("volume"));
				DhhShip.setSailor(rs.getInt("Sailor"));
				DhhShip.setImage(rs.getString("image"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return DhhShip;
	}
	
	public Vector getShipList(String condition)
	{
		Vector shipList = null;
		DhhShipBean shipBean = null;
		// 构建查询语句
		String query = "SELECT * from dhh_ship";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		shipList = new Vector();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				shipBean = new DhhShipBean();
				shipBean.setId(rs.getInt("id"));
				shipBean.setName(rs.getString("name"));
				shipBean.setPrice(rs.getInt("price"));
				shipBean.setSpeed(rs.getInt("speed"));
				shipBean.setVolume(rs.getInt("volume"));
				shipBean.setSailor(rs.getInt("Sailor"));
				shipBean.setImage(rs.getString("image"));
				shipList.add(shipBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return shipList;
	}
	
	public DhhCityBean getCity(String condition)
	{
		DhhCityBean DhhCity = null;

		// 构建查询语句
		String query = "SELECT * from dhh_city";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				DhhCity = new DhhCityBean();
				DhhCity.setId(rs.getInt("id"));
				DhhCity.setName(rs.getString("name"));
				DhhCity.setImage(rs.getString("image"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return DhhCity;
	}
	
	public Vector getCityList(String condition)
	{

		Vector dhhList = null;
		DhhCityBean DhhCity = null;
		// 构建查询语句
		String query = "SELECT * from dhh_city";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		dhhList = new Vector();
		try {
			// 结果不为空
			while (rs.next()) {
				DhhCity = new DhhCityBean();
				DhhCity.setId(rs.getInt("id"));
				DhhCity.setName(rs.getString("name"));
				DhhCity.setImage(rs.getString("image"));
				dhhList.add(DhhCity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return dhhList;
	
	}
	
	public DhhProductBean getProduct(String condition)
	{
		DhhProductBean DhhProduct = null;

		// 构建查询语句
		String query = "SELECT * from dhh_product";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				DhhProduct = new DhhProductBean();
				DhhProduct.setId(rs.getInt("id"));
				DhhProduct.setName(rs.getString("name"));
				DhhProduct.setSell(rs.getInt("sell"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return DhhProduct;
	}
	
	public Vector getProductList(String condition)
	{
		Vector ProductList = null;
		DhhProductBean DhhProduct = null;
		// 构建查询语句
		String query = "SELECT * from dhh_product";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ProductList = new Vector();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				DhhProduct = new DhhProductBean();
				DhhProduct.setId(rs.getInt("id"));
				DhhProduct.setName(rs.getString("name"));
				DhhProduct.setSell(rs.getInt("sell"));
				ProductList.add(DhhProduct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return ProductList;
	}
	
	public DhhCitProBean getCitPro(String condition)
	{
		DhhCitProBean citprobean = null;

		// 构建查询语句
		String query = "SELECT * from dhh_city_product";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				citprobean = new DhhCitProBean();
				citprobean.setId(rs.getInt("id"));
				citprobean.setCityid(rs.getInt("city_id"));
				citprobean.setProductid(rs.getInt("product_id"));
				citprobean.setBuyInit(rs.getInt("buy_rate"));
				citprobean.setSellInit(rs.getInt("sell_rate"));
				citprobean.setQuantityInit(rs.getInt("quantity"));
				citprobean.reset();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return citprobean;
	}
	
	public Vector getCitProList(String condition)
	{
		Vector citproList = null;
		DhhCitProBean citprobean = null;
		// 构建查询语句
		String query = "SELECT * from dhh_city_product";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		citproList = new Vector();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				citprobean = new DhhCitProBean();
				citprobean.setId(rs.getInt("id"));
				citprobean.setCityid(rs.getInt("city_id"));
				citprobean.setProductid(rs.getInt("product_id"));
				citprobean.setBuyInit(rs.getInt("buy_rate"));
				citprobean.setSellInit(rs.getInt("sell_rate"));
				citprobean.setQuantityInit(rs.getInt("quantity"));
				citprobean.reset();
				citproList.add(citprobean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return citproList;
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	public DhhUserBean getUser(String condition) {
		DhhUserBean dhhUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from dhh_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				dhhUser = new DhhUserBean();
				dhhUser.setId(rs.getInt("id"));
				dhhUser.setUserId(rs.getInt("user_id"));
				dhhUser.setHighScore(rs.getInt("total_score"));
				dhhUser.setRecentHighScore(rs.getInt("recent_score"));
				
				dhhUser.setPasttime(rs.getInt("past_time"));
				
				if(dhhUser.getPasttime() > 1) {
					dhhUser.setShip(rs.getInt("ship_id"));
					dhhUser.setCity(rs.getInt("city_id"));
					dhhUser.setMoney(rs.getInt("money"));
				
					dhhUser.setSaving(rs.getInt("saving"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return dhhUser;
	}

	/**
	 * 该实现启用缓存请注意，更新时间为12小时
	 */
	public Vector getUserList(String condition) {
		DhhUserBean dhhUser = null;
		// 构建查询语句
		String query = "SELECT * from dhh_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		//缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector ebookList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.DHH_CACHE_GROUP, OsCacheUtil.DHH_CACHE_FLUSH_PERIOD);
			if (ebookList != null) {
				return ebookList;
			}
		}
		Vector dhhUserList = new Vector();
		DhhUserBean fsUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				dhhUser = new DhhUserBean();
				dhhUser.setId(rs.getInt("id"));
				dhhUser.setUserId(rs.getInt("user_id"));
				dhhUser.setShip(rs.getInt("ship_id"));
				dhhUser.setCity(rs.getInt("city_id"));
				dhhUser.setMoney(rs.getInt("money"));
				dhhUser.setHighScore(rs.getInt("total_score"));
				dhhUser.setRecentHighScore(rs.getInt("recent_score"));
				dhhUser.setPasttime(rs.getInt("past_time"));
				dhhUser.setSaving(rs.getInt("saving"));
				dhhUserList.add(dhhUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		//缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, dhhUserList, OsCacheUtil.DHH_CACHE_GROUP);
		}
		return dhhUserList;
	}

	public boolean addUser(DhhUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO dhh_user(user_id,total_score,money,ship_id,city_id) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getHighScore());
			pstmt.setInt(3, bean.getMoney());
			pstmt.setInt(4, bean.getShip());
			pstmt.setInt(5, bean.getCity());
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

	public boolean delUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM dhh_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE dhh_user SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM dhh_user WHERE "
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
}
