<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	ActionBuyFriend buyAction = new ActionBuyFriend(request, response);
	
	boolean flag = buyAction.buyFriend();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买奴隶" ontimer="<%=response.encodeURL("mySlaves.jsp") %>"><timer value="30" /><p><%=BaseAction.getTop(request, response)%>
<%if(flag) {%>	<%=request.getAttribute("msg")%><br/>
3秒后自动返回朋友买卖<br/>
<a href="mySlaves.jsp">点击直接返回朋友买卖</a><br/>
<%} else {%><%=request.getAttribute("msg")%><br/><a href="<%=("mySlaves.jsp") %>">3秒后自动返回我的奴隶,点击直接返回</a><br/>
<%} %><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

