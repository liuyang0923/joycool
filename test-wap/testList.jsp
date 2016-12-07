<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.wxsj.action.test.*"%><%@ page import="net.wxsj.bean.test.*"%><%@ page import="net.wxsj.util.*"%><%@ page import="java.util.ArrayList"%><%
response.setHeader("Cache-Control","no-cache");

TestAction action = new TestAction();
action.testList(request, response);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<card title="调查问卷">
<p align="left">
调查问卷首页<br/>
--------------<br/>
<%
ArrayList testList = (ArrayList) request.getAttribute("testList");
TestBean test = null;
for(int i = 0; i < testList.size(); i ++){
	test = (TestBean) testList.get(i);
%>

<a href="<%=(request.getContextPath()+"/test-wap/testMain.jsp?id="+test.getId())%>"><%=StringUtil.toWml(test.getTitle())%></a><br/>
<%
}
%>

</p>
</card>
</wml>