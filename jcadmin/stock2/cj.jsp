<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.stock2.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control", "no-cache");			
StockService service = new StockService();
CustomAction action = new CustomAction(request);
int userId=StringUtil.toInt(request.getParameter("id"));
UserBean user = UserInfoUtil.getUser(userId);
PagingBean paging = new PagingBean(action,300,30,"p");
List stockCJList = StockWorld.getStockService().getStockCJList("user_id=" + userId + " order by id desc limit " + paging.getStartIndex() + ",30");
%>
<html>
<head>
</head>
<body>
<H1 align="center">成交记录</H1><hr>
昵称：<a href="../user/queryUserInfo.jsp?id=<%=userId%>"><%=user.getNickNameWml()%>(<%=user.getId()%>)</a><br/>
<table border="1" align="left" >
<tr><td colspan=5>股票成交记录</td></tr>
<tr>
	<td>
		股票
	</td>
	<td>
		价格
	</td>
	<td>
		数量
	</td>
	<td>
		成交前数量
	</td>
	<td>
		类型
	</td>
	<td>
		时间
	</td>
</tr>
<%for (int i = 0; i < stockCJList.size(); i++) {
	StockCJBean cj = (StockCJBean) stockCJList.get(i);
	StockBean stock = StockWorld.getStock("id="+cj.getStockId());
	if(stock==null) continue;
%><tr>
	<td >
		<%=stock.getName()%>(<%=stock.getId()%>)
	</td>
	<td align=right>
		<%=StringUtil.numberFormat(cj.getPrice())%>
	</td>
	<td align=right>
		<%=cj.getCjCount()%>股
	</td>
	<td align=right>
		<%=cj.getCount()%>股
	</td>
	<td><%if(cj.getMark()==0){%>卖出<%}else{%>买入<%}%>
	</td>
	<td><%=DateUtil.formatDate2(cj.getCreatedatetime())%></td>
</tr>
<%}%></table><br/>
<%=paging.shuzifenye("cj.jsp?id="+userId,true,"|",response,20)%>
<br />
<a href="userStock.jsp?id=<%=userId%>">返回</a><br/>

<br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br />
</body>
</html>
