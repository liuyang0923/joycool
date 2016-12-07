<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.world.*" %><%response.setHeader("Cache-Control", "no-cache");			
if (session.getAttribute("adminLogin") == null) {
		BaseAction.sendRedirect("/jcadmin/login.jsp", response);
		return;
	}
String key = request.getParameter("key");
if(key!=null){
	ChatWorld.flushGroup(ChatWorldBean.TONG_CHAT,key);
}

//response.sendRedirect("/jcadmin/cache/cacheAdmin.jsp");
BaseAction.sendRedirect("/jcadmin/tong/chat/chatInfo.jsp", response);
%>