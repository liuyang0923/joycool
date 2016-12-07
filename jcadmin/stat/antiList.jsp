<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.spec.AntiAction" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String marks[]={"城墙","城墙","当铺","商店"};
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};%><%
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
<h3>所有监控用户</h3>
<table cellpadding=2><tr><td>用户名</td><td>状态</td><td>操作</td></tr>
<%
	UserBean user = null;
	
	Iterator iter = AntiAction.antiUser.entrySet().iterator();
	while(iter.hasNext()){
	Map.Entry entry = (Map.Entry)iter.next();
	Integer key = (Integer)entry.getKey();
	AntiAction.AntiUser au = (AntiAction.AntiUser)entry.getValue();
	user=UserInfoUtil.getUser(key.intValue());
	if(user==null)continue;
%>
<tr><td><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=key.intValue()%>"><%=user.getNickNameWml()%></a>
(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)

<%}else{%>(未知)<%}%></td>
<td><%if(au==null){%>无<%}else if(au.userId==0){%>监控<%}else{%>检查中<%}%></td>
<td><a href="/z/antiUser.jsp?id=<%=user.getId()%>">强制检查</a></td>
</tr>
<%}%>
</table><br/>

		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>