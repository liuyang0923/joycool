<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
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
		用户银行统计:<%=start%> - <%=end%>
<%
	DbOperation dbOp = new DbOperation(true);

	String query = "select user_id,sum(money) m from (select * from jc_bank_log where (type=1 or type=9 or type=2) and (money>=10000000 or money <= -10000000) and id between " + start + " and " + end + ")d group by user_id order by m desc limit 20";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%

	dbOp.release();	
%>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
