<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.roomEnter(request);
RoomApplyBean roomEnter = (RoomApplyBean) request.getAttribute("roomEnter");
String result=(String)request.getAttribute("result");
String url=("/chat/roomApply.jsp");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if("failure".equals(result)){
%>
<card title="我要加入" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
%>
<card title="我要加入" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
我要加入<br/>
-------------<br/>
聊天室名称:<%=StringUtil.toWml(roomEnter.getRoomName())%><br/>
聊天室主题:<%=StringUtil.toWml(roomEnter.getRoomSubject())%><br/>
聊天室宣言:<%=StringUtil.toWml(roomEnter.getRoomEnounce())%><br/>
（您只能支持一个聊天室，点击“加入”后您将成为此聊天室的默认成员）<br/>
<a href="/chat/addVote.jsp?roomId=<%=roomEnter.getId()%>">加入</a>&nbsp;<a href="/chat/roomApply.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>