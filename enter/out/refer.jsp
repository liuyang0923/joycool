<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.CacheManage"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
%><%
CustomAction action = new CustomAction(request, response);

int start = action.getParameterInt("start");
int id = action.getParameterInt("id");
int end = action.getParameterInt("end");
PagingBean paging = new PagingBean(action,100000,100,"p");
if(end-start>100000)
	end = start + 100000;
%>
<html>
	<head>
	</head>
<link href="../../jcadmin/farm/common.css" rel="stylesheet" type="text/css">
	<body>
<form action="refer.jsp"><input type=text name="id"><input type=submit value="按友链id搜索"></form>
<%
	DbOperation dbOp = new DbOperation(5);

	String query = "SELECT * from jump_referer order by id desc limit "+(paging.getStartIndex())+",100";
	if(id>0)
		query="SELECT * from jump_referer where link_id="+id+" order by id desc limit "+(paging.getStartIndex())+",100";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../../jcadmin/showQuery.jsp"%><%

	dbOp.release();	%><br/><%

%><br /><%=paging.shuzifenye("refer.jsp?id="+id,true," ",response,20)%><br/>
	</body>
</html>
