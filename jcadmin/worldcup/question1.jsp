<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
action.question();
//取得登陆用户信息
Vector answerList = (Vector)request.getAttribute("answerList");
int count, i;
WcQuestionBean question = (WcQuestionBean)request.getAttribute("question");
WcAnswerBean answer = null;
%>
<p align="center"><%=question.getTitle()%></p>
<p align="left">
==本场选项==<br/>
<%
count = answerList.size();
for(i = 0; i < count; i ++){
	answer = (WcAnswerBean) answerList.get(i);
%>
<%=(1 + i)%>.<%=answer.getTitle()%>(赔率1:<%=answer.getMoney()%>)<a href="answer.jsp?questionId=<%=answer.getQuestionId()%>&answerId=<%=answer.getId()%>">设为结果</a><br/>
<%
}
%>
<br/>
<a href="index.jsp">返回博彩首页</a><br/>
</p>