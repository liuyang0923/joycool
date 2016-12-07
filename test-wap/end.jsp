<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.wxsj.util.*,net.wxsj.util.db.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*,java.util.*,net.joycool.wap.bean.dummy.DummyProductBean"%><%
response.setHeader("Cache-Control","no-cache");

int testId = StringUtil.toInt(request.getParameter("testId"));

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	//response.sendRedirect(("/user/login.jsp?backTo=" + "/test-wap/testMain.jsp?id=" + testId));
	BaseAction.sendRedirect("/user/login.jsp?backTo=" + "/test-wap/testMain.jsp?id=" + testId, response);
	return;
}

TestAction action = new TestAction();
//action.saveTest(request, response);
//放到jsp实现
        int state = action.getUserTestState(request);
		if(state != 0){
			//response.sendRedirect(("http://wap.joycool.net/wapIndex.jsp"));
			BaseAction.sendRedirect(null, response);
			return;
		}
		if (testId == -1) {
			//response.sendRedirect(("http://wap.joycool.net/wapIndex.jsp"));
			BaseAction.sendRedirect(null, response);
			return;
		}
		
		// 创建service
		INewTestService testService = ServiceFactory.createNewTestService(
				IBaseService.CONN_IN_METHOD, null);
		
		// 获的当前问卷
		TestBean testBean = testService.getTest("id = " + testId);
				
		if (session.getAttribute("newTest" + testBean.getId()) == null ){
			testService.releaseAll();
			//response.sendRedirect(("http://wap.joycool.net/wapIndex.jsp"));
			BaseAction.sendRedirect(null, response);
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
                testService.releaseAll();
			    //response.sendRedirect(("http://wap.joycool.net/wapIndex.jsp"));
			    BaseAction.sendRedirect(null, response);
			    return;
            }
		}
		
		session.removeAttribute("newTest" + testBean.getId());
		String result = "success";
		testService.releaseAll();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<%
TestBean test = testBean;
//猎人项目流程
if(testId == 5){
	int resultType = 0;
    enu = hat.keys();	
	while (enu.hasMoreElements()) {			
		testRecord = (TestRecordBean)hat.get(enu.nextElement());
		if(testRecord.getQuestionCode() == 1 && "1".equals(testRecord.getAnswerCode())){
			//加上400点经验值
		    int [] sts = new int[2];
	        sts[0] = JoycoolInfc.GAME_POINT;
	        sts[1] = JoycoolInfc.POINT;
	        int [] svs = {0, 400};
	        JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);
		    resultType = 1;
		}
		else if(testRecord.getQuestionCode() == 1 && "2".equals(testRecord.getAnswerCode())){
			//加上200点经验值
		    int [] sts = new int[2];
	        sts[0] = JoycoolInfc.GAME_POINT;
	        sts[1] = JoycoolInfc.POINT;
	        int [] svs = {0, 200};
	        JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);
		    resultType = 2;
		}
	}
    
	//成功
    if(resultType == 1){
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
感谢您的支持，400点经验值已经给你加上！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
    else if(resultType == 2){
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
感谢您的支持，200点经验值已经给你加上！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
   else if(resultType == 0){
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
您回答完了整个问卷，感谢您的支持！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
}
//猎人项目流程
else if(testId == 6){
	int resultType = 0;
    enu = hat.keys();	
	while (enu.hasMoreElements()) {			
		testRecord = (TestRecordBean)hat.get(enu.nextElement());
		if(testRecord.getQuestionCode() == 2 && ("1".equals(testRecord.getAnswerCode()) || "2".equals(testRecord.getAnswerCode()) || "3".equals(testRecord.getAnswerCode()))){
			//加上400点经验值
		    int [] sts = new int[2];
	        sts[0] = JoycoolInfc.GAME_POINT;
	        sts[1] = JoycoolInfc.POINT;
	        int [] svs = {0, 400};
	        JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);
		    resultType = 1;
		}
		else if(testRecord.getQuestionCode() == 2 && "4".equals(testRecord.getAnswerCode())){
			//加上200点经验值
		    int [] sts = new int[2];
	        sts[0] = JoycoolInfc.GAME_POINT;
	        sts[1] = JoycoolInfc.POINT;
	        int [] svs = {0, 80};
	        JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);
		    resultType = 2;
		}
	}
    
	//成功
    if(resultType == 1){
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
非常感谢您的参与！您已获得400点经验值！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
    else if(resultType == 2){
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
我们的调查似乎不是很适合您哦，不过非常感谢您的参与！您已获得80点经验值！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
   else if(resultType == 0){
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
您回答完了整个问卷，感谢您的支持！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
}
//云雨堂
else if(testId == 7){
	Random rand = new Random();
	int r = rand.nextInt(100);
	int money = 5;
	if(r < 70){
		money = 5;
	}
	else if(r >= 70 && r < 90){
		money = 10;
	}
	else if(r >= 90){
		money = 20;
	}

	String code = rand.nextInt(10) + "" + rand.nextInt(10) + "" + rand.nextInt(10) + "" + rand.nextInt(10) + "" + rand.nextInt(10);

	DbOperation dbOp = new DbOperation();
	dbOp.init();
	String sql = "insert into new_test_yyt(user_id, code, money) values(" + loginUser.getId() + ", '" + code + "', " + money + ")";
	dbOp.executeUpdate(sql);
	dbOp.release();
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
您回答完了整个问卷，感谢您的支持！<br/>
你得到一张<%=money%>元的代金券，使用密码是<%=code%>。请记住使用密码！使用代金券时请注意，1.一次只能用一张代金券2.代金券不能用来充抵邮费。<br/>
再次感谢您的支持！<br/>
<a href="http://wap.yytun.com">返回云雨堂</a>
</p>
</card>
<%
}
//网游
else if(testId == 8){
	DummyProductBean p = JoycoolInfc.addUserBag(loginUser.getId(), 107);
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
您回答完了整个问卷，感谢您的支持！<br/>
你得到一个<%=p.getName()%>作为奖励。<br/>
再次感谢您的支持！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
//其它
else{
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
您回答完了整个问卷，感谢您的支持！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>