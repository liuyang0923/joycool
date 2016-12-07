<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.LoadResource,net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.util.SqlUtil,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fm=FamilyAction.getFmByID(fmLoginUser.getFm_id());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="设置友联"><p align="left"><%=BaseAction.getTop(request, response)%>
友联家族(<%=fm.getAllyCount()%>/<%=fm.getAllyLevel()%>)<br/><%
List list=fm.getAllyList();
int count=0;
for(int i=0;i<list.size();i++){
	Integer fmId=(Integer)list.get(i);
	FamilyHomeBean home=FamilyAction.getFmByID(fmId.intValue());
	if(home!=null){
		count++;
		%><%=count%>.<%=StringUtil.toWml(home.getFm_name())%>|<a href="allyadd.jsp?allyid=<%=fmId%>&amp;cmd=2">取消</a><br/><%
	}
}%>
<a href="allyadd.jsp">添加友联家族</a><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>