<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.vreply();
QuestionSetBean set = (QuestionSetBean)request.getAttribute("set");
boolean detail = action.hasParam("d");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
QuestionReplyBean reply = (QuestionReplyBean)request.getAttribute("reply");
boolean my = (action.getLoginUser().getId() == set.getUserId());
%>
<%if(detail){%>
	<% for(int i2 = 0;i2 < set.getQuestionList().size();i2++){
	QuestionBean q = (QuestionBean)set.getQuestionList().get(i2); 
	String[] options = q.getOptions(); %>
	<%=i2+1%>.<%=StringUtil.toWml(q.getTitle())%><br/>
	
	<%for(int i = 0;i < options.length;i++){%><%=(char)('A'+i)%>.<%=StringUtil.toWml(options[i])%><br/><%}%>
	
	选择:<%=(char)('A'+reply.getAnswer(i2))%><%if(my){%>/答案:<%=(char)('A'+q.getAnswer())%><%}%><br/>
	<%}%>
<%}else{%>
	<a href="vreply.jsp?d=1&amp;id=<%=reply.getId()%>">查看详细选项数据</a><br/>
	<% for(int i2 = 0;i2 < set.getQuestionList().size();i2++){
	QuestionBean q = (QuestionBean)set.getQuestionList().get(i2); %>
	<%=i2+1%>.<%=StringUtil.toWml(q.getTitle())%><br/>
	选择:<%=q.getAnswerOption(reply.getAnswer(i2))%><br/><%if(my){%>答案:<%=q.getAnswerOption(q.getAnswer())%><br/><%}%>
	<%}%>
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