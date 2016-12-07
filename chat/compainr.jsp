<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
int roomId=action.getParameterInt("roomId");
if(session.getAttribute("compain") == null){
	//response.sendRedirect(("/chat/hall.jsp?roomId="+roomId));
	BaseAction.sendRedirect("/chat/hall.jsp?roomId="+roomId, response);
	return;
}
session.removeAttribute("compain");

action.compainr(request);
String result=(String)request.getAttribute("result");
String backTo=request.getParameter("backTo");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="投诉" ontimer="<%=response.encodeURL("/chat/hall.jsp?roomId="+roomId)%>">
<timer value="100"/>
<p align="left">
<%if(result.equals("success")){%>
投诉完成!您可以安心继续聊天啦!<br/><br/>
<%}else if (result.equals("failure")){
String tip=(String)request.getAttribute("tip");
%><%=tip%><br/><br/><%}%>
<a href="hall.jsp?roomId=<%=roomId%>">进入聊天大厅</a><br/>     
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="http://wap.joycool.net">返回乐酷门户</a>
</p>
</card>
</wml>
