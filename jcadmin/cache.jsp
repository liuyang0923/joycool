<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%
if(request.getParameter("clear") != null){
	Hashtable cache = CacheUtil.cache;
	Hashtable urlCache = CacheUtil.urlCache;
	if(cache != null){
		cache.clear();
	}
	if(urlCache != null){
		urlCache.clear();
	}
	//response.sendRedirect("cache.jsp");
	BaseAction.sendRedirect("/jcadmin/cache.jsp", response);
}
%>
<p><a href="cache.jsp?clear=1">清空缓存</a></p>
<p>缓存列表</p>
<table width="100%" border="2">
<%
Hashtable urlCache = CacheUtil.urlCache;
if(urlCache != null){
	Enumeration enu = urlCache.elements();
	int i = 1;
	while(enu.hasMoreElements()){
%>
<tr>
<td width="10%"><%=i%></td>
<td width="90%">http://wap.joycool.net/Column.do?<%=((String)enu.nextElement())%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>