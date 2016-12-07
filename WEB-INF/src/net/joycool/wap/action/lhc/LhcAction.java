package net.joycool.wap.action.lhc;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class LhcAction extends CustomAction {

	public static String LHC_NAME = "六合彩";

	private UserBean loginUser = null;

	static LhcService service =  new LhcService();

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
	 * @author macq
	 * @explain：六合彩首页
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
		LhcResultBean lhcResult = new LhcService().getLhcResult("id="
				+ lhcId);
		if (lhcResult == null) {
			return sb.toString();
		}
		for (int i = 1; i <= 7; i++) {
			String name = LhcWorld.LHC_NAME_ARRAY[i][LhcWorld.LHC_RESULT_ARRAY[lhcResult.getNum7()][i]];
			sb.append(name);
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户购买六合彩页面
	 * @datetime:2007-8-23 16:30:44
	 * @return void
	 */
	public void buy() {
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type <= 0 || type > 9) {
			type = 1;
		}
		request.setAttribute("type", String.valueOf(type));
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：用户购买六合彩结果页面
	 * @datetime:2007-8-23 16:31:12
	 * @return void
	 */
	public void buyResult() {
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		if (currentHour > 18 && currentHour < 21) {
			doTip(null, "六合彩在晚19:00-21:00之间不可以购买!");
			return;
		}
		// 验证是否为表单提交
		if (!request.getMethod().equalsIgnoreCase("post")) {
			doTip(null, null);
			return;
		}
		// 购买类型
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type <= 0) {
			doTip(null, null);
			return;
		}
		// 购买号码
		int num = StringUtil.toInt(request.getParameter("num"));
		if ((num <= 0 || num > 49) && type > 7) {
			doTip(null, "竞猜数字范围为1-49之间的数字!");
			return;
		}
		//判断除平码特码以外的数字
		if(num<0){
			doTip(null, null);
			return;
		}
		// 购买金额
		int money = StringUtil.toInt(request.getParameter("money"));
		if (money < 10000 || money > MAX_GAME_POINT) {
			doTip(null, "下注金额范围:10000-20亿乐币.");
			return;
		}
		synchronized(loginUser.getLock()) {
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if (us == null) {
				doTip(null, null);
				return;
			}
			if (us.getGamePoint() < money) {
				doTip(null, "您没有足够的钱购买六合彩.");
				return;
			}
			// 六合彩期号
			int maxLhcId = SqlUtil.getIntResult("select max(id) from lhc_result",
					Constants.DBShortName);
			if (maxLhcId < 0) {
				maxLhcId = 0;
			}
			int lhcId = maxLhcId + 1;
			// 获取用户历史下注记录
			LhcWagerRecordBean lhcWagetRecord = getLhcService().getLhcWagerRecord(
					"user_id=" + loginUser.getId() + " and lhc_id=" + lhcId
							+ " and type=" + type + " and num=" + num);
			if (lhcWagetRecord == null) {
				// 初始化用户购买记录
				lhcWagetRecord = new LhcWagerRecordBean();
				lhcWagetRecord.setLhcId(lhcId);
				lhcWagetRecord.setUserId(loginUser.getId());
				lhcWagetRecord.setMoney(money);
				lhcWagetRecord.setType(type);
				lhcWagetRecord.setNum(num);
				// 增加用户购买记录
				getLhcService().addLhcWagerRecord(lhcWagetRecord);
	
			} else {
				long totalMoney = lhcWagetRecord.getMoney() + money;
				if (totalMoney > MAX_GAME_POINT) {
					doTip(null, "累计下注金额大于20亿,您不能再下注该分类.");
					return;
				}
				getLhcService().updateLhcWagerRecord("money=money+" + money,
						"id=" + lhcWagetRecord.getId());
			}
			// 更新用户乐币
			UserInfoUtil.updateUserStatus("game_point=game_point-" + money,
					"user_id=" + loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "购买六合彩扣除乐币" + money);
		}
		doTip("success", "购买成功");
	}

	/**
	 * 
	 * @author macq
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
				if (type <= 7) {// 花式赔率
					// 下注类型
					String key = String.valueOf(type) + "-"
							+ String.valueOf(number);
					// 判断是否中奖
					if (set.contains(key)) {
						// 获取游戏乐币
						totalMoney = totalMoney + (double)LhcWorld.LHC_RATE[type]
								* (double)money;
						// 更新下注状态
						getLhcService().updateLhcWagerRecord("mark=1",
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
							totalMoney = totalMoney
									+ (double)LhcWorld.LHC_RATE[type + 1] * (double)money;
						} else {
							totalMoney = totalMoney + (double)LhcWorld.LHC_RATE[type]
									* (double)money;
						}
						flag = true;
						// 更新下注状态
						getLhcService().updateLhcWagerRecord("mark=1",
								"id=" + lhcWagerRecord.getId());
					} else {
						getLhcService().updateLhcWagerRecord("mark=2",
								"id=" + lhcWagerRecord.getId());
					}
				}// else if(type==8){//平码赔率
				// if(lhcResult.checkPingMaNum(lhcWagerRecord.getNum())){
				// totalMoney = totalMoney + (double)LhcWorld.LHC_RATE[type] * (double)money;
				// getLhcService().updateLhcWagerRecord("mark=1",
				// "id=" + lhcWagerRecord.getId());
				// flag = true;
				// }else{
				// getLhcService().updateLhcWagerRecord("mark=2",
				// "id=" + lhcWagerRecord.getId());
				// }
				// }

			}

			if (totalMoney > 0) {
				 IMessageService messageService =ServiceFactory.createMessageService();
				 MessageBean message = new MessageBean();
				 message.setFromUserId(100);
				 message.setToUserId(loginUser.getId());
				 message.setContent("恭喜您中奖了，获得了"+(long)totalMoney+"乐币！（奖金已打入您的银行账户)");
				 message.setMark(0);
				 messageService.addMessage(message);
				 BankCacheUtil.updateBankStoreCacheById((long) totalMoney,
						loginUser.getId(),0,Constants.BANK_LHC_TYPE);
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
	 * @author macq
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
	 * @author macq
	 * @explain：六合彩开奖历史记录
	 * @datetime:2007-8-28 14:06:15
	 * @return void
	 */
	public void lhcHistory() {
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		PUBLIC_NUMBER_PER_PAGE = 2;
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
