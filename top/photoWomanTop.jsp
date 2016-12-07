<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.top.TopAction" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.friend.FriendVoteBean" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%
response.setHeader("Cache-Control","no-cache");
//zhul2006-09-12 限制未登录用户进入start
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser==null) 
{
	String reURL=request.getRequestURL().toString();
	String queryStr=request.getQueryString();
	session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);

	//response.sendRedirect(("http://wap.joycool.net/user/login.jsp"));
	BaseAction.sendRedirect("/user/login.jsp", response);
	return;
}
//zhul2006-09-12 限制未登录用户进入end
TopAction topAction=new TopAction(request);
topAction.friendPhotoWomanTop(request);
Vector friendPhotoWomanList=(Vector)request.getAttribute("friendPhotoWomanList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="美女排行榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
美女排行榜<br/>
乐酷鼓励大家上传自己的照片<br/>
----------<br/>
<%if(friendPhotoWomanList==null){%>

服务器繁忙,请稍后再试<br/>
<a href="photoWomanTop.jsp">重试一次</a><br/>
<%}else{%>

<%
long voteCount=topAction.getFriendVote(loginUser.getId());
if(voteCount==-1){
FriendBean friend=topAction.getFriend(loginUser.getId());
if(friend==null){
%>
<a href="/friend/friendCenter.jsp">开通个人交友中心</a><br/>
<%}else if (friend!=null && friend.getAttach().equals("")){%>
<a href="/friend/addPhoto.jsp">上传个人照片</a>(支持WAP2.0的手机才可以上传图片。)<br/>
<%}else{%>
您目前的投票是0张<br/>
<%}
}else{%>
您目前的投票是<%=voteCount%>张<br/>
<%}
UserBean user=null;
FriendVoteBean friendVote=null;
for(int i=0;i<friendPhotoWomanList.size();i++){
friendVote=(FriendVoteBean)friendPhotoWomanList.get(i);
user=topAction.getUser(friendVote.getUserId());
if(user==null){
continue;
}
%>
<%=i+1+" "%>
<%if(user.getId()!=loginUser.getId()){%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">
<%
String nickname=StringUtil.toWml(user.getNickName());
if(nickname.equals(""))
nickname="乐客"+user.getId();%>
<%=nickname%>
</a><%}else{%>您自己<%}%>(<%=friendVote.getCount()%>票)<br/>
<%}%><br/><br/>
<%}%>
<%@include file="bottom.jsp"%><br/>
</p>
</card>
</wml>