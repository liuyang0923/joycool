<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
String c="";
if(request.getParameter("c")!=null){
	c="&amp;c="+request.getParameter("c");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="雪币说明"><p align="left"><%=BaseAction.getTop(request, response)%>
雪币是打雪仗游戏中的兑换货币,可以在打雪仗中使用,兑换之后在游戏中不能再次兑换,除非花费１酷币才能再次兑换.兑换雪币之后未用完的累计到下次使用!账户中最多只能有１万雪币!<br/>
<a href="snowMoney.jsp?mid=<%=mid %><%=c %>">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>