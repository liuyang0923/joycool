<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId<=0){
forumId=1;
}
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
session.setAttribute("forumcontent","true");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发表主题</title>
</head>
<body>
<p align="left">

<%=BaseAction.getTop(request, response)%>
<font size="1">发表主题于[<%=StringUtil.toWml(forum.getTitle())%>]</font><br/>
<form method="post" action="<%=response.encodeURL("forum.jsp?forumId="+forumId)%>">
<font color="brown">
标题:<input type="text" name="title" maxlength="30" value="" size="30"/><br/>
内容:<textarea name="content" rows="6" cols=40 maxlength="5000"/></textarea><br/>
</font>
<input type="submit" value="发表主题">|<a href="contentAttach.jsp?forumId=<%=forumId%>">贴图发表</a><br/>
</form>
<a href="forum.jsp?forumId=<%=forumId%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<font color="brown">
<%=BaseAction.getBottomShort(request, response)%>
</font>
</p>
</body>
</html>