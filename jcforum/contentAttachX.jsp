<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
String result = (String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl==null){
	returnUrl = BaseAction.INDEX_URL;
}
String backTo = returnUrl; //(String)request.getAttribute("backTo");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>贴图发文</title>
</head>
<body>
<%
//表单
if(result == null){
String forumId = request.getParameter("forumId");
ForumBean forum = ForumCacheUtil.getForumCache(StringUtil.toInt(forumId));
if(forum==null){
	response.sendRedirect("index.jsp");
	return;
}
%>
<form name="forumContentAttachForm" ENCTYPE="multipart/form-data" method="post" action="<%=response.encodeURL("ForumContentAttach.do?forumId="+forumId)%>">
标题:<input type="text" name="title" maxlength="30" value="" size="30"/><br/>
内容:<textarea name="content" rows="6" cols=40 maxlength="5000"/></textarea><br/>
图片文件:(图片大小不能超过50K)<br/><input type="file" name="file"/><br/>
<input type="submit" name="submit" value="发表"/>
</form>
<%if(forum!=null){%>
<a href="/jcforum/forum.jsp?forumId=<%=forumId%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%}%>
<%
}
//留言结果
else {	
	String tip = (String)request.getAttribute("tip");
	ForumBean forum = (ForumBean)request.getAttribute("forum");
	//失败
	if("failure".equals(result)){
%>
<%=tip%><br/>
<%if(forum!=null){%>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%}%>
<a href="/jcforum/index.jsp">返回论坛首页</a><br/>
<%}else {
%>
发表成功！<br/>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回论坛首页</a><br/>
<%}
}%>
<br/>
<font color="brown">
<%=BaseAction.getBottomShort(request, response)%>
</font>
</body>
</html>