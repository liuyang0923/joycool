<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.PsychologyAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PsychologyAction action=new PsychologyAction();
PsychologyAnswerBean answer=null;
Vector vecNext3Questions=null;
int nextPsychologyId=1;
action.answer(request,response);
nextPsychologyId=StringUtil.toInt((String)request.getAttribute("nextPsychologyId"));
answer=(PsychologyAnswerBean)request.getAttribute("answer");
vecNext3Questions=(Vector)request.getAttribute("next3Questions");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心理测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(answer.getExplanation())%><br/>
<a href="question.jsp?id=<%=answer.getPsychologyId()%>">重新选</a><br/>
<a href="question.jsp?id=<%=nextPsychologyId%>">下一题</a><br/>
<%
PsychologyBean psychology=null;
for(int i=0;i<vecNext3Questions.size();i++){
	psychology=(PsychologyBean)vecNext3Questions.get(i);
%><a href="question.jsp?id=<%=psychology.getId()%>"><%=StringUtil.toWml(psychology.getTitle())%></a><br/><%}%>
<a href="index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
