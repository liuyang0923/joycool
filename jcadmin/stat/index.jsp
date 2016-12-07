<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","大富翁","桃花源","六时彩","添加","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};
%><%
CustomAction action = new CustomAction(request, response);
long[] moneyStat = UserInfoUtil.getMoneyStat();
long[] moneyStatTime = UserInfoUtil.getMoneyStatTime();
int clear = action.getParameterIntS("clear");
long now = System.currentTimeMillis();
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%
	DbOperation dbOp = new DbOperation(true);

	String query = "SELECT total 总计,bank 银行,stock 股市,fund 帮会基金,auction 拍卖,cash 现金,log_id 日志,time 时间 from mcoolhis.bank_stat order by id desc limit 10";

	ResultSet rs = dbOp.executeQuery(query);
	
	%><%@ include file="../showQuery.jsp"%><%

	dbOp.release();	
%><br/>
<table width="340">
	<tr>
		<td>
			分类
		</td>
		<td>
			资金
		</td>
		<td>
			统计时间
		</td>
		<td>
			平均
		</td>
		<td>
		</td>
	</tr>
			<%for (int i = 0; i < moneyStat.length; i++) {
			if(names[i].length()==0) continue;
			float va = moneyStat[i]/((float)(now-moneyStatTime[i])/0.8640f);
%>
	<tr>
		<td>
			<%=names[i]%>
		</td>
		<td align=right>
			<%=StringUtil.bigNumberFormat(moneyStat[i])%>
		</td>
		<td>
			<%=DateUtil.formatTimeInterval(now-moneyStatTime[i])%>
		</td>
		<td align=right><b><%=StringUtil.numberFormat(va)%></b></td>
		<td width=40>
			<a href="moneyStat.jsp?clear=<%=i%>">清除</a>
		</td>
	</tr>
	<%}%>
</table>
total<br/>
<form action="bankStat.jsp">
<input type=text name="start">
<input type=text name="end">
<input type=submit value="查询">
</form>
用户<br/>
<form action="userBankStat.jsp">
<input type=text name="start">
<input type=text name="end">
<input type=submit value="查询">
</form>
<a href="bankTop.jsp">银行股市排名</a><br/>
<a href="stock.jsp">股市统计</a><br/><br/>
<a href="tongCity.jsp">帮会加固排名</a>|<a href="tongShop.jsp">商店加固排名</a><form action="tongCity.jsp"><input type=text name="id"><input type=submit value="查询"></form>
<a href="question.jsp">今日问答</a><br/><br/>
<%if(group.isFlag(0)){%>
<a href="wgamepkbig.jsp">big战绩统计</a>-<a href="wgamepkbig.jsp?r=1">反向</a><br/>
<a href="wgamepk.jsp">战绩统计</a>-<a href="wgamepk.jsp?r=1">反向</a><br/><br/>
<a href="shop.jsp">pay统计</a>-<a href="shop2.jsp">分类商城</a><br/>
<a href="bankStat2.jsp">查看每日分类银行数据</a>-<a href="ssc.jsp">六时数据</a><br/>
<a href="hunt.jsp">查看打猎数据</a>|<a href="hunt.jsp?type=1">当铺</a>|<a href="hunt.jsp?type=2">仓库</a>|<a href="hunt.jsp?type=3">钓鱼</a>|<a href="hunt.jsp?type=4">机会卡</a>|<a href="hunt.jsp?type=5">信件</a>|<a href="hunt.jsp?type=7">浮生记</a>|<a href="rich.jsp">大富翁</a>|<a href="family.jsp">家族</a><br/><br/>
<a href="xsl.jsp" onclick="return confirm('确认重新载入?')">重新载入xsl</a><br/>
<%}%>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
