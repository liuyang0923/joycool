<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","大富翁","桃花源","六时彩","","",
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
		每日分类数据<br/>
<%
	DbOperation dbOp = new DbOperation(true);

	String query = "SELECT time,floor(v1/10000) v1,floor(v2/10000) v2,floor(v3/10000) v3,floor(v4/10000) v4,floor(v5/10000) v5,floor(v6/10000) 打猎,floor(v7/10000) v7,floor(v8/10000) v8,floor(v9/10000) 新问答,floor(v10/10000) 丢弃物品,floor(v11/10000) v11,floor(v12/10000) 大转盘,floor(v13/10000) 宝箱,floor(v14/10000) v14,floor(v15/10000) 大富翁,floor(v16/10000) 桃花源,floor(v17/10000) 六时彩,floor(v18/10000) 添加,floor(v19/10000) v19,floor(v20/10000) v20 from mcoolhis.bank_stat2 order by id desc limit 20";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%
	
	dbOp.release();	
%>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
