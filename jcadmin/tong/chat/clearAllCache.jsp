<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.world.ChatWorld" %><%@ page import="net.joycool.wap.framework.BaseAction" %><%response.setHeader("Cache-Control", "no-cache");			
if (session.getAttribute("adminLogin") == null) {
BaseAction.sendRedirect("/jcadmin/login.jsp", response);
return;
	}
//清除所有缓存
String user=request.getParameter("user");
String password = request.getParameter("password");
if("cache".equals(user) && "abc321".equals(password)){
    ChatWorld.flushAll();
}
%>
清除成功.<br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>
<a href="chatInfo.jsp">返回聊天管理首页</a><br/>