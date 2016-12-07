<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.wxsj.framework.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
	return;
}

int nextQuestionId = StringUtil.toInt((String) session.getAttribute("nextQuestion"));
if(nextQuestionId < 1){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
}
else if(nextQuestionId > 30){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/end.jsp"));
}
else {
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/ask.jsp?questionId=" + nextQuestionId));
}
%>