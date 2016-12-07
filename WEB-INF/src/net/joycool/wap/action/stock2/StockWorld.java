package net.joycool.wap.action.stock2;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.stock2.StockAccountBean;
import net.joycool.wap.bean.stock2.StockBean;
import net.joycool.wap.bean.stock2.StockCCBean;
import net.joycool.wap.bean.stock2.StockCJBean;
import net.joycool.wap.bean.stock2.StockWTBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;

/**
 * @author macq
 * @explain： 新股票系统world
 * @datetime:2007-4-25 15:01:38
 */
public class StockWorld {
	public static HashMap stockMap = null;

	public static StockService stockService  = new StockService();

	public static StockService getStockService() {
		return stockService;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取所有股票信息
	 * @datetime:2007-4-25 17:10:22
	 * @return HashMap
	 */
	static byte[] lock1 = new byte[0];

	public static HashMap loadStockMap() {
		if (stockMap != null) {
			return stockMap;
		}
		synchronized (lock1) {
			if (stockMap != null) {
				return stockMap;
			}
			stockMap = new HashMap();
			Vector sotckList = getStockService().getStockList("1=1");
			StockBean stock = null;
			for (int i = 0; i < sotckList.size(); i++) {
				stock = (StockBean) sotckList.get(i);
				stockMap.put(new Integer(stock.getId()), stock);
			}
		}
		return stockMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取股票账户信息
	 * @datetime:2007-4-26 10:09:20
	 * @param userId
	 * @return
	 * @return StockAccountBean
	 */
	public static StockAccountBean getStockAccount(int userId) {
		StockAccountBean stockAccount = getStockService().getStockAccount(
				"user_id=" + userId);
		return stockAccount;
	}

	/**
	 * 
	 * @author macq
	 * @explain：添加股票账户
	 * @datetime:2007-4-26 10:47:40
	 * @param userId
	 * @return
	 * @return StockAccountBean
	 */
	public static boolean addStockAccount(StockAccountBean bean) {
		return getStockService().addStockAccount(bean);
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新股票账户
	 * @datetime:2007-4-26 10:54:10
	 * @param set
	 * @param condition
	 * @return
	 * @return boolean
	 */
	public static boolean UpdateStockAccount(String set, String condition) {
		return getStockService().updateStockAccount(set, condition);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取股票持仓信息
	 * @datetime:2007-4-26 10:10:29
	 * @param userId
	 * @return
	 * @return StockCCBean
	 */
	public static StockCCBean getStockCC(String condition) {
		StockCCBean stockCC = getStockService().getStockCC(condition);
		return stockCC;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取股票持仓信息列表
	 * @datetime:2007-4-26 14:11:31
	 * @param condition
	 * @return
	 * @return List
	 */
	public static List getStockCCList(String condition) {
		List stockCCList = getStockService().getStockCCList(condition);
		return stockCCList;
	}

	/**
	 * 
	 * @author macq
	 * @explain：添加用户股票持仓记录
	 * @datetime:2007-4-29 13:15:38
	 * @param userId
	 * @param stockId
	 * @param cjCount
	 * @param totalCost 
	 * @param mark
	 * @return
	 * @return boolean
	 */
	public static boolean addStockCC(int userId, int stockId, long cjCount,
			long countF, long cost) {
		StockCCBean bean = new StockCCBean();
		bean.setUserId(userId);
		bean.setStockId(stockId);
		bean.setCount(cjCount);
		bean.setCountF(countF);
		bean.setCost(cost);
		return getStockService().addStockCC(bean);
	}
	
	/**
	 * 增加用户持有股票的数量
	 * @param userId
	 * @param stockId
	 * @param count		必须大于0
	 * @param cost		这些股票的成本
	 * @return
	 */
	public static boolean addStockCC(int userId, int stockId, long count, long cost) {
		StockCCBean cc = StockWorld.getStockCC("user_id= "
				+ userId + " and stock_id = " + stockId);// 获取委托购买股票用户持仓中是否拥有准备购买的股票
		if (cc != null) {// 如果拥有更新拥有数量
			return StockWorld.updateStockCC("count=count+" + count
					+ ",cost=cost+" + cost, "id=" + cc.getId());
		} else {// 插入数据
			return StockWorld.addStockCC(userId, stockId, count, 0, cost);
		}
	}
	public static boolean addStockCC(StockCCBean cc, long count, long cost) {
		return StockWorld.updateStockCC("count=count+" + count
				+ ",cost=cost+" + cost, "id=" + cc.getId());
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新用户股票持仓记录
	 * @datetime:2007-4-26 10:54:27
	 * @param set
	 * @param condition
	 * @return
	 * @return boolean
	 */
	public static boolean updateStockCC(String set, String condition) {
		return getStockService().updateStockCC(set, condition);
	}

	/**
	 * 
	 * @author macq
	 * @explain：删除用户股票持仓记录
	 * @datetime:2007-4-26 10:56:39
	 * @param condition
	 * @return
	 * @return boolean
	 */
	public static boolean deleteStockCC(String condition) {
		return getStockService().deleteStockCC(condition);
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取股票委托信息
	 * @datetime:2007-4-26 10:11:05
	 * @param userId
	 * @return
	 * @return StockCCBean
	 */
	public static StockWTBean getStockWT(String condition) {
		StockWTBean stockWT = getStockService().getStockWT(condition);
		return stockWT;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取股票委托信息列表
	 * @datetime:2007-4-26 14:09:57
	 * @param condition
	 * @return
	 * @return List
	 */
	public static Vector getStockWTList(String condition) {
		Vector stockWTList = getStockService().getStockWTList(condition);
		return stockWTList;
	}

	/**
	 * 
	 * @author macq
	 * @explain：添加委托信息
	 * @datetime:2007-4-29 13:15:56
	 * @param userId
	 * @param stockId
	 * @param price
	 * @param count
	 * @param cjCount
	 * @param mark
	 * @return
	 * @return boolean
	 */
	public static boolean addStockWT(int userId, int stockId, float price,
			long count, long cjCount, int mark) {
		StockWTBean bean = new StockWTBean();
		bean.setUserId(userId);
		bean.setStockId(stockId);
		bean.setPrice(price);
		bean.setCount(count);
		bean.setCjCount(cjCount);
		bean.setMark(mark);
		return getStockService().addStockWT(bean);
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新委托信息
	 * @datetime:2007-4-26 10:54:58
	 * @param set
	 * @param condition
	 * @return
	 * @return boolean
	 */
	public static boolean updateStockWT(String set, String condition) {
		return getStockService().updateStockWT(set, condition);
	}

	/**
	 * 
	 * @author macq
	 * @explain：删除委托信息
	 * @datetime:2007-4-26 10:55:27
	 * @param condition
	 * @return
	 * @return boolean
	 */
	public static boolean deleteStockWT(String condition) {
		return getStockService().deleteStockWT(condition);
	}

	/**
	 * 
	 * @author macq
	 * @explain：添加股票成交记录
	 * @datetime:2007-4-29 13:17:22
	 * @param stockId
	 * @param userId
	 * @param price
	 * @param count
	 * @param mark
	 * @param wtId
	 * @return
	 * @return boolean
	 */
	public static boolean addStockCJ(int stockId, int userId, float price,
			long count, int mark, long charge, int wtId, long curCount) {
		StockCJBean bean = new StockCJBean();
		bean.setStockId(stockId);
		bean.setUserId(userId);
		bean.setPrice(price);
		bean.setCjCount(count);
		bean.setMark(mark);
		bean.setCount(curCount);
		bean.setWtId(wtId);
		bean.setCharge(charge);
		return getStockService().addStockCJ(bean);
	}

	/***************************************************************************
	 * 
	 * @author macq
	 * @explain：获取股票信息
	 * @datetime:2007-4-26 16:54:02
	 * @param condition
	 * @return
	 * @return Vector
	 */
	public static StockBean getStock(String condition) {
		StockBean stock = getStockService().getStock(condition);
		return stock;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取一个股票的买或卖的前5手
	 * @datetime:2007-4-25 17:27:38
	 * @param stockId
	 * @param mark
	 * @return Vector
	 */
	public static Vector getStockTop5(int stockId, int mark, String orderBy) {
		Vector stcokList = getStockService()
				.getStockWTTop5List(
						"select price,sum(count-cj_count) as wt_count from stock_wt "
								+ "where stock_id="+stockId+" and mark="+mark+"  group by price order by price "+orderBy+" limit 5");
		return stcokList;
	}

	/**
	 * 
	 * @author macq
	 * @explain：冻结股票系统账户金额
	 * @datetime:2007-4-25 17:34:42
	 * @param bean
	 * @param money
	 * @return
	 * @return boolean
	 */
	public static boolean freezeStockAccountMoney(StockAccountBean bean,
			long money) {
		bean.setMoney(bean.getMoney() - money);
		bean.setMoneyF(bean.getMoneyF() + money);
		return getStockService().updateStockAccount(
				"money=" + bean.getMoney() + ",money_f=" + bean.getMoneyF(),
				"id=" + bean.getId());
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新股票系统账户冻结金额
	 * @datetime:2007-4-25 17:38:14
	 * @param bean
	 * @param money
	 * @return
	 * @return boolean
	 */
	public static boolean unFreezeStockAccountMoney(StockAccountBean bean,
			int money) {
		bean.setMoneyF(bean.getMoneyF() - money);
		return getStockService().updateStockAccount(
				"money_f=" + bean.getMoneyF(), "id=" + bean.getId());
	}

	/**
	 * 
	 * @author macq
	 * @explain：冻结股票系统持仓数量
	 * @datetime:2007-4-25 17:34:32
	 * @param bean
	 * @param count
	 * @return
	 * @return boolean
	 */
	public static boolean freezeStockCCCount(StockCCBean bean, long count) {
		bean.setCount(bean.getCount() - count);
		bean.setCountF(bean.getCountF() + count);
		return getStockService().updateStockCC(
				"count=" + bean.getCount() + ",count_f=" + bean.getCountF(),
				"id=" + bean.getId());
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新或删除股票系统冻结持仓数量，只在订单被成交后使用
	 * @datetime:2007-4-25 17:34:32
	 * @param bean
	 * @param count
	 * @param cost	卖出的总价格，对应成本的减少 
	 * @return
	 * @return boolean
	 */
	public static boolean unFreezeStockCCCount(StockCCBean bean, long count, long cost) {
		bean.setCountF(bean.getCountF() - count);
		if (bean.getCount() == 0 && bean.getCountF() == 0) {// 删除持有股票记录
			return getStockService().deleteStockCC("id=" + bean.getId());
		} else {// 更新持有股票记录
			bean.setCost(bean.getCost() - cost);
			return getStockService().updateStockCC(
					"count_f=" + bean.getCountF() + ",cost=" + bean.getCost(), "id=" + bean.getId());
		}
	}
	
	// 计算所有认的总市值，定时执行
	public static void calcAllStockAccount() {
		getStockService().calcAllStockAccount();
	}

	public static boolean userHasStock(int userId, int stockId) {
		return SqlUtil.exist("select id from stock_cc where user_id=" + userId + 
				" and stock_id=" + stockId + " and count+count_f>0", Constants.DBShortName);
	}
}
