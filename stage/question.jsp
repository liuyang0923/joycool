<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.stage.*,net.wxsj.action.stage.*,java.util.*,net.wxsj.util.*,net.wxsj.framework.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");
if (!JoycoolInfc.checkLogin(request, response, true)) {
	return;
}

StageAction action = new StageAction();
action.question(request, response);

QuestionBean question = (QuestionBean) request.getAttribute("question");
ArrayList list = question.getAnswerList();

int i, count;
AnswerBean answer = null;

String result = (String) request.getAttribute("result");
%>
<wml>
<card title="定向越野任务">
<p align="left">
<%=BaseAction.getTop(request, response)%>
目标代号：<%=StringUtil.toWml(question.getTitle())%><br/>
--------<br/>
<%
if(question.getEnded() == 1){
%>
这个任务已经结束了。<br/>
<%
}
else if("hasTry".equals(result)){
%>
哎呀~你已经做过这个任务了，每个任务只能答一次哦！<br/>
<%
}
else {
%>
<%=StringUtil.toWml(question.getContent())%><br/>
<a href="<%=question.getTipUrl().replace("&", "&amp;")%>">到这里找答案&gt;&gt;</a><br/>
<%
    if(question.getType() == QuestionBean.CHOOSE){
        count = list.size();
        for(i = 0; i < count; i ++){
	        answer = (AnswerBean) list.get(i);
%>
<%=(i + 1)%>.<a href="answer.jsp?id=<%=question.getId() %>&amp;answer=<%=answer.getId()%>"><%=StringUtil.toWml(answer.getContent())%></a><br/>
<%
        }
    }
    else {
%>
知道答案的请回答:(只有一次机会哦)<br/>
<input type="text" name="answer" value="" maxlength="20"/><br/>
<anchor>确认回答
  <go href="answer.jsp?zzz=1" method="post">
	<postfield name="id" value="<%=question.getId()%>"/>
	<postfield name="answer" value="$answer"/>
  </go>
</anchor><br/>
<%
    }
}
%>
<a href="index.jsp">接受其他任务</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>