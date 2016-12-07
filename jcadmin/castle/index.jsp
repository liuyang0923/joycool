<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="top.jsp"%><%
String[] types = {"出兵","建造","运输","训练","攻防升级","其他"};
int[] times = new int[types.length];
int now = (int)(System.currentTimeMillis()/1000);
times[0] = SqlUtil.getIntResult("select min(end_time) / 1000 from cache_attack",5) - now;
times[1] = SqlUtil.getIntResult("select min(end_time) / 1000 from cache_building",5) - now;
times[2] = SqlUtil.getIntResult("select min(end_time) / 1000 from cache_merchant",5) - now;
times[3] = SqlUtil.getIntResult("select min(end_time) / 1000 from cache_soldier",5) - now;
times[4] = SqlUtil.getIntResult("select min(end_time) / 1000 from cache_soldier_smithy",5) - now;
times[5] = SqlUtil.getIntResult("select min(end_time) / 1000 from cache_common",5) - now;

%>
			<html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	城堡战争后台<br/><br/>
	<form action="user.jsp" method="get">
	id:<input type=text name="id">
	<input type=submit value="城主查询">
	</form>
	<form action="castle.jsp" method="get">
	id:<input type=text name="id"><br/>
	(<input type=text name="x">|<input type=text name="y">)
	<input type=submit value="城堡查询">
	</form>
	<form action="tong.jsp" method="get">
	id:<input type=text name="id">
	<input type=submit value="联盟查询">
	</form>
<br/>
<%for(int i=0;i<types.length;i++){
if(times[i] < -100000000) times[i] = 0;
%><%=types[i]%>:<%if(times[i]<-20){%><font color=red><b><%=times[i]%></b></font><%}else{%><%=times[i]%><%}%>,<%}%><br/>
	<br/>
	<form action="user.jsp?massdelete=1" method="post">
	ids:<textarea  name="ids" style="width:400px;height:150px;"></textarea>
	<input type=submit value="批量删除帐号" onclick="return confirm('确认批量删除以上帐号？')">
	</form>
	<br/>
	<form action="user3.jsp" method="post">
	ids:<textarea  name="ids" style="width:400px;height:150px;"></textarea>
	<input type=submit value="批量修正城主数据" onclick="return confirm('确认批量修正以上城主数据？')">
	</form>
	<br/>
	<br/>
	<a href="stat.jsp">城主排名</a>|<a href="stat.jsp?t=1">联盟排名</a><br/><br/>
	<a href="builds.jsp">建筑数据</a>|<a href="soldiers.jsp">兵种数据</a>|<a href="system.jsp">系统参数</a>|<a href="arts.jsp">宝物列表</a><br/><br/>
	</body>
</html>