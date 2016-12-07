<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.search(request);
String result=(String)request.getAttribute("result");
JCRoomBean room=(JCRoomBean)request.getAttribute("room");
Vector roomList=(Vector)request.getAttribute("roomList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//没有查询到结果
if("failure".equals(result)){
%>
<card title="聊天室查询">
<p align="left">
<%=BaseAction.getTop(request, response)%>
聊天室查询<br/>
-------------------<br/>
<%=request.getAttribute("tip")%><br/>
<br/>
<a href="/chat/roomList.jsp">返回建筑群</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {%>
<card title="聊天室查询">
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
<%if(room!=null){%>
<a href="/chat/hall.jsp?roomId=<%=room.getId()%>"><%=room.getName()%></a>(<%=room.getCurrentOnlineCount()%>人在线)<br/>
<%}%><%else if(roomList.size()>0){
for(int i = 0; i < roomList.size(); i ++){
	room = (JCRoomBean)roomList.get(i);
%>
<%=i+1%><a href="/chat/hall.jsp?roomId=<%=room.getId()%>"><%=room.getName()%></a>(<%=room.getCurrentOnlineCount()%>人在线)<br/>
<%}}%>
<a href="/chat/roomList.jsp">返回建筑群</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>