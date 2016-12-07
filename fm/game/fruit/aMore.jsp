<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,jc.family.game.pvz.*,java.util.*,jc.util.SimpleChatLog2,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.*"%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left">
<% List actionList=action.getSubList2(vsGame.getLogList(),10,"actFruitMore","aMore.jsp",false); 
String pageStr = (String)request.getAttribute("actFruitMore");
int p = action.getParameterInt("p");
int pageNum=actionList.size();
if(actionList.size()>10){pageNum=10;}
if(actionList!=null){
if(actionList.size()>0){
int tmp = 0;
for(int i=0;i<pageNum;i++){
%><%=p*10+1+i %>.<%=actionList.get(i)%><br/>
<%}%><%}}%>
<%if(actionList == null || actionList.size()<1){%>
暂无动态!<br/>
<%}%>
<%=pageStr!=null?pageStr:""%>
<a href="game.jsp">返回水果战</a>
<br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>