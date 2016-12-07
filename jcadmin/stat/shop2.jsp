<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","","","","","",
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
<p>每月分类统计</p>

<%
	DbOperation dbOp = new DbOperation(5);
	String query = "SELECT left(time,7) t,case when type=2 or item_id in(106,107,108) then '城堡' when type=0 and item_id in(27) then '乐币' when type=0 and item_id in(103,104,105,21) then '道具' else '乐秀' end as n,floor(sum(gold)) as g from shop_gold_record group by t,n order by t desc,n";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%
	rs.close();
	dbOp.release();
%><br/>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
