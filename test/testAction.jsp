<%@page  import="java.sql.ResultSet"%>
<%@page  import="java.sql.SQLException"%>
<%@page  import="java.util.Calendar"%>
<%@page  import="java.util.Date"%>
<%@page  import="java.util.Vector"%>
<%@page  import="net.joycool.wap.action.user.RankAction"%>
<%@page  import="net.joycool.wap.bean.NoticeBean"%>
<%@page  import="net.joycool.wap.bean.UserBean"%>
<%@page  import="net.joycool.wap.framework.BaseAction"%>
<%@page  import="net.joycool.wap.util.Constants"%>
<%@page  import="net.joycool.wap.util.NoticeUtil"%>
<%@page  import="net.joycool.wap.util.db.DbOperation"%>
<%@page  import="net.joycool.wap.test.TestServiceImpl"%>
<%@page  import="net.joycool.wap.test.RecordBean"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.test.QuestionBean" %><%@ page import="net.joycool.wap.test.AnswerBean" %><%!

/**
 * fanys 2006-07-06
 * 
 * @author Administrator
 * 
 */
 public class TestAction extends BaseAction {
		UserBean loginUser;

		TestServiceImpl testService;
        HttpSession session= null;
		public TestAction() {

		}

		public TestAction(HttpServletRequest request) {
			loginUser = (UserBean) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			testService = new TestServiceImpl();
            session = request.getSession();
		}

		public String getAnswer() {
			return null;
		}

		public String getQuestion() {

			return null;
		}

		/**
		 * 
		 * 获取问题和问题的选项
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
		 * 
		 * 获取问题答案的选项
		 * @param questionId
		 * @return
		 */
		public AnswerBean getAnswer(int answerId) {
			String strWhere = " id=" + answerId;
			AnswerBean answer = testService.getAnswer(strWhere);
			return answer;
		}


		/**
		 * 保存用户回答
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
					if (answer.indexOf(";") > 0) {
						answers = answer.split(";");
						for (int j = 0; j < answers.length; j++) {
							addRecord(i, Integer.parseInt(answers[j]));
						}
					} else {
						addRecord(i, Integer.parseInt(answer));
					}
				}
			}
		}

		/**
		 * 保存回答
		 * @param questionId
		 * @param answerId
		 */
		public void addRecord(int questionId, int answerId) {
			RecordBean record = new RecordBean();
			record.setUserId(loginUser.getId());
			record.setQuestionId(questionId);
			record.setAnswerId(answerId);
            int markQuestion = StringUtil.toInt((String)session.getAttribute("markQuestion"));
            if(markQuestion<0)
            markQuestion=0;
			record.setMark(markQuestion);
			testService.addRecord(record);
		}

		/**
		 * 
		 * 是否填写过调查问卷
		 * @return
		 */
		public boolean isTested() {
			RecordBean record = testService.getRecord("user_id="
					+ loginUser.getId() + " limit 0,1 ");
			if (record == null)
				return false;
			return true;
		}
//是否调查问卷结束
		public boolean isTestFinished() {
			int MAX_COUNT = 1200;
			if (getUserCount() >= MAX_COUNT)
				return true;
			return false;
		}

		/**
		 * 
		 * 获取用户列表
		 * @return
		 */
		public int getUserCount() {
			int count = 0;
			//String strsql = "select  count(distinct user_id)  from  jc_test_record  where user_id not in(select user_id from jc_test_record where question_id=1 and answer_id=1 and mark=1) and mark=1 ";
			int mark = StringUtil.toInt((String)session.getAttribute("markQuestion"));
            if(mark<0)
            mark=0;
			//String strsql = "select  count(distinct user_id)  from  jc_test_record  where mark= "+mark;
			String strsql = "select  count(distinct user_id)  from  jc_test_record ";
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
		 * 
		 * 
		 * @param userId
		 */
		public void addUserPoint(int point) {
			RankAction.addPoint(loginUser, point);
			NoticeBean notice = new NoticeBean();
			notice.setUserId(loginUser.getId());
			notice.setTitle("");
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
		
		public Vector getUserIds(){
			Vector vec=new Vector();
			int i=0;
			String strsql = "select  distinct user_id  from  jc_test_record  where user_id not in(select user_id from jc_test_record where question_id=1 and answer_id=1 and mark=1) and mark=1";
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			ResultSet rs = dbOp.executeQuery(strsql);
			try {
				while (rs.next()) {
					vec.add(rs.getInt(1)+"");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
			return vec;
		}
		public int isAwarded(Vector vecUsers ,int userId){
			int tempUserId=0;
			if(vecUsers==null||vecUsers.size()==0)
				return -1;
			for(int i=0;i<vecUsers.size();i++){
				try{
					tempUserId=Integer.parseInt((String)vecUsers.get(i));
					if(tempUserId==userId)
						return (i+1);
				}catch(Exception e){
					
				}
			}
			return -1;
		}
		public int isAwarded(){
			return isAwarded(getUserIds(),loginUser.getId());
		}
		public int getUserMaxRecordId(){
			int recordId=0;
			RecordBean record=null;
			record=testService.getRecord(" user_id="+loginUser.getId()+" order by id desc limit 0,1");
			if(record!=null)
				recordId=record.getId();
			return recordId;
		}
	}
%>
