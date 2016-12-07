<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);

int questionId = StringUtil.toInt(request.getParameter("questionId"));
int answerId = StringUtil.toInt(request.getParameter("answerId"));
boolean ended = false;
//最后一道问题
if(questionId == 30){
	ended = true;	
}
//不是最后一道问题
session.setAttribute("nextQuestion", "" + (questionId + 1));

KnifeQuestionBean question = KnifeFrk.getQuestionById(questionId);

String nextUrl = "/wxsj/knife/ask.jsp?questionId=" + (question.getId() + 1);
String backForwardUrl = "/wxsj/knife/ask.jsp?questionId=" + (question.getId() + 1);
if(ended){
	nextUrl = "/wxsj/knife/end.jsp";
	backForwardUrl = "/lswjs/index.jsp";
}

//当前问题
int currQuestionId = StringUtil.toInt((String)session.getAttribute("currQuestion"));
if(currQuestionId != questionId){	
	response.sendRedirect((nextUrl));
	return;
}

session.removeAttribute("currQuestion");

if(session.getAttribute("correctCount") == null){
	response.sendRedirect(("/wxsj/knife/start.jsp"));
	return;
}

int correctCount = StringUtil.toInt((String) session.getAttribute("correctCount"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="勇士爱军刀" onenterbackward="<%=(backForwardUrl)%>" ontimer="<%=response.encodeURL(nextUrl)%>">
<timer value="30"/>
<p align="left">
<%
//答对了
if(question.getAnswerId() == answerId){
	session.setAttribute("correctCount", "" + (correctCount + 1));

	//加上乐币
	int [] sts = new int[2];
	sts[0] = JoycoolInfc.GAME_POINT;
	sts[1] = JoycoolInfc.POINT;
	int [] svs = {100, 1};
	JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);
%>
恭喜你答对了！获得100乐币，1经验值<br/>
<a href="<%=(nextUrl)%>">下一题</a><br/>
<%
}
//答错了
else {
%>
很遗憾答错了…下次努力！<br/>
<a href="<%=(nextUrl)%>">下一题</a><br/>
<%
}
%>
<br/>
<a href="/lswjs/wagerHall.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>