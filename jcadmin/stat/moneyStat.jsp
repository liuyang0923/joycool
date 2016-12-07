<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.spec.farm.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%!
static String[] names={
"其他","游戏","赌场","","","机会卡","打猎","","","新问答",
"丢弃物品","钓鱼","大转盘","宝箱","基金","大富翁","桃花源","六时彩","","",
"","","","","","","","","","",
"","","","","","","","","","",
"","","","","","","","","","",
};%><%
CustomAction action = new CustomAction(request, response);
long[] moneyStat = UserInfoUtil.getMoneyStat();
long[] moneyStatTime = UserInfoUtil.getMoneyStatTime();
int clear = action.getParameterIntS("clear");
long now = System.currentTimeMillis();
if(clear >= 0) {
if(clear == 9999)
	for(int i = 0;i<20;i++) {
		moneyStat[i] = 0;
		moneyStatTime[i] = now;
	}
else {
	moneyStat[clear] = 0;
	moneyStatTime[clear] = now;
	
}
action.redirect("moneyStat.jsp");
return;
}

%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<h3>乐币统计</h3>
		<table width="260">
			<tr>
				<td>
					name
				</td>
				<td>
					money
				</td>
				<td>
					统计时间
				</td>
				<td>
				</td>
			</tr>
			<%for (int i = 0; i < moneyStat.length; i++) {
			if(names[i].length()==0) continue;
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
				<td width=40>
					<a href="moneyStat.jsp?clear=<%=i%>">清除</a>
				</td>
			</tr>
			<%}%>
		</table>
		<a href="moneyStat.jsp?clear=9999">清除全部</a><br/><br/>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
