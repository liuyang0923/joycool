<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%
	response.setHeader("Cache-Control","no-cache");
	int uid = StringUtil.toId(request.getParameter("uid"));
	
	
	if(uid == 0) {
		response.sendRedirect("/");
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="邀请"><p>
<%=BaseAction.getTop(request, response)%>
<input name="chat"/><br/>
<anchor title="post">发送<go href="<%=("doC.jsp?roomId=0&amp;uid=" + uid)%>" method="post">
<postfield name="content" value="$chat"/>
</go></anchor><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>