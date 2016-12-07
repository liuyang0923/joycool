package net.joycool.wap.spec.ssc;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class LhcAction extends CustomAction {
	public static int BANK_SSC_TYPE = 17;

	public static String LHC_NAME = "六时彩";

	private UserBean loginUser = null;

	static LhcService service = new LhcService();

	int PUBLIC_NUMBER_PER_PAGE = 8;

	int MAX_GAME_POINT = 2000000000;

	// 构造函数
	public LhcAction(HttpServletRequest request) {
		super(request);
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		// loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
	}

	public LhcService getLhcService() {
		return service;
	}

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：六时彩首页
	 * @datetime:2007-8-23 15:22:12
	 * @return void
	 */
	public void index() {
		LhcResultBean lhcResult = getLhcService().getLhcResult(
				"1 order by id desc limit 1");
		if (lhcResult != null) {
			request.setAttribute("lhcResult", lhcResult);
		}
		return;
	}

	public String countName(int lhcId) {
		StringBuffer sb = new StringBuffer(1);
		LhcResultBean lhcResult = new LhcService().getLhcResult("id=" + lhcId);
		if (lhcResult == null) {
			return sb.toString();
		}
		for (int i = 1; i <= 7; i++) {
			String name = LhcWorld.LHC_NAME_ARRAY[i][LhcWorld.LHC_RESULT_ARRAY[lhcResult
					.getNum7()][i]];
			sb.append(name);
			sb.append(" ");
		}
		return sb.toString();
	}

	public long getUserStore() {
		long storeMoney = 0;
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		if (storeBean != null) {
			storeMoney = storeBean.getMoney();
		}
		return storeMoney;
	}
	
	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：用户购买六时彩页面
	 * @datetime:2007-8-23 16:30:44
	 * @return void
	 */
	public void buy() {
		int left = LhcWorld.getNextTimeLeft();
		// 如果还在投注的期限内
		if (left != -1 && left >= 60) {
			int type = StringUtil.toInt(request.getParameter("type"));
			if (type <= 0 || type > 9) {
				type = 1;
			}
			request.setAttribute("type", String.valueOf(type));
			return;
		} else {
			// 过期
			request.setAttribute("type", "-1");
		}
	}

	public static int MAX_COUNT = 100;
	public static int MAX_SUM_MONEY = 1000;	// 单位：亿 

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：用户购买六时彩结果页面
	 * @datetime:2007-8-23 16:31:12
	 * @return void
	 */
	public void buyResult() {
		int left = LhcWorld.getNextTimeLeft();
		// 如果还在投注的期限内
		if (left != -1 && left >= 60) {
			Calendar cal = Calendar.getInstance();
			// int term=Integer.parseInt(request.getParameter("term"));
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			if (currentHour >= 24 || currentHour < 9) {
				doTip(null, "六时彩在早9点至晚12点之间可以购买!"); // 时间错误
				return;
			}
			// 验证是否为表单提交
			if (!request.getMethod().equalsIgnoreCase("post")) {
				doTip("success", "提交错误");
				return;
			}
			// 购买类型
			int type = StringUtil.toInt(request.getParameter("type"));
			if (type <= 0) {
				doTip("success", "类型错误");
				return;
			}
			// 购买号码
			int num = StringUtil.toInt(request.getParameter("num"));
			if ((num <= 0 || num > 49) && type > 7) {
				doTip(null, "竞猜数字范围为1-49之间的数字!");
				return;
			}
			// 判断除平码特码以外的数字
			if (num < 0 || type == 2 && num >= 12 || (type == 1||type == 4||type == 7||type == 3) && num >= 2 || type == 5 && num >=3 || type == 6 && num >= 5) {
				doTip("success", "输入错误");
				return;
			}
			// 购买金额
			int money = StringUtil.toInt(request.getParameter("money"));
			if (money < 1 || money > MAX_COUNT) {
				doTip(null, "超过了下注范围");
				return;
			}

			// 新增：购买期数
			LhcResultBean lhcResult = LhcWorld.bean;
			int next = lhcResult.getTerm() + 1;
			if(next > LhcWorld.dayTerm)
				next = 1;
//			======MNing修改======
			int term = this.getParameterInt("term");
			if (term < 1 || term > 60){
				doTip(null, "期数错误");
				return;
			} else {
				if (term != LhcWorld.getTerm() || next != term){
					doTip(null, "不是本期,无法购买.");
					return;
				}
			}
//			======修改结果======
			
//			if(term > LhcWorld.dayTerm){
//				Date date = new Date();
//				if (date.getHours() > 9){
//					term = 1;
//				}
//			}
//			 //========新增。如果时间小于1分钟，则不可购买========
//			 if((LhcWorld.getNextTimeLeft() - 60000) / 60000 < 1){
//			 doTip(null, "请等待下期投注");
//			 return;
//			 }
//			 //========完毕========

			synchronized (loginUser.getLock()) {
				// UserStatusBean us =
				// UserInfoUtil.getUserStatus(loginUser.getId());
				// if (us == null) {
				// doTip(null, null);
				// return;
				// }
				// if (us.getGamePoint() < money) {
				// doTip(null, "您没有足够的钱购买六时彩.");
				// return;
				// }

				long needMoney = money * 100000000l;

				StoreBean store = BankCacheUtil.getBankStoreCache(loginUser
						.getId());

				if (store == null) {
					doTip(null, "您没有足够的乐币购买六时彩.");
					return;
				}
				if (store.getMoney() < needMoney) {
					doTip(null, "您没有足够的乐币购买六时彩.");
					return;
				}

				// // 六时彩期号
				// int maxLhcId = SqlUtil.getIntResult("select max(id) from
				// lhc_result",
				// Constants.DBShortName);
				// if (maxLhcId < 0) {
				// maxLhcId = 0;
				// }

				int lhcId = LhcWorld.getSSCBean().getId() + 1;
				
				int termTotal = SqlUtil.getIntResult("select sum(money)/100000000 from lhc_wager_record where user_id=" + loginUser.getId() + " and lhc_id=" + lhcId, 5);	// 本期下注数量，如果超过上限不能再买
				if(termTotal >= MAX_SUM_MONEY) {
					doTip(null, "根据防沉迷设定,你的本期下注已经达到上限,请下期再来");
					return;
				}
				
				// 获取用户历史下注记录
				LhcWagerRecordBean lhcWagetRecord = getLhcService()
						.getLhcWagerRecord(
								"user_id=" + loginUser.getId() + " and lhc_id="
										+ lhcId + " and type=" + type
										+ " and num=" + num);
				if (lhcWagetRecord == null) {
					// 初始化用户购买记录
					lhcWagetRecord = new LhcWagerRecordBean();
					lhcWagetRecord.setLhcId(lhcId);
					lhcWagetRecord.setUserId(loginUser.getId());
					lhcWagetRecord.setMoney(needMoney);
					lhcWagetRecord.setType(type);
					lhcWagetRecord.setNum(num);
					// ========以下新增========
					// lhcWagetRecord.setLhcDate(lhcDate);
					lhcWagetRecord.setTerm(term);
					// ========新增完毕========
					// 增加用户购买记录
					getLhcService().addLhcWagerRecord(lhcWagetRecord);

				} else {
					// long totalMoney = lhcWagetRecord.getMoney() + money;
					// if (totalMoney > MAX_GAME_POINT) {
					// doTip(null, "累计下注金额大于20亿,您不能再下注该分类.");
					// return;
					// }
					getLhcService().updateLhcWagerRecord(
							"money=money+" + needMoney,
							"id=" + lhcWagetRecord.getId());
				}
				// 更新用户存款 扣币
				BankCacheUtil.updateBankStoreCacheById((-1) * needMoney,
						loginUser.getId(), 0, BANK_SSC_TYPE);
				UserInfoUtil.getMoneyStat()[BANK_SSC_TYPE] -= needMoney;

				// // 更新用户乐币
				// UserInfoUtil.updateUserStatus("game_point=game_point-" +
				// money,
				// "user_id=" + loginUser.getId(), loginUser.getId(),
				// UserCashAction.OTHERS, "购买六时彩扣除乐币" + money);
			}
			doTip("success", "购买成功");
		} else {
			doTip("success", "超时无法购买,请购买下期");
		}
	}

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：用户兑奖
	 * @datetime:2007-8-24 13:52:06
	 * @return void
	 */
	public void exchange() {
		// 获取期号参数
		int lhcId = StringUtil.toInt(request.getParameter("lhcId"));
		if (lhcId <= 0) {
			doTip(null, null);
			return;
		}
		// //获取当前最大期号
		// int maxLhcId = SqlUtil.getIntResult("select max(id) from lhc_result",
		// Constants.DBShortName);
		// if (maxLhcId < 0) {
		// maxLhcId = 0;
		// }
		// //比较获取是否为当前最大期
		// if(maxLhcId!=lhcId){
		// doTip(null,null);
		// return;
		// }
		LhcResultBean lhcResult = getLhcService().getLhcResult("id=" + lhcId);
		if (lhcResult == null) {
			doTip(null, "该期等待开奖中!");
			return;
		}
		Set set = LhcWorld.getLhcSet(lhcResult.getId());
		if (set == null) {
			doTip(null, "该期等待开奖中!");
			return;
		}
		request.setAttribute("lhcResult", lhcResult);
		synchronized (loginUser.getLock()) {
			double totalMoney = 0;
			List lhcWagerRecordList = getLhcService().getLhcWagerRecordList(
					"lhc_id=" + lhcResult.getId() + " and user_id="
							+ loginUser.getId() + " and mark=0");
			if (lhcWagerRecordList.size() == 0) {
				doTip(null, "您没有下注或已兑完奖.");
				return;
			}
			boolean flag = false;
			Iterator it = lhcWagerRecordList.iterator();
			while (it.hasNext()) {
				LhcWagerRecordBean lhcWagerRecord = (LhcWagerRecordBean) it
						.next();
				int type = lhcWagerRecord.getType();
				int number = lhcWagerRecord.getNum();
				long money = lhcWagerRecord.getMoney();
				long prize = 0;
				if (type <= 7) {// 花式赔率
					// 下注类型
					String key = String.valueOf(type) + "-"
							+ String.valueOf(number);
					// 判断是否中奖
					if (set.contains(key)) {
						// 获取游戏乐币
						if(type == LhcWorld.LIU_XIAO && number == 10)
							prize = (long) (9 * money);
						else
							prize = (long) (LhcWorld.LHC_RATE[type] * money);
						totalMoney = totalMoney + prize;
						// 更新下注状态
						getLhcService().updateLhcWagerRecord("mark=1,prize=" + prize,
								"id=" + lhcWagerRecord.getId());
						// 中奖标志
						flag = true;
					} else {
						// 更新下注状态
						getLhcService().updateLhcWagerRecord("mark=2",
								"id=" + lhcWagerRecord.getId());
					}
				} else if (type == 9) {// 特码赔率
					if (lhcResult.checkTeMaNum(lhcWagerRecord.getNum())) {
						
						if (lhcWagerRecord.getNum() == 49) {
							// 中49号特码特殊赔率
							prize = (long) (LhcWorld.LHC_RATE[type + 1] * money);
						} else {
							prize = (long) (LhcWorld.LHC_RATE[type] * money);
						}
						totalMoney = totalMoney + prize;
						flag = true;
						// 更新下注状态
						getLhcService().updateLhcWagerRecord("mark=1,prize=" + prize,
								"id=" + lhcWagerRecord.getId());
					} else {
						getLhcService().updateLhcWagerRecord("mark=2",
								"id=" + lhcWagerRecord.getId());
					}
				} else if (type == 8) {// 平码赔率
					if (lhcResult.checkPingMaNum(lhcWagerRecord.getNum())) {
						prize = (long) (LhcWorld.LHC_RATE[type] * money);
						totalMoney = totalMoney + prize;
						getLhcService().updateLhcWagerRecord("mark=1,prize=" + prize,
								"id=" + lhcWagerRecord.getId());
						flag = true;
					} else {
						getLhcService().updateLhcWagerRecord("mark=2",
								"id=" + lhcWagerRecord.getId());
					}
				}
			}

			if (totalMoney > 0) {
//				IMessageService messageService = ServiceFactory
//						.createMessageService();
//				MessageBean message = new MessageBean();
//				message.setFromUserId(100);
//				message.setToUserId(loginUser.getId());
//				message.setContent("恭喜您中奖了，获得了"
//						+ (int) (totalMoney / 100000000) + "."
//						+ (int) (totalMoney % 100000000 / 10000000)
//						+ "亿乐币！（奖金已打入您的银行账户)");
//				message.setMark(0);
//				messageService.addMessage(message);
				BankCacheUtil.updateBankStoreCacheById((long) totalMoney,
						loginUser.getId(), 0, BANK_SSC_TYPE);
				UserInfoUtil.getMoneyStat()[BANK_SSC_TYPE] += totalMoney;
				request.setAttribute("totalMoney", String
						.valueOf((long) totalMoney));
			}
			request.setAttribute("flag", new Boolean(flag));
		}
		doTip("success", null);
		return;

	}

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：购买列表
	 * @datetime:2007-8-27 17:56:56
	 * @return void
	 */
	public void buyList() {
		// 获取期号参数
		// int lhcId =StringUtil.toInt(request.getParameter("lhcId"));
		// if(lhcId<=0){
		// doTip(null,null);
		// return;
		// }
		// LhcResultBean lhcResult = getLhcService().getLhcResult("id="+lhcId);
		// if(lhcResult==null){
		// doTip(null,null);
		// return;
		// }
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		// int totalCount =
		// getLhcService().getLhcWagerRecordCount("lhc_id="+lhcId+" and
		// user_id="+loginUser.getId()+" order by id desc");
		int totalCount = getLhcService().getLhcWagerRecordCount(
				"user_id=" + loginUser.getId() + " order by id desc");
		int totalPageCount = totalCount / PUBLIC_NUMBER_PER_PAGE;
		if (totalCount % PUBLIC_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// String prefixUrl = "buyList.jsp?lhcId="+lhcId;
		String prefixUrl = "buyList.jsp";

		int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;
		int end = PUBLIC_NUMBER_PER_PAGE;

		// 开始查询
		// List lhcWagerRecordList
		// =getLhcService().getLhcWagerRecordList("lhc_id="+lhcId+" and
		// user_id="+loginUser.getId()+" order by id desc limit " + start
		// + ", " + end);
		List lhcWagerRecordList = getLhcService().getLhcWagerRecordList(
				"user_id=" + loginUser.getId() + " order by id desc limit "
						+ start + ", " + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("lhcWagerRecordList", lhcWagerRecordList);
		// request.setAttribute("lhcResult",lhcResult);
		doTip("success", null);
		return;
	}

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：六时彩开奖历史记录
	 * @datetime:2007-8-28 14:06:15
	 * @return void
	 */
	public void lhcHistory() {
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		PUBLIC_NUMBER_PER_PAGE = 4;
		int totalCount = getLhcService().getLhcResultCount(" 1");
		int totalPageCount = totalCount / PUBLIC_NUMBER_PER_PAGE;
		if (totalCount % PUBLIC_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		String prefixUrl = "lhcHistory.jsp";

		int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;
		int end = PUBLIC_NUMBER_PER_PAGE;

		List lhcResultList = getLhcService().getLhcResultList(
				"1 order by id desc limit " + start + ", " + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("lhcResultList", lhcResultList);
		doTip("success", null);
		return;
	}
}
