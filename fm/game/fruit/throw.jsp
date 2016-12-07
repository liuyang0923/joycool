<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,net.joycool.wap.util.*"%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<% int type = action.getParameterInt("t");
if(type==1){// 生产水果%>
生产水果比例<br/>
<a href="o.jsp?cmd=fp&amp;fp=1&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">20%</a>|<a href="o.jsp?cmd=fp&amp;fp=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">40%</a>|<a href="o.jsp?cmd=fp&amp;fp=3&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">60%</a>|<a href="o.jsp?cmd=fp&amp;fp=4&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">80%</a>|<a  href="o.jsp?cmd=fp&amp;fp=5&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">100%</a><br/>
<a href="o.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">返回</a><br/>
<%}else if(type==2){// 变更生产水果比例%>
变更丢水果比例<br/>
<a href="o.jsp?cmd=cp&amp;cp=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">小心谨慎(20%)</a><br/>
<a href="o.jsp?cmd=cp&amp;cp=4&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">投石问路(40%)</a><br/>
<a href="o.jsp?cmd=cp&amp;cp=6&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">攻守平衡(60%)</a><br/>
<a href="o.jsp?cmd=cp&amp;cp=8&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">水果大军(80%)</a><br/>
<a href="o.jsp?cmd=cp&amp;cp=10&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">倾巢而出(100%)</a><br/>		
<a href="o.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">返回</a><br/>
<%}else if(type==3){// 扔水果提示
	String mark=action.getAttack();
%>
<%=mark %><br/>
<a href="game.jsp">返回水果战</a><br/>
<%}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>