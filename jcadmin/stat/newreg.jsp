<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
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
		用户留存统计:<%=start%> - <%=end%>
<%
	DbOperation dbOp = new DbOperation(true);

	String query = "select cd,count(days),sum(case when days=1 then 1 else 0 end) day1,sum(case when days between 2 and 3 then 1 else 0 end) day3,sum(case when days between 4 and 7 then 1 else 0 end) day7,sum(case when days between 8 and 30 then 1 else 0 end) day30,sum(case when days >30 then 1 else 0 end) day999 from (select a.id,date(create_datetime) cd,DATEDIFF((last_login_time),(create_datetime)) days from user_info a,user_status b where a.id=b.user_id order by a.id desc limit 30000) ls group by cd";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%

	dbOp.release();	
%>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
