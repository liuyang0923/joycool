<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.reply();
QuestionSetBean set = (QuestionSetBean)request.getAttribute("set");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else if(action.isResult("confirm")){%>
<%=action.getTip()%><br/>
<a href="reply.jsp?a=1&amp;sid=<%=set.getId()%>">提交答题结果</a><br/>
<a href="reply.jsp?a=2&amp;sid=<%=set.getId()%>">重做一遍</a><br/>
<%}else{
QuestionReplyBean reply = (QuestionReplyBean)session.getAttribute("question-reply");
QuestionBean q = (QuestionBean)set.getQuestionList().get(reply.getCurrent());
String[] options = q.getOptions();
%>
第<%=reply.getCurrent()+1%>题:<%=StringUtil.toWml(q.getTitle())%><br/>
<%
for(int i = 0;i < options.length;i++){%>
<%=(char)('A'+i)%>.<a href="reply.jsp?a=4&amp;o=<%=i%>"><%=StringUtil.toWml(options[i])%></a><br/>
<%}%>
<%if(reply.getCurrent()>0){%>
<a href="reply.jsp?a=3&amp;sid=<%=set.getId()%>">回到上一题</a>|<a href="reply.jsp?a=2&amp;sid=<%=set.getId()%>">重做一遍</a><br/>
<%}%>
<%}%>

<%if(set!=null){%>
<a href="set.jsp?sid=<%=set.getId()%>">返回</a><br/>
<%}%>
<a href="index.jsp">缘分测试</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>