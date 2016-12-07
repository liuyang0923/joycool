/*
 * Created on 2006-10-26
 *
 */

package net.wxsj.action.test;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wxsj.bean.test.TestAnswerBean;
import net.wxsj.bean.test.TestBean;
import net.wxsj.bean.test.TestPageBean;
import net.wxsj.bean.test.TestQuestionBean;
import net.wxsj.bean.test.TestRecordBean;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.INewTestService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：张毅
 * 
 * 创建日期：2007-1-25
 * 
 * 说明：
 */
public class TestAction {

	/**
	 * 
	 * 作者：张毅
	 * 
	 * 创建日期：2007-1-25
	 * 
	 * 说明：得到问卷列表
	 * 
	 * 参数及返回值说明：
	 * 
	 * @param request
	 * @param response
	 */
	public void testList(HttpServletRequest request,
			HttpServletResponse response) {

		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService();
		ArrayList testList = testService.getTestList("is_closed = 0", 0, -1,
				"create_datetime desc");

		request.setAttribute("testList", testList);
	}

	/**
	 * 
	 * 作者：张陶
	 * 
	 * 创建日期：2007-1-26
	 * 
	 * 说明：得到所有的调查问卷，包括已经关闭的。
	 * 
	 * 参数及返回值说明：
	 * 
	 * @param request
	 * @param response
	 */
	public void allTestList(HttpServletRequest request,
			HttpServletResponse response) {

		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService();
		ArrayList testList = testService.getTestList(null, 0, -1,
				"create_datetime desc");

		request.setAttribute("testList", testList);
	}

	/**
	 * 
	 * 作者：张陶
	 * 
	 * 创建日期：2007-1-26
	 * 
	 * 说明：根据问卷的ID，获取用户填写的调查信息
	 * 
	 * 参数及返回值说明：
	 * 
	 * @param request
	 * @param response
	 */
	public void getRecordByTestId(HttpServletRequest request,
			HttpServletResponse response) {
		String testId = request.getParameter("testId");
		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService();
		String condition = "test_id = " + testId;
		ArrayList recordList = testService.getTestRecordList(condition, 0, -1,
				" user_id asc, question_code asc ");
		request.setAttribute("recordList", recordList);
	}

	/**
	 * 
	 * 作者：张毅
	 * 
	 * 创建日期：2007-1-25
	 * 
	 * 说明：得到问卷
	 * 
	 * 参数及返回值说明：
	 * 
	 * @param request
	 * @param response
	 */
	public void getTestById(HttpServletRequest request,
			HttpServletResponse response) {
		// 问卷id
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "参数不正确!");
			return;
		}

		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService();
		TestBean testBean = testService.getTest("id = " + id);

		request.setAttribute("result", "success");
		request.setAttribute("testBean", testBean);

	}

	public int getUserTestState(HttpServletRequest request) {
		int reslut = 0;
		// 问卷id
		int testId = StringUtil.toInt(request.getParameter("testId"));
		if (testId == -1) {
			testId = 1;
		}

		// 创建service，整个service共用一个数据库连接
		INewTestService testService = ServiceFactory.createNewTestService(
				IBaseService.CONN_IN_METHOD, null);

		// 获的当前问卷
		TestBean testBean = testService.getTest("id = " + testId);
		int isClosed = testBean.getIsClosed();
		if (isClosed == 1) {
			// 问卷已结束
			testService.releaseAll();
			return 1;
		}

		int userId = testService.getLoginUserId(request);
		int userAnswerCount = testService.getTestRecordCount("test_id = "
				+ testId + " and user_id = " + userId);
		if (userAnswerCount > 0) {
			// 您已经回答完了问卷
			testService.releaseAll();
			return 2;
		}
		testService.releaseAll();
		return reslut;
	}

	public void selectTestQuestionsOfPage(HttpServletRequest request,
			HttpServletResponse response) {
		int testId = StringUtil.toInt(request.getParameter("testId"));
		if (testId == -1) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "参数不正确或数据不合规则!");
			return;
		}
		int pageId = StringUtil.toInt(request.getParameter("pageId"));
		if (pageId == -1) {
			pageId = 1;
		}

		HttpSession session = request.getSession();

		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService(
				IBaseService.CONN_IN_METHOD, null);

		TestBean testBean = testService.getTest("id = " + testId);

		// 验证是否可以答题
		int result = getUserTestState(request);
		if (result == 1) {
			// 问卷已结束
			testService.releaseAll();
			request.setAttribute("result", "overCanNot");
			request.setAttribute("testBean", testBean);
			return;
		}
		if (result == 2) {
			// 您已经回答完了问卷
			testService.releaseAll();
			request.setAttribute("result", "overDone");
			request.setAttribute("testBean", testBean);
			return;
		}

		// 提问
		if ("get".equalsIgnoreCase(request.getMethod())) {
			TestPageBean testPageBean = testService.getTestPage("test_id = "
					+ testId + " and code = " + pageId);

			// 查找问卷当前页中所有试题
			String condition = "test_id = " + testId + " and page_code="
					+ pageId;
			ArrayList questionList = testService.getTestQuestionList(condition,
					0, -1, "code");

			TestQuestionBean testQuestion;
			int questionId;
			ArrayList questionAnswerList;

			// 查找问卷当前页中所有试题的答案
			for (int i = 0; i < questionList.size(); i++) {
				testQuestion = (TestQuestionBean) questionList.get(i);
				questionId = testQuestion.getCode();
				condition = "test_id = " + testId + " and question_code="
						+ questionId;
				// 试题所有答案列表
				questionAnswerList = testService.getTestAnswerList(condition,
						0, -1, "code");
				testQuestion.setQuestionAnswerList(questionAnswerList);
				questionList.set(i, testQuestion);
			}

			request.setAttribute("questionList", questionList);
			request.setAttribute("result", "enterPage");
			request.setAttribute("testBean", testBean);
			request.setAttribute("testPageBean", testPageBean);
			request.setAttribute("pageId", pageId + "");
			return;
		}
		// 回答
		else if ("post".equalsIgnoreCase(request.getMethod())) {
			// 查找问卷当前页中所有试题
			String condition = "test_id = " + testId + " and page_code="
					+ pageId;
			ArrayList questionList = testService.getTestQuestionList(condition,
					0, -1, null);
			// 检查答案
			Iterator itr = questionList.iterator();
			TestQuestionBean testQuestion;
			TestAnswerBean testAnswer;
			String answerIdStr = null;
			String[] answerIdsStr = null;
			// 下一页的ID
			int nextPageId = 0;
			while (itr.hasNext()) {
				testQuestion = (TestQuestionBean) itr.next();
				answerIdStr = request.getParameter("question"
						+ testQuestion.getCode());
				// 如果有问题没有回答
				if (answerIdStr == null || answerIdStr.equals("")) {
					// 到定时跳转页
					request.setAttribute("nextPageId", new Integer(pageId));
					session.setAttribute("tip", "请选择第" + testQuestion.getCode()
							+ "题的答案!");
					request.setAttribute("result", "timerJump");
					request.setAttribute("testBean", testBean);
					testService.releaseAll();
					return;
				}
				// 否则
				else {
					answerIdsStr = answerIdStr.split(";");
					for (int i = 0; i < answerIdsStr.length; i++) {
						testAnswer = testService.getTestAnswer("test_id = "
								+ testId + " and question_code = "
								+ testQuestion.getCode() + " and code = "
								+ answerIdsStr[i]);
						nextPageId = testAnswer.getNextPageCode();
					}

					// 保存临时结果
					Hashtable hat = (Hashtable) session.getAttribute("newTest"
							+ testId);
					if (hat == null) {
						hat = new Hashtable();
					}
					TestRecordBean testRecord = new TestRecordBean();
					testRecord.setTestId(testId);
					testRecord.setQuestionCode(testQuestion.getCode());
					testRecord.setAnswerCode(answerIdStr);

					hat.put(testQuestion.getCode() + "", testRecord);
					session.setAttribute("newTest" + testId, hat);
				}
			}

			session.removeAttribute("tip");
			// 求下一页
			if (nextPageId == 0) {
				nextPageId = getNextPageId(testId, pageId);
			}

			// 到定时跳转页
			request.setAttribute("result", "timerJump");
			request.setAttribute("nextPageId", new Integer(nextPageId));
			request.setAttribute("testBean", testBean);
			testService.releaseAll();
		}
	}

	public void saveTest(HttpServletRequest request,
			HttpServletResponse response) {
		
		int testId = StringUtil.toInt(request.getParameter("testId"));
		if (testId == -1) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "参数不正确或数据不合规则!");
			return;
		}
		
		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService(
				IBaseService.CONN_IN_METHOD, null);
		
		// 获的当前问卷
		TestBean testBean = testService.getTest("id = " + testId);
		
		HttpSession session = request.getSession();
		if (session.getAttribute("newTest" + testBean.getId()) == null ){
			testService.releaseAll();
			request.setAttribute("result", "failure");
			request.setAttribute("testBean", testBean);
			session.setAttribute("tip", "参数不正确或数据不合规则!");
			return;
			
		}
		
		Hashtable hat = (Hashtable)session.getAttribute("newTest" + testBean.getId());
		
		Enumeration enu = hat.keys();
		TestRecordBean testRecord;
		
		while (enu.hasMoreElements()) {
			
			testRecord = (TestRecordBean)hat.get(enu.nextElement());
			testRecord.setUserId(testService.getLoginUserId(request));
			testRecord.setCreateDatetime(DateUtil.getNow());
			
            if (!testService.addTestRecord(testRecord)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "添加失败!");
                testService.releaseAll();
                return;
            }
		}
		
		session.removeAttribute("newTest" + testBean.getId());
		request.setAttribute("result", "success");
		request.setAttribute("testBean", testBean);
		testService.releaseAll();
	}
	
	private int getNextPageId(int testId, int currentPageId) {
		int nextPageId;
		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService();
		int maxPageId = testService.getTestPageCount("test_id = " + testId);
		if (currentPageId == maxPageId) {
			// 答完了问卷
			nextPageId = 0;
		} else if (currentPageId > maxPageId) {
			// 非法操作或数据不合规则
			nextPageId = -1;
		} else {
			nextPageId = currentPageId + 1;
		}

		return nextPageId;
	}
}
