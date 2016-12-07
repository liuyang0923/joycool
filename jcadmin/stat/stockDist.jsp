<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.stock2.*" %><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
CustomAction action = new CustomAction(request, response);
int stockId = action.getParameterInt("id");
StockBean stock = StockWorld.getStockService().getStock("id="+stockId);
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>股票分布</h3>
<%=stock.getName()%>(发行量<%=stock.getTotalCount()/10000%>万)<br/>
<table cellpadding=2><tr><td>用户名</td><td>持仓</td><td>持仓比例</td></tr>
<%
	UserBean user = null;
	List top = SqlUtil.getIntsList("select user_id,(count+count_f)/10000 s from stock_cc where stock_id="+stockId+" order by s desc limit 50");
	for(int i=0;i<top.size();i++){
	int[] res = (int[])top.get(i);
	user=UserInfoUtil.getUser(res[0]);
%>
<tr><td><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=res[0]%>"><%=user.getNickNameWml()%></a><%}else{%>(未知)<%}%></td>
<td align=right><%=res[1]%></td>
<td align=right><%=StringUtil.numberFormat((float)res[1]*100/(stock.getTotalCount()/10000))%>%</td>
</tr>
<%}%>
</table><br/>
		<a href="stock.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>