<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");

TestAction action=new TestAction(request);
if(action.isTested()==false)
	action.saveRecord(request);
else{
	//response.sendRedirect(("infoTested.jsp"));
	BaseAction.sendRedirect("/test/infoTested.jsp", response);
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人用户问卷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
谢谢参与！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>