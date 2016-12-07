<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.ask.*"%><%
AskAction action=new AskAction(request,response);
String quickTime=request.getParameter("t");
String answer=request.getParameter("a");
int mid=action.getParameterInt("mid");
if(mid==0){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族问答" ontimer="<%=response.encodeURL("ask.jsp?mid="+mid+"&#38;a="+answer)%>"><timer value="<%=quickTime%>"/><p align="left">
<%=action.getQuickPrompt()%><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>