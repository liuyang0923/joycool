package net.joycool.wap.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * fanys 2006-07-06
 * 
 * @author Administrator
 * 
 */
public class TestAction extends BaseAction {
	UserBean loginUser;

	TestServiceImpl testService;

	public TestAction() {

	}

	public TestAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		testService = new TestServiceImpl();

	}

	public String getAnswer() {
		return null;
	}

	public String getQuestion() {

		return null;
	}

	/**
	 * 获取一个问题和该问题的所有选项
	 * 
	 * @param questionId
	 * @return
	 */
	public Vector getQuestionAndAnswer(int questionId) {
		Vector vec = new Vector();
		String strWhere = " question_id=" + questionId;
		vec.add(testService.getQuestion(strWhere));
		vec.add(testService.getAnswerList(strWhere));
		return vec;
	}

	/**
	 * 保存用户的所有回答
	 * 
	 * @param request
	 */
	public void saveRecord(HttpServletRequest request) {
		int questionCount = 0;
		questionCount = testService.getQuestionCount("1=1");
		String questionName = "question";
		String answer = null;
		RecordBean record = null;
		String[] answers = null;
		for (int i = 1; i <= questionCount; i++) {
			answer = (String) request.getSession().getAttribute(
					questionName + i);
			if (answer != null) {
				if (answer.indexOf(";") > 0) {// 多选题,如果选择多个答案时
					answers = answer.split(";");
					for (int j = 0; j < answers.length; j++) {
						addRecord(i, Integer.parseInt(answers[j]));
					}
				} else {// 单选题
					addRecord(i, Integer.parseInt(answer));
				}
			}
		}
	}

	/**
	 * 添加回答记录
	 * 
	 * @param questionId
	 * @param answerId
	 */
	public void addRecord(int questionId, int answerId) {
		RecordBean record = new RecordBean();
		record.setUserId(loginUser.getId());
		record.setQuestionId(questionId);
		record.setAnswerId(answerId);
		record.setMark(0);
		testService.addRecord(record);
	}

	/**
	 * 用户是否已经参加过调研问卷
	 * 
	 * @return
	 */
	public boolean isTested() {
		RecordBean record = testService.getRecord("user_id="
				+ loginUser.getId() + " limit 0,1 ");
		if (record == null)
			return false;
		return true;
	}

	public boolean isTestFinished() {
		int MAX_COUNT = 350;// 最多问卷调查份数
		if (getUserCount() >= MAX_COUNT)
			return true;
		return false;
	}

	/**
	 * 用户总数
	 * 
	 * @return
	 */
	public int getUserCount() {
		int count = 0;
		String strsql = "select  count(distinct user_id)  from  jc_test_record  where user_id not in(select user_id from jc_test_record where question_id=1 and answer_id=1)";
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		ResultSet rs = dbOp.executeQuery(strsql);
		try {
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return count;
	}

	/**
	 * 更新用户经验值
	 * 
	 * @param userId
	 */
	public void addUserPoint(int point) {
		RankAction.addPoint(loginUser, point);
		NoticeBean notice = new NoticeBean();
		notice.setUserId(loginUser.getId());
		notice.setTitle("谢谢参与,您获取了" + point + "点经验值！");
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/chat/hall.jsp");
		NoticeUtil.getNoticeService().addNotice(notice);

	}

	/**
	 * @return Returns the loginUser.
	 */
	public UserBean getLoginUser() {
		return loginUser;
	}

}
