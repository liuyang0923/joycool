<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.answer.*"%><%
	response.setHeader("Cache-Control","no-cache");
	HelpAction action = new HelpAction(request);
	String[] show = {"回复成功!","输入内容不能为空!","内容长度不能超过100字!","帖子已经结束!","不能给自己的帖子回复哦!","操作太快了,休息一下吧!"};
	int pid = action.getParameterInt("pid");
	int back = 0;
	if(pid > 0)
		back = action.goBack2(pid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%
if(back == 99){
%><card title="有偿求助-返回"><p><%=BaseAction.getTop(request, response)%><%
String ss = (String)request.getAttribute("tip");
%><%=ss%><br/><a href="hpindex.jsp">返回有偿求助</a><br/><%=BaseAction.getBottom(request, response)%></p></card><%
}else if(pid <= 0){
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hpindex.jsp")%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
3秒钟自动<a href="hpindex.jsp">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}else if(back <= 5){
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hppost.jsp?pid="+pid)%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
<%=show[back]%>3秒钟自动<a href="hppost.jsp?pid=<%=pid%>">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}else{
%><card title="有偿求助-返回" ontimer="<%=response.encodeURL("hppost.jsp?pid="+pid)%>"><timer value="30"/><p><%=BaseAction.getTop(request, response)%>
3秒钟自动<a href="hppost.jsp?pid=<%=pid%>">返回</a>!<br/><%=BaseAction.getBottom(request, response)%>
</p>
</card><%
}
 %>
</wml>