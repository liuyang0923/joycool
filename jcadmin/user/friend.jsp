<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.impl.UserServiceImpl" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%!
	static UserServiceImpl service = new UserServiceImpl();
%><%
CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("userId");
UserBean user=UserInfoUtil.getUser(userId);
List userFriends = UserInfoUtil.getUserFriends(userId);
int del = action.getParameterInt("del");
if(del > 0){
	service.deleteFriend(userId, del);
	response.sendRedirect("friend.jsp?userId=" + userId);
	return;
}
	//在线好友
	List onlineFriendsList = new ArrayList();
	//离线好友
	List outlineFriendsList = new ArrayList();

	for(int i=0;i<userFriends.size();i++)
	{
		String userIdKey=(String)userFriends.get(i);
		if(OnlineUtil.isOnline(userIdKey))
			onlineFriendsList.add(userIdKey);
		else
			outlineFriendsList.add(userIdKey);
	}
	onlineFriendsList.addAll(outlineFriendsList);

PagingBean paging = new PagingBean(action,userFriends.size(),20,"p");
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<div align="center">
<H2 align="center">用户好友后台</H2>
<%if(user!=null){%>
<p><%=StringUtil.toWml(user.getNickName())%>(<%=user.getId()%>)</p>
<%}%>
<table border="1" align="center" width="500">
<tr><td></td>
	<td>
		好友ID
	</td>
	<td>
		昵称
	</td>
	<td>
		操作
	</td>
</tr>
<%
if(onlineFriendsList!=null){
for(int i = paging.getStartIndex(); i < paging.getEndIndex(); i ++){
%><tr><%
	String userIdStr=(String)onlineFriendsList.get(i);
	UserBean user2 = UserInfoUtil.getUser(StringUtil.toInt(userIdStr));
%><td width="30"><%=i+1%></td>
<td width="120"><a href="queryUserInfo.jsp?id=<%=userIdStr%>"><%=userIdStr%></a><%if(user2!=null){%>(<%=LoadResource.getPostionNameByUserId2(user2.getId())%>)<%}%></td>
<td><%if(user2!=null){%><%=user2.getNickNameWml()%><%}%></td>
<td width="40"><a href="friend.jsp?userId=<%=userId%>&del=<%=userIdStr%>" onclick="return confirm('确认要删除?')">删除</a></td>
<td><a href="friend2.jsp?userId=<%=userId%>&toId=<%=userIdStr%>">详细</a></td>
</tr>
<%}%>
<%}%>
</table>
<%=paging.shuzifenye("friend.jsp?userId="+userId,true,"|",response)%><br/>
根据ID查找好友<br/>
<form name="form1" method="post" action="friend.jsp">
用户ID：<input type="text" name="inputUserId"/><input type="submit" name="submit" value="确定"/>
</form>
根据双方ID查找好友<br/>
<form name="form1" method="post" action="friend2.jsp">
用户ID：<input type="text" name="userId" value="<%=user.getId()%>"/>,<input type="text" name="toId"/><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
</html>