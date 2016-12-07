<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int id=snowAction.getParameterInt("id");
int tid=snowAction.getParameterInt("tid");
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
int isGameOver=snowAction.isGameOver(mid);// 比赛结束跳到别的页面
if(isGameOver==2||isGameOver==0){
	out.clearBuffer();
	response.sendRedirect("fight.jsp?mid="+mid);return;
}else if(isGameOver==3||isGameOver==4){
	out.clearBuffer();
	response.sendRedirect("/fm/game/fmgame.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="投雪球"><p align="left"><%=BaseAction.getTop(request, response)%>
<% SnowQuestionBean bean= snowAction.getOneQuestion();
snowAction.setAttribute2("QuestionBean",bean);
 %>
请选择以下题目的答案,选择正确既能命中目标,错误则打飞.<br/>
题目:<%=bean==null?"题库里没有题目":net.joycool.wap.util.StringUtil.toWml(bean.getQuestion()) %><br/>
<a href="fight.jsp?qid=<%=bean.getId() %>&amp;a=1&amp;id=<%=id %>&amp;tid=<%=tid %>&amp;mid=<%=mid %>">A:1</a>&#160;<a href="fight.jsp?qid=<%=bean.getId() %>&amp;a=2&amp;id=<%=id %>&amp;tid=<%=tid %>&amp;mid=<%=mid %>">B:2</a>&#160;<a href="fight.jsp?qid=<%=bean.getId() %>&amp;a=3&amp;id=<%=id %>&amp;tid=<%=tid %>&amp;mid=<%=mid %>">C:3</a>&#160;<a href="fight.jsp?qid=<%=bean.getId() %>&amp;a=4&amp;id=<%=id %>&amp;tid=<%=tid %>&amp;mid=<%=mid %>">D:4</a><br/>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>