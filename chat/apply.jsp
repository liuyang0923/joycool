<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
action.apply(request,response);
int roomId=action.getParameterInt("roomId");
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申请进入聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result.equals("success")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%}else if(result.equals("failure")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%}%>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>