<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.stock2.*" %><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
CustomAction action = new CustomAction(request, response);
List stockList = StockWorld.getStockService().getStockList("1=1 order by id");
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>股票行情</h3>
<table cellpadding=2><tr><td>id</td><td>股票名称</td><td>发行量</td><td>换手率</td><td>现价</td><td>状态</td></tr>
<%
	StockBean stock = null;
	for(int i=0;i<stockList.size();i++){
	stock = (StockBean)stockList.get(i);
%>
<tr>
<td><%=stock.getId()%></td>
<td><a href="stockDist.jsp?id=<%=stock.getId()%>"><%=stock.getName()%></a></td>
<td align=right><%=stock.getTotalCount()/10000%>万</td>
<td align=right><%=StringUtil.numberFormat((float)stock.getCount() / stock.getTotalCount()*100)%>%</td>
<td align=right><%=StringUtil.numberFormat(stock.getPrice())%></td>
<td><%=stock.getStatusName()%></td>
</tr>
<%}%>
</table><br/>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>