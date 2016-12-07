<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
action.question();
//取得登陆用户信息
//UserBean loginUser = action.loginUser;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status = UserInfoUtil.getUserStatus(loginUser.getId());
Vector answerList = (Vector)request.getAttribute("answerList");
int count, i;
WcQuestionBean question = (WcQuestionBean)request.getAttribute("question");
WcAnswerBean answer = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="世界杯博彩">
<p align="center"><%=StringUtil.toWml(question.getTitle())%></p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您现有<%=status.getGamePoint()%>乐币<br/>
==本场选项==<br/>
<%
count = answerList.size();
for(i = 0; i < count; i ++){
	answer = (WcAnswerBean) answerList.get(i);
%>
<%=(1 + i)%>.<%=StringUtil.toWml(answer.getTitle())%>(赔率1:<%=answer.getMoney()%>)<br/>
<%
}
%>
==我要下注==<br/>
乐币数:<input type="text" name="wager"  maxlength="9" value="100"/><br/>
<%
for(i = 0; i < count; i ++){
	answer = (WcAnswerBean) answerList.get(i);
	if(i > 0){
%>
|
<%
	}
%>
<anchor title="post">选<%=(i + 1)%>
  <go href="answer.jsp" method="post">
    <postfield name="questionId" value="<%=question.getId()%>"/>
    <postfield name="answerId" value="<%=answer.getId()%>"/>
	<postfield name="wager" value="$wager"/>
  </go>
</anchor>
<%
}
%>
<br/>
<a href="index.jsp">返回博彩首页</a><br/>
<a href="http://wap.joycool.net/forum/forumIndex.jsp?id=13">返回世界杯论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>