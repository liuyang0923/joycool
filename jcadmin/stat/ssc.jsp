<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","大富翁","桃花源","六时彩","","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};%><%
CustomAction action = new CustomAction(request, response);

%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		六时数据<br/>
<%
	DbOperation dbOp = new DbOperation(5);

	String query = "SELECT lhc_id id,create_datetime time,floor(sum(money)/100000000) money,floor(sum(prize)/100000000) prize,count(id) cnt FROM lhc_wager_record l group by lhc_id order by lhc_id desc limit 50";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%
	
	dbOp.release();	
%>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
