package net.joycool.wap.action.beginner;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.beginner.BeginnerHelpBean;
import net.joycool.wap.bean.beginner.BeginnerQuestionBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBeginnerService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class BeginnerQuestionAction extends CustomAction{

	UserBean loginUser;
	
	private int NUMBER_PER_PAGE = 5;
	
	int winPoint = 1000;
 
	//int losePoint = 1000;

	IBeginnerService beginnerService = null;
	
	HttpSession session = null;
	
	public BeginnerQuestionAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		session = request.getSession();
	}
	
	public IBeginnerService getBeginnerService() {
		if (beginnerService == null) {
			beginnerService = ServiceFactory.createBeginnerService();
		}
		return beginnerService;
	}

	// 获取在线管理员和热心用户清单
	public void index(HttpServletRequest request) {
		Vector beginnerHelpList = getBeginnerService().getBeginnerHelpList("1=1");
		Vector beginnerHelpOnlineList= new Vector();
		BeginnerHelpBean beginnerHelp = null;
		for (int i = 0; i < beginnerHelpList.size(); i++) {
			beginnerHelp=(BeginnerHelpBean)beginnerHelpList.get(i);
			if(OnlineUtil.isOnline(beginnerHelp.getUserId() + "")){
				beginnerHelpOnlineList.add(beginnerHelp.getUserId()+"");
			}
		}int totalCount = beginnerHelpOnlineList.size();
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
		// 取得要显示的消息列表x
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, beginnerHelpOnlineList.size());
		int endIndex = Math.min(start + end, beginnerHelpOnlineList.size());
		List onlineList = beginnerHelpOnlineList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("beginnerHelpOnlineList", onlineList);
		return;
	}
	
	// 随机取得一道问答题目
	public void question(HttpServletRequest request) {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser.getId());
		if(userStatus.getRank()>=3){
			request.setAttribute("tip", "您已经不是新手了!不用回答这些问题了,谢谢合作!");
			request.setAttribute("result", "failure");
			return;
		}
		String answerId =(String)session.getAttribute("beginnerAnswer");
		if(answerId==null){
			answerId="0";
		}
		int id=StringUtil.toInt(answerId);
		Vector BeginnerQuestionList=getBeginnerService().getBeginnerQuestionList("1=1");
		 if(id>=BeginnerQuestionList.size()){
			 session.removeAttribute("beginnerAnswer");
			 request.setAttribute("result", "answerOver");
			 return;
		 }
		 BeginnerQuestionBean beginnerQuestion=(BeginnerQuestionBean)BeginnerQuestionList.get(id);
		request.setAttribute("beginnerQuestion", beginnerQuestion);
		//防止刷新
		session.setAttribute("beginnerAnswerCheck", "success");
		request.setAttribute("result", "success");
		return;
	}

	// 判断选择的答案是否与标准答案一致,并更改数据库
	public void result(HttpServletRequest request, HttpServletResponse response) {
		//从session中读取打过的题目
		String answerId =(String)session.getAttribute("beginnerAnswer");
		if(answerId==null){
			answerId="0";
		}
		int answe=StringUtil.toInt(answerId);
		//设置下一题目
		session.setAttribute("beginnerAnswer",(answe+1)+"");
		// 取得参数
		int id = getParameterInt("id");
		int rs1 = getParameterInt("rs");
		// 通过参数得到一条题目记录
		BeginnerQuestionBean beginnerQuestion = getBeginnerService().getBeginnerQuestion("id=" + id);
		// 判断是否得到题目
		if (beginnerQuestion != null) {
			// 判断标准答案是否和用户选择答案一致,如果一致加分
			if (beginnerQuestion.getResult() == rs1) {
				// 更新用户状态
				boolean flag = UserInfoUtil.updateUserCash(loginUser.getId(), winPoint, UserCashAction.GAME, "用户答对题给用户加100乐币");
				// 判断是否更新成功
				if (flag) {
					//乐酷现金流日志
					MoneyAction.addMoneyFlowRecord(Constants.JOB, winPoint,
							Constants.PLUS, loginUser.getId());
					session.setAttribute("beginnerAnswerResult","回答正确,加1000乐币!");
				} else {
					request.setAttribute("tip", "参数错误");
					request.setAttribute("result", "failure");
					return;
				}
				// 断标准答案是否和用户选择答案一致,如果不一致扣分
			} else if (beginnerQuestion.getResult() != rs1) {
				//用户答错了!什么都不做
				session.setAttribute("beginnerAnswerResult","回答错误哦!看一下题目吧");
				// // 取得用户状态
				// status = this.getUserStatus(loginUser.getId());
				// // 更新用户状态
				// boolean flag = UserInfoUtil.updateUserStatus("game_point="
				// + (status.getGamePoint() - losePoint), "user_id="
				// + loginUser.getId(), loginUser.getId(), UserCashAction.GAME,
				// "用户答错题用户扣10乐币");
				// // zhul_modify_us 2006-08-14 end
				// // 判断是否更新成功
				// if (flag) {
				// // add by zhangyi 2006-07-24 for stat user money history
				// // start
				// MoneyAction.addMoneyFlowRecord(Constants.JOB, losePoint,
				// Constants.SUBTRATION, loginUser.getId());
				// // add by zhangyi 2006-07-24 for stat user money history end
				//
				//					request.setAttribute("error", "error");
				//				}
			} else {
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "failure");
				return;
			}

		}
		// 没有得到题目
		else {
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "failure");
			return;
		}
		request.setAttribute("result", "success");
		return;
	}

	// 判断登陆用户在user_status表中的状态
	public UserStatusBean getUserStatus(int userId) {
		IUserService service = ServiceFactory.createUserService();
		// UserStatusBean status = service.getUserStatus("user_id = " + userId);
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			service.addUserStatus(status);
			// add by zhangyi 2006-07-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER, 10000,
					Constants.PLUS, userId);
			// add by zhangyi 2006-07-24 for stat user money history end
			return status;
		}
	}
}
