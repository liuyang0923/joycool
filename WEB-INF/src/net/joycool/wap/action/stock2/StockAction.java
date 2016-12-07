package net.joycool.wap.action.stock2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.stock2.StockAccountBean;
import net.joycool.wap.bean.stock2.StockBean;
import net.joycool.wap.bean.stock2.StockCCBean;
import net.joycool.wap.bean.stock2.StockNoticeBean;
import net.joycool.wap.bean.stock2.StockWTBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RoomCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author macq
 * @explain：新版股票
 * @datetime:2007-4-25 15:01:38
 */
public class StockAction extends CustomAction {

	static byte[] lock = new byte[0];

	public static String STOCK_USER_KEY = "stock_user_key";

	public static float MAX_STOCK_PRICE_RATE = 1.1f;

	public static float MIN_STOCK_PRICE_RATE = 0.9f;

	public static float EXTRA_CHARGE = 0.003f; // 手续费

	// 撤销委托时间 (单位是小时)
	public static long STOCK_WT_TIMEOVER = 6;

	// 股票帐户最大持有金额
	public static long STOCK_MONEY_MAX = 1000000;

	public static String STOCK_TITLE = "乐酷股市";

	static int NUMBER_PER_PAGE = 5;

	static int STOCKLIST_NUMBER_PER_PAGE = 10;

	static int NUMBER_PER_PAGE_HSTOCK = 5;

	public StockAccountBean stockAccount = null;

	private UserBean loginUser = null;

	static IUserService userService = ServiceFactory.createUserService();;

	static IBankService bankService = ServiceFactory.createBankService();;

	public StockAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		if(loginUser != null)
			check();
	}

	/**
	 * 
	 * @author macq
	 * @explain： 检查session中用户股票账户信息
	 * @datetime:2007-4-26 11:50:07
	 * @return void
	 */
	public void check() {
		stockAccount = (StockAccountBean) session.getAttribute(STOCK_USER_KEY);
		if (stockAccount == null) {
			stockAccount = StockWorld.getStockAccount(loginUser.getId());
			if (stockAccount == null) {
				stockAccount = new StockAccountBean();
				stockAccount.setUserId(loginUser.getId());
				stockAccount.setMoney(0);
				stockAccount.setMoneyF(0);
				StockWorld.addStockAccount(stockAccount);
			}
			session.setAttribute(STOCK_USER_KEY, stockAccount);
		}

	}

	public IUserService getUserService() {
		return userService;
	}

	public IBankService getBankService() {
		return bankService;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 股票首页
	 * @datetime:2007-4-28 10:38:41
	 * @return void
	 */
	public void index() {
		int accountCount = SqlUtil.getIntResult(// 计算入市人数
				"select count(id) from stock_account", Constants.DBShortName);
		float total = SqlUtil.getFloatResult(
				"select sum(price*weight) from stock", Constants.DBShortName);// 计算大盘指数
		float total2 = SqlUtil.getFloatResult(
				"select sum(end_price*weight) from stock",
				Constants.DBShortName);// 计算大盘昨天指数
		request.setAttribute("accountCount", accountCount + "");
		request.setAttribute("stockTotal", Float.valueOf(total));
		request.setAttribute("stockTotalLast", Float.valueOf(total2));

		int totalCount = StockWorld.getStockService().getStockCount("1=1");
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		PagingBean page = new PagingBean(pageIndex, totalCount,
				NUMBER_PER_PAGE_HSTOCK);
		request.setAttribute("page", page);

		// 取得要显示的股票
		Vector stockList = StockWorld.getStockService().getStockList(
				"status!=3 order by count desc limit " + page.getStartIndex() + ","
						+ NUMBER_PER_PAGE_HSTOCK);
		request.setAttribute("stockList", stockList);
		Vector stockNoticeList = StockWorld.getStockService()
				.getStockNoticeList("1=1 order by id desc limit 3");
		request.setAttribute("stockNoticeList", stockNoticeList);

		// macq_2007-5-15_增加股票聊天室信息_start
		Vector contentIdList = (Vector) RoomCacheUtil.getRoomContentCountMap(2);
		Vector chatList = new Vector();
		int contentCount = 0;
		if (contentIdList != null) {
			contentCount = contentIdList.size();
		}
		JCRoomContentBean roomContent = null;
		int roomContentId = 0;
		int j = 0;
		for (int i = 0; i < contentCount; i++) {
			roomContentId = ((Integer) contentIdList.get(i)).intValue();
			roomContent = RoomCacheUtil.getRoomContent(roomContentId);
			// if(roomContent.getMark()==0 && roomContent.getToId()!=0){
			if (roomContent.getMark() == 0) {
				j++;
				chatList.add(roomContent);
				if (j == 3) {
					break;
				}
			}
		}
		request.setAttribute("chatList", chatList);
		// macq_2007-5-15_增加股票聊天室信息_end
		doTip("success", null);
	}

	/**
	 * 股票公告列表页
	 */
	public void noticeList() {
		int totalCount = SqlUtil.getIntResult(
				"select count(id) from stock_notice", Constants.DBShortName);
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		PagingBean page = new PagingBean(pageIndex, totalCount, 10);
		request.setAttribute("page", page);

		Vector stockNoticeList = StockWorld.getStockService()
				.getStockNoticeList(
						"1=1 order by id desc limit " + page.getStartIndex()
								+ ",10");
		request.setAttribute("stockNoticeList", stockNoticeList);
	}

	/**
	 * 
	 * @author macq
	 * @explain：新版本股票系统公告
	 * @datetime:2007-5-15 14:13:58
	 * @param request
	 * @return void
	 */
	public void stockNotice() {
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId < 0) {
			doTip(null, null);
			return;
		}
		StockNoticeBean stockNotice = StockWorld.getStockService()
				.getStockNotice("id=" + noticeId);
		if (stockNotice == null) {
			doTip(null, "没有查询到该公告！");
			return;
		}
		request.setAttribute("stockNotice", stockNotice);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 股票交易委托中心
	 * @datetime:2007-4-28 11:13:25
	 * @return void
	 */
	public void wtList() {
		int totalCount = StockWorld.getStockService().getStockCount("1=1");
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / STOCKLIST_NUMBER_PER_PAGE;
		if (totalCount % STOCKLIST_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "wtList.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * STOCKLIST_NUMBER_PER_PAGE;
		int end = STOCKLIST_NUMBER_PER_PAGE;
		Vector stockList = StockWorld.getStockService().getStockList(
				"1=1 order by id limit " + start + "," + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("stockList", stockList);
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-4-30 10:56:31
	 * @return void
	 */
	public void account() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户持有股票
	 * @datetime:2007-4-30 10:44:20
	 * @return void
	 */
	public void holdStock() {
		int totalCount = StockWorld.getStockService().getStockCCCount(
				"user_id=" + loginUser.getId());
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		PagingBean page = new PagingBean(pageIndex, totalCount,
				STOCKLIST_NUMBER_PER_PAGE);
		request.setAttribute("page", page);

		// 取得要显示的消息列表
		Vector stockCCList = StockWorld.getStockService().getStockCCList(
				"user_id=" + loginUser.getId() + " order by id limit "
						+ page.getStartIndex() + ","
						+ STOCKLIST_NUMBER_PER_PAGE);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("stockCCList", stockCCList);
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户委托记录
	 * @datetime:2007-4-30 11:09:01
	 * @return void
	 */
	public void accountWTList() {
		int orderBy = StringUtil.toInt(request.getParameter("orderBy"));
		if (orderBy < 0 || orderBy > 1) {
			orderBy = 1;
		}
		int totalCount = StockWorld.getStockService().getStockWTCount(
				"user_id=" + loginUser.getId() + " and mark =" + orderBy);
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE_HSTOCK;
		if (totalCount % NUMBER_PER_PAGE_HSTOCK != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "accountWTList.jsp?orderBy=" + orderBy;
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE_HSTOCK;
		int end = NUMBER_PER_PAGE_HSTOCK;
		Vector stockWTList = StockWorld.getStockService().getStockWTList(
				"user_id=" + loginUser.getId() + " and mark =" + orderBy
						+ " order by id desc limit " + start + "," + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("stockWTList", stockWTList);
		request.setAttribute("orderBy", orderBy + "");
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-4-30 14:01:23
	 * @return void
	 */
	public void cjHistory() {
		int orderBy = StringUtil.toInt(request.getParameter("orderBy"));
		if (orderBy < 0 || orderBy > 1) {
			orderBy = 1;
		}
		int totalCount = StockWorld.getStockService().getStockCJCount(
				"user_id=" + loginUser.getId() + " and mark =" + orderBy);
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "cjHistory.jsp?orderBy=" + orderBy;
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector stockCJList = StockWorld.getStockService().getStockCJList(
				"user_id=" + loginUser.getId() + " and mark =" + orderBy
						+ " order by id desc limit " + start + "," + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("stockCJList", stockCJList);
		request.setAttribute("orderBy", orderBy + "");
		doTip("success", null);
	}

	public void cjToday() {
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		// 取得要显示的消息列表
		Vector stockCJList = StockWorld
				.getStockService()
				.getStockCJList(
						"stock_id="
								+ stockId
								+ " and id>367725 and mark=1 and create_datetime>curdate() order by id desc limit 20");
		request.setAttribute("stockCJList", stockCJList);

		StockBean stock = StockWorld.getStockService()
				.getStock("id=" + stockId);// 查询股票信息
		request.setAttribute("stock", stock);

		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 用户持有股票
	 * @datetime:2007-4-30 10:15:18
	 * @return void
	 */
	public void stockList() {
		int type = StringUtil.toInt(request.getParameter("type"));
		setAttribute("type", type);

		String cond = "status!=3";
		if (type >= 0)
			cond = "status!=3 and type=" + type;

		int totalCount = StockWorld.getStockService().getStockCount(cond);
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		PagingBean page = new PagingBean(pageIndex, totalCount,
				STOCKLIST_NUMBER_PER_PAGE);
		request.setAttribute("page", page);

		Vector stockList = StockWorld.getStockService().getStockList(
				cond + " order by id limit " + page.getStartIndex() + ","
						+ STOCKLIST_NUMBER_PER_PAGE);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("stockList", stockList);
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 个股信息
	 * @datetime:2007-4-30 9:29:14
	 * @return void
	 */
	public void stockInfo() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		StockBean stock = StockWorld.getStock("id=" + stockId);
		if (stock == null) {
			doTip(null, null);
			return;
		}
		request.setAttribute("stock", stock);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：委托交易记录撤单操作
	 * @datetime:2007-4-30 11:43:52
	 * @return void
	 */
	public void destroy() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		int wtId = StringUtil.toInt(request.getParameter("wtId"));
		if (wtId <= 0) {
			doTip(null, null);
			return;
		}
		synchronized (lock) {// 同步数据更新
			StockWTBean stockWT = StockWorld.getStockWT("id=" + wtId);
			if (stockWT == null || stockWT.getUserId() != loginUser.getId()) {
				doTip(null, "非法的撤单操作。");
				return;
			}
			// 获取剩余委托数量
			long changeCount = (stockWT.getCount() - stockWT.getCjCount());
			// 获取当时下单的委托价格
			float wtPrice = stockWT.getPrice();

			StockBean stock = StockWorld.getStock("id=" + stockWT.getStockId());
			if (stock == null) {
				doTip(null, "股票不存在。");
				return;
			}

			float extraCharge = EXTRA_CHARGE; // 手续费，新股发行的时候不需要
			if (stock.isStatusNew())
				extraCharge = 0;

			if (stockWT.getMark() == 1) {// 判断委托类型 1为购买0为出售
				// 计算当前剩余委托金额
				long changePrice = (long) (changeCount * wtPrice * (1 + extraCharge));
				StockWorld.UpdateStockAccount("money=money+" + changePrice
						+ ",money_f=money_f-" + changePrice, "user_id="
						+ loginUser.getId());
			} else {
				StockWorld.updateStockCC("count=count+" + changeCount
						+ ",count_f=count_f-" + changeCount, "user_id="
						+ loginUser.getId() + " and stock_id="
						+ stockWT.getStockId());
			}
			// 删除委托记录
			StockWorld.deleteStockWT("id=" + stockWT.getId());
			doTip("success", "操作成功!");
			return;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 账户信息
	 * @datetime:2007-4-29 17:29:39
	 * @return void
	 */
	public void accountInfo() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		getStockService().calcStockAccount(account);
		if (account == null) {
			doTip(null, null);
			return;
		}
		request.setAttribute("account", account);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-4-28 11:46:35
	 * @return void
	 */
	public void wt() {
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		StockBean stock = StockWorld.getStockService()
				.getStock("id=" + stockId);// 查询股票信息
		if (stock == null) {
			doTip(null, "该股票不存在!");
			return;
		}
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		StockCCBean stockCC = StockWorld.getStockCC("user_id="
				+ loginUser.getId() + " and stock_id=" + stock.getId());// 查询用户是否持有该股票
		if (stockCC == null) {
			stockCC = new StockCCBean();
		}
		request.setAttribute("account", account);
		request.setAttribute("stockCC", stockCC);
		request.setAttribute("stock", stock);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：乐币转换股币
	 * @datetime:2007-5-9 16:01:53
	 * @return void
	 */
	public void toStockMoney() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		StoreBean store = BankCacheUtil.getBankStoreCache(loginUser.getId());
		if (store == null) {
			doTip(null, "银行没有资金可以转入股市！");
			return;
		}
		// long totalMoney = account.getMoney() + account.getMoneyF();
		// if (totalMoney > STOCK_MONEY_MAX) {
		// doTip(null, "股票帐户中最大持有金额为100万！");
		// return;
		// }
		// StockCCBean stockCC = StockWorld.getStockCC("user_id="
		// + loginUser.getId() + " limit 1");// 查询用户股票持有记录
		// if (stockCC != null) {
		// doTip(null, "您已经持有股票不能增加股票账户金额！");
		// return;
		// }
		request.setAttribute("account", account);
		request.setAttribute("store", store);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：乐币转换股币结果页面
	 * @datetime:2007-5-9 16:05:50
	 * @return void
	 */
	public void toStockMoneyResult() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		// if (totalMoney > STOCK_MONEY_MAX) {
		// doTip(null, "股票帐户中最大持有金额为100万！");
		// return;
		// }
		// StockCCBean stockCC = StockWorld.getStockCC("user_id="
		// + loginUser.getId() + " limit 1");// 查询用户股票持有记录
		// if (stockCC != null) {
		// doTip(null, "您已经持有股票不能增加股票账户金额！");
		// return;
		// }
		long price = StringUtil.toLong(request.getParameter("price"));
		if (price <= 0) {
			doTip(null, "兑换金额数量输入错误！");
			return;
		}
		synchronized(loginUser.getLock()){
			boolean res = BankCacheUtil.updateBankStoreCacheById(-price, loginUser
					.getId(),0,Constants.BANK_STOCK_TYPE);
			if (!res) {
				doTip(null, "银行没有足够的资金，请确认！");
				return;
			}
		}
		// long changeMoney = totalMoney + price;
		// if (changeMoney > STOCK_MONEY_MAX) {
		// doTip(null, "现在你的帐户中共有" + totalMoney
		// + "乐币,股票帐户中最大持有金额为100万！请确认兑换金额数量！");
		// return;
		// }
		StockWorld.getStockService().updateStockAccount("money=money+" + price,
				"user_id=" + loginUser.getId());// 更新股市账户资金
		// UserInfoUtil.updateUserStatus("game_point=game_point-" + price,
		// "user_id=" + loginUser.getId(), loginUser.getId(),
		// UserCashAction.OTHERS, "乐币兑换股市资金" + price + "乐币");// 更新用户乐币
		request.setAttribute("account", account);
		doTip("success", "成功转帐" + price + "股市资金");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：股币转换乐币
	 * @datetime:2007-5-9 16:01:53
	 * @return void
	 */
	public void toGamePoint() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		request.setAttribute("account", account);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：股币转换乐币结果页面
	 * @datetime:2007-5-9 16:05:50
	 * @return void
	 */
	public void toGamePointResult() {
		StockAccountBean account = null;
		long price = 0;
		synchronized(loginUser.getLock()){
			account = StockWorld
					.getStockAccount(loginUser.getId());// 查询用户股票帐号
			if (account == null) {
				doTip(null, null);
				return;
			}
			price = StringUtil.toLong(request.getParameter("price"));
			if (price <= 0) {
				doTip(null, "兑换金额数量输入错误！");
				return;
			}
			long changeMoney = account.getMoney() - price;
			if (changeMoney < 0) {
				doTip(null, "您的帐户中没有足够的可用资金，请确认兑换金额数量！");
				return;
			}
			//
			// UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			// if (us == null) {
			// doTip(null, null);
			// return;
			// }
			BankCacheUtil.updateBankStoreCacheById(price, loginUser.getId(),0,Constants.BANK_STOCK_TYPE);
	
			StockWorld.getStockService().updateStockAccount("money=money-" + price,
					"user_id=" + loginUser.getId());// 更新股市账户资金
		}
		// UserInfoUtil.updateUserStatus("game_point=game_point+" + price,
		// "user_id=" + loginUser.getId(), loginUser.getId(),
		// UserCashAction.OTHERS, "股市资金兑换" + price + "乐币");// 更新用户乐币
		request.setAttribute("account", account);
		doTip("success", "成功转帐" + price + "乐币");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 买股票结果
	 * @datetime:2007-4-26 16:09:51
	 * @return
	 * @return
	 */

	public void buyResult() {
		if (!StockBean.isOpen()) {
			doTip(null, "股票交易时间为11-23点!");
			return;
		}
		float price = StringUtil.toFloat(request.getParameter("buyPrice"));
		if (price <= 0) {
			doTip(null, null);
			return;
		}
		price = ((int)(price * 100)) / 100.0f;
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		long count = StringUtil.toLong(request.getParameter("buyCount"));
		if (count <= 0) {
			doTip(null, null);
			return;
		}
		if (count % 100 != 0) {
			doTip(null, "购买数量必须为100的整数倍!");
			return;
		}

		synchronized (lock) {// 同步数据更新
			StockAccountBean stockAccount = StockWorld
					.getStockAccount(loginUser.getId());
			if (stockAccount == null) {
				doTip("userError", null);
				return;
			}
			double totalPrice = price * count;
			long money = stockAccount.getMoney();
			if (money < totalPrice) {
				doTip("moneyError", "您没有足够的钱购买股票");
				return;
			}
			StockBean stock = StockWorld.getStock("id=" + stockId);
			if (stock == null) {
				doTip(null, "股票不存在。");
				return;
			}
			if (stock.isStatusStop()) {
				doTip(null, "该股票今日停牌，无法交易。");
				return;
			}
			if (stock.isStatusOff()) {
				doTip(null, "该股票已经退市，无法交易。");
				return;
			}
			if (stock.isStatusNew()) {
				if (count > stock.getNewCount()) {
					doTip(null, "购买数量超过新股购买上限，委托失败。");
					return;
				}
				price = stock.getStartPrice(); // 以发行价买卖，不能根据用户输入
				if (StockWorld.userHasStock(loginUser.getId(), stockId)) {
					doTip(null, "新股发行只能购买一次，上市后方可交易。");
					return;
				}
			}
			float priceRate = price / stock.getEndPrice();
			if (priceRate > MAX_STOCK_PRICE_RATE
					|| priceRate < MIN_STOCK_PRICE_RATE || price > 2000 && price > stock.getPrice()) {
				doTip(null, "输入价格大于当前股票价格涨跌幅范围!");
				return;
			}
			doTip("success", "成功申请委托购买" + stock.getName() + ",价格:" + price
					+ ",数量:" + count + "手,请等待交易.");
			request.setAttribute("stockId", stockId + "");
			long buyCJCount = 0, totalCost = 0;
			float extraCharge = EXTRA_CHARGE; // 手续费，新股发行的时候不需要
			if (stock.isStatusNew())
				extraCharge = 0;
			
			StockCCBean cc = StockWorld.getStockCC("user_id= "
					+ loginUser.getId() + " and stock_id = " + stock.getId());// 获取委托购买股票用户持仓中是否拥有准备购买的股票
			long initCount = (cc == null ? 0 : cc.getCount() + cc.getCountF()); 
			
			while (true) {
				StockWTBean stockWT = StockWorld.getStockWT("stock_id="
						+ stock.getId()
						+ " and mark=0 order by price asc, id asc limit 1");// 获取该股最低出售价
				if (stockWT == null || stockWT.getPrice() > price) {// 如果委托记录不存在,出入委托出售记录
					break;
				} else {// 如果委托记录存在,开始交易
					long saleCount = stockWT.getCount() - stockWT.getCjCount();// 委托剩余出售数量
					float salePrice = stockWT.getPrice();// 委托出售价格
					long count1 = count - buyCJCount;// 委托剩余购买数量
					long count2 = Math.min(saleCount, count1);
					// -------------------------------------------------
					// 更新委托1和委托2的成交数量
					buyCJCount += count2;

					// ---------------------------------------------------
					// 如果委托2的成交数量>=委托数量
					if (count1 >= saleCount) {
						// 删除数据库记录
						StockWorld.deleteStockWT("id=" + stockWT.getId());// 删除委托记录
					} else {
						// 更新数据库记录
						StockWorld.updateStockWT("cj_count=cj_count+" + count2,
								"id=" + stockWT.getId());
					}
					// ---------------------------------------------------
					StockCCBean stockCC = StockWorld.getStockCC("user_id= "
							+ stockWT.getUserId() + " and stock_id = "
							+ stockWT.getStockId());// 获取委托出售股票用户持仓信息
					long cost = (long) (salePrice * count2); // 计算成本
					long charge = (long) (cost * extraCharge); // 手续费
					long charge2 = charge;		// 卖方手续费

					// 写入两条成交记录到成交表
					if(UserBagCacheUtil.getUserBagItemCount(59, stockWT.getUserId()) > 0)	// 如果有乐酷股市卡，卖股票不收手续费
						charge2 = 0;

					StockWorld.addStockCJ(stock.getId(), stockWT.getUserId(),
							salePrice, count2, 0, charge2, stockWT.getId(), stockCC.getCount() + stockCC.getCountF());// 插入委托出售购票用户的成交记录
					StockWorld.addStockCJ(stock.getId(), loginUser.getId(),
							salePrice, count2, 1, charge, stockWT.getId(), initCount);// 插入委托购买购票用户的成交记录

					initCount += count2;
					totalCost += cost + charge;
					if (stockCC != null) {
						StockWorld.unFreezeStockCCCount(stockCC, count2, cost
								- charge2);// 更新或者删除委托出售股票用户被冻结的股票
					}

					StockAccountBean saleStockAccount = StockWorld
							.getStockAccount(stockWT.getUserId());// 获取委托出售股票用户账户信息
					if (saleStockAccount != null) {
						StockWorld.UpdateStockAccount(// 更新委托出售股票用户账户金额
								"money=money+" + (cost - charge2), "user_id="
										+ stockWT.getUserId());
					}
					stockAccount.setMoney(stockAccount.getMoney() - cost);
					StockWorld.UpdateStockAccount("money=money-"
							+ (cost + charge), "user_id=" + loginUser.getId());// 更新委托购买股票用户账户金额
					// ---------------------------------------------- 更新股票价格
					StockWorld.getStockService().updateStock(
							"price=" + salePrice + ",count=count+" + count2,
							"id=" + stock.getId());

				}
				if (buyCJCount >= count) {
					break;
				}
			}
			// ---------------------------------------------------
			// 更新委托2的用户的持仓表
			if (buyCJCount > 0) {
				if(cc == null) {
					StockWorld.addStockCC(loginUser.getId(), stock.getId(), buyCJCount, 0, totalCost);
				} else {
					StockWorld.addStockCC(cc, buyCJCount, totalCost);
				}
			}
			if (buyCJCount < count) {
				// StockWTBean buyStockWT = StockWorld.getStockWT("user_id="
				// + loginUser.getId() + " and stock_id=" + stock.getId()
				// + " and mark=1 and price=" + price);
				// if (buyStockWT != null) {// 更新委托购买成交数量
				// StockWorld.updateStockWT("count=count+" + count
				// + ",cj_count=cj_count+" + buyCJCount, "id="
				// + buyStockWT.getId());
				// } else {// 增加委托购买成交数量
				StockWorld.addStockWT(loginUser.getId(), stock.getId(), price,
						count, buyCJCount, 1);// 添加委托记录
				long freeze = (long) ((count - buyCJCount) * price * (1 + extraCharge));
				StockWorld.freezeStockAccountMoney(stockAccount, freeze);// 更新用户账户冻结金额
				// }
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：卖股票结果
	 * @datetime:2007-4-26 16:10:27
	 * @return void
	 */
	public void saleResult() {
		if (!StockBean.isOpen()) {
			doTip(null, "股票交易时间为11-23点!");
			return;
		}
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		float price = StringUtil.toFloat(request.getParameter("salePrice"));
		if (price <= 0) {
			doTip(null, null);
			return;
		}
		price = ((int)(price * 100)) / 100.0f;
		long count = StringUtil.toLong(request.getParameter("saleCount"));
		if (count <= 0) {
			doTip(null, null);
			return;
		}
		if (count % 100 != 0) {
			doTip(null, "出售数量必须为100的整数倍!");
			return;
		}
		synchronized (lock) {// 同步数据更新
			StockAccountBean stockAccount = StockWorld
					.getStockAccount(loginUser.getId());
			if (stockAccount == null) {
				doTip("userError", null);
				return;
			}
			StockCCBean stockCC = StockWorld.getStockCC("user_id="
					+ loginUser.getId() + " and stock_id=" + stockId);
			if (stockCC == null || stockCC.getCount() < count) {
				doTip("countError", "您没有足够数量的股票出售");
				return;
			}
			StockBean stock = StockWorld.getStock("id=" + stockId);
			if (stock == null) {
				doTip(null, "股票不存在。");
				return;
			}
			if (stock.isStatusStop()) {
				doTip(null, "股票停牌中，无法交易。");
				return;
			}
			if (stock.isStatusOff()) {
				doTip(null, "该股票已经退市，无法交易。");
				return;
			}
			float priceRate = price / stock.getEndPrice();
			if (priceRate > MAX_STOCK_PRICE_RATE
					|| priceRate < MIN_STOCK_PRICE_RATE) {
				doTip(null, "输入价格大于当前股票价格涨跌幅范围!");
				return;
			}
			doTip("success", "成功申请委托出售" + stock.getName() + ",价格:" + price
					+ ",数量:" + count + "手,请等待交易.");
			request.setAttribute("stockId", stockId + "");
			long saleCJCount = 0, totalCost = 0;
			float extraCharge = EXTRA_CHARGE; // 手续费，新股发行的时候不需要
			if (stock.isStatusNew())
				extraCharge = 0;
			
			boolean hasCard = (UserBagCacheUtil.getUserBagItemCount(59, loginUser.getId()) > 0);	// 是否有乐酷股市卡
				
			while (true) {
				StockWTBean stockWT = StockWorld.getStockWT("stock_id="
						+ stock.getId()
						+ " and mark=1 order by price desc,id asc limit 1");// 获取该股最高求购价
				if (stockWT == null || stockWT.getPrice() < price) {// 委托购买记录为空
					break;
				} else {// 如果委托记录存在，开始交易
					long buyCount = stockWT.getCount() - stockWT.getCjCount();// 委托购买数量
					float buyPrice = stockWT.getPrice();// 委托购买价格
					long count1 = count - saleCJCount;
					long count2 = Math.min(buyCount, count1);
					// -------------------------------------------------
					// 更新委托1和委托2的成交数量
					saleCJCount += count2;
					// -------------------------------------------------
					long cost = (long) (buyPrice * count2);
					long charge = (long) (cost * extraCharge); // 手续费
					long charge2 = charge;	
					if(hasCard)		// 有卡就不收卖方手续费
						charge2 = 0;

					StockCCBean cc = StockWorld.getStockCC("user_id= "
							+ stockWT.getUserId() + " and stock_id = " + stock.getId());// 获取委托购买股票用户持仓中是否拥有准备购买的股票
					long initCount = (cc == null ? 0 : cc.getCount() + cc.getCountF()); 
					
					// 写入两条成交记录到成交表
					StockWorld.addStockCJ(stock.getId(), stockWT.getUserId(),
							buyPrice, count2, 1, charge, stockWT.getId(), initCount);// 插入委托购买购票用户的成交记录
					StockWorld.addStockCJ(stock.getId(), loginUser.getId(),
							buyPrice, count2, 0, charge2, stockWT.getId(), stockCC.getCount() + stockCC.getCountF());// 插入委托出售购票用户的成交记录
					initCount -= count2;
					// ---------------------------------------------------
					// 如果委托2的成交数量>=委托数量
					if (count1 >= buyCount) {
						// 删除数据库记录
						StockWorld.deleteStockWT("id=" + stockWT.getId());// 删除委托购买用户记录
					} else {
						// 更新数据库记录
						StockWorld.updateStockWT("cj_count=cj_count+" + count2,
								"id=" + stockWT.getId());
					}
					// ---------------------------------------------------

					StockAccountBean saleStockAccount = StockWorld
							.getStockAccount(stockWT.getUserId());// 获取委托出售股票用户账户信息
					if (saleStockAccount != null) {
						StockWorld.UpdateStockAccount(// 更新委托出售股票用户账户金额
								"money_f=money_f-" + (cost + charge),
								"user_id=" + stockWT.getUserId());
					}
					StockWorld.UpdateStockAccount("money=money+"
							+ (cost - charge2), "user_id=" + loginUser.getId());// 更新委托购买股票用户账户金额

					totalCost += cost - charge2;

					if(cc == null)
						StockWorld.addStockCC(stockWT.getUserId(), stock.getId(), count2, 0, cost + charge);
					else
						StockWorld.addStockCC(cc, count2, cost + charge);
					// 更新股票价格
					StockWorld.getStockService().updateStock(
							"price=" + buyPrice + ",count=count+" + count2,
							"id=" + stock.getId());
				}
				if (saleCJCount >= count) {
					break;
				}
			}
			// ---------------------------------------------------
			if (saleCJCount > 0) {
				stockCC = StockWorld.getStockCC("user_id=" + loginUser.getId() + " and stock_id=" + stockId);
				stockCC.setCount(stockCC.getCount() - saleCJCount);
				if (stockCC.getCount() == 0 && stockCC.getCountF() == 0) {
					StockWorld.deleteStockCC("id=" + stockCC.getId());// 删除委托出售股票用户的股票
				} else {
					StockWorld.updateStockCC("count=count-" + saleCJCount
							+ ",cost=cost-" + totalCost, "id="
							+ stockCC.getId());// 更新委托出售股票用户的股票
				}
			}

			if (saleCJCount < count) {
				// StockWTBean buyStockWT = StockWorld.getStockWT("user_id="
				// + loginUser.getId() + " and stock_id=" + stock.getId()
				// + " and mark=0 and price=" + price);
				// if (buyStockWT != null) {// 更新委托购买成交数量
				// StockWorld.updateStockWT("count=count+" + count
				// + ",cj_count=cj_count+" + saleCJCount, "id="
				// + buyStockWT.getId());
				// } else {// 增加委托购买成交数量
				StockWorld.addStockWT(loginUser.getId(), stock.getId(), price,
						count, saleCJCount, 0);// 添加委托记录
				StockWorld.freezeStockCCCount(stockCC, count - saleCJCount);// 冻结委托出售股票用户的股票
				// }
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 每小时执行的计划任务，删除6个小时内没有成交的委托订单，并相应解冻用户相应资产或者持有数量
	 * @datetime:2007-4-26 14:05:08
	 * @return void
	 */
	public static void timeTask() {
		// 解除所有冻结
		SqlUtil
				.executeUpdate(
						"update stock_account set money=money+money_f,money_f=0 where money_f!=0",
						Constants.DBShortName);

		SqlUtil
				.executeUpdate(
						"update stock_cc set count=count+count_f,count_f=0 where count_f!=0",
						Constants.DBShortName);
		// 删除所有委托
		StockWorld.deleteStockWT(" 1=1");
	}

	/**
	 * 总资产排行榜
	 */
	public void topall() {
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());
		getStockService().calcStockAccount(account);
		int count = SqlUtil.getIntResult(
				"select count(id) from stock_account where asset> "
						+ account.getAsset(), Constants.DBShortName);
		request.setAttribute("count", Integer.valueOf(count + 1));
		request.setAttribute("stockUser", account);

		// 该查询在impl中启用缓存请注意
		List userList = getStockService().getUserList(
				" asset>0 order by asset desc limit 100");
		int totalCount = userList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));

		PagingBean page = new PagingBean(pageIndex, totalCount,
				STOCKLIST_NUMBER_PER_PAGE);
		request.setAttribute("page", page);

		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * STOCKLIST_NUMBER_PER_PAGE;
		int end = Math.min(start + STOCKLIST_NUMBER_PER_PAGE, totalCount);
		List fsUserList1 = userList.subList(start, end);
		request.setAttribute("userList", fsUserList1);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 计算机2个时间之差
	 * @datetime:2007-4-26 13:44:00
	 * @return void
	 */
	public long countTime(String datetime) {
		Date ret = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ret = sdf.parse(datetime);
		} catch (Exception e) {
			ret = new Date();
		}
		long currentTime = System.currentTimeMillis();
		long time = currentTime - ret.getTime();
		return time;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取系统时间
	 * @datetime:2007-5-10 11:24:34
	 * @return
	 * @return String
	 */
	public String getTime() {
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinite = cal.get(Calendar.MINUTE);
		if (currentMinite == 0)

		{
			return currentHour + ":00";
		} else if (currentMinite < 10) {
			return currentHour + ":0" + currentMinite;
		} else
			return currentHour + ":" + currentMinite;

	}

	public static StockService getStockService() {
		return StockWorld.getStockService();
	}

	/**
	 * 
	 * @author macq
	 * @explain：按系统发售价格回收用户持有股票
	 * @datetime:2007-8-7 13:56:30
	 * @return void
	 */
	public void wdStock() {
		if (!StockBean.isOpen()) {
			doTip(null, "股票交易时间为11-23点!");
			return;
		}
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		StockBean stock = StockWorld.getStockService()
				.getStock("id=" + stockId);// 查询股票信息
		if (stock == null) {
			doTip(null, "该股票不存在!");
			return;
		} else if (stock.isStatusStop()) {
			doTip(null, "该股票今日停牌，无法交易。");
			return;
		}
		if (stock.isStatusOff()) {
			doTip(null, "该股票已经退市，无法交易。");
			return;
		}
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		StockCCBean stockCC = StockWorld.getStockCC("user_id="
				+ loginUser.getId() + " and stock_id=" + stock.getId());// 查询用户是否持有该股票
		if (stockCC == null) {
			doTip(null, "您目前尚未持有该股票");
			return;
		}
		request.setAttribute("account", account);
		request.setAttribute("stockCC", stockCC);
		request.setAttribute("stock", stock);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：按系统发售价格回收用户持有股票处理页面
	 * @datetime:2007-8-7 14:00:26
	 * @return void
	 */
	public void wdStockResult() {
		if (!StockBean.isOpen()) {
			doTip(null, "股票交易时间为11-23点!");
			return;
		}
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		StockBean stock = StockWorld.getStockService()
				.getStock("id=" + stockId);// 查询股票信息
		if (stock == null) {
			doTip(null, "该股票不存在!");
			return;
		} else if (stock.isStatusStop()) {
			doTip(null, "该股票今日停牌，无法交易。");
			return;
		}
		if (stock.isStatusOff()) {
			doTip(null, "该股票已经退市，无法交易。");
			return;
		}
		request.setAttribute("stock", stock);
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count <= 0) {
			doTip(null, null);
			return;
		}

		// else if (count % 100 != 0) {
		// doTip(null, "购买数量必须为100的整数倍!");
		// return;
		// }
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		synchronized (lock) {// 同步数据更新
			StockCCBean stockCC = StockWorld.getStockCC("user_id="
					+ loginUser.getId() + " and stock_id=" + stock.getId());// 查询用户是否持有该股票
			if (stockCC == null) {
				doTip(null, "您目前尚未持有该股票");
				return;
			}
			if (stockCC.getCount() < count) {
				doTip(null, "您没有足够股票出售给系统!");
				return;
			}
			float stockStartPrice = stock.getStartPrice();
			long totalMoney = (long) (count * stockStartPrice);
			long charge = (long) (totalMoney * EXTRA_CHARGE); // 手续费
			StockWorld.updateStockCC("count=count-" + count + ",cost=cost-"
					+ (totalMoney + charge), "id=" + stockCC.getId());
			// 写入两条成交记录到成交表
			StockWorld.addStockCJ(stock.getId(), loginUser.getId(), stock
					.getStartPrice(), count, 0, charge, 0, stockCC.getCount() + stockCC.getCountF());
			StockWorld.UpdateStockAccount("money=money+"
					+ (totalMoney - charge), "user_id=" + loginUser.getId());
			StockWorld.getStockService().updateStock(
					"withdraw=withdraw+" + count, "id=" + stock.getId());
			doTip("success", "成功出售" + stock.getName() + ",价格:"
					+ stock.getStartPrice() + ",数量:" + count + "手.");
			return;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：购买系统回收用户股票
	 * @datetime:2007-8-7 14:00:14
	 * @return void
	 */
	public void buyWDStock() {
		if (!StockBean.isOpen()) {
			doTip(null, "股票交易时间为11-23点!");
			return;
		}
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		StockBean stock = StockWorld.getStockService()
				.getStock("id=" + stockId);// 查询股票信息
		if (stock == null) {
			doTip(null, "该股票不存在!");
			return;
		} else if (stock.getWithdraw() <= 0) {
			doTip(null, "该股票目前没有剩余!");
			return;
		} else if (stock.isStatusStop()) {
			doTip(null, "该股票今日停牌，无法交易。");
			return;
		}
		if (stock.isStatusOff()) {
			doTip(null, "该股票已经退市，无法交易。");
			return;
		}
		StockAccountBean account = StockWorld
				.getStockAccount(loginUser.getId());// 查询用户股票帐号
		if (account == null) {
			doTip(null, null);
			return;
		}
		StockCCBean stockCC = StockWorld.getStockCC("user_id="
				+ loginUser.getId() + " and stock_id=" + stock.getId());// 查询用户是否持有该股票
		if (stockCC == null) {
			stockCC = new StockCCBean();
		}
		request.setAttribute("account", account);
		request.setAttribute("stockCC", stockCC);
		request.setAttribute("stock", stock);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：购买系统回收用户股票处理页面
	 * @datetime:2007-8-7 13:58:32
	 * @return void
	 */
	public void buyWDStockResult() {
		if (!StockBean.isOpen()) {
			doTip(null, "股票交易时间为11-23点!");
			return;
		}
		int stockId = StringUtil.toInt(request.getParameter("stockId"));
		if (stockId <= 0) {
			doTip(null, null);
			return;
		}
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count <= 0) {
			doTip(null, null);
			return;
		} else if (count % 100 != 0) {
			doTip(null, "购买数量必须为100的整数倍!");
			return;
		}
		synchronized (lock) {// 同步数据更新
			StockBean stock = StockWorld.getStock("id=" + stockId);
			if (stock == null) {
				doTip(null, "股票不存在。");
				return;
			} else if (stock.getWithdraw() == 0) {
				doTip(null, "该股票目前没有剩余!");
				return;
			} else if (count > stock.getWithdraw()) {
				doTip(null, "购买数量大于股票目前剩余数量!");
				return;
			} else if (stock.isStatusStop()) {
				doTip(null, "该股票今日停牌，无法交易。");
				return;
			}
			if (stock.isStatusOff()) {
				doTip(null, "该股票已经退市，无法交易。");
				return;
			}
			request.setAttribute("stock", stock);
			StockAccountBean stockAccount = StockWorld
					.getStockAccount(loginUser.getId());
			if (stockAccount == null) {
				doTip(null, null);
				return;
			}
			float stockStartPrice = stock.getStartPrice();
			long totalMoney = (long) (count * stockStartPrice);

			long money = stockAccount.getMoney();
			if (money < totalMoney) {
				doTip(null, "您的帐户中没有足够的钱购买股票");
				return;
			}

			long charge = (long) (totalMoney * EXTRA_CHARGE); // 手续费
			StockCCBean stockCC = StockWorld.getStockCC("user_id= "
					+ loginUser.getId() + " and stock_id = " + stock.getId());
			long oldCount = 0;
			if (stockCC == null) {
				StockWorld.addStockCC(loginUser.getId(), stock.getId(), count,
						(totalMoney + charge));
			} else {
				oldCount = stockCC.getCount() + stockCC.getCountF();
				StockWorld.updateStockCC("count=count+" + count + ",cost=cost+"
						+ (totalMoney + charge), "id=" + stockCC.getId());
			}

			StockWorld.addStockCJ(stock.getId(), loginUser.getId(), stock
					.getStartPrice(), count, 1, charge, 0, oldCount);
			StockWorld.UpdateStockAccount("money=money-"
					+ (totalMoney + charge), "user_id=" + loginUser.getId());
			StockWorld.getStockService().updateStock(
					"withdraw=withdraw-" + count, "id=" + stock.getId());
			doTip("success", "成功购买" + stock.getName() + ",价格:"
					+ stock.getStartPrice() + ",数量:" + count + "手.");
			return;
		}
	}

}