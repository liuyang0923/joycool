<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.answer.*"%><%
	response.setHeader("Cache-Control","no-cache");
	HelpAction action = new HelpAction(request);
	String[] show={"没有此投票!","您好，您已经选择过了鲜花，不能重复选择。","您好，您已经选择过了臭鸡蛋，不能重复选择。","投票成功!","您好，您不能进行“采用”功能。","没有此回复!","已经采用回复!","不能对自己的帖子投票!","您好，一个帖子只可以采用3个答案!","帖子已经结束!","帖子结束成功!","您好，您只能结束自己发的帖子!"};
	int pid = action.getParameterInt("pid");
	int back = 0;
	if(pid > 0)
		back = action.goBack3(pid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(back == 99){
%><card title="有偿求助-返回"><p><%=BaseAction.getTop(request, response)%><%
String ss = (String)request.getAttribute("tip");
%><%=ss%><br/><a href="hpindex.jsp">返回有偿求助</a><br/><%=BaseAction.getBottom(request, response)%></p></card><%
}else if(pid > 0){
if(back <= 11){
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hppost.jsp?pid="+pid)%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
<%=show[back]%>3秒钟自动<a href="hppost.jsp?pid=<%=pid%>">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}else{
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hpindex.jsp")%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
您好，该主题不存在。3秒钟自动<a href="hpindex.jsp">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}
}else{
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hpindex.jsp")%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
您好，该主题不存在。3秒钟自动<a href="hpindex.jsp">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}
%>
</wml>