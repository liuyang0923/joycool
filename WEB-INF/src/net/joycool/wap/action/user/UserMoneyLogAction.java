package net.joycool.wap.action.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.BankServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.StringUtil;

/** 
 * @author macq
 * @explain：
 * @datetime:2007-6-19 9:12:12
 */
public class UserMoneyLogAction extends CustomAction{
	private UserBean loginUser = null;
	
	static int NUMBER_PER_PAGE = 8;

	static UserServiceImpl userService = new UserServiceImpl();
	
	static BankServiceImpl bankService = new BankServiceImpl();
	
	public UserMoneyLogAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
	}
	
	
	public void getUserMoneyList(){
		int totalCount = userService.getMoneyLogCount("from_id=" + loginUser.getId());
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

		String prefixUrl = "userMoneyLog.jsp";

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		List userMoneyLogList = userService.getMoneyLogList("from_id=" + loginUser.getId() + " order by id desc limit " + start
						+ ", " + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userMoneyLogList", userMoneyLogList);
		
	}
	
	public void getUserMoneyReceiveList(){
		int totalCount = userService.getMoneyLogCount("to_id=" + loginUser.getId());
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

		String prefixUrl = "userMoneyReceiveLog.jsp";

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		List userMoneyLogList = userService.getMoneyLogList("to_id=" + loginUser.getId() + " order by id desc limit " + start
						+ ", " + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userMoneyLogList", userMoneyLogList);
		
	}
	public void getMoneyLogList(){
		List moneyLogList = bankService.getMoneyLogListCache(loginUser.getId());
		int totalCount = moneyLogList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		PagingBean page = new PagingBean(pageIndex, totalCount, NUMBER_PER_PAGE);
		request.setAttribute("page", page);

		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalCount);
		List list = moneyLogList.subList(start, end);
		request.setAttribute("MoneyLogList", list);
		
	}
	public void getMoneyLogList2(){
		List moneyLogList = bankService.getMoneyLogListCache2(loginUser.getId());
		int totalCount = moneyLogList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		PagingBean page = new PagingBean(pageIndex, totalCount, NUMBER_PER_PAGE);
		request.setAttribute("page", page);

		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalCount);
		List list = moneyLogList.subList(start, end);
		request.setAttribute("MoneyLogList", list);
		
	}
}
