<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
	static CastleService castleService = CastleService.getInstance();
%><%
	
	SortedSet soilerQuery = SoldierThread.soilerQuery;
	CustomAction action = new CustomAction(request);
int del = action.getParameterInt("del");
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="造兵队列"><p>
<%
int limit = 20;
Iterator iterator = soilerQuery.iterator();
while(iterator.hasNext() && limit>0) {
	SoldierThreadBean bean = (SoldierThreadBean)iterator.next();
	if(del > 0) {
		iterator.remove();
		del--;
		continue;
	}
%>
(<%=bean.getUid()%>)<%=bean.getCount()%>个(<%=DateUtil.sformatTime(bean.getEndTime())%>)<br/>

<%limit--;}%>
<input name="del" value="1"/><br/>
<a href="soldier.jsp?del=$del">删除$del个</a><br/>
</p></card></wml>