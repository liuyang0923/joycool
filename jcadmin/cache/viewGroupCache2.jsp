<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %><%@ page import="java.util.*"%><%@ page import="java.net.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%
int index = StringUtil.toInt(request.getParameter("i"));
List cacheList = CacheManage.getCacheList();
if(index<0){
BaseAction.sendRedirect("cacheAdmin2.jsp", response);
return;
}

String strKey = request.getParameter("key");
int rmikey = StringUtil.toInt(strKey);
Object rmKey = strKey;
if(rmikey != -1) {
	rmKey = new Integer(rmikey);
} else {
	Integer2 rmiikey = Integer2.parse(strKey);
	if(rmiikey != null)
		rmKey = rmiikey;
}

ICacheMap cache = (ICacheMap)cacheList.get(index);
if(rmKey!=null) {
cache.srm(rmKey);
BaseAction.sendRedirect("viewGroupCache2.jsp?i="+index, response);
return;
}

List list = cache.keyList();
CustomAction action = new CustomAction(request);
PagingBean paging = new PagingBean(action, list.size(), 20, "p", "go");
%><html>
<head>
<title>缓存管理</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
<%=paging.getCurrentPageIndex()+1%>/<%=paging.getTotalPageCount()%><br/>
<form action="viewCache.jsp?i=<%=index%>" method="get">
<input type="hidden" name="i" value="<%=index%>">
<input type="text" name="key">
<input type="submit" value="查看">
</form>
<table cellpadding=1 cellspacing=0>
  <tr>
    <td></td>
    <td>key</td>
    <td>size</td>
  </tr>
<%
int lsize = list.size();
for(int i=paging.getStartIndex();i<paging.getEndIndex() && i<lsize;i++){
Object ko = list.get(lsize-i-1);
String key = ko.toString();
Object value = cache.sgt(ko);
int size = 1;
if(value instanceof Collection) size = ((Collection)value).size();
if(value instanceof Map) size = ((Map)value).size();
%><tr><td width="30" align="center">
<a href="viewGroupCache2.jsp?i=<%=index%>&key=<%=URLEncoder.encode(key,"utf-8")%>">删</a></td><td><a href="viewCache.jsp?i=<%=index%>&key=<%=URLEncoder.encode(key,"utf-8")%>"><%=key%></a></td>
<td align="right"><%=size%></td>
</tr><%}%></table>
<%=paging.shuzifenye("viewGroupCache2.jsp?i="+index, true, "|", response)%>
<%if(paging.getTotalPageCount()>5){%>
<form action="viewGroupCache2.jsp?i=<%=index%>" method="post">
跳到<input name="go" type="text" maxlength="5" format="*N" value="1"/>页
<input type="submit" value="GO">
</form>
<br/><%}%>
<p>
<a href="cacheAdmin2.jsp?detail=1">返回</a>
</p>
</body>
</html>