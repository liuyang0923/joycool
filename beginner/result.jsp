<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.beginner.BeginnerQuestionAction" %><%
response.setHeader("Cache-Control","no-cache");
//判断页面刷新
if(session.getAttribute("beginnerAnswerCheck") == null){
	response.sendRedirect("/beginner/question.jsp");
	return;
} 
session.removeAttribute("beginnerAnswerCheck");
BeginnerQuestionAction action = new BeginnerQuestionAction(request);
action.result(request,response);
String result=(String)request.getAttribute("result");
String url="/beginner/question.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="新手趣闻问答" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>,3秒后自动跳转答题页面!<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card id="main" title="载入中..." onenterbackward="<%=url%>">
<onevent type="ontimer">
<go href="<%=url%>"/>
</onevent>
<timer value="30"/>
<p mode="nowrap" align="left">载入中...</p>
</card>
<%}%>
</wml>