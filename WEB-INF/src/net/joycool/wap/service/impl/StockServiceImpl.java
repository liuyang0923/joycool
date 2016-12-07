package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.stock.StockBean;
import net.joycool.wap.bean.stock.StockDealBean;
import net.joycool.wap.bean.stock.StockGrailBean;
import net.joycool.wap.bean.stock.StockHolderBean;
import net.joycool.wap.bean.stock.StockInfoBean;
import net.joycool.wap.bean.stock.StockNoticeBean;
import net.joycool.wap.bean.stock.StockPvBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IStockService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class StockServiceImpl implements IStockService {
	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#addStockPv(net.joycool.wap.bean.stock.StockPvBean)
	 */
	public boolean addStockPv(StockPvBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock_pv(stock_id,pv,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getStockId());
			pstmt.setInt(2, bean.getPv());
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
	 * @see net.joycool.wap.service.infc.IStockService#delStockPv(java.lang.String)
	 */
	public boolean delStockPv(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock_pv WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockPv(java.lang.String)
	 */
	public StockPvBean getStockPv(String condition) {
		StockPvBean stockPv = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_pv";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				stockPv = this.getStockPv(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockPv;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockPvCount(java.lang.String)
	 */
	public int getStockPvCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock_pv WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockPvList(java.lang.String)
	 */
	public Vector getStockPvList(String condition) {
		Vector stockPvList = new Vector();
		StockPvBean stockPv = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_pv";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockPv = this.getStockPv(rs);
				stockPvList.add(stockPv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockPvList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStockPv(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStockPv(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock_pv SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#addStock(net.joycool.wap.bean.stock.StockBean)
	 */
	public boolean addStock(StockBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock(name,introduction,total_count,sold_count,price,create_datetime) VALUES(?,?,?,?,?,now())";
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
			// liuyi 2006-12-28 股票bug修改 start
			pstmt.setLong(3, bean.getTotalCount());
			pstmt.setLong(4, bean.getSoldCount());
			// liuyi 2006-12-28 股票bug修改 end
			pstmt.setFloat(5, bean.getPrice());
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
	 * @see net.joycool.wap.service.infc.IStockService#delStockv(java.lang.String)
	 */
	public boolean delStockv(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStock(java.lang.String)
	 */
	public StockBean getStock(String condition) {
		StockBean stock = (StockBean) OsCacheUtil.get(condition,
				OsCacheUtil.STOCK_TIME_GROUP,
				OsCacheUtil.STOCK_TIME_FLUSH_PERIOD);
		if (stock == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			String query = "SELECT * from jc_stock";
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				while (rs.next()) {
					stock = this.getStock(rs);
					OsCacheUtil.put(condition, stock,
							OsCacheUtil.STOCK_TIME_GROUP);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();

		}
		return stock;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockCount(java.lang.String)
	 */
	public int getStockCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockList(java.lang.String)
	 */
	public Vector getStockList(String condition) {
		Vector stockList = new Vector();
		StockBean stock = null;

		Vector stockIdList = getStockIdList(condition);

		for (int i = 0; i < stockIdList.size(); i++) {
			int id = StringUtil.toInt((String) stockIdList.get(i));
			stock = getStock("id=" + id);
			if (stock != null)
				stockList.add(stock);
		}

		return stockList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStock(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStock(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#addStockDeal(net.joycool.wap.bean.stock.StockDealBean)
	 */
	public boolean addStockDeal(StockDealBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock_deal(user_id,mark,stock_id,total_count,price,create_datetime) VALUES(?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getMark());
			pstmt.setInt(3, bean.getStockId());
			pstmt.setInt(4, bean.getTotalCount());
			pstmt.setFloat(5, bean.getPrice());

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
	 * @see net.joycool.wap.service.infc.IStockService#delStockDeal(java.lang.String)
	 */
	public boolean delStockDeal(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock_deal WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockDeal(java.lang.String)
	 */
	public StockDealBean getStockDeal(String condition) {
		StockDealBean stockDeal = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_deal";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				stockDeal = this.getStockDeal(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockDeal;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockDealCount(java.lang.String)
	 */
	public int getStockDealCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock_deal WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockDealList(java.lang.String)
	 */
	public Vector getStockDealList(String condition) {
		Vector stockDealList = new Vector();
		StockDealBean stockDeal = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_deal";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockDeal = this.getStockDeal(rs);
				stockDealList.add(stockDeal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockDealList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStockDealk(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStockDealk(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock_deal SET " + set + " WHERE "
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
	 * @see net.joycool.wap.service.infc.IStockService#addStockHolder(net.joycool.wap.bean.stock.StockHolderBean)
	 */
	public boolean addStockHolder(StockHolderBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock_holder(user_id,total_count,stock_id,average_price,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			// liuyi 2006-12-28 股票bug修改 start
			pstmt.setLong(2, bean.getTotalCount());
			// liuyi 2006-12-28 股票bug修改 end
			pstmt.setInt(3, bean.getStockId());
			pstmt.setFloat(4, bean.getAveragePrice());
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
	 * @see net.joycool.wap.service.infc.IStockService#delStockHolder(java.lang.String)
	 */
	public boolean delStockHolder(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock_holder WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockHolder(java.lang.String)
	 */
	public StockHolderBean getStockHolder(String condition) {
		StockHolderBean stockHolder = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_holder";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				stockHolder = this.getStockHolder(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockHolder;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockHolderCount(java.lang.String)
	 */
	public int getStockHolderCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock_holder WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockHolderList(java.lang.String)
	 */
	public Vector getStockHolderList(String condition) {
		Vector stockHolderList = new Vector();
		StockHolderBean stockHolder = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_holder";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockHolder = this.getStockHolder(rs);
				stockHolderList.add(stockHolder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockHolderList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStockHolder(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStockHolder(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock_holder SET " + set + " WHERE "
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
	 * @see net.joycool.wap.service.infc.IStockService#addStockInfo(net.joycool.wap.bean.stock.StockInfoBean)
	 */
	public boolean addStockInfo(StockInfoBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock_info(stock_id,today_price,yesterday_price,rate_value,rate,high_price,"
				+ "low_price,base_line,create_datetime) VALUES(?,?,?,?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getStockId());
			pstmt.setFloat(2, bean.getTodayPrice());
			pstmt.setFloat(3, bean.getYesterdayPrice());
			pstmt.setFloat(4, bean.getRateValue());
			pstmt.setFloat(5, bean.getRate());
			pstmt.setFloat(6, bean.getHighPrice());
			pstmt.setFloat(7, bean.getLowPrice());
			pstmt.setInt(8, bean.getBaseLine());
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
	 * @see net.joycool.wap.service.infc.IStockService#delStockInfo(java.lang.String)
	 */
	public boolean delStockInfo(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock_info WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockInfo(java.lang.String)
	 */
	public StockInfoBean getStockInfo(String condition) {
		StockInfoBean stockInfo = (StockInfoBean) OsCacheUtil.get(condition,
				OsCacheUtil.STOCK_GROUP, OsCacheUtil.STOCK_FLUSH_PERIOD);
		if (stockInfo == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			String query = "SELECT * from jc_stock_info";
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				if (rs.next()) {
					stockInfo = this.getStockInfo(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
			OsCacheUtil.put(condition, stockInfo, OsCacheUtil.STOCK_GROUP);
		}
		return stockInfo;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockInfoCount(java.lang.String)
	 */
	public int getStockInfoCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock_info WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockInfoList(java.lang.String)
	 */
	public Vector getStockInfoList(String condition) {
		Vector stockInfoList = new Vector();
		StockInfoBean stockInfo = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_info";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockInfo = this.getStockInfo(rs);
				stockInfoList.add(stockInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockInfoList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStockInfo(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStockInfo(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock_info SET " + set + " WHERE "
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
	 * @see net.joycool.wap.service.infc.IStockService#addStockGrail(net.joycool.wap.bean.stock.StockGrailBean)
	 */
	public boolean addStockGrail(StockGrailBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock_grail(now_price,today_price,yesterday_price,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setFloat(1, bean.getNowPrice());
			pstmt.setFloat(2, bean.getTodayPrice());
			pstmt.setFloat(3, bean.getYesterdayPrice());
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
	 * @see net.joycool.wap.service.infc.IStockService#delStockGrail(java.lang.String)
	 */
	public boolean delStockGrail(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock_grail WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockGrail(java.lang.String)
	 */
	public StockGrailBean getStockGrail(String condition) {
		StockGrailBean stockGrail = (StockGrailBean) OsCacheUtil.get(condition,
				OsCacheUtil.GRAIL_TIME_GROUP,
				OsCacheUtil.GRAIL_TIME_FLUSH_PERIOD);

		if (stockGrail == null) {// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			String query = "SELECT * from jc_stock_grail";
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				while (rs.next()) {
					stockGrail = this.getStockGrail(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
			OsCacheUtil
					.put(condition, stockGrail, OsCacheUtil.GRAIL_TIME_GROUP);
		}
		return stockGrail;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockGrailCount(java.lang.String)
	 */
	public int getStockGrailCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock_grail WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockGrailList(java.lang.String)
	 */
	public Vector getStockGrailList(String condition) {
		Vector stockGrailList = new Vector();
		StockGrailBean stockGrail = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_grail";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockGrail = this.getStockGrail(rs);
				stockGrailList.add(stockGrail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockGrailList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStockGrail(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStockGrail(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock_grail SET " + set + " WHERE "
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
	 * @see net.joycool.wap.service.infc.IStockService#addStockNotice(net.joycool.wap.bean.stock.StockNoticeBean)
	 */
	public boolean addStockNotice(StockNoticeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_stock_notice(title,content,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
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
	 * @see net.joycool.wap.service.infc.IStockService#delStockNotice(java.lang.String)
	 */
	public boolean delStockNotice(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_stock_notice WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockNotice(java.lang.String)
	 */
	public StockNoticeBean getStockNotice(String condition) {
		StockNoticeBean stockNotice = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_notice";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				stockNotice = this.getStockNotice(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockNotice;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockNoticeCount(java.lang.String)
	 */
	public int getStockNoticeCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_stock_notice WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#getStockNoticeList(java.lang.String)
	 */
	public Vector getStockNoticeList(String condition) {
		Vector stockNoticeList = new Vector();
		StockNoticeBean stockNotice = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_stock_notice";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockNotice = this.getStockNotice(rs);
				stockNoticeList.add(stockNotice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockNoticeList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IStockService#updateStockNotice(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStockNotice(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_stock_notice SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockPvBean getStockPv(ResultSet rs) throws SQLException {
		StockPvBean stockPv = new StockPvBean();
		stockPv.setId(rs.getInt("id"));
		stockPv.setStockId(rs.getInt("stock_id"));
		stockPv.setPv(rs.getInt("pv"));
		stockPv.setCreateDatetime(rs.getString("create_datetime"));
		return stockPv;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockBean getStock(ResultSet rs) throws SQLException {
		StockBean stock = new StockBean();
		stock.setId(rs.getInt("id"));
		stock.setName(rs.getString("name"));
		stock.setIntroduction(rs.getString("introduction"));
		// liuyi 2006-12-28 股票bug修改 start
		stock.setTotalCount(rs.getLong("total_count"));
		stock.setSoldCount(rs.getLong("sold_count"));
		// liuyi 2006-12-28 股票bug修改 end
		stock.setPrice(rs.getFloat("price"));
		stock.setSoldIn(rs.getLong ("sold_in"));
		stock.setSoldOut(rs.getLong("sold_out"));
		stock.setCreate_datetime(rs.getString("create_datetime"));
		return stock;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockDealBean getStockDeal(ResultSet rs) throws SQLException {
		StockDealBean stockDeal = new StockDealBean();
		stockDeal.setId(rs.getInt("id"));
		stockDeal.setUserId(rs.getInt("user_id"));
		stockDeal.setMark(rs.getInt("mark"));
		stockDeal.setStockId(rs.getInt("stock_id"));
		stockDeal.setTotalCount(rs.getInt("total_count"));
		stockDeal.setPrice(rs.getFloat("price"));
		stockDeal.setCreateDatetime(rs.getString("create_datetime"));
		return stockDeal;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockHolderBean getStockHolder(ResultSet rs) throws SQLException {
		StockHolderBean stockHolder = new StockHolderBean();
		stockHolder.setId(rs.getInt("id"));
		stockHolder.setUserId(rs.getInt("user_id"));
		// liuyi 2006-12-28 股票bug修改 start
		stockHolder.setTotalCount(rs.getLong("total_count"));
		// liuyi 2006-12-28 股票bug修改 start
		stockHolder.setStockId(rs.getInt("stock_id"));
		stockHolder.setAveragePrice(rs.getFloat("average_price"));
		stockHolder.setCreateDatetime(rs.getString("create_datetime"));
		return stockHolder;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockInfoBean getStockInfo(ResultSet rs) throws SQLException {
		StockInfoBean stockInfo = new StockInfoBean();
		stockInfo.setId(rs.getInt("id"));
		stockInfo.setStockId(rs.getInt("stock_id"));
		stockInfo.setTodayPrice(rs.getFloat("today_price"));
		stockInfo.setYesterdayPrice(rs.getFloat("yesterday_price"));
		stockInfo.setRateValue(rs.getFloat("rate_value"));
		stockInfo.setRate(rs.getFloat("rate"));
		stockInfo.setHighPrice(rs.getFloat("high_price"));
		stockInfo.setLowPrice(rs.getFloat("low_price"));
		stockInfo.setBaseLine(rs.getInt("base_line"));
		stockInfo.setCreateDatetime(rs.getString("create_datetime"));
		return stockInfo;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockGrailBean getStockGrail(ResultSet rs) throws SQLException {
		StockGrailBean stockGrail = new StockGrailBean();
		stockGrail.setId(rs.getInt("id"));
		stockGrail.setNowPrice(rs.getFloat("now_price"));
		stockGrail.setTodayPrice(rs.getFloat("today_price"));
		stockGrail.setYesterdayPrice(rs.getFloat("yesterday_price"));
		stockGrail.setCreateDatetime(rs.getString("create_datetime"));
		return stockGrail;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StockNoticeBean getStockNotice(ResultSet rs) throws SQLException {
		StockNoticeBean stockNotice = new StockNoticeBean();
		stockNotice.setId(rs.getInt("id"));
		stockNotice.setTitle(rs.getString("title"));
		stockNotice.setContent(rs.getString("content"));
		stockNotice.setCreateDatetime(rs.getString("create_datetime"));
		return stockNotice;
	}

	public Vector getStockUser(String condition) {
		Vector stockHolderList = new Vector();

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT user_id from jc_stock_holder";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				stockHolderList.add(rs.getString("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockHolderList;
	}

	public Vector getStockUserList() {
		Vector stockList = new Vector();
		StockDealBean bean = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句SELECT count(id) as c_id FROM jc_stock_deal WHERE
		String query = "select distinct user_id,count(id) as cid from jc_stock_deal  as a  group by a.user_id";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				bean = new StockDealBean();
				bean.setUserId(rs.getInt("user_id"));
				bean.setTotalCount(rs.getInt("cid"));
				stockList.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockList;
	}

	public Vector getStockIdList(String condition) {
		Vector stockIdList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句SELECT count(id) as c_id FROM jc_stock_deal WHERE
		String query = "select id from jc_stock ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {

				stockIdList.add(rs.getString("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return stockIdList;
	}
}
