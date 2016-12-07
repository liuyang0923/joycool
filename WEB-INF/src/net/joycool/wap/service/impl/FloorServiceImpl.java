package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.action.floor.FloorTopBean;
import net.joycool.wap.bean.FloorBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.db.DbOperation;

/** 
 * @author guip
 * @explain：
 * @datetime:2007-8-27 17:41:01
 */
public class FloorServiceImpl {
	public FloorBean getFloorProduct(String condition) {
		// 构建查询语句
		String query = "SELECT * FROM tread_floor";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = "query";
			FloorBean Product = (FloorBean) OsCacheUtil.get(key,
					OsCacheUtil.FLOOR_CACHE_GROUP,
					OsCacheUtil.FLOOR_FLUSH_PERIOD);
			if (Product != null) {
				return Product;
			}
		}
		FloorBean Product = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				Product = new FloorBean();
				Product.setId(rs.getInt("id"));
				Product.setContent(rs.getString("content"));
				Product.setUserId(rs.getInt("user_id"));
				Product.setFloor(rs.getInt("floor"));
				Product.setNumber(rs.getInt("number"));
				Product.setPrize(rs.getInt("prize"));
				Product.setCreateTime(rs.getTimestamp("create_time"));
				Product.setCount(rs.getInt("count"));
				Product.setMark(rs.getInt("mark"));
				Product.setNowPrize(rs.getInt("nowPrize"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = "query";
			if (Product != null) {
				OsCacheUtil.put(key, Product, OsCacheUtil.FLOOR_CACHE_GROUP);
			}
		}
		return Product;
	}
	
	public boolean addFloorProduct(FloorBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO tread_floor(user_id,content,prize,number,floor,create_time) VALUES(?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getPrize());
			pstmt.setInt(4, bean.getNumber());
			pstmt.setInt(5, bean.getFloor());
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
	public boolean updateFloorProduct(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE tread_floor SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}
	
	public static boolean updateFloorTopCacheById(int floorId,int userId) {
		
		FloorTopBean top = getFloorTop("user_id="+userId);
			//不存在存款记录,插入一条
			if (top == null) {
				if(floorId<=0)
				return false;
				top = new FloorTopBean();
				top.setUserId(userId);
				top.setFloorId(floorId);
				top.setCount(1);
				addFloorTop(top);
			}
			else{
			//修改count值
			top.setCount(top.getCount()+1);
		
			//加入count值
			String set = "count="+top.getCount();
			//条件
			String condition = "user_id="+userId;
			// 更新排行榜记录
			if (!updateFloorTop(set, condition)) {
				return false;
			}
			}
			// 清空缓存
		
	 OsCacheUtil.flushGroup(OsCacheUtil.FLOORTOP_CACHE_GROUP, String.valueOf(userId));
		return true;
	}
	public static Vector getFloorTopList(String condition) {
		// 构建查询语句
		String query = "SELECT * FROM floortop";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector Product = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.FLOORTOP_CACHE_GROUP,
					OsCacheUtil.FLOORTOP_FLUSH_PERIOD);
			if (Product != null) {
				return Product;
			}
		}
		Vector ProductList = new Vector();
		FloorTopBean Product = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				Product = new FloorTopBean();
				Product.setId(rs.getInt("id"));
				Product.setUserId(rs.getInt("user_id"));
				Product.setFloorId(rs.getInt("floorId"));
				Product.setCreateTime(rs.getTimestamp("create_time"));
				Product.setCount(rs.getInt("count"));
				ProductList.add(Product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (Product != null) {
				OsCacheUtil.put(key, ProductList, OsCacheUtil.FLOORTOP_CACHE_GROUP);
			}
		}
		return ProductList;
	}
	public static FloorTopBean getFloorTop(String condition) {
		// 构建查询语句
		String query = "SELECT * FROM floortop";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			FloorTopBean Product = (FloorTopBean) OsCacheUtil.get(key,
					OsCacheUtil.FLOORTOP_CACHE_GROUP,
					OsCacheUtil.FLOORTOP_FLUSH_PERIOD);
			if (Product != null) {
				return Product;
			}
		}
		FloorTopBean Product = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				Product = new FloorTopBean();
				Product.setId(rs.getInt("id"));
				Product.setUserId(rs.getInt("user_id"));
				Product.setFloorId(rs.getInt("floorId"));
				Product.setCreateTime(rs.getTimestamp("create_time"));
				Product.setCount(rs.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (Product != null) {
				OsCacheUtil.put(key, Product, OsCacheUtil.FLOORTOP_CACHE_GROUP);
			}
		}
		return Product;
	}
	public static boolean addFloorTop(FloorTopBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO floortop(user_id,floorId,create_time,count) VALUES(?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getFloorId());
			pstmt.setInt(3, bean.getCount());
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
	public static boolean updateFloorTop(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE floortop SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
	public static boolean deleteFloorTop(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM floortop WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}
}
