<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.RoomCacheUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean" %><%@ page import="java.util.HashMap" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
  </head> 
  <body>
<%
String roomId=request.getParameter("roomId");
HashMap roomContentMap=RoomCacheUtil.getRoomContentCountMap();
if(roomContentMap!=null){
Vector roomContentList=(Vector)roomContentMap.get(new Integer(roomId));
int roomContentId=0;
JCRoomContentBean roomContent=null;
if(roomContentList!=null){
%>
<h1><%=roomContentList.size()%></h1><BR/>
<%
for(int i=0;i<roomContentList.size();i++){
roomContentId=((Integer)roomContentList.get(i)).intValue();
roomContent=RoomCacheUtil.getRoomContent1(roomContentId);
%>
<%=i+1+"----->"%><%=roomContentId%>
<%if(roomContent!=null){%>
<%=roomContent.getContent()%>
<%if(!roomContent.getAttach().equals("")){%>
有图片
<%}%>
<%}%><BR/>
<%}}else{%>没有数据<%}}else{%>没有数据<%}%>
  </body>
</html>
