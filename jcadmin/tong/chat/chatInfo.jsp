..<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.world.ChatWorld" %><%@ page import="java.util.Hashtable" %><%@ page import="java.util.Iterator" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.framework.BaseAction" %><%response.setHeader("Cache-Control", "no-cache");			
if (session.getAttribute("adminLogin") == null) {
		BaseAction.sendRedirect("/jcadmin/login.jsp", response);
		return;
	}
%>
<br>
<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>

<br/>
<br/>
<a href="/jcadmin/tong/chat/clearAllCache.jsp?user=cache&password=abc321">清除所有缓存（慎用）</a><br/>
<table border=1>
  <tr>
    <td>缓存组</td>
    <td>操作</td>
  </tr>
<%
Hashtable cacheMap = ChatWorld.getCacheMap();
Hashtable ht= (Hashtable)cacheMap.get("tong");
if(ht!=null){
Iterator it = ht.keySet().iterator();
while(it.hasNext()){
	String group = (String)it.next();
	TongBean tong = TongCacheUtil.getTong(StringUtil.toInt(group));
	if(tong==null){continue;}
	%>
  <tr>
    <td><%= tong.getTitle() %></td>
    <td><a href="/jcadmin/tong/chat/chatList.jsp?key=<%= group %>" >查看</a></td>
  </tr>
	<%
}
}
%>
</table>