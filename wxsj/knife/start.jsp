<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
	return;
}

session.setAttribute("correctCount", "0");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="勇士爱军刀" ontimer="<%=response.encodeURL("/wxsj/knife/ask.jsp?questionId=1")%>">
<timer value="1"/>
<p align="left">
载入中...
</p>
</card>
</wml>