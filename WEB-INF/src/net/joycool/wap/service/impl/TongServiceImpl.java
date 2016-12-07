package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.tong.TongApplyBean;
import net.joycool.wap.bean.tong.TongAssistantBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongCityRecordBean;
import net.joycool.wap.bean.tong.TongDestroyHistoryBean;
import net.joycool.wap.bean.tong.TongFriendBean;
import net.joycool.wap.bean.tong.TongFundBean;
import net.joycool.wap.bean.tong.TongHockshopBean;
import net.joycool.wap.bean.tong.TongHonorBean;
import net.joycool.wap.bean.tong.TongLocationBean;
import net.joycool.wap.bean.tong.TongManageLogBean;
import net.joycool.wap.bean.tong.TongTitleRateLogBean;
import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @datetime 2006-12-24 下午02:09:20
 * @explain 城帮系统
 */
public class TongServiceImpl implements ITongService {
	static ICacheMap tongCityRecordCache = CacheManage.tongCityRecord;
	
	public TongBean getTong(String condition) {
		TongBean tong = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tong = this.getTong(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tong;
	}

	public Vector getTongList(String condition) {
		Vector tongList = new Vector();
		TongBean tong = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tong = this.getTong(rs);
				tongList.add(tong);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongList;
	}

	public boolean addTong(TongBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong(location_id,title,total_count,guest_count,"
				+ "description,fund,fund_total,mark,user_id,"
				+ "user_id_a,user_id_b,user_count,rate,now_endure,high_endure,"
				+ "create_datetime,short_title) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getLocationId());
			pstmt.setString(2, bean.getTitle());
			pstmt.setInt(3, bean.getTotalCount());
			pstmt.setInt(4, bean.getGuestCount());
			pstmt.setString(5, bean.getDescription());
			pstmt.setLong(6, bean.getFund());
			pstmt.setLong(7, bean.getFundTotal());
			pstmt.setInt(8, bean.getMark());
			pstmt.setInt(9, bean.getUserId());
			pstmt.setInt(10, bean.getUserIdA());
			pstmt.setInt(11, bean.getUserIdB());
			pstmt.setInt(12, bean.getUserCount());
			pstmt.setInt(13, bean.getRate());
			pstmt.setInt(14, bean.getNowEndure());
			pstmt.setInt(15, bean.getHighEndure());
			pstmt.setString(16, bean.getShortTitle());
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

	public boolean delTong(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTong(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong WHERE "
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

	private TongBean getTong(ResultSet rs) throws SQLException {
		TongBean tong = new TongBean();
		tong.setId(rs.getInt("id"));
		tong.setLocationId(rs.getInt("location_id"));
		tong.setTitle(rs.getString("title"));
		tong.setTotalCount(rs.getInt("total_count"));
		tong.setGuestCount(rs.getInt("guest_count"));
		tong.setDescription(rs.getString("description"));
		tong.setFund(rs.getLong("fund"));
		tong.setFundTotal(rs.getLong("fund_total"));
		tong.setReward(rs.getInt("reward"));
		tong.setMark(rs.getInt("mark"));
		tong.setUserId(rs.getInt("user_id"));
		tong.setUserIdA(rs.getInt("user_id_a"));
		tong.setUserIdB(rs.getInt("user_id_b"));
		tong.setUserCount(rs.getInt("user_count"));
		tong.setRate(rs.getInt("rate"));
		tong.setNowEndure(rs.getInt("now_endure"));
		tong.setHighEndure(rs.getInt("high_endure"));
		tong.setCreateDatetime(rs.getString("create_datetime"));
		tong.setCadreCount(rs.getInt("cadre_count"));
		tong.setStockId(rs.getInt("stock_id"));
		tong.setShop(rs.getInt("shop"));
		tong.setShortTitle(rs.getString("short_title"));
		tong.setDestroyDatetime(rs.getTimestamp("destroy_datetime").getTime());
		tong.setHonor(rs.getInt("honor"));
		tong.setHonorDrop(rs.getInt("honor_drop"));
		tong.setTongAssaultTime(rs.getTimestamp("tong_assault_time"));
		tong.setTongRecoveryTime(rs.getTimestamp("tong_recovery_time"));
		tong.setDepot(rs.getInt("depot"));
		tong.setDepotWeek(rs.getInt("depot_week"));
		tong.setDepotLast(rs.getInt("depot_last"));
		return tong;
	}

	public TongLocationBean getTongLocation(String condition) {
		TongLocationBean tongLocation = (TongLocationBean) OsCacheUtil.get(
				"locationid" + condition,
				OsCacheUtil.TONG_LOCATION_CACHE_GROUP,
				OsCacheUtil.TONG_LOCATION_FLUSH_PERIOD);
		if (tongLocation == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			String query = "SELECT * from jc_tong_location";
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				if (rs.next()) {
					tongLocation = this.getTongLocation(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
			OsCacheUtil.put("locationid" + condition, tongLocation,
					OsCacheUtil.TONG_LOCATION_CACHE_GROUP);
		}
		return tongLocation;
	}

	public Vector getTongLocationList(String condition) {
		Vector tongLocationList = (Vector) OsCacheUtil.get("location"
				+ condition, OsCacheUtil.TONG_LOCATION_CACHE_GROUP,
				OsCacheUtil.TONG_LOCATION_FLUSH_PERIOD);
		if (tongLocationList == null) {
			tongLocationList = new Vector();
			TongLocationBean tongLocation = null;
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			String query = "SELECT * from jc_tong_location";
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				while (rs.next()) {
					tongLocation = this.getTongLocation(rs);
					tongLocationList.add(tongLocation);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
			OsCacheUtil.put("location" + condition, tongLocationList,
					OsCacheUtil.TONG_LOCATION_CACHE_GROUP);
		}
		return tongLocationList;
	}

	public boolean addTongLocation(TongLocationBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_location(name,description) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
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

	public boolean delTongLocation(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_location WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongLocation(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_location SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongLocationCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_location WHERE "
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

	private TongLocationBean getTongLocation(ResultSet rs) throws SQLException {
		TongLocationBean tongLocation = new TongLocationBean();
		tongLocation.setId(rs.getInt("id"));
		tongLocation.setDescription(rs.getString("description"));
		tongLocation.setName(rs.getString("name"));
		return tongLocation;
	}

	public TongApplyBean getTongApply(String condition) {
		TongApplyBean tongApply = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_apply";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongApply = this.getTongApply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongApply;
	}

	public Vector getTongApplyList(String condition) {
		Vector tongApplyList = new Vector();
		TongApplyBean tongApply = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_apply";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongApply = this.getTongApply(rs);
				tongApplyList.add(tongApply);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongApplyList;
	}

	public boolean addTongApply(TongApplyBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_apply(tong_id,user_id,manager_id,content,mark,create_datetime) VALUES(?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getManagerId());
			pstmt.setString(4, bean.getContent());
			pstmt.setInt(5, bean.getMark());
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

	public boolean delTongApply(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_apply WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongApply(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_apply SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongApplyCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_apply WHERE "
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

	private TongApplyBean getTongApply(ResultSet rs) throws SQLException {
		TongApplyBean tongApply = new TongApplyBean();
		tongApply.setId(rs.getInt("id"));
		tongApply.setTongId(rs.getInt("tong_id"));
		tongApply.setUserId(rs.getInt("user_id"));
		tongApply.setManagerId(rs.getInt("manager_id"));
		tongApply.setContent(rs.getString("content"));
		tongApply.setMark(rs.getInt("mark"));
		tongApply.setCreateDatetime(rs.getString("create_datetime"));
		return tongApply;
	}

	public TongUserBean getTongUser(String condition) {
		TongUserBean tongUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongUser = this.getTongUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongUser;
	}

	public Vector getTongUserList(String condition) {
		Vector tongUserList = new Vector();
		TongUserBean tongUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongUser = this.getTongUser(rs);
				tongUserList.add(tongUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongUserList;
	}

	public boolean addTongUser(TongUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_user(tong_id,user_id,"
				+ "mark,donation,create_datetime,honor) VALUES(?,?,?,?,now(),'')";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getMark());
			pstmt.setInt(4, bean.getDonation());
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

	public boolean delTongUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_user SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_user WHERE "
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

	private TongUserBean getTongUser(ResultSet rs) throws SQLException {
		TongUserBean tongUser = new TongUserBean();
		tongUser.setId(rs.getInt("id"));
		tongUser.setTongId(rs.getInt("tong_id"));
		tongUser.setUserId(rs.getInt("user_id"));
		tongUser.setMark(rs.getInt("mark"));
		tongUser.setDonation(rs.getInt("donation"));
		tongUser.setCreateDatetime(rs.getString("create_datetime"));
		tongUser.setHonor(rs.getString("honor"));
		return tongUser;
	}

	public TongFundBean getTongFund(String condition) {
		TongFundBean tongFund = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_fund";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongFund = this.getTongFund(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongFund;
	}

	public Vector getTongFundList(String condition) {
		Vector tongFundList = new Vector();
		TongFundBean tongFund = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_fund";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongFund = this.getTongFund(rs);
				tongFundList.add(tongFund);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongFundList;
	}

	public boolean addTongFund(TongFundBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_fund(tong_id,current_tong_id,money,mark,"
				+ "user_id,create_datetime) VALUES(?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());
			pstmt.setLong(2, bean.getCurrentTongId());
			pstmt.setLong(3, bean.getMoney());
			pstmt.setInt(4, bean.getMark());
			pstmt.setInt(5, bean.getUserId());
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

	public boolean delTongFund(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_fund WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongFund(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_fund SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongFundCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_fund WHERE "
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

	private TongFundBean getTongFund(ResultSet rs) throws SQLException {
		TongFundBean tongFund = new TongFundBean();
		tongFund.setId(rs.getInt("id"));
		tongFund.setTongId(rs.getInt("tong_id"));
		tongFund.setCurrentTongId(rs.getInt("current_tong_id"));
		tongFund.setMoney(rs.getLong("money"));
		tongFund.setMark(rs.getInt("mark"));
		tongFund.setUserId(rs.getInt("user_id"));
		tongFund.setCreateDatetime(rs.getString("create_datetime"));
		return tongFund;
	}

	public TongHockshopBean getTongHockshop(String condition) {
		TongHockshopBean tongHockshop = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_hockshop";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongHockshop = this.getTongHockshop(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongHockshop;
	}

	public Vector getTongHockshopList(String condition) {
		Vector tongHockshopList = new Vector();
		TongHockshopBean tongHockshop = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_hockshop";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongHockshop = this.getTongHockshop(rs);
				tongHockshopList.add(tongHockshop);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongHockshopList;
	}

	public boolean addTongHockshop(TongHockshopBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_hockshop(tong_id,develop,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getDevelop());
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

	public boolean delTongHockshop(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_hockshop WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongHockshop(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_hockshop SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongHockshopCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_hockshop WHERE "
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

	private TongHockshopBean getTongHockshop(ResultSet rs) throws SQLException {
		TongHockshopBean tongHockshop = new TongHockshopBean();
		tongHockshop.setId(rs.getInt("id"));
		tongHockshop.setTongId(rs.getInt("tong_id"));
		tongHockshop.setDevelop(rs.getInt("develop"));
		tongHockshop.setCreateDatetime(rs.getString("create_datetime"));
		return tongHockshop;
	}

	public int getTongAssistantCount(int tongId) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "select  COUNT(a.id) c_id FROM jc_tong_user a join user_status b  on b.user_id=a.user_id and  b.rank>10 and   a.mark=0 and tong_id="
				+ tongId + " order by donation desc ";
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

	public Vector getTongAssistantList(int tongId, int start, int end) {
		Vector tongAssistantList = new Vector();
		TongAssistantBean tongAssistant = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "select donation,a.user_id user_id,rank  FROM jc_tong_user a join user_status b  on b.user_id=a.user_id and  b.rank>10 and   a.mark=0 and tong_id="
				+ tongId
				+ " order by donation desc limit "
				+ start
				+ " ,"
				+ end;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongAssistant = this.getTongAssistant(rs);
				tongAssistantList.add(tongAssistant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongAssistantList;
	}

	private TongAssistantBean getTongAssistant(ResultSet rs)
			throws SQLException {
		TongAssistantBean tongAssistant = new TongAssistantBean();
		tongAssistant.setDonation(rs.getInt("donation"));
		tongAssistant.setUserId(rs.getInt("user_id"));
		tongAssistant.setRank(rs.getInt("rank"));
		return tongAssistant;
	}

	public long getTongDonationCount(int tongId) {
		long count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT sum(money) as c_id FROM jc_tong_fund WHERE mark=1 and tong_id="
				+ tongId;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getLong("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public long getTongFundCount(int tongId, int userId) {
		long count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT sum(money) as c_id FROM jc_tong_fund WHERE mark=0 and tong_id="
				+ tongId + " and user_id=" + userId;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getLong("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public TongManageLogBean getTongManageLog(String condition) {
		TongManageLogBean tongManageLog = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_manage_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongManageLog = this.getTongManageLog(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongManageLog;
	}

	public Vector getTongManageLogList(String condition) {
		Vector tongManageLogList = new Vector();
		TongManageLogBean tongManageLog = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_manage_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongManageLog = this.getTongManageLog(rs);
				tongManageLogList.add(tongManageLog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongManageLogList;
	}

	public boolean addTongManageLog(TongManageLogBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_manage_log(user_id,receive_user_id,tong_id,"
				+ "mark,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getReceiveUserId());
			pstmt.setInt(3, bean.getTongId());
			pstmt.setInt(4, bean.getMark());
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

	public boolean delTongManageLog(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_manage_log WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongManageLog(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_manage_log SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongManageLogCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_manage_log WHERE "
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

	private TongManageLogBean getTongManageLog(ResultSet rs)
			throws SQLException {
		TongManageLogBean tongManageLog = new TongManageLogBean();
		tongManageLog.setId(rs.getInt("id"));
		tongManageLog.setUserId(rs.getInt("user_id"));
		tongManageLog.setReceiveUserId(rs.getInt("receive_user_id"));
		tongManageLog.setTongId(rs.getInt("tong_id"));
		tongManageLog.setMark(rs.getInt("mark"));
		tongManageLog.setCreateDatetime(rs.getString("create_datetime"));
		return tongManageLog;
	}

	public TongTitleRateLogBean getTongTitleRateLog(String condition) {
		TongTitleRateLogBean tongTitleRateLog = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_title_rate_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongTitleRateLog = this.getTongTitleRateLog(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongTitleRateLog;
	}

	public Vector getTongTitleRateLogList(String condition) {
		Vector tongTitleRateLogList = new Vector();
		TongTitleRateLogBean tongTitleRateLog = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_title_rate_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongTitleRateLog = this.getTongTitleRateLog(rs);
				tongTitleRateLogList.add(tongTitleRateLog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongTitleRateLogList;
	}

	public boolean addTongTitleRateLog(TongTitleRateLogBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_title_rate_log(user_id,tong_id,goods_id,goods_type,money,"
				+ "rate_money,create_datetime) VALUES(?,?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getTongId());
			pstmt.setInt(3, bean.getGoodsId());
			pstmt.setInt(4, bean.getGoodsType());
			pstmt.setInt(5, bean.getMoney());
			pstmt.setInt(6, bean.getRateMoney());
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

	public boolean delTongTitleRateLog(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_title_rate_log WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongTitleRateLog(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_title_rate_log SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongTitleRateLogCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_title_rate_log WHERE "
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

	private TongTitleRateLogBean getTongTitleRateLog(ResultSet rs)
			throws SQLException {
		TongTitleRateLogBean tongTitleRateLog = new TongTitleRateLogBean();
		tongTitleRateLog.setId(rs.getInt("id"));
		tongTitleRateLog.setUserId(rs.getInt("user_id"));
		tongTitleRateLog.setTongId(rs.getInt("tong_id"));
		tongTitleRateLog.setGoodsId(rs.getInt("goods_id"));
		tongTitleRateLog.setGoodsType(rs.getInt("goods_type"));
		tongTitleRateLog.setMoney(rs.getInt("money"));
		tongTitleRateLog.setRateMoney(rs.getInt("rate_money"));
		tongTitleRateLog.setCreateDatetime(rs.getString("create_datetime"));
		return tongTitleRateLog;
	}

	public TongCityRecordBean getTongCityRecord(String condition) {
		TongCityRecordBean tongCityRecord = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_city_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				tongCityRecord = this.getTongCityRecord(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongCityRecord;
	}

	public Vector getTongCityRecordList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_tong_city_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		synchronized(tongCityRecordCache) {
			String key = condition;
			Vector tongCityRecordList = (Vector)tongCityRecordCache.get(key);
			if (tongCityRecordList != null) {
				return tongCityRecordList;
			}

			tongCityRecordList = new Vector();
			TongCityRecordBean tongCityRecord = null;

			DbOperation dbOp = new DbOperation(true);
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				while (rs.next()) {
					tongCityRecord = this.getTongCityRecord(rs);
					tongCityRecordList.add(tongCityRecord);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
			if (query != null) {
				tongCityRecordCache.put(key, tongCityRecordList);
			}
			return tongCityRecordList;
		}
	}

	public boolean addTongCityRecord(TongCityRecordBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_city_record(tong_id,user_id,count,mark,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getCount());
			pstmt.setInt(4, bean.getMark());
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

	public boolean delTongCityRecord(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_city_record WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongCityRecord(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_city_record SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongCityRecordCount(String condition) {

		String query = "SELECT count(id) as c_id FROM jc_tong_city_record WHERE "
				+ condition;

		String key = "c" + condition;
		synchronized(tongCityRecordCache) {
			Integer c = (Integer) tongCityRecordCache.get(key);
			if (c != null) {
				return c.intValue();
			}

			int count = 0;
			DbOperation dbOp = new DbOperation(true);
	
			ResultSet rs = dbOp.executeQuery(query);
			try {
				if (rs.next()) {
					count = rs.getInt("c_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			tongCityRecordCache.put(key, new Integer(count));

			return count;
		}
	}

	private TongCityRecordBean getTongCityRecord(ResultSet rs)
			throws SQLException {
		TongCityRecordBean tongCityRecord = new TongCityRecordBean();
		tongCityRecord.setId(rs.getInt("id"));
		tongCityRecord.setTongId(rs.getInt("tong_id"));
		tongCityRecord.setUserId(rs.getInt("user_id"));
		tongCityRecord.setCount(rs.getInt("count"));
		tongCityRecord.setMark(rs.getInt("mark"));
		tongCityRecord.setCreateDatetime(rs.getString("create_datetime"));
		return tongCityRecord;
	}

	public TongDestroyHistoryBean getTongDestroyHistoryBean(int id) {
		TongDestroyHistoryBean bean = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_destroy_history WHERE id=" + id;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				bean = new TongDestroyHistoryBean();
				bean.setId(id);
				bean.setTongId(rs.getInt("tong_id"));
				bean.setTime(rs.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return bean;
	}

	public boolean addTongDestroyHistory(TongDestroyHistoryBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_destroy_history(tong_id,time) VALUES(?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());

			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 释放资源
		dbOp.release();
		return true;
	}

	public TongFriendBean getTongFriend(String condition) {
		TongFriendBean tongFriend = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_friend";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongFriend = this.getTongFriend(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongFriend;
	}

	public Vector getTongFriendList(String condition) {
		Vector tongFriendList = new Vector();
		TongFriendBean tongFriend = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_tong_friend";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				tongFriend = this.getTongFriend(rs);
				tongFriendList.add(tongFriend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return tongFriendList;
	}

	public boolean addTongFriend(TongFriendBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_tong_friend(tong_id,ftong_id,mark) VALUES(?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getFTongId());
			pstmt.setInt(3, bean.getMark());
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

	public boolean delTongFriend(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_tong_friend WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateTongFriend(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_tong_friend SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getTongFriendCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_friend WHERE "
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

	private TongFriendBean getTongFriend(ResultSet rs)throws SQLException {
		TongFriendBean tongFriend = new TongFriendBean();
		tongFriend.setId(rs.getInt("id"));
		tongFriend.setTongId(rs.getInt("tong_id"));
		tongFriend.setFTongId(rs.getInt("ftong_id"));
		tongFriend.setMark(rs.getInt("mark"));
		return tongFriend;
	}

	public void changeHonor(int userId, int tongId, int flag) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "insert into jc_tong_honor (user_id,tong_id,flag,create_datetime)values("
			+ userId + "," + tongId + "," + flag + ",now())";
		dbOp.executeUpdate(sql);
		dbOp.release();
	}

	public List getHonorList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "SELECT * from jc_tong_honor";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTongHonor(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return list;
	}
	
	public int getHonorCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_tong_honor WHERE "
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

	private TongHonorBean getTongHonor(ResultSet rs) throws SQLException {
		TongHonorBean bean = new TongHonorBean();
		bean.setUserId(rs.getInt("user_id"));
		bean.setTongId(rs.getInt("tong_id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setCreateDatetime(rs.getTimestamp("create_datetime"));
		return bean;
	}
}
