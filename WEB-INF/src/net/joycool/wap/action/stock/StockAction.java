package net.joycool.wap.action.stock;

import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.stock.StockBean;
import net.joycool.wap.bean.stock.StockDealBean;
import net.joycool.wap.bean.stock.StockGrailBean;
import net.joycool.wap.bean.stock.StockHolderBean;
import net.joycool.wap.bean.stock.StockInfoBean;
import net.joycool.wap.bean.stock.StockNoticeBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IStockService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class StockAction {

	private int NUMBER_PER_PAGE = 4;

	private HttpSession session = null;

	private UserBean loginUser;

	private IUserService userService = null;

	private IBankService bankService = null;

	private IStockService stockService = null;

	public StockAction() {
	}

	public StockAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		session = request.getSession();
	}

	public IUserService getUserService() {
		if (userService == null) {
			userService = ServiceFactory.createUserService();
		}
		return userService;
	}

	public IBankService getBankService() {
		if (bankService == null) {
			bankService = ServiceFactory.createBankService();
		}
		return bankService;
	}

	public IStockService getStockService() {
		if (stockService == null) {
			stockService = ServiceFactory.createStockService();
		}
		return stockService;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public void getStock(HttpServletRequest request) {
		int totalCount = getStockService().getStockCount("price>10");
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

		String prefixUrl = "index.jsp";

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector stockList = getStockService().getStockList(
				"price>10 order by id desc limit " + start + ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("stockList", stockList);
		StockGrailBean stockGrail = getStockService().getStockGrail(
				"1=1 order by create_datetime desc limit 0,1");

		request.setAttribute("stockGrail", stockGrail);
		return;
	}

	/**
	 * 
	 * @param request
	 */
	public void getStockNotice(HttpServletRequest request) {
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId > 0) {
			StockNoticeBean stockNotice = getStockService().getStockNotice(
					"id=" + noticeId);
			if (stockNotice != null) {
				request.setAttribute("stockNotice", stockNotice);
				request.setAttribute("result", "success");
				return;
			}
		}
		int totalCount = getStockService().getStockNoticeCount("1=1");
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
		String prefixUrl = "stockNotice.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector stockNoticeList = getStockService().getStockNoticeList(
				"1=1 order by id desc limit " + start + ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("stockNoticeList", stockNoticeList);
		return;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public StockBean getStock(int id) {
		String condition = "id=" + id;
		StockBean bean = getStockService().getStock(condition);
		return bean;
	}

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public StockHolderBean getStockHoder(String condition) {
		StockHolderBean bean = getStockService().getStockHolder(condition);
		return bean;
	}

	// 交易结果显示
	public void stockBusiness(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		String result = null;
		String tip = null;
		StockHolderBean holder = getStockHoder("stock_id=" + id
				+ " and user_id=" + loginUser.getId());
		StockBean stock = getStock(id);
		if (stock != null) {

			request.setAttribute("stock", stock);
			request.setAttribute("holder", holder);
			request.setAttribute("id", id + "");
			return;

		} else {
			tip = "该股票不存在!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
		}

	}

	// 交易过程
	public void jump(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (session.getAttribute("stockbusiness") == null) {
			request.setAttribute("id", id + "");
			return;
		}
		session.removeAttribute("stockbusiness");
		String businessCount = request.getParameter("businessCount");
		if (session.getAttribute("businessCount") != null
				&& !(((String) session.getAttribute("businessCount"))
						.equals(businessCount))) {
			request.setAttribute("id", id + "");
			return;
		}
		int counts = StringUtil.toInt(businessCount) + 1;
		session.setAttribute("businessCount", counts + "");
		String result = null;
		String tip = null;
		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		StockHolderBean holder = getStockHoder("stock_id=" + id
				+ " and user_id=" + loginUser.getId());
		StockBean stock = getStock(id);
		if (stock != null) {
			if (request.getParameter("number") != null) {
				int number = StringUtil.toInt(request.getParameter("number"));
				if (stock.getPrice() < 10) {
					tip = "该股票处于跌停状态，股票交易停止!";
					request.setAttribute("tip", tip);
					request.setAttribute("id", id + "");
					return;
				}
				if (number < 0 || number == 0) {
					tip = "交易数量不能小于0!";
					request.setAttribute("tip", tip);
					request.setAttribute("id", id + "");
					return;
				}

				if (request.getParameter("buy") != null) {
					if (status.getGamePoint() < number * stock.getPrice()
							* 1.005) {
						tip = "你的乐币不够，交易失败！";
						request.setAttribute("tip", tip);
						request.setAttribute("id", id + "");
						return;
					} else {
						if (UserInfoUtil
								.updateUserStatus(
										"game_point=game_point-"
												+ Math.round(number
														* stock.getPrice()
														* 1.005)
												+ " ,stock=stock+"
												+ Math.round(number
														* stock.getPrice()),
										"user_id=" + loginUser.getId(),
										loginUser.getId(),
										UserCashAction.STOCK, "买股票花钱"
												+ Math.round(number
														* stock.getPrice()

														* 1.005)
												+ "乐币，增加股票市值"
												+ Math.round(number
														* stock.getPrice()))) {
							// liuyi 2007-01-03 股票现金流统计 start
							MoneyAction.addMoneyFlowRecord(Constants.STOCK,
									Math.round(number * stock.getPrice()
											* 1.005), Constants.SUBTRATION,
									loginUser.getId());
							// liuyi 2007-01-03 股票现金流统计 end
//							if (status.getStockDatetime() == null) {
//								UserInfoUtil.updateUserStatus(
//										"stock_datetime=now()", "user_id="
//												+ loginUser.getId(), loginUser
//												.getId(), UserCashAction.STOCK,
//										"买股票开户");
//							}
							// liuyi 20070102 股票bug修改 start
							// getStockService().updateStock("sold_count=sold_count+"
							// + number, "id=" + id);
							// liuyi 20070102 股票bug修改 end
							OsCacheUtil.flushGroup(
									OsCacheUtil.USER_STOCK_GROUP, "user_id="
											+ loginUser.getId()
											+ " and total_count>0");
							// wucx 2007 01-12 交易记录，股票买入值增加
							getStockService().updateStock(
									"sold_in=sold_in+"
											+ Math.round(number
													* stock.getPrice()),
									"id=" + id);
							// wucx 2007 01-12 交易记录，股票买入值增加
							tip = "交易成功，我们将收取您交易额的5‰作为手续费！";
							StockDealBean deal = new

							StockDealBean();
							deal.setMark(0);
							deal.setPrice(stock.getPrice());
							deal.setStockId(id);
							deal.setUserId(loginUser.getId());
							deal.setTotalCount(number);

							if (getStockService().addStockDeal(deal)) {
								if (holder != null) {
									// liuyi 2006-12-28 股票bug修改 start
									long count = holder.getTotalCount();
									// liuyi 2006-12-28 股票bug修改 end
									float price = holder.getAveragePrice();
									float total = count * price + number *

									stock.getPrice();
									holder.setTotalCount(count + number);

									holder.setAveragePrice(total
											/ (count + number));
									getStockService().updateStockHolder(

											"total_count="
													+ holder.getTotalCount()

													+ ",average_price="
													+ holder.getAveragePrice() +

													",create_datetime=now()",
											"user_id=" + loginUser.getId()

											+ " and stock_id=" + id);
								} else {
									holder = new StockHolderBean();

									holder.setAveragePrice(stock.getPrice());
									holder.setStockId(id);
									holder.setUserId(loginUser.getId());
									holder.setTotalCount(number);
									getStockService().addStockHolder(holder);
								}

							}
							request.setAttribute("tip", tip);
							request.setAttribute("id", id + "");
							return;
						}
					}
				} else if (request.getParameter("sale") != null) {
					if (holder == null) {
						tip = "对不起,您还没有股票卖!";
						request.setAttribute("tip", tip);
						request.setAttribute("id", id + "");
						return;
					} else if (number > holder.getTotalCount()) {
						tip = "对不起,您没有那么的股票!";
						request.setAttribute("tip", tip);
						request.setAttribute("id", id + "");
						return;
					} else if ((long) (status.getGamePoint() + number
							* stock.getPrice() * 0.995) >

					Constants.MAX_GAME_POINT) {
						tip = "对不起！您身上不能带那么多现金，请分次交易。";
						request.setAttribute("tip", tip);
						request.setAttribute("id", id + "");
						return;
					} else {
						if (UserInfoUtil
								.updateUserStatus(
										"game_point=game_point+"
												+ Math.round(number
														* stock.getPrice()
														* 0.995)
												+ " ,stock=stock-"
												+ Math.round(number
														* stock.getPrice()),
										"user_id=" + loginUser.getId(),
										loginUser.getId(),
										UserCashAction.STOCK, "卖股票赚钱"
												+ Math.round(number
														* stock.getPrice()
														* 0.995)
												+ "乐币，减少股票市值"
												+ Math.round(number
														* stock.getPrice()))) {
							// liuyi 2007-01-03 股票现金流统计 start
							MoneyAction.addMoneyFlowRecord(Constants.STOCK,
									Math.round(number * stock.getPrice()
											* 0.995), Constants.PLUS, loginUser
											.getId());
							// liuyi 2007-01-03 股票现金流统计 end

							// liuyi 20070102 股票bug修改 start
							// getStockService().updateStock(
							// "sold_count=sold_count-" + number,
							// "id=" + id);
							// liuyi 20070102 股票bug修改 end
							OsCacheUtil.flushGroup(
									OsCacheUtil.USER_STOCK_GROUP, "user_id="
											+ loginUser.getId()
											+ " and total_count>0");
							tip = "交易成功，我们将收取您交易额的5‰作为手续费！";
							StockDealBean deal = new

							StockDealBean();
							deal.setMark(1);
							deal.setPrice(stock.getPrice());
							deal.setStockId(id);
							deal.setUserId(loginUser.getId());
							deal.setTotalCount(number);

							if (getStockService().addStockDeal(deal)) {
								// liuyi 2006-12-28 股票bug修改 start
								long count = holder.getTotalCount() - number;
								// liuyi 2006-12-28 股票bug修改 end
								getStockService().updateStockHolder(
										"total_count="

										+ count, "user_id=" + loginUser.getId()

										+ " and stock_id=" + id);
							}
							// wucx 2007 01-12 交易记录，股票卖出值增加
							getStockService().updateStock(
									"sold_out=sold_out+"
											+ Math.round(number
													* stock.getPrice()),
									"id=" + id);
							// wucx 2007 01-12 交易记录，股票卖出值增加
							tip = "交易成功，我们将收取您交易额的5‰作为手续费！";
							request.setAttribute("tip", tip);
							request.setAttribute("id", id + "");

							return;
						}
					}
				} else {
					request.setAttribute("id", id + "");
					return;
				}
			} else {
				request.setAttribute("id", id + "");
				return;
			}
		} else {
			tip = "该股票不存在!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "failure");
		}

	}

	/**
	 * 
	 * @return
	 */
	public StockGrailBean getStockGrail() {
		StockGrailBean bean = getStockService().getStockGrail(
				"1=1 order by create_datetime desc limit 0,1");
		return bean;
	}

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public StockInfoBean getStockInfo(String condition) {
		StockInfoBean bean = getStockService().getStockInfo(condition);
		return bean;
	}

	/**
	 * 获取系统时间
	 * 
	 * @param id
	 * @return
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

	/**
	 * 个股信息
	 * 
	 * @param request
	 */
	public void getStockInfo(HttpServletRequest request) {

		String condition = null;
		StockBean stock = null;
		StockInfoBean stockInfo = null;
		String tip = null;
		Vector vec = null;
		if (request.getParameter("stockName") != null) {
			String name = request.getParameter("stockName");
			String cond = "name='" + name + "'";
			vec = (Vector) getStockService().getStockIdList(cond);
			if (vec != null && vec.size() > 0)
				condition = "id=" + vec.get(0);

		}

		if (request.getParameter("id") != null) {
			int id = StringUtil.toInt(request.getParameter("id"));
			condition = "id=" + id;
		}
		if (condition != null) {
			stock = getStockService().getStock(condition);
			if (stock != null) {
				stockInfo = getStockInfo("stock_id=" + stock.getId()
						+ " order by create_datetime desc limit 0,1");
				if (stock.getPrice() < 10 || stock.getPrice() == 10) {
					tip = "该股票跌幅过大，暂时跌停不能交易！";

				}

			} else {
				tip = "对不起，该个股不存在！";
			}
		} else
			tip = "对不起，该个股不存在！";
		request.setAttribute("tip", tip);
		request.setAttribute("stock", stock);
		request.setAttribute("stockInfo", stockInfo);
	}

	/**
	 * 上市公司简介
	 * 
	 * @param id
	 * @return
	 */
	public void getStockIntroduce(HttpServletRequest request) {

		String condition = null;
		StockBean stock = null;
		if (request.getParameter("id") != null) {
			int id = StringUtil.toInt(request.getParameter("id"));
			condition = "id=" + id;
		}
		if (condition != null) {
			stock = getStockService().getStock(condition);

		}
		if (stock != null) {
			request.setAttribute("stockIntroduce", stock.getIntroduction());

		}
		request.setAttribute("id", request.getParameter("id"));
	}

	/**
	 * 公司10大股东
	 */
	public void stockPartner(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));

		Vector partner = getStockService().getStockUser(
				"stock_id=" + id + " order by total_count desc limit 0,10");

		request.setAttribute("partner", partner);
		request.setAttribute("id", request.getParameter("id"));
	}

	/**
	 * 获取股票随机价格升降比率
	 */
	public int getRate(float price, int baseLine) {
		if (price < 10) {
			return 0;
		}
		// 涨停的时候最多涨千分之3,跌千分之五
		int[] up = { -5, -4, -3, -2, -1, 1, 2, 3 };
		// 跌停的时候最多跌千分之3,涨千分之五
		int[] down = { -3, -2, -1, 1, 2, 3, 4, 5 };
		// 正常状态下最多涨跌各千分之5
		int[] normal = { -5, -4, -3, -2, -1, 1, 2, 3, 4, 5 };
		// String[] fs = getMaleNicks();
		// user.setNickName(fs[RandomUtil.nextInt(fs.length)]);
		float account = 0;
		if (baseLine != 0)
			account = price / (float) (baseLine);
		if (account > 1.1) {
			return up[RandomUtil.nextInt(up.length)];
		} else if (account < 0.9) {
			return down[RandomUtil.nextInt(down.length)];
		}
		return normal[RandomUtil.nextInt(normal.length)];
	}

	/**
	 * 用户持有股票信息
	 */
	public void stockHolder(HttpServletRequest request) {

		int NUMBER = 2;
		int totalCount = getStockService().getStockHolderCount(
				"user_id=" + loginUser.getId() + " and total_count>0");
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER;
		if (totalCount % NUMBER != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		String prefixUrl = "stockHolder.jsp?holder=1";

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER;
		int end = NUMBER;
		Vector holder = getStockService()
				.getStockHolderList(
						"user_id="
								+ loginUser.getId()
								+ "  and total_count>0 order by total_count desc limit "
								+ start + ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("holder", holder);
	}

	/**
	 * 得到 用户信息中的股票市值
	 */
	public long stockUserInfo(HttpServletRequest request) {

		String cache = (String) (OsCacheUtil.get("user_id=" + loginUser.getId()
				+ " and total_count>0", OsCacheUtil.USER_STOCK_GROUP,
				OsCacheUtil.USER_STOCK_FLUSH_PERIOD));

		long myprice = 0;
		if (cache == null) {
			float price = 0;
			Vector stockHolder = (Vector) getStockService().getStockHolderList(
					"user_id=" + loginUser.getId() + " and total_count>0");

			if (stockHolder != null) {
				for (int i = 0; i < stockHolder.size(); i++) {
					StockHolderBean holder = (StockHolderBean) stockHolder
							.get(i);
					if (holder != null) {
						StockBean stock = getStock(holder.getStockId());
						if (stock != null)
							price = price + holder.getTotalCount()
									* stock.getPrice();
					}
				}
			}

			myprice = (long) (price);
			OsCacheUtil.put("user_id=" + loginUser.getId()
					+ " and total_count>0", myprice + "",
					OsCacheUtil.USER_STOCK_GROUP);
			UserInfoUtil.updateUserStatus("stock=" + myprice, "user_id="
					+ loginUser.getId(), loginUser.getId(),
					UserCashAction.STOCK, "更新股票市值");
		} else {
			myprice = StringUtil.toLong(cache);
			if (myprice < 0)
				myprice = 0;
		}
		return myprice;
	}

	/**
	 * 交易记录
	 */
	public void stockBusinessInfo(HttpServletRequest request) {
		Vector buy = getStockService()
				.getStockDealList(
						"user_id="
								+ loginUser.getId()
								+ " and mark=0 order by create_datetime desc limit 0,5");
		Vector sale = getStockService()
				.getStockDealList(
						"user_id="
								+ loginUser.getId()
								+ " and mark=1 order by create_datetime desc limit 0,5");

		request.setAttribute("buy", buy);
		request.setAttribute("sale", sale);
	}

	/**
	 * 定期把用户的交易记录中大与10条的改为10条
	 */
	public static void updateStockDeal() {
		IStockService service = ServiceFactory.createStockService();
		Vector deal = service.getStockUserList();
		for (int i = 0; i < deal.size(); i++) {
			StockDealBean bean = (StockDealBean) deal.get(i);
			if (bean.getTotalCount() > 10) {
				Vector del = service.getStockDealList("user_id="
						+ bean.getUserId()
						+ " order by create_datetime desc limit 0,10");
				if (del != null) {
					StockDealBean delbean = (StockDealBean) del.get(9);
					if (delbean != null)
						service.delStockDeal(" user_id=" + bean.getUserId()
								+ " and create_datetime<'"
								+ delbean.getCreateDatetime() + "'");

				}
			}
		}
	}
}