package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.auction.AuctionHistoryBean;
import net.joycool.wap.service.infc.IAuctionService;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @datetime 2006-12-12 下午05:02:22
 * @explain  拍卖系统数据库接口实现
 */
public class AuctionServiceImpl implements IAuctionService {

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#addAuction(net.joycool.wap.bean.auction.AuctionBean)
	 */
	public boolean addAuction(AuctionBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_auction(left_user_id,right_user_id,product_id,dummy_id,time,start_price,bite_price," +
				"current_price,mark,userbag_id,hall_id,create_datetime) VALUES(?,?,?,?,?,?,?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getLeftUserId());
			pstmt.setInt(2, bean.getRightUserId());
			pstmt.setInt(3, bean.getProductId());
			pstmt.setInt(4, bean.getDummyId());
			pstmt.setInt(5, bean.getTime());
			pstmt.setLong(6, bean.getStartPrice());
			pstmt.setLong(7, bean.getBitePrice());
			pstmt.setLong(8, bean.getCurrentPrice());
			pstmt.setInt(9, bean.getMark());
			pstmt.setInt(10, bean.getUserBagId());
			pstmt.setInt(11, bean.getHallId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(SqlUtil.getLastInsertId(dbOp, "jc_auction"));
		// 释放资源
		dbOp.release();
		return true;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#addAuctionHistory(net.joycool.wap.bean.auction.AuctionHistoryBean)
	 */
	public boolean addAuctionHistory(AuctionHistoryBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_auction_history(auction_id,product_id,dummy_id,user_id,price,create_datetime) VALUES(?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getAuctionId());
			pstmt.setInt(2, bean.getProductId());
			pstmt.setInt(3, bean.getDummyId());
			pstmt.setInt(4, bean.getUserId());
			pstmt.setLong(5, bean.getPrice());
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

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#deleteAuction(java.lang.String)
	 */
	public boolean deleteAuction(String condition) {
		  boolean result;
			//数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			//构建更新语句
			String query = "DELETE FROM jc_auction WHERE " + condition;
			//执行更新
			result = dbOp.executeUpdate(query);
			//释放资源
			dbOp.release();
			return result;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#deleteAuctionHistory(java.lang.String)
	 */
	public boolean deleteAuctionHistory(String condition) {
		  boolean result;
			//数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			//构建更新语句
			String query = "DELETE FROM jc_auction_history WHERE " + condition;
			//执行更新
			result = dbOp.executeUpdate(query);
			//释放资源
			dbOp.release();
			return result;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#getAuction(java.lang.String)
	 */
	public AuctionBean getAuction(String condition) {
		AuctionBean auction = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_auction";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				auction = this.getAuction(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return auction;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#getAuctionCount(java.lang.String)
	 */
	public int getAuctionCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_auction WHERE "
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

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#getAuctionHistory(java.lang.String)
	 */
	public AuctionHistoryBean getAuctionHistory(String condition) {
		AuctionHistoryBean auctionHistory = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_auction_history";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				auctionHistory = this.getAuctionHistory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return auctionHistory;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#getAuctionHistoryCount(java.lang.String)
	 */
	public int getAuctionHistoryCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_auction_history WHERE "
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

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#getAuctionHistoryList(java.lang.String)
	 */
	public Vector getAuctionHistoryList(String condition) {
		Vector auctionHistoryList = new Vector();
		AuctionHistoryBean auctionHistory = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_auction_history";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				auctionHistory = this.getAuctionHistory(rs);
				auctionHistoryList.add(auctionHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return auctionHistoryList;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#getAuctionList(java.lang.String)
	 */
	public Vector getAuctionList(String condition) {
		Vector auctionList = new Vector();
		AuctionBean auction = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_auction";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				auction = this.getAuction(rs);
				auctionList.add(auction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return auctionList;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#updateAuction(java.lang.String, java.lang.String)
	 */
	public boolean updateAuction(String set, String condition) {
		 boolean result;
		//数据库操作类
		DbOperation dbOp = new DbOperation(true);
		String query = "UPDATE jc_auction SET " + set + " WHERE " + condition;
		//执行更新
		result = dbOp.executeUpdate(query);
		//释放资源
		dbOp.release();
		return result;
	}

	/* （非 Javadoc）
	 * @see net.joycool.wap.service.infc.IAuctionService#updateAuctionHistory(java.lang.String, java.lang.String)
	 */
	public boolean updateAuctionHistory(String set, String condition) {
		 boolean result;
		//数据库操作类
		DbOperation dbOp = new DbOperation(true);
		String query = "UPDATE jc_auction_history SET " + set + " WHERE " + condition;
		//执行更新
		result = dbOp.executeUpdate(query);
		//释放资源
		dbOp.release();
		return result;
	}
	private AuctionBean getAuction(ResultSet rs) throws SQLException {
		AuctionBean auction = new AuctionBean();
		auction.setId(rs.getInt("id"));
		auction.setLeftUserId(rs.getInt("left_user_id"));
		auction.setRightUserId(rs.getInt("right_user_id"));
		auction.setProductId(rs.getInt("product_id"));
		auction.setDummyId(rs.getInt("dummy_id"));
		auction.setUserBagId(rs.getInt("userbag_id"));
		auction.setTime(rs.getInt("time"));
		auction.setStartPrice(rs.getLong("start_price"));
		auction.setBitePrice(rs.getLong("bite_price"));
		auction.setCurrentPrice(rs.getLong("current_price"));
		auction.setMark(rs.getInt("mark"));
		auction.setHallId(rs.getInt("hall_id"));
		auction.setCreateDatetime(rs.getString("create_datetime"));
		return auction;
	}
	
	private AuctionHistoryBean getAuctionHistory(ResultSet rs) throws SQLException {
		AuctionHistoryBean auctionHistory = new AuctionHistoryBean();
		auctionHistory.setId(rs.getInt("id"));
		auctionHistory.setAuctionId(rs.getInt("auction_id"));
		auctionHistory.setProductId(rs.getInt("product_id"));
		auctionHistory.setDummyId(rs.getInt("dummy_id"));
		auctionHistory.setUserId(rs.getInt("user_id"));
		auctionHistory.setPrice(rs.getLong("price"));
		auctionHistory.setCreateDatetime(rs.getString("create_datetime"));
		return auctionHistory;
	}

}
