<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,jc.family.game.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史排行"><p align="left">
打雪仗历史排行榜<br/>
<% 
List list=snowAction.getFmGameList();
if(list!=null){
if(list.size()>0){
for(int i=0;i<list.size();i++){
SnowBean bean=(SnowBean)list.get(i);
int p=snowAction.getParameterInt("p");
String pstr="&amp;t="+p;
%>
<a href="historyDetails.jsp?mid=<%=bean.getMid() %><%=pstr %>" ><%=(i+1)+p*10 %>.<%=bean.getHoldTime() %>|<%if(bean.getFid1()==0){%>未参赛
<%}else{ %>
<%if(bean.getRank()==1){%>赢<%}else if(bean.getRank()==2){%>输<%}else if(bean.getRank()==3||bean.getRank()==4){%>平<%}%><%} %></a><br/>
<%}}}%>
<%if(request.getAttribute("gameFights")!=null&&!"".equals(request.getAttribute("gameFights"))){%>
<%=request.getAttribute("gameFights") %>
<%}%> 
<a href="snowBallFight.jsp">返回打雪仗</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>