<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.stock2.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.stock2.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control", "no-cache");			
StockService service = new StockService();
CustomAction action = new CustomAction(request);
int userId=StringUtil.toInt(request.getParameter("id"));
UserBean user = UserInfoUtil.getUser(userId);
if(group.isFlag(0)){
	int type=action.getParameterInt("type");
	if(user!=null&&type>0) {
		int add = action.getParameterInt("add");
		if(type==1){
			StockWorld.UpdateStockAccount("money=money+(" + add*10000l+")", "user_id=" + user.getId());
		}
		response.sendRedirect("userStock.jsp?id="+userId);
		return;
	}
}
StockAccountBean account = StockWorld.getStockAccount(userId);// 查询用户股票帐号
StockWorld.stockService.calcStockAccount(account);
List stockCCList = StockWorld.stockService.getStockCCList("user_id=" + userId + " order by id");
List stockWTList = StockWorld.getStockService().getStockWTList("user_id=" + userId + " order by id desc");
%>
<html>
<head>
</head>
<body>
<H1 align="center">用户股票数据</H1><hr>
昵称：<a href="../user/queryUserInfo.jsp?id=<%=userId%>"><%=user.getNickNameWml()%>(<%=user.getId()%>)</a><br/>
开户日期：<%=DateUtil.formatDate1(account.getCreateDatetime())%><br/>
总资产：<%=account.getAsset()%>乐币<br/>
可用资金：<%=account.getMoney()%>乐币<br/>
冻结资金：<%=account.getMoneyF()%>乐币<br/>
股票市值：<%=account.getStockPrice()%>乐币<br/>
<p>
<table border="1" align="left" >
<tr><td colspan=2>股票持仓记录</td></tr>
<tr>
	<td>
		股票
	</td>
	<td>
		价格
	</td>
	<td>
		数量 / 冻结
	</td>
</tr>
<%for (int i = 0; i < stockCCList.size(); i++) {
	StockCCBean cc = (StockCCBean) stockCCList.get(i);
	StockBean stock = StockWorld.getStock("id="+cc.getStockId());
%><tr>
	<td>
		<%=stock.getName()%>(<%=stock.getId()%>)
	</td>
	<td align=right>
		<%=StringUtil.numberFormat(stock.getPrice())%>
	</td>
	<td>
		<%=cc.getCount()%> / <%=cc.getCountF()%>
	</td>
</tr>
<%}%></table>
<p>
<table border="1" align="left" >
<tr><td colspan=4>股票委托记录</td></tr>
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
		类型
	</td>
</tr>
<%for (int i = 0; i < stockWTList.size(); i++) {
	StockWTBean wt = (StockWTBean) stockWTList.get(i);
	StockBean stock = StockWorld.getStock("id="+wt.getStockId());
%><tr>
	<td >
		<%=stock.getName()%>(<%=stock.getId()%>)
	</td>
	<td align=right>
		<%=StringUtil.numberFormat(wt.getPrice())%>
	</td>
	<td align=right>
		<%=wt.getCount()%>股票
	</td>
	<td><%if(wt.getMark()==0){%>卖出<%}else{%>买入<%}%>
	</td>
</tr>
<%}%></table>
<p>
<a href="cj.jsp?id=<%=userId%>">查看成交记录</a><br/>
<a href="wt.jsp?id=<%=userId%>">查看全部委托记录</a><br/>

<br>
<%if(group.isFlag(0)){%>
<form method="post" action="userStock.jsp?id=<%=userId%>&type=1">
<input type="text" name="add" size="8" maxlength="10">万
<input type="submit" value="增加股市账户资金">
</form>
<%}%>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br />
</body>
</html>
