<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request,response);
int contentId=action.getParameterInt("contentId");
ForumContentBean cons = null;
if(contentId>0)
	cons = ForumCacheUtil.getForumContent(contentId);
if(cons==null){
action.redirect("forum.jsp");
return;
}
session.setAttribute("forumtitle","true");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>更改主题</title>
</head>
<body>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<form method="post" action="<%=response.encodeURL("viewContent.jsp?contentId="+contentId)%>">
主题:<input type="text" name="title" maxlength="30" value="<%=cons.getTitle()%>" size="30"/><br/>
内容:<textarea name="content" rows="6" cols=40 maxlength="1000"/><%=cons.getContent()%></textarea><br/>
<input type="submit" value="更改主题"><br/>
</form>
<a href="viewContent.jsp?contentId=<%=contentId%>">返回主题</a><br/>
<font color="brown">
<%=BaseAction.getBottom(request, response)%>
</font>
</p>
</body>
</html>