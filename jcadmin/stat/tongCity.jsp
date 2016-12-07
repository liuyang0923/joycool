<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.spec.AntiAction" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String marks[]={"城墙","城墙","当铺","商店"};
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};%><%
CustomAction action = new CustomAction(request, response);

int userId = action.getParameterInt("id");
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
<h3>帮会加固排名</h3>
<table cellpadding=2 style="word-break:break-all"><tr><td>用户名</td><td>帮会</td>
<td>数值</td><td>类型</td><td>状态</td><%--td>操作</td--%><td>ip</td><td>ua</td></tr>
<%
	UserBean user = null;
	
	List top;
	if(userId<=0)
		top = SqlUtil.getIntsList("select user_id,tong_id,count,mark from jc_tong_city_record where mark!=2 and count>200 order by count desc limit 500");
	else
		top = SqlUtil.getIntsList("select user_id,tong_id,count,mark from jc_tong_city_record where user_id="+userId+" order by count desc");
	for(int i=0;i<top.size();i++){
	int[] res = (int[])top.get(i);
	user=UserInfoUtil.getUser(res[0]);
	TongBean tong = TongCacheUtil.getTong(res[1]);
	AntiAction.AntiUser au = AntiAction.getAntiUser(user.getId());
%>
<tr><td><%if(user!=null){%><%=user.getId()%><a href="../user/queryUserInfo.jsp?id=<%=res[0]%>"><%=user.getNickNameWml()%></a><%}else{%>(未知)<%}%>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)</td>
<td><%=tong.getTitle()%>(<%=tong.getId()%>)</td>
<td align=right><%=res[2]%></td>
<td><%=marks[res[3]]%></td>
<td><%if(au==null){%>无<%}else if(au.userId==0){%>监控<%}else{%>检查<%=au.rand%><%}%></td>
<%--td><a href="/z/antiUser.jsp?id=<%=user.getId()%>">强制检查</a>&nbsp;<a href="/z/unAntiUser.jsp?id=<%=user.getId()%>">取消检查</a></td--%>
<td><%=user.getIpAddress()%></td><td width="200"><%=user.getUserAgent()%></td>
</tr>
<%}%>
</table><br/>
	<a href="antiList.jsp">所有监控用户</a><br/>
	<a href="index.jsp">返回上一级</a>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
	<br />
</body>
</html>