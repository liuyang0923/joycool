<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.tong.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.*" %><%

int  tongId=StringUtil.toInt(request.getParameter("tongId"));
if(tongId<=0){
response.sendRedirect("index.jsp");
return;
}
TongBean tong = TongCacheUtil.getTong(tongId);
if(tong==null){
response.sendRedirect("index.jsp");
return;
}
CustomAction action = new CustomAction(request);

int c = action.getParameterIntS("c");
if(c!=-1){
tong.setUserCount(c);
SqlUtil.executeUpdate("update jc_tong set user_count=" + c + " where id=" + tongId);
response.sendRedirect("member.jsp?tongId=" + tongId);
}
List list=TongCacheUtil.getTongUserListById(tongId);
PagingBean paging = new PagingBean(action, list.size(), 10, "p");
%>
<html>
	<head>
<meta http-equiv="expires" content="0" />
	<title>帮会管理后台</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	<H1 align="center">帮会当铺开发记录</H1>
	<hr>
	<br/>
	<table width="500" border="1" align="center">
	<tr>
	<td width="40"></td>
	<td>玩家</td>
	<td width="80">称号</td>
	<td width="120">ip</td>
	</tr>
<%
int endIndex = paging.getEndIndex();
for(int i=paging.getStartIndex();i<endIndex;i++){
Integer userId =  (Integer) list.get(i);
UserBean user=(UserBean)UserInfoUtil.getUser(userId.intValue());
if(user==null)continue;
UserBean user2 = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(user.getId()));
TongUserBean tongUser=TongCacheUtil.getTongUser(tong.getId(),userId.intValue());
if(tongUser==null)continue;
%>
	<tr>	
		<td><%=i+1%></td>
		<td width=200><a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)(<%=user.getId()%>)</td>
		<td><%=tongUser.getHonor()%></td>
		<td><%if(user2!=null){%><%=user2.getIpAddress()%><%}else{%><%}%></td>
	</tr>
<%}%>
	</table><br/>
	<p align="center"><%=paging.shuzifenye("member.jsp?tongId="+tongId,true,"|",response)%><br/>
	共有成员<%=tong.getUserCount()%>名<%if(list.size()!=tong.getUserCount()){%><a href="member.jsp?tongId=<%=tongId%>&c=<%=list.size()%>"><font color=red>修复</font></a><%}%></p>
	<br />
<form  action="member.jsp" method="post"  name="tongForm"><br/>
帮会ID：
<input type="text" name="tongId" size="10" >&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="查询"><br/>
</form><br/>
<p align="center">
<a href="search.jsp?tongId=<%=tongId%>">返回<%=tong.getTitle()%></a><br/>
	<a href="index.jsp">返回帮会管理后台</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
</p>
</body>
</html>
