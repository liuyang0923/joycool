<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.chat.*,java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
int roomId=action.getParameterInt("roomId");
action.inviteFriends(request);
//在线好友
Vector onlineFriendsList = (Vector)request.getAttribute("onlineFriendsList");
//离线好友
Vector outlineFriendsList = (Vector)request.getAttribute("outlineFriendsList");
int onlineFriendsCount=onlineFriendsList.size();
int outlineFriendsCount=outlineFriendsList.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷门户">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(onlineFriendsCount<=10){%>
在线好友(<%=onlineFriendsCount%>):<br/>
<%
for(int i = 0; i < onlineFriendsList.size(); i ++){
	String userId = (String) onlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
%>
<%=(i + 1)%>.<a href="invite.jsp?roomId=<%=roomId%>&amp;userId=<%=user.getId()%>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%></a>
<br/>
<%
}
%>
离线好友(<%=outlineFriendsCount%>):<br/>
<%
for(int i = 0; i < outlineFriendsList.size(); i ++){
	if(i>2) break;
	String userId = (String) outlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
%>
<%=(i + 1)%>.<a href="/chat/invite.jsp?roomId=<%=roomId%>&amp;userId=<%=user.getId()%>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%></a>
<br/>
<%
}
%>
<% if(outlineFriendsCount>3) {%> <a href="/chat/inviteMoreFriends.jsp?roomId=<%=roomId%>&amp;option=outlinefriends">更多离线好友</a><br/><%}%> 
<%}else{%>

在线好友(<%=onlineFriendsCount%>):<br/>
<%
for(int i = 0; i < onlineFriendsList.size(); i ++){
	if(i>9) break;
	String userId = (String) onlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
%>
<%=(i + 1)%>.<a href="invite.jsp?roomId=<%=roomId%>&amp;userId=<%=user.getId()%>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%></a>
<br/>
<%
}
%>
<a href="/chat/inviteMoreFriends.jsp?roomId=<%=roomId%>&amp;option=onlinefriends">更多在线好友</a><br/>
<a href="/chat/inviteMoreFriends.jsp?roomId=<%=roomId%>&amp;option=outlinefriends">离线好友(<%=outlineFriendsCount%>)</a><br/>
<%}%>
<a href="/chat/onlineListMan.jsp?roomId=<%=roomId%>">在线男</a>&nbsp;|
<a href="/chat/onlineListWoman.jsp?roomId=<%=roomId%>">在线女</a><br/>
<a href="/chat/inviteRoom.jsp">邀好友抢王冠！</a><br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a><br/> 
<a href="/user/searchUser.jsp" title="进入">查找添加好友</a><br/>
<a href="/user/ViewBadGuys.do">我的黑名单</a><br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>

