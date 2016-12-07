package net.joycool.wap.action.charitarian;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.charitarian.CharitarianBean;
import net.joycool.wap.bean.charitarian.CharitarianHistoryBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.util.CharitarianCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class CharitarianAction extends CustomAction{
	UserBean loginUser;
	private int PAGE_COUNT = 5;

	// liuyi 2006-12-26 慈善基金修改 start
	public static int CHARITARIAN_MONEY = Constants.CHARITARIAN_USER_MONEY;

	// liuyi 2006-12-26 慈善基金修改 end

	private ICharitarianService charitarianService = null;

	public CharitarianAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}

	public ICharitarianService getCharitarianService() {
		if (charitarianService == null) {
			charitarianService = ServiceFactory.createCharitarianService();
		}
		return charitarianService;
	}

	/**
	 * 首页
	 */
	public void index(HttpServletRequest request) {
		if (loginUser != null){
			long money = 0;
			int count = 0;
			// 获取捐献记录
			CharitarianBean charitarian = CharitarianCacheUtil
					.getCharitarianCahceById(loginUser.getId());
			if (charitarian != null) {
				money = charitarian.getMoney();
			}
			// 获取领过捐献者所捐乐币的总人数
			count = getCharitarianService().getCharitarianHistoryCacheCount(
					"select COUNT(id) c_id from"
							+ " jc_charitarian_history where charitarian_id="
							+ loginUser.getId());
			request.setAttribute("money", money + "");
			request.setAttribute("count", count + "");
			session.setAttribute("charitarianCommit", "true");
			return;
		}
	}

	/**
	 * 捐款结果页面
	 */
	public void result(HttpServletRequest request) {
		String checkCommit = (String) session.getAttribute("charitarianCommit");
		if (checkCommit == null) {
			request.setAttribute("tip", "页面刷新错误！");
			request.setAttribute("result", "failure");
			return;
		}
		session.removeAttribute("charitarianCommit");
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count <= 0 || count > 200000) {
			request.setAttribute("tip", "请确认捐赠数量!");
			request.setAttribute("result", "failure");
			return;
		}
		int money = count * CHARITARIAN_MONEY;
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		// 判断用户是否有足够的乐币
		if (userStatus.getGamePoint() < money) {
			request.setAttribute("tip", "你没有足够的乐币捐助,请确认!");
			request.setAttribute("result", "failure");
			return;
		}
		// 扣除用户捐款金额
		userStatus.setGamePoint(userStatus.getGamePoint() - money);
		userStatus.setCharitarian(userStatus.getCharitarian() + count);
		UserInfoUtil.service.updateUserStatus("game_point=game_point-" + money
				+ ",charitarian=charitarian+" + count, "user_id=" + loginUser.getId());
		// 获取用户是否捐过款
		CharitarianBean charitarian = CharitarianCacheUtil
				.getCharitarianCahceById(loginUser.getId());
		// 如果没有捐款则插入记录
		if (charitarian == null) {
			charitarian = new CharitarianBean();
			charitarian.setUserId(loginUser.getId());
			charitarian.setMoney(money);
			charitarian.setCount(count);
			getCharitarianService().addCharitarian(charitarian);
		} else {// 更新捐款记录
			CharitarianCacheUtil.updateBankStoreCahceById("money=money+"
					+ money + ",count=count+" + count, "user_id="
					+ loginUser.getId(), loginUser.getId());
		}
		request.setAttribute("money", money + "");
		request.setAttribute("count", count + "");
		request.setAttribute("result", "success");
		return;
	}

	/**
	 * 捐款历史记录页面
	 */
	public void history(HttpServletRequest request) {
		long money = 0;
		int count = 0;
		// 获取捐献记录
		CharitarianBean charitarian = CharitarianCacheUtil
				.getCharitarianCahceById(loginUser.getId());
		if (charitarian != null) {
			money = charitarian.getMoney();
		}
		// 获取领过捐献者所捐乐币的总人数
		count = getCharitarianService().getCharitarianHistoryCacheCount(
				"select COUNT(id) c_id from"
						+ " jc_charitarian_history where charitarian_id="
						+ loginUser.getId());
		// 传值到页面
		request.setAttribute("money", money + "");
		request.setAttribute("count", count + "");
		// 初始化参数
		String sql = null;
		// 接收页面参数
		String order = request.getParameter("order");
		// 获取捐助人捐助过的对象ID列表sql
		if (order == null) {
			order = "online";
		}
		if (order.equals("online")) {
			sql = "select DISTINCT(receive_id) as char_id from jc_charitarian_history where charitarian_id="
					+ loginUser.getId();
		} else {
			sql = "select DISTINCT(a.receive_id) as char_id from jc_charitarian_history a "
					+ "left JOIN user_status as b on a.receive_id=b.user_id where a.charitarian_id="
					+ loginUser.getId() + " order by b.rank desc";
		}
		// 查询出的排序记录
		Vector CharitarianList = getCharitarianService()
				.getCharitarianHistoryCacheList(sql);
		// 初始化
		Vector userList = new Vector();
		Integer userId = null;
		if (order.equals("online")) {
			// 初始化
			Vector noOnlineList = new Vector();
			// 循环获取每个得到捐助的用户id
			for (int i = 0; i < CharitarianList.size(); i++) {
				userId = (Integer) CharitarianList.get(i);
				// 判断用户是否在线,如果在线,加入在线vector
				if (OnlineUtil.isOnline(userId.toString())) {
					userList.add(userId);
				} else {
					// 不在线加入离线Vector
					noOnlineList.add(userId);
				}
			}
			// 循环不在线用户的id列表,并把用户id加入到在线用户列表后面
			for (int i = 0; i < noOnlineList.size(); i++) {
				userId = (Integer) noOnlineList.get(i);
				userList.add(userId);
			}
		} else {
			// 循环获取每个得到捐助的用户id
			for (int i = 0; i < CharitarianList.size(); i++) {
				userId = (Integer) CharitarianList.get(i);
				userList.add(userId);
			}
		}
		int totalCount = userList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / PAGE_COUNT;
		if (totalCount % PAGE_COUNT != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "history.jsp?order=" + order;
		// 取得要显示的消息列表x
		int start = pageIndex * PAGE_COUNT;
		int end = PAGE_COUNT;
		int startIndex = Math.min(start, userList.size());
		int endIndex = Math.min(start + end, userList.size());
		List userIdList = userList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userIdList", userIdList);
		request.setAttribute("order", order);

		// 接收页面参数
		String order1 = request.getParameter("order1");
		// 获取捐助人捐助过的对象ID列表sql
		if (order1 == null) {
			order1 = "online";
		}
		if (order1.equals("online")) {
			sql = "select DISTINCT(charitarian_id) as char_id from jc_charitarian_history where receive_id="
					+ loginUser.getId();
		} else {
			sql = "select DISTINCT(a.charitarian_id) as char_id from jc_charitarian_history a "
					+ "left JOIN user_status as b on a.charitarian_id=b.user_id where"
					+ " a.receive_id="
					+ loginUser.getId()
					+ " order by b.rank desc";
		}
		// 查询出的排序记录
		Vector CharitarianList1 = getCharitarianService()
				.getCharitarianHistoryCacheList(sql);
		// 初始化
		Vector userList1 = new Vector();
		Integer userId1 = null;
		if (order1.equals("online")) {
			// 初始化
			Vector noOnlineList1 = new Vector();
			// 循环获取每个得到捐助的用户id
			for (int i = 0; i < CharitarianList1.size(); i++) {
				userId1 = (Integer) CharitarianList1.get(i);
				// 判断用户是否在线,如果在线,加入在线vector
				if (OnlineUtil.isOnline(userId1.toString())) {
					userList1.add(userId1);
				} else {
					// 不在线加入离线Vector
					noOnlineList1.add(userId1);
				}
			}
			// 循环不在线用户的id列表,并把用户id加入到在线用户列表后面
			for (int i = 0; i < noOnlineList1.size(); i++) {
				userId1 = (Integer) noOnlineList1.get(i);
				userList1.add(userId1);
			}
		} else {
			// 循环获取每个得到捐助的用户id
			for (int i = 0; i < CharitarianList1.size(); i++) {
				userId1 = (Integer) CharitarianList1.get(i);
				userList1.add(userId1);
			}
		}
		int totalCount1 = userList1.size();
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 == -1) {
			pageIndex1 = 0;
		}
		int totalPageCount1 = totalCount1 / PAGE_COUNT;
		if (totalCount1 % PAGE_COUNT != 0) {
			totalPageCount1++;
		}
		if (pageIndex1 > totalPageCount1 - 1) {
			pageIndex1 = totalPageCount1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		String prefixUrl1 = "history.jsp?order1=" + order1;
		// 取得要显示的消息列表x
		int start1 = pageIndex1 * PAGE_COUNT;
		int end1 = PAGE_COUNT;
		int startIndex1 = Math.min(start1, userList1.size());
		int endIndex1 = Math.min(start1 + end1, userList1.size());
		List userIdList1 = userList1.subList(startIndex1, endIndex1);
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("userIdList1", userIdList1);
		request.setAttribute("order1", order1);
		// 获取捐献记录人数
		int count1 = getCharitarianService().getCharitarianHistoryCacheCount(
				"select COUNT(id) c_id from "
						+ "jc_charitarian_history where receive_id="
						+ loginUser.getId());
		// 传值到页面
		request.setAttribute("count1", count1 + "");
		session.setAttribute("charitarianCommit", "true");
		return;
	}

	/**
	 * 首页
	 */
	public void notice(HttpServletRequest request) {
		// 获取捐献记录
		CharitarianHistoryBean charitarianHistory = getCharitarianService()
				.getCharitarianHistory(
						"receive_id=" + loginUser.getId()
								+ " order by id desc limit 0,1");
		request.setAttribute("charitarianHistory", charitarianHistory);
		return;
	}

	/*
	 * 数字分页
	 * 
	 * @param totalPageCount @param currentPageIndex @param prefixUrl @param
	 * addAnd @param separator @param pageIndex @param response @return
	 */
	public String shuzifenye(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			String pageIndex, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();

		if (addAnd) {
			prefixUrl += "&amp;" + pageIndex + "=";
		} else {
			prefixUrl += "?" + pageIndex + "=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

}
