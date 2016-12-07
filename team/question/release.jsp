<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.release();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
QuestionSetBean set = (QuestionSetBean)request.getAttribute("set");
List qList = set.getQuestionList();
for(int i = 0;i < qList.size();i++) {
QuestionBean q = (QuestionBean)qList.get(i);
%>
<a href="setQ.jsp?sid=<%=set.getId() %>&amp;qid=<%=q.getId()%>"><%=q.getTitle()%></a><br/>
<%}%>
<a href="addQ.jsp?sid=<%=set.getId()%>">添加题目</a><br/>

<%}%>
<a href="index.jsp">缘分测试</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>