<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.servlet.WGameServlet"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.framework.*"%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
String query = "SELECT id  FROM user_info where id>3600000 and TO_DAYS(now()) - TO_DAYS(create_datetime)=1 order by id desc limit 1";
ResultSet rs = dbOp.executeQuery(query);
int yesterdayMaxId = 0;
if(rs.next()){
	yesterdayMaxId = rs.getInt("id");
}
int regCount = 0;
if(yesterdayMaxId>0){
	query = "SELECT count(id) as c_id FROM user_info where id>"+yesterdayMaxId+" and TO_DAYS(now()) - TO_DAYS(create_datetime) < 1";
	rs = dbOp.executeQuery(query);
	if(rs.next()){
		regCount = rs.getInt("c_id");
	}
}
dbOp.release();
int maxSessionCount = JoycoolSessionListener.maxSessionCount;
int totalCount = CountUtil.totalCount;
long now = System.currentTimeMillis();
long[] moneyStatTime = UserInfoUtil.getMoneyStatTime();// 最后一个用于保存启动时间
%>
<html>
	<head>
	</head>
<link href="farm/common.css" rel="stylesheet" type="text/css">
	<body>
<p align="center">
<table  cellspacing="2">
<tr><td width="100">新增用户数</td><td width="50" align=right><%=regCount%></td></tr>
<tr><td>用户在线峰值</td><td align=right><%=maxSessionCount%></td></tr>
<tr><td>记录的PV</td><td align=right><%=totalCount%></td></tr>
<tr><td>未记录的有效PV</td><td align=right><%=CountUtil.otherCount%></td></tr>
<tr><td>未记录的无效PV</td><td align=right><%=CountUtil.specialCount%></td></tr>
<tr><td>总跳转数</td><td align=right><%=LogUtil.totalRedirect%></td></tr>
<tr><td>总跳回数</td><td align=right><%=LogUtil.totalBack%></td></tr>
<tr><td>上次重启距离</td><td align=right><%=DateUtil.formatTimeInterval(now-WGameServlet.startTime)%></td></tr>
</table>
</p>
</body>
</html>