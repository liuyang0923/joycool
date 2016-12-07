<%--
if(net.joycool.wap.util.SecurityUtil.isWap20(request)){
request.getRequestDispatcher("editContentX.jsp").forward(request, response);
return;
}
--%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
net.joycool.wap.bean.UserBean loginUser = (net.joycool.wap.bean.UserBean) session.getAttribute(net.joycool.wap.util.Constants.LOGIN_USER_KEY);
if(loginUser == null) {
	response.sendRedirect("/user/login.jsp");
	return;
}
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId<=0){
forumId=1;
}
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
session.setAttribute("forumcontent","true");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
发表主题于[<%=StringUtil.toWml(forum.getTitle())%>]<br/>
标题:<input name="title"  maxlength="30" value=""/><br/>
内容:<input name="content"  maxlength="5000" value=""/><br/>
<anchor title="确定">发表主题
    <go href="forum.jsp?forumId=<%=forumId%>" method="post">
    <postfield name="content" value="$content"/>
    <postfield name="title" value="$title"/>
    </go>
</anchor>
<a href="contentAttach.jsp?forumId=<%=forumId%>">贴图发表</a><br/>
<a href="forum.jsp?forumId=<%=forumId%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>