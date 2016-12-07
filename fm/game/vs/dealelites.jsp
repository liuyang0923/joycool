<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
action.dealVsUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="添加成员"><p align="left"><%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="addelites.jsp">返回添加成员</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>