package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.dummy.DummyTypeBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @datetime 2006-12-12 下午05:01:07
 * @explain  虚拟物品系统数据库接口实现
 */
public class DummyServiceImpl implements IDummyService {
	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#addDummyProduct(net.joycool.wap.bean.dummy.DummyProductBean)
	 */
	public boolean addDummyProduct(DummyProductBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO item(name,introduction,mode,value,time,dummy_id,price,description) VALUES(?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getIntroduction());
			pstmt.setInt(3, bean.getMode());
			pstmt.setInt(4, bean.getValue());
			pstmt.setInt(5, bean.getTime());
			pstmt.setInt(6, bean.getDummyId());
			pstmt.setInt(7, bean.getPrice());
			pstmt.setString(8, bean.getDescription());
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#addDummyType(net.joycool.wap.bean.dummy.DummyTypeBean)
	 */
	public boolean addDummyType(DummyTypeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_dummy_type(name) VALUES(?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#deleteDummyProduct(java.lang.String)
	 */
	public boolean deleteDummyProduct(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 构建更新语句
		String query = "DELETE FROM item WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#deleteDummyType(java.lang.String)
	 */
	public boolean deleteDummyType(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_dummy_type WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#getDummyProduct(java.lang.String)
	 */
	public DummyProductBean getDummyProduct(String condition) {
		// 构建查询语句
		String query = "SELECT * from item";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			DummyProductBean dummyProduct = (DummyProductBean) OsCacheUtil.get(key,
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
			if (dummyProduct != null) {
				return dummyProduct;
			}
		}
		DummyProductBean dummyProduct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				dummyProduct = this.getDummyProduct(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (dummyProduct != null) {
				OsCacheUtil.put(key, dummyProduct, OsCacheUtil.DUMMY_CACHE_GROUP);
			}
		}
		return dummyProduct;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#getDummyProductCount(java.lang.String)
	 */
	public int getDummyProductCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM item WHERE "
				+ condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, 
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
			if (c != null) {
				return c.intValue();
			}
		}
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);

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
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.DUMMY_CACHE_GROUP);
		}
		return count;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#getDummyProductList(java.lang.String)
	 */
	public Vector getDummyProductList(String condition) {
		// 构建查询语句
		String query = "SELECT * from item";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector dummyTypeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
			if (dummyTypeList != null) {
				return dummyTypeList;
			}
		}
		Vector dummyProductList = new Vector();
		DummyProductBean dummyProduct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				dummyProduct = this.getDummyProduct(rs);
				dummyProductList.add(dummyProduct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, dummyProductList, OsCacheUtil.DUMMY_CACHE_GROUP);
		}
		return dummyProductList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#getDummyType(java.lang.String)
	 */
	public DummyTypeBean getDummyType(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_dummy_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			DummyTypeBean dummyType = (DummyTypeBean) OsCacheUtil.get(key,
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
			if (dummyType != null) {
				return dummyType;
			}
		}
		DummyTypeBean dummyType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				dummyType = this.getDummyType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (dummyType != null) {
				OsCacheUtil.put(key, dummyType, OsCacheUtil.DUMMY_CACHE_GROUP);
			}
		}
		return dummyType;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#getDummyTypeCount(java.lang.String)
	 */
	public int getDummyTypeCount(String condition) {
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_dummy_type WHERE "
				+ condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Integer c = (Integer) OsCacheUtil.get(key, 
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
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
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.DUMMY_CACHE_GROUP);
		}
		return count;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#getDummyTypeList(java.lang.String)
	 */
	public Vector getDummyTypeList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_dummy_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector dummyTypeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
			while (dummyTypeList != null) {
				return dummyTypeList;
			}
		}
		Vector dummyTypeList = new Vector();
		DummyTypeBean dummyType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				dummyType = this.getDummyType(rs);
				dummyTypeList.add(dummyType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, dummyTypeList, OsCacheUtil.DUMMY_CACHE_GROUP);
		}
		return dummyTypeList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#updateDummyProduct(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateDummyProduct(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "UPDATE item SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IDummyService#updateDummyType(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateDummyType(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_dummy_type SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private DummyProductBean getDummyProduct(ResultSet rs) throws SQLException {
		DummyProductBean dummyProduct = new DummyProductBean();
		dummyProduct.setId(rs.getInt("id"));
		dummyProduct.setName(rs.getString("name"));
		dummyProduct.setIntroduction(rs.getString("introduction"));
		dummyProduct.setMode(rs.getInt("mode"));
		dummyProduct.setValue(rs.getInt("value"));
		dummyProduct.setTime(rs.getInt("time"));
		dummyProduct.setDummyId(rs.getInt("dummy_id"));
		dummyProduct.setPrice(rs.getInt("price"));
		dummyProduct.setDescription(rs.getString("description"));
		dummyProduct.setMark(rs.getInt("mark"));
		dummyProduct.setBrush(rs.getInt("brush"));
		dummyProduct.setStartTime(rs.getTimestamp("start_time"));
		dummyProduct.setBuyPrice(rs.getInt("buy_price"));
		dummyProduct.setBind(rs.getInt("bind"));
		dummyProduct.setDue(rs.getInt("due"));
		dummyProduct.setRank(rs.getInt("rank"));
		dummyProduct.setStack(rs.getInt("stack"));
		dummyProduct.setUnique(rs.getInt("unique"));
		dummyProduct.setCooldown(rs.getInt("cooldown"));
		dummyProduct.setSeq(rs.getInt("seq"));
		dummyProduct.setClass1(rs.getInt("class1"));
		dummyProduct.setClass2(rs.getInt("class2"));
		dummyProduct.setAttribute(rs.getString("attribute"));
		dummyProduct.setUsage(rs.getString("usage"));
		dummyProduct.setGrade(rs.getInt("grade"));
		dummyProduct.init();
		return dummyProduct;
	}

	private DummyTypeBean getDummyType(ResultSet rs) throws SQLException {
		DummyTypeBean dummyType = new DummyTypeBean();
		dummyType.setId(rs.getInt("id"));
		dummyType.setName(rs.getString("name"));
		return dummyType;
	}
	public Vector getDummyProductLists(String condition) {
		// 构建查询语句
		String query = "SELECT * from item";
		if (condition != null) {
			query += " WHERE " + condition ;
		}
		query += " order by seq";
		String key = condition;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			Vector dummyTypeList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.DUMMY_CACHE_GROUP,
					OsCacheUtil.DUMMY_FLUSH_PERIOD);
			if (dummyTypeList != null) {
				return dummyTypeList;
			}
		}
		Vector dummyProductList = new Vector();
		DummyProductBean dummyProduct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				dummyProduct = this.getDummyProduct(rs);
				dummyProductList.add(dummyProduct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			OsCacheUtil.put(key, dummyProductList, OsCacheUtil.DUMMY_CACHE_GROUP);
		}
		return dummyProductList;
	}
	
	static DummyProductBean nullProduct;
	static {
		nullProduct = new DummyProductBean();
		nullProduct.setName("(未知)");
		nullProduct.setIntroduction("");
		nullProduct.setDescription("");
	}
	
	ICacheMap dummyProductCache = CacheManage.itemProto;
	
	public DummyProductBean getDummyProducts(int dummyId) {
		if(dummyId <= 0)
			return nullProduct;
		Integer key = Integer.valueOf(dummyId);

		DummyProductBean dummyProduct = (DummyProductBean) dummyProductCache.get(key);	// static cache可以直接get
		if (dummyProduct != null) {
			return dummyProduct;
		}

		DbOperation dbOp = new DbOperation(4);
		
		String query = "SELECT * from item WHERE id=" + dummyId;;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				dummyProduct = this.getDummyProduct(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();

		if (dummyProduct != null) {
			dummyProductCache.spt(key, dummyProduct);
			return dummyProduct;
		}
		return nullProduct;
	}
	public void addDummyProducts(DummyProductBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO item(name,introduction,mode,value,time,dummy_id,price,description,mark,start_time,brush,buy_price) VALUES(?,?,?,?,?,?,?,?,1,now(),?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getIntroduction());
			pstmt.setInt(3, bean.getMode());
			pstmt.setInt(4, bean.getValue());
			pstmt.setInt(5, bean.getTime());
			pstmt.setInt(6, bean.getDummyId());
			pstmt.setInt(7, bean.getPrice());
			pstmt.setString(8, bean.getDescription());
			pstmt.setInt(9, bean.getBrush());
			pstmt.setInt(10, bean.getBuyPrice());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
	}
}
