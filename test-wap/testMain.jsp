<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.wxsj.util.*"%><%@ page import="java.util.ArrayList,java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");

TestAction action = new TestAction();
int testId = StringUtil.toInt(request.getParameter("id"));

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	//response.sendRedirect(("/user/login.jsp?backTo=" + URLEncoder.encode("/test-wap/testMain.jsp?id=" + testId)));
	BaseAction.sendRedirect("/user/login.jsp?backTo=" + URLEncoder.encode("/test-wap/testMain.jsp?id=" + testId), response);
	return;
}
action.getTestById(request, response);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<%
//成功
if("success".equals(result)){
	TestBean test = (TestBean) request.getAttribute("testBean");
%>
<card title="调查问卷">
<p align="left">
<%=StringUtil.toWml(test.getTitle())%><br/>
--------------<br/>

<%=StringUtil.toWml(test.getIntroduction())%><br/>
<a href="<%=(request.getContextPath()+"/test-wap/testAnswer.jsp?testId=" + test.getId())%>">开始回答<%=StringUtil.toWml(test.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
else{
	String tip = (String) request.getAttribute("tip");
%>
<card title="调查问卷" >
<p align="left">
调查问卷<br/>
--------------<br/>
<%=tip%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>

</wml>