<!-- fanys-worldcup-2006-6-14-14 total-->
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
WorldCupAction  action=new WorldCupAction(request);
Vector answerList=null;
String questionId=request.getParameter("id");
if(request.getParameter("answerId")!=null){
	//删除一个结果
	action.deleteAnswer(request.getParameter("answerId"));
}
if(request.getParameter("resultId")!=null){
	//设置博彩场次结果
	action.result(Integer.parseInt(questionId),
			Integer.parseInt(request.getParameter("resultId")));
	//response.sendRedirect(("index.jsp"));
	BaseAction.sendRedirect("/jcadmin/worldcup/index.jsp", response);
}
answerList=action.getAnswerList(questionId);
%>
<form>
<table>
<tr><td align="center">序号</td><td align="center">结果名称</td><td align="center">赔率</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td></tr>
<%
WcAnswerBean answer;
for(int i = 0; i < answerList.size(); i ++){
	answer = (WcAnswerBean) answerList.get(i);
%>
<tr><td><%=(i + 1)%></td><td><%=answer.getTitle()%></td><td><%=answer.getMoney() %></td>
<td><a href="viewQuestion.jsp?id=<%=questionId%>&resultId=<%=answer.getId()%>">设为结果</a></td>
<td><a href="answer.jsp?answerId=<%=answer.getId()%>&id=<%=questionId%>">修改</a></td>
<td><a href="viewQuestion.jsp?id=<%=questionId%>&answerId=<%=answer.getId()%>">删除</a></td>
</tr>
<%}%>
</table>
<a href="answer.jsp?id=<%=questionId%>">
增加
</a>
&nbsp;&nbsp;
<a href="index.jsp">
返回
</a>
</body>
</html>