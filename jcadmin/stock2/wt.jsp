<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.stock2.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control", "no-cache");			
StockService service = new StockService();
CustomAction action = new CustomAction(request);
int userId=StringUtil.toInt(request.getParameter("id"));
int count = StockWorld.getStockService().getStockCount("1");
PagingBean paging = new PagingBean(action,count,20,"p");
List stockWTList = StockWorld.getStockService().getStockWTList("1 order by id desc limit "+paging.getStartIndex()+",20");
%>
<html>
<head>
</head>
<body>
<H1 align="center">所有委托记录</H1><hr>
<table border="1" align="left" >
<tr><td colspan=5>股票委托记录</td></tr>
<tr>
	<td>
		用户
	</td>
	<td>
		股票
	</td>
	<td>
		价格
	</td>
	<td>
		数量 / 成交
	</td>
	<td>
		类型
	</td>
	<td>
		时间
	</td>
</tr>
<%for (int i = 0; i < stockWTList.size(); i++) {
	StockWTBean wt = (StockWTBean) stockWTList.get(i);
	StockBean stock = StockWorld.getStock("id="+wt.getStockId());
	if(stock==null) continue;
%><tr>
	<td >
		<a href="userStock.jsp?id=<%=wt.getUserId()%>"><%=wt.getUserId()%></a>
	</td>
	<td >
		<%=stock.getName()%>(<%=stock.getId()%>)
	</td>
	<td align=right>
		<%=StringUtil.numberFormat(wt.getPrice())%>
	</td>
	<td align=right>
		<%=wt.getCount()%>股 / <%=wt.getCjCount()%>股
	</td>
	<td><%if(wt.getMark()==0){%>卖出<%}else{%>买入<%}%>
	<td><%=wt.getCreatedatetime()%></td>
	</td>
</tr>
<%}%></table><br/>
<%=paging.shuzifenye("wt.jsp?id="+userId,true,"|",response)%>
<br />
<a href="userStock.jsp?id=<%=userId%>">返回</a><br/>

<br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br />
</body>
</html>
