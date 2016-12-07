<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};%><%
CustomAction action = new CustomAction(request, response);
String mobile = request.getParameter("mobile");

if(mobile != null){
mobile = mobile.trim();
	if(mobile.length()==11)
	SqlUtil.executeUpdate("insert into receive_message set addtime=now(),content='zc',mobile='"+mobile+"'",5);
	response.sendRedirect("sms.jsp");
	return;
}

%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body onload="document.f1.mobile.focus()">
<p>注册短信</p>
<form action="sms.jsp" method=get name="f1">
<input type=text name="mobile">
<input type=submit value="添加注册短信">
</form>
<%
	DbOperation dbOp = new DbOperation(5);
	String query = "SELECT * from receive_message_history order by id desc limit 30";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%
	rs.close();
%><br/>未处理<%
	query = "SELECT * from receive_message order by id desc limit 20";

	rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%

	rs.close();
%><br/>拨打<%

	query = "SELECT * from call_log order by id desc limit 20";

	rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%

	dbOp.release();	
%><br/>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
