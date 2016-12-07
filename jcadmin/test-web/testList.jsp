<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.wxsj.util.*"%><%@ page import="java.util.ArrayList"%><%
response.setHeader("Cache-Control","no-cache");

WebTestAction action = new WebTestAction();
action.allTestList(request, response);

%>
<html>
<head>
<title>调查问卷</title>
</head>
<body>
<p align="left">
调查问卷首页<br/>
--------------<br/>
<%
ArrayList testList = (ArrayList) request.getAttribute("testList");
TestBean test = null;
for(int i = 0; i < testList.size(); i ++){
	test = (TestBean) testList.get(i);
%>

<a href="<%=StringUtil.dealLink(request.getContextPath()+"/jcadmin/test-web/viewTestRecord.jsp?id="+test.getId(), request, response)%>"><%=StringUtil.toWml(test.getTitle())%></a>&nbsp;<%if(test.getIsClosed() == 1){%>已结束&nbsp;<a href="setTest.jsp?testId=<%=test.getId()%>">开启</a><%}else{%>进行中&nbsp;<a href="setTest.jsp?testId=<%=test.getId()%>">关闭</a><%}%><br/>
<%
}
%>
</body>
</html>