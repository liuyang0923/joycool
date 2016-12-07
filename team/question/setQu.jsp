<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.setQuestion();
QuestionSetBean set = (QuestionSetBean)request.getAttribute("set");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
int qi = action.getAttributeInt("question");
// 修改题目
QuestionBean q = (QuestionBean)set.getQuestionList().get(qi);
String[] options = q.getOptions();
%>
第<%=qi+1%>题:<%=StringUtil.toWml(q.getTitle())%><br/>
<%
for(int i = 0;i < options.length;i++){
%><%=(char)('A'+i)%>.<%=StringUtil.toWml(options[i])%><br/>
<%}%>
正确答案:<%=q.getAnswerChar()%><br/>
<select name="answer">
<%for(int i = 0;i < options.length;i++){%><option  value="<%=i%>"><%=(char)('A'+i)%></option><%}%>
</select>
<a href="setQ.jsp?answer=$answer&amp;sid=<%=set.getId() %>&amp;qid=<%=q.getId()%>">修改答案</a><br/>
<a href="setQ.jsp?sid=<%=set.getId() %>&amp;qid=<%=q.getId()%>">返回</a><br/>
<%}%>
<a href="index.jsp">返回首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>