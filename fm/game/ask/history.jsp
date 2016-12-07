<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,jc.family.game.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.ask.*"%><%@ page import="jc.family.*"%><%
AskAction snowAction=new AskAction(request,response);
List list=snowAction.getFmAskHistoryList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史排行"><p align="left">
问答历史排行榜<br/>
<% 
if(list!=null&&list.size()>0){
int p=snowAction.getParameterInt("p");
for(int i=0;i<list.size();i++){
GameHistoryBean bean=(GameHistoryBean)list.get(i);
%><a href="historyDetails.jsp?mid=<%=bean.getMid()%>" ><%=(i+1)+p*10 %>.<%=bean.getHoldTimeToString() %>|<%=bean.getRank()==-1?"未参赛":"第"+bean.getRank()+"名"%></a><br/><%
}%><%=request.getAttribute("paging")%>
<%}else{%>
暂无家族记录<br/>
<%}%> 
<a href="index.jsp">返回问答</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>