<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%>
<wml><card title="家族易帜"><p align="left">
要改变"<%=fmLoginUser.getFamilyName()%>"的名字?<br/>
请输入家族新名称(不超过6个字)<br/>
需要花费乐币10亿<br/>
<input name="fname" maxlength="6"/><br/>
<anchor title="改名">
确定<go href="rerename.jsp" method="post">
<postfield name="fname" value="$(fname)" /></go>
</anchor><br/>
<a href="management.jsp">返回家族管理</a><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>