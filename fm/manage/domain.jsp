<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
if(fmUser==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
FamilyHomeBean fm =FamilyAction.getFmByID(fmUser.getFm_id());

%>
<wml><card title="家族域名"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(fm.getFm_level()<3){%>家族等级达到4级后才能修改!<%}else{%>
请到<a href="/jcforum/forum.jsp?forumId=11799">+家族玩家交流区+</a>发帖申请修改域名，然后等待管理员审批
<%}%><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>