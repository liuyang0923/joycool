<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fHome =FamilyAction.getFmByID(fmLoginUser.getFm_id());
if(fHome==null){
response.sendRedirect("/fm/index.jsp");return;
}
%>
<wml><card title="家族公告"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fHome.getBulletin()!=null&&!"".equals(fHome.getBulletin())){%>
	<%=net.joycool.wap.util.StringUtil.toWml(fHome.getBulletin())%><br/>改变<%
}else{
	%>设置<%
}
%>为:(最多50字)<br/>
<input name="bulletin" maxlength="50"/><br/>
<anchor title="提取">
确定<go href="rebulletin.jsp" method="post">
<postfield name="bulletin" value="$(bulletin)" />
</go></anchor><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>