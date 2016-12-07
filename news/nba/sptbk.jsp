<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	int show = 0;
	int mid = action.getParameterInt("mid");
	if(action.getLoginUser() != null && mid > 0)
		show = action.updSupport(mid);
	else
		show = -1;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="返回" ontimer="<%=response.encodeURL("alive.jsp?mid="+mid)%>"><timer value="30"/> <p><%
	if(show == 1){
	%>投票成功!3秒钟自动<a href="alive.jsp?mid=<%=mid%>">返回</a><br/><%
	}else if(show == -2){
	%>您已经支持过了,感谢您的支持!3秒钟自动<a href="alive.jsp?mid=<%=mid%>">返回</a><br/><%
	}else if(show == -3){
	%>请合法操作!3秒钟自动<a href="alive.jsp?mid=<%=mid%>">返回</a><br/><%
	}else{
	%>您没有<a href="../../user/login.jsp">登录</a>!还不能支持哦!3秒钟自动<a href="alive.jsp?mid=<%=mid%>">返回</a><br/><%
	}
%>
<a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>