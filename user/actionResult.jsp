<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.user.SendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page errorPage=""%><%@ page import="java.util.Vector" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("chat",loginUser.getId());
if(forbid!=null){
	response.sendRedirect("/chat/hall.jsp");
	return;
}
//得到房间号
SendAction action = new SendAction(request);
int roomId=action.getParameterInt("roomId");
action.result(request,response);
String result = (String) request.getAttribute("result");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
UserBean toUser = (UserBean)request.getAttribute("toUser");
//出错
if("failure".equals(result)){
%>
<card title="发送动作" ontimer="<%=response.encodeURL("/chat/hall.jsp")%>">
<timer value="500"/>
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
<%=request.getAttribute("tip")%><br/>
<br/>
<a href="ViewUserInfo.do?userId=<%=toUser.getId()%>">返回用户信息</a><br/>
<a href="ViewFriends.do">返回好友列表</a><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
RankActionBean rankAcion=(RankActionBean)request.getAttribute("rankAcion");
String sendMessage=rankAcion.getSendMessage();
//mcq_更改显示用户昵称  时间 2006-6-19
sendMessage=StringUtil.toWml(sendMessage.replace("XXX",toUser.getNickName()));
//mcq_end
%>
<card title="发送动作" ontimer="<%=response.encodeURL("/chat/hall.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
3秒后自动返回<br/>
动作发送成功:<br/>
<%=sendMessage%><br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="ViewUserInfo.do?userId=<%=toUser.getId()%>">返回用户信息</a><br/>
<a href="ViewFriends.do">返回好友列表</a><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>