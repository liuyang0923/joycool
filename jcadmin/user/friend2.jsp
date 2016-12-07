<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.impl.UserServiceImpl" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%!
	static UserServiceImpl service = new UserServiceImpl();
	static String[] markNames={"普通","结义","结婚"};
%><%
CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("userId");
int toId = action.getParameterInt("toId");
UserFriendBean uf = service.getUserFriend(userId, toId);

HashMap noteMap = UserInfoUtil.getUserNoteMap(userId);
UserNoteBean n = (UserNoteBean)noteMap.get(new Integer(toId));
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<div align="center">
<H2 align="center">用户好友后台</H2>
<table border="1" align="center" width="600">
<tr><td>id</td>
	<td>
		玩家1
	</td>
	<td>
		玩家2
	</td>
	<td>
		创建时间
	</td>
	<td>
		关系
	</td>
</tr>
<%
if(uf!=null){
UserBean user=UserInfoUtil.getUser(userId);
UserBean user2=UserInfoUtil.getUser(toId);
%><tr><%
%>
<td><%=uf.getId()%></td>
<td><a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%>(<%=user.getId()%>)</a></td>
<td><a href="queryUserInfo.jsp?id=<%=user2.getId()%>"><%=user2.getNickNameWml()%>(<%=user2.getId()%>)</a></td>
<td><%=uf.getCreateDatetime()%></td>
<td><%=markNames[uf.getMark()]%></td>
</tr>
<%}%>
</table><br/>
<%if(n!=null){%>
备注：<%=StringUtil.toWml(n.getShortNote())%><br/>
<%=StringUtil.toWml(n.getNote())%>
<br/>
<%}%>
根据双方ID查找好友<br/>
<form name="form1" method="post" action="friend2.jsp">
用户ID：<input type="text" name="userId" value="<%=userId%>"/>,<input type="text" name="toId"/><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
</html>