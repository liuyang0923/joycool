<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="jc.family.game.snow.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*,jc.family.game.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int his=snowAction.getOneMatch();
int mid=snowAction.getParameterInt("mid");
int t=snowAction.getParameterInt("t");
String str="?p="+t;
String pstr="&amp;t="+t;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="详细历史排行"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(his==1){ %>
请先登录!<br/>
<%}else if(his==2){%>
您不是家族成员!<br/>
<%}else if(his==3){%>
该赛事不存在!<br/>
<a href="history.jsp">返回历史排行</a><br/>
<%
	}else if(his==4){if(request.getAttribute("matchDetail")!=null){ SnowBean bean=(SnowBean)request.getAttribute("matchDetail");
%>
<%=bean.getHoldTime()%>|<%if(bean.getFid1()==0){%>未参赛<br/><%}else{%>
<%if(bean.getRank()==1){
%>赢<%}else if(bean.getRank()==2){
%>输<%}else if(bean.getRank()==3||bean.getRank()==4){
%>平<%}%><br/><%} }%>对阵双方<br/>
<%
	List list=snowAction.getFightList("historyDetails.jsp?mid="+mid+pstr,true,mid);
if(list!=null){
if(list.size()>0){
for(int i=0;i<list.size();i++){
SnowBean bean=(SnowBean)list.get(i);
%>
<a href="/fm/myfamily.jsp?id=<%=bean.getFid1() %>"><%if(bean.getRank()==1){ %> 【赢】<%}else if(bean.getRank()==3){%>【平】<%} %><%=bean.getFmName1() %>家族</a>|<a  href="../../myfamily.jsp?id=<%=bean.getFid2() %>"><%=bean.getFmName2()%><%if(bean.getFid2()!=0){%>家族<%}%></a><br/>
 <%} }else{%>无家族参赛!<br/><%}}else {%>无家族参赛!<br/><%} %><%if(request.getAttribute("pageFight")!=null&&!"".equals(request.getAttribute("pageFight"))){%>
<%=request.getAttribute("pageFight") %>
<%}%> 
<a href="history.jsp<%=str %>">返回历史排行</a><br/>
<%} %>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>