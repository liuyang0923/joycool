<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.action.StatisticAction"%><%
response.setHeader("Cache-Control","no-cache");
StatisticAction statis=new StatisticAction();
int usercount=statis.getUserCount("2006-9-1","1=1");
int mobilecount=statis.getMobileCount("2006-9-1","1=1");
int count1=statis.getNewUserNumber("2006-9-1","login_count=1 ");
int count2=statis.getNewUserNumber("2006-9-1","login_count between 2 and 5 ");
int count3=statis.getNewUserNumber("2006-9-1","login_count between 6 and 15 ");
int count4=statis.getNewUserNumber("2006-9-1","login_count>15 ");
%>
<html>
<head>
</head>
<body>
乐酷忠实用户分析<br/><br/>
<table width="800" border="1">
<tr><td>9月份的新用户总数</td><td ><%=usercount%></td></tr>
<tr><td>9月份的独立手机号总数（包括老用户数）</td><td ><%=mobilecount%></td></tr>
<tr><td>9月份</td><td >总体情况</td><td >新号码</td><td>老号码</td></tr>
<tr><td>登陆1次的用户</td><td >总体情况</td><td ><%=count1%></td><td>老号码</td></tr>
<tr><td>登陆2-5次的用户</td><td >总体情况</td><td ><%=count2%></td><td>老号码</td></tr>
<tr><td>登陆6-15次的用户</td><td >总体情况</td><td ><%=count3%></td><td>老号码</td></tr>
<tr><td>登陆15次以上的用户</td><td >总体情况</td><td ><%=count4%></td><td>老号码</td></tr>
<tr><td>每天都登陆的人</td><td >总体情况</td><td >新号码</td><td>老号码</td></tr>
<tr><td>每周登陆3次以上的人（包括上面）</td><td >总体情况</td><td >新号码</td><td>老号码</td></tr>
<tr><td>每周登陆1-2次的</td><td >总体情况</td><td >新号码</td><td>老号码</td></tr>


<tr>
</table>

<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html> 

