package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.bean.bank.AccountsBean;
import net.joycool.wap.bean.bank.LoadBean;
import net.joycool.wap.bean.bank.LoadLimitBean;
import net.joycool.wap.bean.bank.MoneyLogBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class BankServiceImpl implements IBankService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#addAccounts(net.joycool.wap.bean.bank.AccountsBean)
	 */
	public boolean addAccounts(AccountsBean accountsBean) {
		// TODO Auto-generated method stub
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_bank_money(store_money,withdraw_money,load_money,return_money,time) values(?,?,?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setLong(1, accountsBean.getStoreMoney());
			pstmt.setLong(2, accountsBean.getWithdrawMoney());
			pstmt.setLong(3, accountsBean.getLoadMoney());
			pstmt.setLong(4, accountsBean.getReturnMoney());
			pstmt.setString(5, accountsBean.getTime());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#addLoad(net.joycool.wap.bean.bank.LoadBean)
	 */
	public boolean addLoad(LoadBean loadBean, MoneyLogBean moneyLogBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_bank_load_money(user_id,money,create_time,expire_time) values(?,?,NOW(),ADDDATE(NOW(),INTERVAL ? HOUR))";
		String query1 = "INSERT INTO jc_bank_log(user_id,money,type,time) values(?,?,?,NOW())";

		try {
			dbOp.startTransaction();
			// 插入jc_bank_load_money表
			if (!dbOp.prepareStatement(query)) {
				dbOp.release();
				return false;
			}
			// 传递参数
			PreparedStatement pstmt = dbOp.getPStmt();
			pstmt.setInt(1, loadBean.getUserId());
			pstmt.setLong(2, loadBean.getMoney());
			pstmt.setInt(3, loadBean.getDelayTime());
			// 执行
			dbOp.executePstmt();

			// 插入jc_bank_log表
			if (!dbOp.prepareStatement(query1)) {
				dbOp.release();
				return false;
			}
			// 传递参数
			PreparedStatement pstmt1 = dbOp.getPStmt();
			pstmt1.setInt(1, moneyLogBean.getUserId());
			pstmt1.setLong(2, moneyLogBean.getMoney());
			pstmt1.setInt(3, moneyLogBean.getType());
			// 执行
			dbOp.executePstmt();

			dbOp.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#addLoadLimit(net.joycool.wap.bean.bank.LoadLimitBean)
	 */
	public boolean addLoadLimit(LoadLimitBean loadLimitBean) {
		// TODO Auto-generated method stub
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_bank_load_limit(rank,load_limit) values(?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, loadLimitBean.getRank());
			pstmt.setInt(2, loadLimitBean.getLimit());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#addMoneyLog(net.joycool.wap.bean.bank.MoneyLogBean)
	 */
	public boolean addMoneyLog(MoneyLogBean moneyLogBean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT delayed INTO jc_bank_log(user_id,r_user_id,money,type,current,time) values(?,?,?,?,?,NOW())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, moneyLogBean.getUserId());
			pstmt.setInt(2, moneyLogBean.getRUserId());
			pstmt.setLong(3, moneyLogBean.getMoney());
			pstmt.setInt(4, moneyLogBean.getType());
			pstmt.setLong(5, moneyLogBean.getCurrent());

			dbOp.executePstmt();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if(moneyLogBean.getType() == 14) {

			query = "INSERT delayed INTO jc_bank_log2(user_id,r_user_id,money,type,current,time) values(?,?,?,?,?,NOW())";

			if (!dbOp.prepareStatement(query)) {
				dbOp.release();
				return false;
			}
			pstmt = dbOp.getPStmt();
			try {
				pstmt.setInt(1, moneyLogBean.getUserId());
				pstmt.setInt(2, moneyLogBean.getRUserId());
				pstmt.setLong(3, moneyLogBean.getMoney());
				pstmt.setInt(4, moneyLogBean.getType());
				pstmt.setLong(5, moneyLogBean.getCurrent());

				dbOp.executePstmt();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		dbOp.release();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#addStore(net.joycool.wap.bean.bank.StoreBean)
	 */
	public boolean addStore(StoreBean storeBean) {
		// TODO Auto-generated method stub
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_bank_store_money(user_id,money,time) values(?,?,NOW())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, storeBean.getUserId());
			pstmt.setLong(2, storeBean.getMoney());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#deleteAccounts(java.lang.String)
	 */
	public boolean deleteAccounts(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_bank_money WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#deleteLoad(java.lang.String)
	 */
	public boolean deleteLoad(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_bank_load_money WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#deleteLoadLimit(java.lang.String)
	 */
	public boolean deleteLoadLimit(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_bank_load_limit WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#deleteMoneyLog(java.lang.String)
	 */
	public boolean deleteMoneyLog(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_bank_log WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#deleteStore(java.lang.String)
	 */
	public boolean deleteStore(String condition) {
		// TODO Auto-generated method stub
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_bank_store_money WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getAccounts(java.lang.String)
	 */
	public AccountsBean getAccounts(String condition) {

		AccountsBean account = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id,store_money,withdraw_money,load_money,return_money,time"
				+ " FROM jc_bank_money ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				account = new AccountsBean();
				account.setId(rs.getInt("id"));
				account.setStoreMoney(rs.getLong("store_money"));
				account.setWithdrawMoney(rs.getLong("withdraw_money"));
				account.setLoadMoney(rs.getLong("load_money"));
				account.setReturnMoney(rs.getLong("return_money"));
				account.setTime(rs.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getAccountsCount(java.lang.String)
	 */
	public int getAccountsCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_bank_money";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getAccountsList(java.lang.String)
	 */
	public Vector getAccountsList(String condition) {

		Vector accountList = new Vector();
		AccountsBean account = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 构建查询语句
		String query = "SELECT id,store_money,withdraw_money,load_money,return_money,time"
				+ " FROM jc_bank_money ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				account = new AccountsBean();
				account.setId(rs.getInt("id"));
				account.setStoreMoney(rs.getLong("store_money"));
				account.setWithdrawMoney(rs.getLong("withdraw_money"));
				account.setLoadMoney(rs.getLong("load_money"));
				account.setReturnMoney(rs.getLong("return_money"));
				account.setTime(rs.getString("time"));
				accountList.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		// 返回结果
		return accountList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getLoad(java.lang.String)
	 */
	public LoadBean getLoad(String condition) {

		LoadBean load = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, user_id, money, create_time, expire_time, now() as currentTime, floor((UNIX_TIMESTAMP(expire_time)-UNIX_TIMESTAMP(now()))/60) as leaveDays"
				+ " FROM jc_bank_load_money ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				load = new LoadBean();
				load.setId(rs.getInt("id"));
				load.setUserId(rs.getInt("user_id"));
				load.setMoney(rs.getLong("money"));
				load.setCreateTime(rs.getString("create_time"));
				load.setExpireTime(rs.getString("expire_time"));
				load.setCurrentTime(rs.getString("currentTime"));
				load.setLeaveTime(rs.getInt("leaveDays"));
				// 没有及时删除的到期的贷款
				if (rs.getInt("leaveDays") < 0) {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return load;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getLoadCount(java.lang.String)
	 */
	public int getLoadCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_bank_load_money";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getLoadLimit(java.lang.String)
	 */
	public LoadLimitBean getLoadLimit(String condition) {

		LoadLimitBean loadLimit = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, rank, load_limit FROM jc_bank_load_limit ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				loadLimit = new LoadLimitBean();
				loadLimit.setId(rs.getInt("id"));
				loadLimit.setRank(rs.getInt("rank"));
				loadLimit.setLimit(rs.getInt("load_limit"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return loadLimit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getLoadLimitCount(java.lang.String)
	 */
	public int getLoadLimitCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_bank_load_limit";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getLoadLimitList(java.lang.String)
	 */
	public Vector getLoadLimitList(String condition) {

		Vector loadLimitList = new Vector();
		LoadLimitBean loadLimit = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, rank, load_limit FROM jc_bank_load_limit ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				loadLimit = new LoadLimitBean();
				loadLimit.setId(rs.getInt("id"));
				loadLimit.setRank(rs.getInt("rank"));
				loadLimit.setLimit(rs.getInt("load_limit"));
				loadLimitList.add(loadLimit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return loadLimitList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getLoadList(java.lang.String)
	 */
	public Vector getLoadList(String condition) {

		Vector loadList = new Vector();
		LoadBean load = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, user_id, money, create_time, expire_time, now() as currentTime, floor((UNIX_TIMESTAMP(expire_time)-UNIX_TIMESTAMP(now()))/60) as leaveDays"
				+ " FROM jc_bank_load_money ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				load = new LoadBean();
				load.setId(rs.getInt("id"));
				load.setUserId(rs.getInt("user_id"));
				load.setMoney(rs.getLong("money"));
				load.setCreateTime(rs.getString("create_time"));
				load.setExpireTime(rs.getString("expire_time"));
				load.setCurrentTime(rs.getString("currentTime"));
				load.setLeaveTime(rs.getInt("leaveDays"));

				// 没有及时删除到期的贷款
				// if (rs.getInt("leaveDays") < 0){
				// break;
				// }

				loadList.add(load);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return loadList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getMoneyLog(java.lang.String)
	 */
	public MoneyLogBean getMoneyLog(String condition) {

		MoneyLogBean moneyLog = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, user_id, money, type, time FROM jc_bank_log ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				moneyLog = new MoneyLogBean();
				moneyLog.setId(rs.getInt("id"));
				moneyLog.setUserId(rs.getInt("user_id"));
				moneyLog.setRUserId(rs.getInt("r_user_id"));
				moneyLog.setMoney(rs.getLong("money"));
				moneyLog.setType(rs.getInt("type"));
				moneyLog.setTime(rs.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return moneyLog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getMoneyLogCount(java.lang.String)
	 */
	public int getMoneyLogCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_bank_log";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}


	public List getMoneyLogList(String condition) {

		List moneyLogList = new ArrayList();
		MoneyLogBean moneyLog = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * FROM jc_bank_log ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);

		try {
			while (rs.next()) {
				moneyLog = new MoneyLogBean();
				moneyLog.setId(rs.getInt("id"));
				moneyLog.setUserId(rs.getInt("user_id"));
				moneyLog.setRUserId(rs.getInt("r_user_id"));
				moneyLog.setMoney(rs.getLong("money"));
				moneyLog.setType(rs.getInt("type"));
				moneyLog.setTime(rs.getString("time"));
				moneyLog.setCurrent(rs.getLong("current"));
				moneyLogList.add(moneyLog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return moneyLogList;
	}
	public List getMoneyLogList2(String condition) {

		List moneyLogList = new ArrayList();
		MoneyLogBean moneyLog = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * FROM jc_bank_log2 ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);

		try {
			while (rs.next()) {
				moneyLog = new MoneyLogBean();
				moneyLog.setId(rs.getInt("id"));
				moneyLog.setUserId(rs.getInt("user_id"));
				moneyLog.setRUserId(rs.getInt("r_user_id"));
				moneyLog.setMoney(rs.getLong("money"));
				moneyLog.setType(rs.getInt("type"));
				moneyLog.setTime(rs.getString("time"));
				moneyLog.setCurrent(rs.getLong("current"));
				moneyLogList.add(moneyLog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return moneyLogList;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getStore(java.lang.String)
	 */
	public StoreBean getStore(String condition) {

		StoreBean store = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, user_id, money, time FROM jc_bank_store_money ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				store = new StoreBean();
				store.setId(rs.getInt("id"));
				store.setUserId(rs.getInt("user_id"));
				store.setMoney(rs.getLong("money"));
				store.setTime(rs.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getStoreCount(java.lang.String)
	 */
	public int getStoreCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_bank_store_money";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#getStoreList(java.lang.String)
	 */
	public Vector getStoreList(String condition) {

		Vector storeList = new Vector();
		StoreBean store = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, user_id, money, time FROM jc_bank_store_money ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				store = new StoreBean();
				store.setId(rs.getInt("id"));
				store.setUserId(rs.getInt("user_id"));
				store.setMoney(rs.getLong("money"));
				store.setTime(rs.getString("time"));
				storeList.add(store);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return storeList;
	}

	public HashMap getRankLoadMoneyMap() {
		HashMap rankLoadMoneyMap = new HashMap();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT id,rank,load_limit from jc_bank_load_limit";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				rankLoadMoneyMap.put(new Integer(rs.getInt("rank")), new Long(
						rs.getInt("load_limit")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return rankLoadMoneyMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#updateAccounts(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateAccounts(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_bank_money SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#updateLoad(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateLoad(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_bank_load_money SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#updateLoadLimit(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateLoadLimit(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_bank_load_limit SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#updateMoneyLog(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateMoneyLog(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_bank_log SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}


	/*更新用户信息
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IBankService#updateStore(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateStore(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_bank_store_money SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public int getLoadHour(String condition) {

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT floor((UNIX_TIMESTAMP(expire_time)-UNIX_TIMESTAMP(create_time))/3600) as loadDays "
				+ " FROM jc_bank_load_money ";
		query = query + " WHERE " + condition;

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				return rs.getInt("loadDays");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return -1;
	}

	/**
	 * fanys 206-08-03
	 */
	public boolean deleteLoad(LoadBean loadBean, MoneyLogBean moneyLogBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "delete from  jc_bank_load_money where id=?";
		String query1 = "INSERT INTO jc_bank_log(user_id,money,type,time) values(?,?,?,NOW())";

		try {
			dbOp.startTransaction();
			// 插入jc_bank_load_money表
			if (!dbOp.prepareStatement(query)) {
				dbOp.release();
				return false;
			}
			// 传递参数
			PreparedStatement pstmt = dbOp.getPStmt();
			pstmt.setInt(1, loadBean.getId());
			// 执行
			dbOp.executePstmt();

			// 插入jc_bank_log表
			if (!dbOp.prepareStatement(query1)) {
				dbOp.release();
				return false;
			}
			// 传递参数
			PreparedStatement pstmt1 = dbOp.getPStmt();
			pstmt1.setInt(1, moneyLogBean.getUserId());
			pstmt1.setLong(2, moneyLogBean.getMoney());
			pstmt1.setInt(3, moneyLogBean.getType());
			// 执行
			dbOp.executePstmt();

			dbOp.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	public void addMoneyFlux(String date) {
		// TODO Auto-generated method stub
		String sql = "select sum(money) from jc_bank_log where type=1 and to_days('"
				+ date + "')-to_days(time)=0 ";
		long storeMoney = SqlUtil.getLongResult(sql, "mcool");

		sql = "select sum(money) from jc_bank_log where type=2 and to_days('"
				+ date + "')-to_days(time)=0 ";
		long withdrawMoney = SqlUtil.getLongResult(sql, "mcool");

		sql = "select sum(money) from jc_bank_log where type=3 and to_days('"
				+ date + "')-to_days(time)=0 ";
		long loadMoney = SqlUtil.getLongResult(sql, "mcool");

		sql = "select sum(money) from jc_bank_log where type=4 and to_days('"
				+ date + "')-to_days(time)=0 ";
		long returnMoney = SqlUtil.getLongResult(sql, Constants.DBShortName);

		AccountsBean flux = new AccountsBean();
		flux.setStoreMoney(storeMoney);
		flux.setWithdrawMoney(withdrawMoney);
		flux.setLoadMoney(loadMoney);
		flux.setReturnMoney(returnMoney);
		flux.setTime(date);

		this.addAccounts(flux);

	}
	public List getMoneyLogListCache(int userId) {
		String key = getKey(userId);

		List moneyLogList = (List) OsCacheUtil.get(key,
				OsCacheUtil.MONEYLOG_CACHE_GROUP,
				OsCacheUtil.MONEYLOG_FLUSH_PERIOD);

		if (moneyLogList == null) {

			moneyLogList = this.getMoneyLogList("user_id=" + userId + " and type=14 order by id desc limit 100" );

			if (moneyLogList == null) {
				moneyLogList = new Vector();
			}

			OsCacheUtil.put(key, moneyLogList,
					OsCacheUtil.MONEYLOG_CACHE_GROUP);
		}
		return moneyLogList;
	}
	public List getMoneyLogListCache2(int userId) {
		String key = getKey(userId);

		List moneyLogList = (List) OsCacheUtil.get(key,
				OsCacheUtil.MONEYLOG_CACHE_GROUP,
				OsCacheUtil.MONEYLOG_FLUSH_PERIOD);

		if (moneyLogList == null) {

			moneyLogList = this.getMoneyLogList2("user_id=" + userId + " and type=14 order by id desc limit 100" );

			if (moneyLogList == null) {
				moneyLogList = new Vector();
			}

			OsCacheUtil.put(key, moneyLogList,
					OsCacheUtil.MONEYLOG_CACHE_GROUP);
		}
		return moneyLogList;
	}
	public static String getKey(int userId) {
		return String.valueOf(userId);
	}
}
