<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.game.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int tid=snowAction.getParameterInt("tid");
int isEixts=snowAction.getOneTypeTool();
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
SnowGameToolTypeBean tool=(SnowGameToolTypeBean)request.getAttribute("oneTool");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏动态"><p align="left"><%=BaseAction.getTop(request, response)%>
<% List snowList=snowAction.getActivit(); 
if(snowList!=null){if(snowList.size()>0){
for(int i=0;i<snowList.size();i++){
SnowActivityBean ab=(SnowActivityBean)snowList.get(i);
%>
<%=ab.getFmName() %> 家族的<%=ab.getUName() %><%if(ab.getAType()==2){ %> 做了一个<%}else if(ab.getAType()==1){%>使用了<%} %><%if(ab.getTType()==4){%>投雪机<%}else if(ab.getTType()==7){ %>推雪机<%} %> <br/>
<%}}}else {%>
暂无游戏动态!<br/>
<%} %>
<%if(request.getAttribute("ActivityList")!=null&&!"".equals(request.getAttribute("ActivityList"))){%>
<%=request.getAttribute("ActivityList") %>
<%}%> 
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>