<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.impl.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.ArrayList"%><%@ page import="java.util.List"%><html>
	<b>每天金融统计报表&nbsp;&nbsp;<a href="index.jsp">返回</a></b><br><hr><br>
<%
long totalMoney = 0;
long totalUser = 0;

String sql = "select sum(game_point) from user_status";
totalMoney = SqlUtil.getLongResult(sql, Constants.DBShortName);

sql = "select count(id) from user_status";
totalUser = SqlUtil.getLongResult(sql, Constants.DBShortName);

long totalStoreMoney = 0;
long totalLoanMoney = 0;

sql = "SELECT sum(money) FROM jc_bank_store_money ";
totalStoreMoney = SqlUtil.getLongResult(sql, Constants.DBShortName);

sql = "SELECT sum(money) FROM jc_bank_load_money ";
totalLoanMoney = SqlUtil.getLongResult(sql, Constants.DBShortName);

totalMoney = totalMoney + totalStoreMoney - totalLoanMoney;
%>
	<table width="90%" align="center" border="1">
		<tr>
			<td width="30%">
				当前系统总乐币数
			</td>
			<td width="70%">
				<%= totalMoney %>
			</td>
		</tr>
		<tr>	
			<td width="30%">
				平均每个用户的乐币数
			</td>
			<td width="70%">
				<%= totalMoney/totalUser %>
			</td>
		</tr>
	</table>
	<br><br>
      <table width="90%" align="center" border="1">
		<tr>
			<td width="30%">
				当前银行存款总额
			</td>
			<td width="70%">
				<%= totalStoreMoney %>
			</td>
		</tr>
		<tr>	
			<td width="30%">
				当前银行贷款总额
			</td>
			<td width="70%">
				<%= totalLoanMoney %>
			</td>
		</tr>
	</table>
</html>