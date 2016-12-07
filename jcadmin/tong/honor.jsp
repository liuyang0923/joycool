<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.bean.tong.TongCityRecordBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.*" %><%
if(!group.isFlag(0)) return;

%>
<html>
	<head>
<meta http-equiv="expires" content="0" />
	<title>勋章查询</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	<H1 align="center">勋章排名</H1>
	<hr>
	<table border="1" align="center">
	<tr>
	<td width="40">排名</td>
	<td width="200">玩家</td>
	<td width="80">当前荣誉</td>
	<td width="80">上周荣誉</td>
	<td>勋章</td>
	</tr>
<%

List list = SqlUtil.getIntsList("select user_id,last_week,honor,place from user_honor order by last_week desc limit 50",0);
for(int i=0;i<list.size();i++){
int[] objs = (int[])list.get(i);
UserBean user = UserInfoUtil.getUser(objs[0]);
int rank = 0;
if(user.getUserHonor()!=null)
	rank = user.getUserHonor().getRank();
%>
	<tr>	
		<td><%=objs[3]%></td>
		<td>(<%=user.getId()%>)<a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)</td>
		<td><%=objs[1]%></td>
		<td><%=objs[2]%></td>
		<td><%if(rank>1){%><img src="/img/honor/h<%=rank%>.gif" alt="<%=rank%>勋"/><%}%></td>
	</tr>
<%}%>
	</table>
	<br />

	<a href="/jcadmin/manage.jsp">返回管理首页</a>
</p>
</body>
</html>
