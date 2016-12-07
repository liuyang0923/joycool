<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
%><%
CustomAction action = new CustomAction(request, response);

int start = action.getParameterInt("start");
int end = action.getParameterInt("end");
if(end-start>100000)
	end = start + 100000;
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>银行排名</h3>
<table cellpadding=2><tr><td>用户名</td><td>赢取</td></tr>
<%
	UserBean user = null;
	List top;
	if(!action.hasParam("r"))
		top = SqlUtil.getIntsList("select user_id,money/100000000 from wgame_history0 where money>10000000000 order by money desc");
	else
		top = SqlUtil.getIntsList("select user_id,money/100000000 from wgame_history0 where money<-10000000000 order by money");
	for(int i=0;i<top.size();i++){
	int[] res = (int[])top.get(i);
	user=UserInfoUtil.getUser(res[0]);
%>
<tr><td><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=res[0]%>"><%=user.getNickNameWml()%></a><%}else{%>(未知)<%}%></td><td align=right>
<%=res[1]%>
</td><td>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)</td></tr>
<%}%>
</table>

		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>