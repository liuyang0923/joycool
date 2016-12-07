<%--mcq add 2006-07-5--%>
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.IChatService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%
response.setHeader("Cache-Control","no-cache");
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
String top = request.getParameter("top");
String roomId = request.getParameter("id");
if(top==null || StringUtil.toInt(top)<0 || roomId==null || StringUtil.toInt(roomId)<0){
	response.sendRedirect("roomList.jsp");
	return;
}
IChatService chat = ServiceFactory.createChatService();
chat.updateJCRoom("top="+top,"id="+roomId);
	response.sendRedirect("roomList.jsp");
	return;
%>