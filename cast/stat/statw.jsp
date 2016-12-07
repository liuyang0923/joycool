<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.db.*"%><%
	
	CustomAction action = new CustomAction(request);
	String time = StringUtil.toSql(request.getParameter("t"));
	
	List list1 = SqlUtil.getObjectsList("select uid,name,point from castle_stat_week where type=5 and time='"+time+"' order by rank", 5);
	List list2 = SqlUtil.getObjectsList("select uid,name,point from castle_stat_week where type=6 and time='"+time+"' order by rank", 5);
	List list3 = SqlUtil.getObjectsList("select uid,name,point from castle_stat_week where type=7 and time='"+time+"' order by rank", 5);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="<%=time%>排行"><p>
【<%=time%>】周排名<br/>
[攻击排名前十]<br/>
<%for(int i=0;i<list1.size();i++){
	Object[] obj = (Object[])list1.get(i);
%><%=i+1%>.<a href="../user.jsp?uid=<%=obj[0]%>"><%=StringUtil.toWml((String)obj[1])%></a>/<%=obj[2]%>
<br/><%}%>
[防御排名前十]<br/>
<%for(int i=0;i<list2.size();i++){
	Object[] obj = (Object[])list2.get(i);
%><%=i+1%>.<a href="../user.jsp?uid=<%=obj[0]%>"><%=StringUtil.toWml((String)obj[1])%></a>/<%=obj[2]%>
<br/><%}%>
[抢夺排名前十]<br/>
<%for(int i=0;i<list3.size();i++){
	Object[] obj = (Object[])list3.get(i);
%><%=i+1%>.<a href="../user.jsp?uid=<%=obj[0]%>"><%=StringUtil.toWml((String)obj[1])%></a>/<%=obj[2]%>
<br/><%}%>
<a href="stats.jsp">返回城堡排名页</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>