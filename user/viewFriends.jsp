<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*,java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*"%><%!
static IUserService userService = ServiceFactory.createUserService();
static int COUNT_PER_PAGE = 10;	// 一页10个好友
%><%
response.setHeader("Cache-Control","no-cache");
String roomId = (String) request.getParameter("roomId");
if (roomId == null) {
	roomId = "0";
}
//liuyi 2006-11-01 结义显示 start 
UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserSettingBean set = loginUser.getUserSetting();
//liuyi 2006-11-01 结义显示 end
//在线好友
Vector onlineFriendsList = (Vector)request.getAttribute("onlineFriendsList");
//离线好友
Vector outlineFriendsList = (Vector)request.getAttribute("outlineFriendsList");
if(onlineFriendsList==null||outlineFriendsList==null){
response.sendRedirect(("/lswjs/index.jsp"));
return;
}
int onlineFriendsCount=onlineFriendsList.size();
int outlineFriendsCount=outlineFriendsList.size();
HashMap noteMap = null;
if(onlineFriendsCount+outlineFriendsCount>0 && set!=null && set.isFlag(16)){
	noteMap = UserInfoUtil.getUserNoteMap(loginUser.getId());
}
//三个同城异性
Vector vecOnline=(Vector)request.getAttribute("vecOnline");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友列表">
<p align="left">
<a href="searchUser.jsp">用户大搜索</a>|<a href="/chat/linkMan.jsp">最近联系人</a><br/>
<%=BaseAction.getTop(request, response)%>
<% 
if(onlineFriendsCount<=COUNT_PER_PAGE){%>
=在线好友(<%=onlineFriendsCount%>)=<br/>
<%
for(int i = 0; i < onlineFriendsList.size(); i ++){
	String userId = (String) onlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;	
	//liuyi 2006-11-01 结义显示 start 
	UserFriendBean friend = userService.getUserFriend(loginUser.getId(), user.getId());
	UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(""+user.getId());
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%><% if(friend!=null){ if(friend.getMark()==1){%>(义)<%}else if(friend.getMark()==2){%>(<%= (user.getGender()==1)?"夫":"妻"%>)<%} } %></a>
<%if(noteMap!=null){UserNoteBean n = (UserNoteBean)noteMap.get(new Integer(user.getId()));if(n!=null){%><%=StringUtil.toWml(n.getShortNote())%><%}}%>(<%=LoadResource.getPostionNameByUserId(user.getId())%>)<br/>
<%
    //liuyi 2006-11-01 结义显示 end 
}
%>
=离线好友(<%=outlineFriendsCount%>)=<br/>
<%
for(int i = 0; i < outlineFriendsList.size(); i ++){
	if(i>2) break;
	String userId = (String) outlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
    //liuyi 2006-11-01 结义显示 start 
	UserFriendBean friend = userService.getUserFriend(loginUser.getId(), user.getId());
	//UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(""+user.getId());
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%><% if(friend!=null){ if(friend.getMark()==1){%>(义)<%}else if(friend.getMark()==2){%>(<%= (user.getGender()==1)?"夫":"妻"%>)<%} } %></a>
<%if(noteMap!=null){UserNoteBean n = (UserNoteBean)noteMap.get(new Integer(user.getId()));if(n!=null){%><%=StringUtil.toWml(n.getShortNote())%><%}}%><br/>
<%
    //liuyi 2006-11-01 结义显示 end 
}
%>
<% if(outlineFriendsCount>3) {%> <a href="FriendsList.do?roomId=<%=roomId%>&amp;option=outlinefriends">更多离线好友</a><br/><%}%> 
<%}else{
CustomAction action = new CustomAction(request);
PagingBean paging = new PagingBean(action, onlineFriendsCount, COUNT_PER_PAGE, "p");
%>=在线好友(<%=onlineFriendsCount%>)=<br/>
<%
int endIndex = paging.getEndIndex();
for(int i = paging.getStartIndex(); i < endIndex; i ++){
	String userId = (String) onlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
	//liuyi 2006-11-01 结义显示 start 
	UserFriendBean friend = userService.getUserFriend(loginUser.getId(), user.getId());
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%><% if(friend!=null){ if(friend.getMark()==1){%>(义)<%}else if(friend.getMark()==2){%>(<%= (user.getGender()==1)?"夫":"妻"%>)<%} } %></a>
<%if(noteMap!=null){UserNoteBean n = (UserNoteBean)noteMap.get(new Integer(user.getId()));if(n!=null){%><%=StringUtil.toWml(n.getShortNote())%><%}}%>(<%=LoadResource.getPostionNameByUserId(user.getId())%>)<br/>
<%
    //liuyi 2006-11-01 结义显示 end 
}
%><%=paging.shuzifenye("ViewFriends.do",false,"|",response)%>
<a href="FriendsList.do?roomId=<%=roomId%>&amp;option=outlinefriends">离线好友(<%=outlineFriendsCount%>)</a><br/>
<%--<a href="NewViewFriends.do?roomId=<%=roomId%>&amp;option=strange&amp;totaPages=0&amp;pageIndex=0&amp;marker=0">陌生人</a><br/>--%>
<%}%>
<a href="/chat/onlineListMan.jsp?roomId=<%=roomId%>">在线男</a>&nbsp;|
<a href="/chat/onlineListWoman.jsp?roomId=<%=roomId%>">在线女</a><br/>
<%for(int i = 0; i < vecOnline.size(); i ++){
	if(i>2) break;
	String userId = (String) vecOnline.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="ViewUserInfo.do?roomId=<%=roomId %>&amp;userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>(<%=(user.getGender() == 0 ? "女":"男")%>|<%=user.getAge()%>岁<%if(user.getCityname() != null){%>|<%=user.getCityname()%><%}%>)<br/>
<%}%>
<%--<a href="/chat/inviteRoom.jsp">邀好友抢王冠！</a><br/>--%>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<a href="viewBadGuys.jsp">我的黑名单</a><br/>
<a href="groupSendMsg.jsp">好友群发器</a><br/>
<a href="/friend/friendCenter.jsp">交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
