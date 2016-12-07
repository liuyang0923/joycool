package net.joycool.wap.action.lhc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class LhcService {
	public LhcResultBean getLhcResult(String condition) {
		LhcResultBean lhcResult = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from lhc_result";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				lhcResult = this.getLhcResult(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return lhcResult;
	}

	public List getLhcResultList(String condition) {
		List lhcResultList = new ArrayList();
		LhcResultBean lhcResult = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from lhc_result";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				lhcResult = this.getLhcResult(rs);
				lhcResultList.add(lhcResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return lhcResultList;
	}

	public boolean addLhcResult(LhcResultBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO lhc_result(num1,num2,num3,num4,num5,num6,num7,create_datetime) VALUES(?,?,?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getNum1());
			pstmt.setInt(2, bean.getNum2());
			pstmt.setInt(3, bean.getNum3());
			pstmt.setInt(4, bean.getNum4());
			pstmt.setInt(5, bean.getNum5());
			pstmt.setInt(6, bean.getNum6());
			pstmt.setInt(7, bean.getNum7());
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

	public boolean delLhcResult(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM lhc_result WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateLhcResult(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE lhc_result SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getLhcResultCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM lhc_result WHERE "
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
	
	private LhcResultBean getLhcResult(ResultSet rs) throws SQLException {
		LhcResultBean lhcResult = new LhcResultBean();
		lhcResult.setId(rs.getInt("id"));
		lhcResult.setNum1(rs.getInt("num1"));
		lhcResult.setNum2(rs.getInt("num2"));
		lhcResult.setNum3(rs.getInt("num3"));
		lhcResult.setNum4(rs.getInt("num4"));
		lhcResult.setNum5(rs.getInt("num5"));
		lhcResult.setNum6(rs.getInt("num6"));
		lhcResult.setNum7(rs.getInt("num7"));
		lhcResult.setFlag(rs.getInt("flag"));
		lhcResult.setCreateDatetime(rs.getTimestamp("create_datetime"));
		return lhcResult;
	}
	
	public LhcWagerRecordBean getLhcWagerRecord(String condition) {
		LhcWagerRecordBean lhcWagerRecord = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from lhc_wager_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				lhcWagerRecord = this.getLhcWagerRecord(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return lhcWagerRecord;
	}

	public List getLhcWagerRecordList(String condition) {
		List lhcWagerRecordList = new ArrayList();
		LhcWagerRecordBean lhcWagerRecord = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from lhc_wager_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				lhcWagerRecord = this.getLhcWagerRecord(rs);
				lhcWagerRecordList.add(lhcWagerRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return lhcWagerRecordList;
	}

	public boolean addLhcWagerRecord(LhcWagerRecordBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO lhc_wager_record(lhc_id,user_id,type,num,money,create_datetime) VALUES(?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getLhcId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getType());
			pstmt.setInt(4, bean.getNum());
			pstmt.setLong(5, bean.getMoney());
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

	public boolean delLhcWagerRecord(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM lhc_wager_record WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateLhcWagerRecord(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE lhc_wager_record SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getLhcWagerRecordCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM lhc_wager_record WHERE "
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
	
	private LhcWagerRecordBean getLhcWagerRecord(ResultSet rs) throws SQLException {
		LhcWagerRecordBean lhcWagerRecord = new LhcWagerRecordBean();
		lhcWagerRecord.setId(rs.getInt("id"));
		lhcWagerRecord.setLhcId(rs.getInt("lhc_id"));
		lhcWagerRecord.setUserId(rs.getInt("user_id"));
		lhcWagerRecord.setType(rs.getInt("type"));
		lhcWagerRecord.setNum(rs.getInt("num"));
		lhcWagerRecord.setMoney(rs.getLong("money"));
		lhcWagerRecord.setMark(rs.getInt("mark"));
		lhcWagerRecord.setCreateDatetime(rs.getTimestamp("create_datetime"));
		return lhcWagerRecord;
	}
}
