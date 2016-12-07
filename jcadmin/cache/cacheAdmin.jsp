<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.bean.money.*"%><br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>
<br/>
<br/>
<table border=1>
  <tr>
    <td>缓存组</td>
    <td>缓存记录数</td>
    <td>操作</td>
  </tr>
<%
Hashtable cacheMap = CacheAdmin.getCacheMap();
Enumeration enu = cacheMap.keys();
while(enu.hasMoreElements()){
	String group = (String)enu.nextElement();
	Hashtable groupHash = (Hashtable)cacheMap.get(group);
	if(groupHash==null)continue;
	%>
  <tr>
    <td><%= group %></td>
    <td><%= groupHash.size() %></td>
    <td><a href="/jcadmin/cache/clearGroupCache.jsp?key=<%= group %>" >清除</a></td>
  </tr>
	<%
}
%>
</table>
<br/>
<a href="/jcadmin/cache/clearAllCache.jsp?user=cache&password=abc321">清除所有缓存（慎用）</a><br/>