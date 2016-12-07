<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.emperor.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
EmperorAction action = new EmperorAction(request,response);
%><%@include file="inc.jsp"%><%
List list = vsGame.getCheckUserList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="帝王之争"><p align="left"><%=BaseAction.getTop(request, response)%>
角色|贡献点<br/>
<% 
if (list.size()>0){
Collections.sort(list); // 按照贡献排序
	for (int i=0;i<list.size();i++){
		EmperorUserBean userA =(EmperorUserBean)list.get(i);
		if(userA.getSide()==0){%>[攻]<%}else{%>[守]<%}
		EmperorRoleBean tempRole = userA.getRole();
		if(tempRole != null){
%>
<a href="hro.jsp?uid=<%=userA.getUserId()%>"><%=tempRole.getName()%></a>|<%=userA.getContribute()%><br/>
<%
		}else{
%>
无|<%=userA.getContribute()%><br/>	
<%
		}
	}
}else{
%>
无人员排名<br/>
<%
}
%>
<a href="over.jsp">返回结束页</a><br/><a href="war.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>