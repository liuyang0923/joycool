<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%
String group = request.getParameter("group");
Hashtable map = CacheAdmin.getCacheMap(group);
int totalCount = map.size();
int i = 0;
Enumeration enu = map.keys();
String key = null;
%>
<a href="index.jsp">返回首页</a><br>
本类当前共有<%=totalCount%>条缓存。<br>
<table width="100%" border="1">
<tr>
<td>序号</td>
<td>语句</td>
</tr>
<%
while(enu.hasMoreElements()){
	key = (String) enu.nextElement();
	i ++;
%>
<tr>
<td><%=i%></td>
<td><%=key%></td>
</tr>
<%
}
%>
</table>