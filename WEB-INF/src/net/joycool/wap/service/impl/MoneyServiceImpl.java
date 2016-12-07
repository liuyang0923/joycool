/*
 * 作者:马长青
 * 日期:2006-7-24
 * 功能:实现统计现金流操作数据库方法接口实现
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.bank.AccountsBean;
import net.joycool.wap.bean.money.MoneyFlowRecordBean;
import net.joycool.wap.bean.money.MoneyFlowTypeBean;
import net.joycool.wap.bean.money.MoneyFluxBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IMoneyService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class MoneyServiceImpl implements IMoneyService {

	// 现金流类型表
	public MoneyFlowTypeBean getMoneyFlowType(String condition) {
		MoneyFlowTypeBean moneyFlowType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_flow_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				moneyFlowType = this.getMoneyFlowType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return moneyFlowType;
	}

	public HashMap getMoneyFlowTypeMap(String condition) {
		HashMap moneyFlowTypeMap = new HashMap();
		MoneyFlowTypeBean moneyFlowType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_flow_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				moneyFlowType = this.getMoneyFlowType(rs);
				moneyFlowTypeMap.put(String.valueOf(moneyFlowType.getId()),
						moneyFlowType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return moneyFlowTypeMap;
	}

	public boolean addMoneyFlowType(MoneyFlowTypeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_money_flow_type(description) VALUES(?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getDescription());
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

	public boolean delMoneyFlowType(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_money_flow_type WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateMoneyFlowType(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_money_flow_type SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getMoneyFlowTypeCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_money_flow_type WHERE "
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

	// 现金流日志表
	public MoneyFlowRecordBean getMoneyFlowRecord(String condition) {
		MoneyFlowRecordBean moneyFlowRecord = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_flow_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				moneyFlowRecord = this.getMoneyFlowRecord(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return moneyFlowRecord;
	}

	public Vector getMoneyFlowRecordList(String condition) {
		Vector moneyFlowRecordList = new Vector();
		MoneyFlowRecordBean moneyFlowRecord = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_flow_record";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				moneyFlowRecord = this.getMoneyFlowRecord(rs);
				moneyFlowRecordList.add(moneyFlowRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return moneyFlowRecordList;
	}

	public boolean addMoneyFlowRecord(MoneyFlowRecordBean bean) {
		return true;/*
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_money_flow_record(type_id,money,mark,user_id,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTypeId());
			// liuyi 2007-01-03 现金流统计修改 start
			pstmt.setLong(2, bean.getMoney());
			// liuyi 2007-01-03 现金流统计修改 start
			pstmt.setInt(3, bean.getMark());
			pstmt.setInt(4, bean.getUserId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;*/
	}

	public boolean delMoneyFlowRecord(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_money_flow_record WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateMoneyFlowRecord(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_money_flow_record SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getMoneyFlowRecordCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_money_flow_record WHERE "
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

	// 现金流每天总量表
	public MoneyFluxBean getMoneyFlux(String condition) {
		MoneyFluxBean moneyFlux = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_flux";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				moneyFlux = this.getMoneyFlux(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return moneyFlux;
	}

	public Vector getMoneyFluxList(String condition) {
		Vector MoneyFluxList = new Vector();
		MoneyFluxBean moneyFlux = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_money_flux";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				moneyFlux = this.getMoneyFlux(rs);
				MoneyFluxList.add(moneyFlux);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return MoneyFluxList;
	}

	public boolean addMoneyFlux(MoneyFluxBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_money_flux(create_date,total_in,total_out,type_id) VALUES(?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getCreateDate());
			pstmt.setLong(2, bean.getTotalIn());
			pstmt.setLong(3, bean.getTotalOut());
			pstmt.setLong(4, bean.getType());
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

	public boolean delMoneyFlux(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_money_flux WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateMoneyFlux(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_money_flux SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getMoneyFluxCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_money_flux WHERE "
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

	// 现金流每天总量统计查询
	public void calculateEveryDayMoneyFlux() {
		String date = DateUtil.formatDate(DateUtil.rollDate(-1));

		// 获取所有的现金流类型
		String sql = "select id from jc_money_flow_type";
		List idList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (idList == null)
			return;

		try {
			// 分别计算前一天的每种现金流总量
			for (int i = 0; i < idList.size(); i++) {
				Integer id = (Integer) idList.get(i);
				if (id == null)
					continue;

				// 总的流出
				sql = "select SUM(bb.money) from jc_money_flow_record bb "
						+ "where mark=1 and type_id=" + id.intValue()
						+ " and to_days(now()) - to_days(create_datetime)=1";
				long totalOut = SqlUtil.getLongResult(sql,
						Constants.DBShortName);
				// 总的流入
				sql = "select SUM(bb.money) from jc_money_flow_record bb "
						+ "where mark=0 and type_id=" + id.intValue()
						+ " and to_days(now()) - to_days(create_datetime)=1";
				long totalIn = SqlUtil
						.getLongResult(sql, Constants.DBShortName);

				// 银行的现金流加到银行类别里
				if (id.intValue() == 18) {
					IBankService bankService = ServiceFactory
							.createBankService();
					AccountsBean flux = bankService
							.getAccounts("to_days(time)-to_days('" + date
									+ "')=0");
					if (flux != null) {
						totalOut = totalOut + flux.getStoreMoney()
								+ flux.getReturnMoney();
						totalIn = totalIn + flux.getLoadMoney()
								+ flux.getWithdrawMoney();
					}
				}

				// 保存到数据库
				MoneyFluxBean moneyFlux = new MoneyFluxBean();
				moneyFlux.setTotalIn(totalIn);
				moneyFlux.setTotalOut(totalOut);
				moneyFlux.setCreateDate(date);
				moneyFlux.setType(id.intValue());

				this.addMoneyFlux(moneyFlux);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MoneyFluxBean getMoneyFlux(ResultSet rs) throws SQLException {
		MoneyFluxBean moneyFlux = new MoneyFluxBean();
		moneyFlux.setId(rs.getInt("id"));
		moneyFlux.setCreateDate(rs.getString("create_date"));
		moneyFlux.setTotalIn(rs.getLong("total_in"));
		moneyFlux.setTotalOut(rs.getLong("total_out"));
		moneyFlux.setType(rs.getInt("type_id"));
		return moneyFlux;
	}

	private MoneyFlowRecordBean getMoneyFlowRecord(ResultSet rs)
			throws SQLException {
		MoneyFlowRecordBean moneyFlowRecord = new MoneyFlowRecordBean();
		moneyFlowRecord.setId(rs.getInt("id"));
		moneyFlowRecord.setTypeId(rs.getInt("type_id"));
		// liuyi 2007-01-03 现金流统计修改 start
		moneyFlowRecord.setMoney(rs.getLong("money"));
		// liuyi 2007-01-03 现金流统计修改 start
		moneyFlowRecord.setMark(rs.getInt("mark"));
		moneyFlowRecord.setUserId(rs.getInt("user_id"));
		moneyFlowRecord.setCreateDatetime(rs.getString("create_datetime"));
		return moneyFlowRecord;
	}

	private MoneyFlowTypeBean getMoneyFlowType(ResultSet rs)
			throws SQLException {
		MoneyFlowTypeBean moneyFlowType = new MoneyFlowTypeBean();
		moneyFlowType.setId(rs.getInt("id"));
		moneyFlowType.setDescription(rs.getString("description"));
		return moneyFlowType;
	}
}
