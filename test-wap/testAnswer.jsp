<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.wxsj.util.*"%><%@ page import="java.util.ArrayList,java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");

TestAction action = new TestAction();
int testId = StringUtil.toInt(request.getParameter("testId"));

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	//response.sendRedirect(("/user/login.jsp?backTo=" + "/test-wap/testMain.jsp?id=" + testId));
	BaseAction.sendRedirect("/user/login.jsp?backTo=" + "/test-wap/testMain.jsp?id=" + testId, response);
	return;
}

action.selectTestQuestionsOfPage(request, response);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<%
TestBean test = (TestBean) request.getAttribute("testBean");

//成功
if("enterPage".equals(result)){    
    TestPageBean testPage = (TestPageBean)request.getAttribute("testPageBean");
    String pageId = (String) request.getAttribute("pageId");
    ArrayList questionList = (ArrayList)request.getAttribute("questionList");

	String pageTitle = StringUtil.toWml(testPage.getTitle());
	if ("".equals(pageTitle)){
		pageTitle = StringUtil.toWml(test.getTitle());
	}
%>
<card title="<%=pageTitle%>">
<p align="left">
第<%=testPage.getCode()%>页<br/>
<%
String tip = (String) session.getAttribute("tip");
if(tip != null){
%>
<%=tip%><br/>
<%
}
%>
--------------<br/>

<%
TestQuestionBean question;
TestAnswerBean answer;
ArrayList questionAnswerList;
String selectName;
for(int i=0;i<questionList.size();i++){
	question = (TestQuestionBean)questionList.get(i);
	questionAnswerList = question.getQuestionAnswerList();
	selectName = "question"+question.getCode();
%>
<%=question.getCode()%>.<%=StringUtil.toWml(question.getQuestion())%><%
	if(question.getMultiple()==1){
%>
 （多选）
<%
	}
%><br/>
<select <% if(question.getMultiple()==1){%> multiple="true" <%}%> name="<%=selectName%>">
<option value="">请选择答案</option>
<%
	for(int j=0;j<questionAnswerList.size();j++){
 		answer = (TestAnswerBean)questionAnswerList.get(j);
%>
<option value="<%=answer.getCode()%>"><%=answer.getAnswer()%></option>
<%
	}
%>
</select><br/>
<%	
}
%>

<anchor title="下一页">下一页
    <go href="<%=(request.getContextPath()+"/test-wap/testAnswer.jsp")%>" method="post">
<%    

for(int i=0;i<questionList.size();i++){
	question = (TestQuestionBean)questionList.get(i);
	questionAnswerList = question.getQuestionAnswerList();
	selectName = "question"+question.getCode();
%>
<postfield name="<%=selectName%>" value="$<%=selectName%>"/>

<%	
}
%>
<postfield name="step" value="true"/>
<postfield name="testId" value="<%=test.getId()%>"/>
<postfield name="pageId" value="<%=pageId%>"/>
    </go>
</anchor>


</p>
</card>
<%
}

//转至下一页
else if("timerJump".equals(result)){
	int nextPageId = ((Integer) request.getAttribute("nextPageId")).intValue();
	String nextUrl = null;
	//答完题了，到结束页面
	if(nextPageId == 0 || nextPageId == 999){
		nextUrl = (request.getContextPath()+"/test-wap/end.jsp?testId="+testId);
	}
	else {
		nextUrl = (request.getContextPath()+"/test-wap/testAnswer.jsp?testId="+testId+"&pageId="+nextPageId+"&aa=aa").replace("&", "&amp;");
	}
%>
<card title="调查问卷" ontimer="<%=response.encodeURL(nextUrl)%>">
<timer value="1"/>
<p align="left">
载入中...
</p>
</card>
<%
}

else if("overDone".equals(result)){
%>
<card title="调查问卷" >
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
您已经回答完了<%=StringUtil.toWml(test.getTitle())%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}

else if("overCanNot".equals(result)){
%>
<card title="调查问卷" >
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>
<%=StringUtil.toWml(test.getTitle())%>已结束<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>