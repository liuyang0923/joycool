<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%

response.setHeader("Cache-Control","no-cache");
WorldCupAction action=new WorldCupAction(request);
WcAnswerBean answer=null;
if(request.getParameter("id")!=null){
	int questionId=Integer.parseInt(request.getParameter("id"));
	action.deleteQuestion(questionId);
}
//WorldCupAction action = new WorldCupAction(request);
action.index();
//取得登陆用户信息
Vector questionList = (Vector)request.getAttribute("questionList");
int count, i;
WcQuestionBean question = null;
%>
<html>
<head>
</head>
<body>
<p align="center">世界杯博彩</p>
<p align="left">
==博彩场次==<br/>
<!-- fanys 2006-06-15 start -->
<table>
<tr><td align="center">序号</td><td align="center">名称</td><td align="center">开球时间</td><td align="center">比赛结果</td>
<td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td></tr>
<%

count = questionList.size();
for(i = 0; i < count; i ++){
	question = (WcQuestionBean) questionList.get(i);
%>
<tr><td><%=(i + 1)%></td><td><%=question.getTitle()%></td><td><%=question.getEndDatetime() %></td>
<td align="center">
<%
	String result="未开始";
	if(question.getResult()!=0){
		answer=action.getAnswer(question.getResult()+"");
		result=answer.getTitle();
	}

%>
<%=result%></td>
<td><a href="viewQuestion.jsp?id=<%=question.getId()%>">查看详情</a></td>
<td><a href="question.jsp?id=<%=question.getId()%>">修改</a></td>
<td><a href="index.jsp?id=<%=question.getId()%>">删除</a></td></tr>
<%
}
%>
</table>
<a href="<%=("question.jsp") %>">增加</a><br/>
<!-- fanys 2006-06-15 end -->
==博彩说明==<br/>
猜中结果的用户将按赔率获得相应乐币,如赔率为1：1.5,则下注100的用户将获得150乐币!届时中奖用户将会收到系统通知!<br/>
</p>
</body>
</html>
