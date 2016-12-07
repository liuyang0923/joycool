<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.home.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.bean.home.*"%><%
response.setHeader("Cache-Control","no-cache");

CustomAction action= new CustomAction(request);
String key = action.getParameterString("key");
if("null".equals(key))
	key=null;
int del = action.getParameterIntS("d");
if(del>=0){
	String key2 = SqlUtil.getStringResult("select `key` from simple_chat where id="+del, "chat");
	SqlUtil.executeUpdate("delete from simple_chat where id=" + del, 3);
	SimpleChatLog sc = SimpleChatLog.getChatLog(key2);
	sc.clear();
	sc.getChatList();
}
PagingBean paging = new PagingBean(action,10000,30,"p");
List l;
if(key==null)
	l = SqlUtil.getObjectsList("select id,content,time,`key` from simple_chat order by id desc limit " + paging.getStartIndex()+","+paging.getCountPerPage(), 3);
else
	l = SqlUtil.getObjectsList("select id,content,time,`key` from simple_chat where `key`='"+key+"' order by id desc limit " + paging.getStartIndex()+","+paging.getCountPerPage(), 3);
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table align="center" border="1" width="95%">
<td width="60">序号</td>
<td>组</td>
<td>内容</td>
<td>时间</td>
<td width="40">操作</td>
<%
for(int i = 0; i < l.size(); i ++){
Object[] objs = (Object[])l.get(i);
%>
<tr>
<td><%=objs[0]%></td>
<td width="40"><a href="simpleChat.jsp?key=<%=objs[3]%>">(<%=objs[3]%>)</a></td>
<td><%=objs[1]%></td>
<td width="80px"><%=objs[2].toString().substring(10,16)%></td>
<td>
<a href="simpleChat.jsp?key=<%=key%>&d=<%=objs[0]%>&p=<%=paging.getCurrentPageIndex()%>">删</a>
</td>
</tr>
<%
}
%>
</table>
<p align="center">
<%=paging.shuzifenye("simpleChat.jsp?key="+key,true,"|",response)%><br/>
</p>
<p align="center"><a href="simpleChat.jsp?key=<%=key%>&d=0">清空缓存</a></p>
</body>
</html>