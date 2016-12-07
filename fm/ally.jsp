<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.LoadResource,net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.util.SqlUtil,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyHomeBean fm=action.getFamily();
int id=action.getParameterInt("id");
if(fm==null){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族"><p align="left"><%=BaseAction.getTop(request, response)%>
=友联家族=<br/><%
List list=fm.getAllyList();
int count=0;
for(int i=0;i<list.size();i++){
	Integer fmId=(Integer)list.get(i);
	FamilyHomeBean home=FamilyAction.getFmByID(fmId.intValue());
	if(home!=null){
		count++;
		%><%=count%>.<a href="myfamily.jsp?id=<%=home.getId()%>"><%=StringUtil.toWml(home.getFm_name())%></a><br/><%
	}
}
%>&lt;<a href="myfamily.jsp?id=<%=id%>"><%=fm.getFm_nameWml()%></a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>