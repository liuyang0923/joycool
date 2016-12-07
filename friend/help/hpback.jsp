<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.answer.*"%><%
	response.setHeader("Cache-Control","no-cache");
	HelpAction action = new HelpAction(request);
	String[] show = {"","标题不能为空!","奖品不能为空!","内容不能为空!","标题长度不能超过20个字!","奖品长度不能超过50个字!","内容长度不能超过1000个字!","您好，有偿求助中只有女性才可求助，快去帮她们解决问题吧。","您好，每个用户每天只能发一个求助哦，还有问题明天再来求助吧。","您好，您所发的求助还未解决，不能发其他求助。","发帖成功!"};
	int back = action.goBack();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(back == 99){
%><card title="有偿求助-返回"><p><%=BaseAction.getTop(request, response)%><%
String ss = (String)request.getAttribute("tip");
%><%=ss%><br/><a href="hpindex.jsp">返回有偿求助</a><br/><%=BaseAction.getBottom(request, response)%></p></card><%
}else if(back>0 && back <= 6){
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hp.jsp")%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
<%=show[back]%>3秒钟自动<a href="hp.jsp">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}else {
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hpindex.jsp")%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%><%
	if(back >= 7 && back < 11){
	%><%=show[back]%><%
	}
%>3秒钟自动<a href="hpindex.jsp">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}
 %>
</wml>