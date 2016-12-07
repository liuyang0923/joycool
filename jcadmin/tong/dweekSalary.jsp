<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.util.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*,net.joycool.wap.action.jcadmin.*"%><%@ page import="net.joycool.wap.action.tong.*"%><%@ page import="net.joycool.wap.bean.tong.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><html>
<head>
<title>帮会周工资扣除</title>
</head>
<body><%
Long t = (Long)application.getAttribute("lastWeekTongSalary");
boolean justsend = (t != null && System.currentTimeMillis() - t.longValue() < 1000*3600*24);

String type = request.getParameter("type");
if(type==null){%>
<a href="weekSalary.jsp?type=send">现在发工资</a><br/>
<a href="../manage.jsp">返回</a>

<%} else if(type.equals("send") && justsend) {%>
刚发过工资，确认再发？<br/>
<a href="weekSalary.jsp?type=force">发工资</a><br/>
<a href="../manage.jsp">返回</a>

<%} else {
TongAction.weekSalary(request);
List list = (List)request.getAttribute("salary");
application.setAttribute("lastWeekTongSalary", Long.valueOf(System.currentTimeMillis()));
%>

	
		<table width="800" border="1">
			<th>帮会ID</th>
			<th>帮会名称</th>
			<th>城墙</th>
			<th>当铺</th>
			<th>工资</th>
			<th>基金</th>
<%
for(int i = 0;i < list.size();i++){
int[] cur = (int[])list.get(i);
TongBean tong = TongCacheUtil.getTong(cur[0]);
%>
			<tr>	
					<td>
						<%=cur[0]%>
					</td>
					<td>
						<%=tong.getTitle()%>
					</td>
					<td>
						<%=cur[1]%>
					</td>
					<td>
						<%=cur[2]%>
					</td>
					<td>
						<%=(long)cur[1] * (long)(5000 * ((float)(int)(cur[2] / 1000000) / 10 + 1))%>
					</td>
					<td>
						<%=tong.getFund()%>
					</td>

			</tr>
<%}%>
		</table>
		<br>
<table>
帮会工资已发
</table>
<a href="../manage.jsp">返回</a>

<%}%>
</body>
</html>