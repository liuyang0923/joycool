<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
if(action.getLoginUser() == null) {
	response.sendRedirect(("/user/login.jsp"));
	return;
}
action.vote();
int contentId = action.getParameterInt("cid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="论坛投票" ontimer="<%=response.encodeURL("viewContent.jsp?contentId="+contentId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%>(3秒钟后跳回帖子)<br/>
注意，如果您多次投票，将以最后一票为准。<br/>
<a href="viewContent.jsp?contentId=<%=contentId%>">返回帖子</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>