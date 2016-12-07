<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.action.StatisticAction"%><%
response.setHeader("Cache-Control","no-cache");
StatisticAction statis=new StatisticAction();
int count1=statis.getNewUserNumber("2006-9-1","login_count=1 and game_point%1000=0 and point%1000=0");
int count2=statis.getNewUserNumber("2006-9-1","login_count between 2 and 5 and game_point%1000=0 and point%1000=0");
int count3=statis.getNewUserNumber("2006-9-1","login_count between 6 and 15 and game_point%1000=0 and point%1000=0");
int count4=statis.getNewUserNumber("2006-9-1","login_count between 16 and 30 and game_point%1000=0 and point%1000=0 ");
int count5=statis.getNewUserNumber("2006-9-1","login_count >30 and game_point%1000=0 and point%1000=0 ");
%>
<html>
<head>
</head>
<body>
9月份用户中，乐酷经验值，乐币能被1000整除的用户中<br/><br/>
<table width="800" border="1">
<tr><td></td><td >总体情况</td><td >新号码</td><td>老号码</td></tr>
<tr><td>登陆1次的</td><td >总体情况</td><td ><%=count1%></td><td>老号码</td></tr>
<tr><td>登陆2-5次的</td><td >总体情况</td><td ><%=count2%></td><td>老号码</td></tr>
<tr><td>登陆6-15次的</td><td >总体情况</td><td ><%=count3%></td><td>老号码</td></tr>
<tr><td>登陆16-30次的</td><td >总体情况</td><td ><%=count4%></td><td>老号码</td></tr>
<tr><td>登陆30次以上的</td><td >总体情况</td><td ><%=count5%></td><td>老号码</td></tr>


<tr>
</table>

<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html> 

