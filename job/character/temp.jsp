<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
if(request.getParameter("username")!=null){
	request.getSession().setAttribute("characterName",request.getParameter("username"));
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="诸葛神推" ontimer="<%=response.encodeURL("result.jsp")%>">
<timer value="30"/>
<p align="left" >

<%=BaseAction.getTop(request, response)%>
正在科学的推算中，请稍等……(3秒钟跳转)<br/>
<a href="<%=("result.jsp") %>">直接进入</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>