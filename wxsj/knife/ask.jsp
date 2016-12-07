<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="java.util.ArrayList"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
	return;
}

int questionId = StringUtil.toInt(request.getParameter("questionId"));

//记录当前问题ID
session.setAttribute("currQuestion", "" + questionId);

KnifeQuestionBean question = KnifeFrk.getQuestionById(questionId);
UserStatusBean us = JoycoolInfc.getUserStatus(loginUser.getId(), request);

//取得应该显示的问题ID
String backForwardUrl = "/wxsj/knife/end.jsp";
if(questionId < 30){
	backForwardUrl = "/wxsj/knife/ask.jsp?questionId=" + (questionId + 1);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="勇士爱军刀" onenterbackward="<%=(backForwardUrl)%>">
<p align="left">
<%
if(questionId == 1){
%>
<img src="/wxsj/knife/images/logo.gif" alt="图片"/><br/>
<%
}
%>
第<%=questionId%>题（共30题）：<br/>
<%=StringUtil.toWml(question.getQuestion())%><br/>
<%
ArrayList answerList = question.getAnswerList();
int i, count;
count = answerList.size();
String answer = null;
for(i = 0; i < count; i ++){
	answer = (String) answerList.get(i);
%>
<%=(i + 1)%>.<a href="/wxsj/knife/answer.jsp?questionId=<%=question.getId() %>&amp;answerId=<%=i%>"><%=StringUtil.toWml(answer)%></a><br/>
<%
}
%>
您现有乐币<%=us.getGamePoint()%>，经验<%=us.getPoint()%><br/>
<%
if(questionId == 1){
%>
<a href="/wxsj/knife/rule.jsp">游戏规则</a><br/>
<%
}
%>
<br/>
<a href="/lswjs/wagerHall.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>