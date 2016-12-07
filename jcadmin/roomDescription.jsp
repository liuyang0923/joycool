<%--mcq add 2006-07-5--%>
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.IChatService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%
response.setHeader("Cache-Control","no-cache");
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
String description = request.getParameter("description");
String roomId = request.getParameter("id");
if(description==null || description.trim().equals("") || roomId==null || StringUtil.toInt(roomId)<0){
	response.sendRedirect("roomList.jsp");
	return;
}
IChatService chat = ServiceFactory.createChatService();
chat.updateJCRoom("description='"+description+"'","id="+roomId);
	response.sendRedirect("roomList.jsp");
	return;
%>