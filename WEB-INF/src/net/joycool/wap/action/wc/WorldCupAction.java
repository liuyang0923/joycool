/*
 * Created on 2006-6-9
 *
 */
package net.joycool.wap.action.wc;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wc.WcAnswerBean;
import net.joycool.wap.bean.wc.WcAnswerRecordBean;
import net.joycool.wap.bean.wc.WcQuestionBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IWorldCupService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class WorldCupAction extends CustomAction{
	public UserBean loginUser;

	public IWorldCupService wcService;

	public HttpServletRequest request;

	public WorldCupAction(HttpServletRequest request) {
		this.request = request;
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		wcService = ServiceFactory.createWorldCupService();
	}

	/**
	 * 首页
	 */
	public void index() {
		String condition = "id > 0 and result = 0 order by id desc";
		Vector questionList = wcService.getQuestionList(condition);
		request.setAttribute("questionList", questionList);
	}

	public WcQuestionBean getQuestion(String questionId) {
		WcQuestionBean question = null;
		question = wcService.getQuestion("id=" + questionId);
		// String condition="question_id="+questionId;
		// Vector answerList=wcService.getAnswerList(condition);
		// request.setAttribute("answerList",answerList);
		return question;
	}

	/**
	 * 场次页。
	 */
	public void question() {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			return;
		}
		WcQuestionBean question = wcService.getQuestion("id = " + id);
		if (question == null) {
			return;
		}
		Vector answerList = wcService.getAnswerList("question_id = " + id
				+ " order by id");

		request.setAttribute("question", question);
		request.setAttribute("answerList", answerList);
	}

	public UserStatusBean getUserStatus(int userId) {
		IUserService service = ServiceFactory.createUserService();
		//fanys2006-08-11
		UserStatusBean status = UserInfoUtil.getUserStatus( userId);
//		UserStatusBean status = service.getUserStatus("user_id = " + userId);
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			service.addUserStatus(status);
			// add by mcq 2006-07-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER,10000,
					Constants.PLUS,userId);
			// add by mcq 2006-07-24  for stat user money history end
			return status;
		}
	}

	public void answer() {
		int questionId = StringUtil.toInt(request.getParameter("questionId"));
		int answerId = StringUtil.toInt(request.getParameter("answerId"));
		int wager = StringUtil.toInt(request.getParameter("wager"));
		WcQuestionBean question = wcService.getQuestion("id = " + questionId);
		WcAnswerBean answer = wcService.getAnswer("id = " + answerId);
		//fanys2006-08-11
		UserStatusBean status = UserInfoUtil.getUserStatus( loginUser.getId());
//		UserStatusBean status = getUserStatus(loginUser.getId());

		IUserService service = ServiceFactory.createUserService();

		String tip = null;
		boolean flag = true;
		if (wager < 0) {
			tip = "下注额度不对！";
			flag = false;
		} else if (question == null || answer == null) {
			tip = "未知错误！";
			flag = false;
		} else if (wager > status.getGamePoint()) {
			tip = "你的乐币数不够！";
			flag = false;
		} else if (question.getResult() != 0
				|| !checkDatetime(question.getEndDatetime())) {
			tip = "比赛已经开始，不能再下注！";
			flag = false;
		}

		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", tip);
		} else {
			WcAnswerRecordBean record = null;
			String condition = "question_id = " + questionId
					+ " and answer_id = " + answerId + " and user_id = "
					+ loginUser.getId();
			record = wcService.getAnswerRecord(condition);
			if (record == null) {
				record = new WcAnswerRecordBean();
				record.setAnswerId(answerId);
				record.setQuestionId(questionId);
				record.setWager(wager);
				record.setUserId(loginUser.getId());
				wcService.addAnswerRecord(record);
			} else {
				wcService.updateAnswerRecord("wager = wager + " + wager,
						"id = " + record.getId());
			}
			// mcq_2006-7-25_用户增减乐币记录_start
			MoneyAction.addMoneyFlowRecord(Constants.OTHER,wager,Constants.SUBTRATION,loginUser.getId());
			// mcq_2006-7-25_用户增减乐币记录_end
			//fanys2006-08-11 start
			UserInfoUtil.updateUserStatus("game_point = game_point -" + wager,"user_id = " + loginUser.getId(),loginUser.getId(), UserCashAction.WAGER,"世界杯下注"+wager);
//			service.updateUserStatus("game_point = game_point - " + wager,
//					"user_id = " + loginUser.getId());
			// MCQ_WC_更改用户session中的乐币数 时间:2006-6-11
			// mcq_1_添加用户更改过状态信息
//			UserInfoUtil.addUserInfo(record.getUserId());
			// 首先判断用户乐币是否更改过
//			loginUser = UserInfoUtil.getUserInfo(loginUser);
			//fanys2006-08-11 end
			// mcq_end

			request.setAttribute("result", "success");
		}
	}

	public boolean checkDatetime(String dateTime) {
		String[] dd = dateTime.split(" ");
		int month = StringUtil.toInt(dd[0].split("-")[0]);
		int date = StringUtil.toInt(dd[0].split("-")[1]);
		int hour = StringUtil.toInt(dd[1].split(":")[0]);
		int minute = StringUtil.toInt(dd[1].split(":")[1]);

		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.MONTH) + 1 > month) {
			return false;
		}
		if (cal.get(Calendar.MONTH) + 1 < month) {
			return true;
		}
		if (cal.get(Calendar.DATE) > date) {
			return false;
		}
		if (cal.get(Calendar.DATE) < date) {
			return true;
		}
		if (cal.get(Calendar.HOUR_OF_DAY) > hour) {
			return false;
		}
		if (cal.get(Calendar.HOUR_OF_DAY) < hour) {
			return true;
		}
		if (cal.get(Calendar.MINUTE) > minute) {
			return false;
		}
		if (cal.get(Calendar.MINUTE) < minute) {
			return true;
		}
		return true;
	}

	/**
	 * 产生结果
	 * 
	 * @param questionId
	 * @param answerId
	 */
	public void result(int questionId, int answerId) {
		WcQuestionBean question = wcService.getQuestion("id = " + questionId);
		WcAnswerBean answer = wcService.getAnswer("id = " + answerId);
		if (question == null || answer == null) {
			return;
		}

		IUserService userService = ServiceFactory.createUserService();
		INoticeService noticeService = ServiceFactory.createNoticeService();

		wcService.updateQuestion("result = " + answerId, "id = " + questionId);
		String condition = "answer_id = " + answerId;
		Vector recordList = wcService.getAnswerRecordList("answer_id = "
				+ answerId);
		Iterator itr = recordList.iterator();
		WcAnswerRecordBean record = null;
		int wager = 0;
		while (itr.hasNext()) {
			record = (WcAnswerRecordBean) itr.next();
			// mcq_2006-7-25_用户增减乐币记录_start
			MoneyAction.addMoneyFlowRecord(Constants.OTHER,(int) (record.getWager() * answer.getMoney()),Constants.PLUS,record.getUserId());
			// mcq_2006-7-25_用户增减乐币记录_end
			//fanys2006-08-11 start
			UserInfoUtil.updateUserStatus("game_point = game_point + "
					+ (int) (record.getWager() * answer.getMoney()),
					"user_id = " + record.getUserId(),record.getUserId(), UserCashAction.WAGER,"世界杯挣钱"+(int) (record.getWager() * answer.getMoney())+"乐币");
//			userService.updateUserStatus("game_point = game_point + "
//					+ (int) (record.getWager() * answer.getMoney()),
//					"user_id = " + record.getUserId());
			wager = (int) (record.getWager() * answer.getMoney());
			// mcq_wc_添加中奖用户更改过状态信息 时间:2006-6-11
//			UserInfoUtil.addUserInfo(record.getUserId());
			//fanys2006-08-11 end
			// mcq_end
			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle("您在世界杯博采中赢得" + wager + "乐币");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(record.getUserId());
			notice.setHideUrl("");
			notice.setContent("比赛:" + question.getTitle() + "***" + "结果:"
					+ answer.getTitle() + "***" + "赔率:1:" + answer.getMoney()
					+ "***" + "你的下注:" + record.getWager() + "乐币");
			noticeService.addNotice(notice);
		}
	}
	//fanys-2006-06-15 start
	/**
	 * 删除一个博彩场次
	 * @param questionId
	 */
	public void deleteQuestion(int questionId) {
		String strWhere = " id=" + questionId;
		wcService.deleteAnswer(" question_id=" + questionId);
		wcService.deleteQuestion(strWhere);

	}
    /**
     * 添加一个博彩场次
     *
     */
	public void addQuestion() {
		WcQuestionBean question = new WcQuestionBean();
		String starttime = getParameterInt("datetime") + " "
				+ getParameterInt("hour") + ":"
				+ getParameterInt("minute")+":00";
		question.setEndDatetime(starttime);
		question.setTitle(request.getParameter("title"));
		question.setResult(0);
		wcService.addQuestion(question);
	}
	/**
	 * 修改一个博彩场次
	 *
	 */
	public void updateQuestion() {
		int questionId = getParameterInt("id");
		String title = request.getParameter("title");
		String starttime = getParameterInt("datetime") + " "
				+ getParameterInt("hour") + ":"
				+ getParameterInt("minute")+":00";

		wcService.updateQuestion("title='" + StringUtil.toSql(title) + "',end_datetime='"
				+ starttime + "'", "id=" + questionId);
	}

	

	/**
	 * 获取一个博彩场次的结果
	 * 
	 * @param questionId
	 * @return
	 */
	public Vector getAnswerList(String questionId) {
		String condition = "question_id=" + questionId;
		Vector answerList = wcService.getAnswerList(condition);
		return answerList;
	}

	/**
	 * 获取一个结果
	 * 
	 * @param answerId
	 * @return
	 */
	public WcAnswerBean getAnswer(String answerId) {
		String condition = "id=" + answerId;
		WcAnswerBean answer = wcService.getAnswer(condition);
		return answer;
	}

	/**
	 * 
	 * 增加一个结果
	 */
	public void addAnswer() {
		String title = request.getParameter("title");
		float money = Float.parseFloat(request.getParameter("money"));
		WcAnswerBean answer = new WcAnswerBean();
		int questionId = Integer.parseInt(request.getParameter("id"));
		answer.setTitle(title);
		answer.setMoney(money);
		answer.setQuestionId(questionId);
		wcService.addAnswer(answer);
	}

	/**
	 * 更新一个结果
	 * 
	 */
	public void updateAnswer() {
		String answerId = request.getParameter("answerId");
		String title = request.getParameter("title");
		float money = Float.parseFloat(request.getParameter("money"));
		wcService.updateAnswer("title='" + StringUtil.toSql(title) + "',money=" + money, "id="
				+ answerId);
	}
	/**
	 * 删除一个结果
	 * @param answerId
	 */
	public void deleteAnswer(String answerId) {
		wcService.deleteAnswer("id=" + answerId);

	}
	//fanys 2006-06-15 end

}
