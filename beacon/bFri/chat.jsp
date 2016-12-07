<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = StringUtil.toId(request.getParameter("uid"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="和主人对话"><p><%=BaseAction.getTop(request, response)%>
和主人对话：<br/>
<input name="chat"/><br/>
<anchor title="post">发送<go href="<%=("doChat.jsp?roomId=0&amp;uid=" + uid)%>" method="post">
<postfield name="content" value="$chat"/>
</go></anchor> <a href="myInfo.jsp">返回朋友买卖</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>