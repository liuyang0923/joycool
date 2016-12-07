<%@ page contentType="text/html;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="java.sql.*"%><%@ page import="javax.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%
CustomAction action = new CustomAction(request);
String[] dbs = DbOperation.connDb;
if(!action.isMethodGet()) {
	DbOperation.modifyConfig(action.getParameterInt("i"), request.getParameter("db"));
	response.sendRedirect("db.jsp");
	return;
}
%>
<html>
<head>
</head>
<body>
<p>数据库连接配置</p>
<table border="1" cellpadding=5>
<tr>
<td></td>
<td>jndi</td>
<td>DataSource</td>
<td>Connection</td>
<td></td>
</tr>
<%
for(int i=0;i<dbs.length;i++){
	String db = dbs[i];
	DataSource ds = DbUtil.getDataSource(db);
	boolean ok = DbUtil.checkConnection(db);
	
%>
<tr>
<td><%=i+1%></td>
<td><%=db%></td>
<td><%if(ds!=null){%>正常<%}else{%><font color=red>异常</font><%}%></td>
<td><%if(ok){%>正常<%}else{%><font color=red>异常</font><%}%></td>
<%if(i>0){%>
<form action="db.jsp?i=<%=i%>" method="post">
<td><input name="db" value="<%=db%>"><input type=submit value="修改"></td>
</form>
<%}%>
</tr>
<%
}

%>
</table>
<body>
</html>