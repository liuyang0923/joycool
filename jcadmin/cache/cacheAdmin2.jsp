<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.bean.*"%><%!
static java.text.DecimalFormat numFormat = new java.text.DecimalFormat("0.0");
static String[] colors = {
"#00FF00", "#66DD00", "#88DD00", "#AADD00", 
"#DDDD00", "#FFDD00", "#FFAA00", "#FF8800", "#FF6600", "#FF0000", "#FF0000", "#FF00DD"};
%><%
response.setHeader("Cache-Control","no-cache");
Runtime rt = Runtime.getRuntime();
if(request.getParameter("gc")!=null){
rt.gc();
response.sendRedirect("cacheAdmin2.jsp");
return;
}

if(request.getParameter("clearAll")!=null){
CacheManage.clearAll();
response.sendRedirect("cacheAdmin2.jsp");
return;
}
boolean detail = (request.getParameter("detail")!=null);
%>
<html>
<head>
<title>缓存管理</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
<a href="cacheAdmin2.jsp">刷新</a>|
<a href="cacheAdmin2.jsp?detail=1">详细</a>|
<a href="/jcadmin/manage.jsp">返回</a><br/>
<font size=1>
<p>
使用内存：<%=(rt.totalMemory()-rt.freeMemory())/1024/1024%>M，分配内存：<%=rt.totalMemory()*100/rt.maxMemory()%>%
<%if(detail){%><br/>
分配内存：<%=rt.totalMemory()/1024/1024%>M，最大内存：<%=rt.maxMemory()/1024/1024%>M
<%}%>
</p>
<table cellpadding=1 cellspacing=0>
  <tr>
    <td>缓存组</td>
    <td>缓存数</td>
    <td>已使用</td>
<%if(detail){%>
    <td>最大数</td>
    <td>类型</td>
    <td></td>
<%}%>
    <td></td>
  </tr>
<%
List cacheList = CacheManage.getCacheList();
for(int i=0;i<cacheList.size();i++){
	ICacheMap cache = (ICacheMap)cacheList.get(i);
	float perc = (float)cache.size()*100/cache.maxSize();if(perc<0)perc=0;
	int icolor = (int)perc/10;
	if(icolor>10) icolor=10;
	if(cache.size()==0) continue;
	%>
  <tr>
    <td><%= CacheManage.getCacheName(i)%></td>
    <td align=right><font color="<%=colors[icolor]%>"><b><%=cache.size()%></b></font></td>
    <td align=right>
<font color="<%=colors[icolor]%>"><b>
    <%if(cache.maxSize()>0){%><%= numFormat.format(perc)%><%}else{%>0.0<%}%></b>%</font></td>
<%if(detail){%>
	<td align=right><%=cache.maxSize()%></td>
    <td><%=CacheManage.getCacheType(i).replace("CacheMap","")%></td>
<%}%>
    <td><a href="clearGroupCache2.jsp?i=<%=i%>">清<%if(detail){%>除<%}%></a></td>
<%if(detail){%>
	<td><a href="viewGroupCache2.jsp?i=<%=i%>">详细</a></td>
<%}%>
  </tr>
	<%
}
%>
</table>
</font>
<p>
<a href="cacheAdmin2.jsp?gc=1" >运行垃圾收集</a>
<a href="cacheAdmin2.jsp?clearAll=1" >清除所有缓存</a>
</p>
</body>
</html>