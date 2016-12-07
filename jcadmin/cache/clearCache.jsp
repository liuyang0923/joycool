<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%
String clear = request.getParameter("clear");
if(clear != null){
	if("news".equals(clear)){
		OsCacheUtil.flushGroup(OsCacheUtil.NEWS_GROUP);
	}	
	else if("column".equals(clear)){
		CacheManage.column.clear();
	}	
	//response.sendRedirect("clearCache.jsp");
	BaseAction.sendRedirect("/jcadmin/cache/clearCache.jsp", response);
	return;
}
%>

缓存管理<br>
<br>
<a href="clearCache.jsp?clear=news">清空新闻缓存</a><br>
<a href="clearCache.jsp?clear=column">清空树状页面缓存</a><br>