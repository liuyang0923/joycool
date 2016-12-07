<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%
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
<title>贴图</title>
</head>
<body>
<%
//表单
if(result == null){
String contentId = request.getParameter("contentId");
ForumContentBean forumContent = ForumCacheUtil.getForumContent(StringUtil.toInt(contentId));
int pageIndex =  StringUtil.toInt((String) request.getParameter("pageIndex"));
if(pageIndex == -1) {
	pageIndex = 0;
}
%>
<form name="forumContentAttachForm" enctype="multipart/form-data" method="post" action="<%=response.encodeURL("ForumReplyAttach.do?contentId="+contentId+"&amp;pageIndex="+pageIndex)%>">
内容：<input type="text" name="content" value="你好啊"/><br/>
图片文件：(图片大小不能超过50K)<br/><input type="file" name="file"/><br/>
<input type="submit" name="submit" value="发表"/>
</form>
<%if(forumContent!=null){%>
<a href="/jcforum/viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;pageIndex=<%=pageIndex%>">返回主题</a><br/>
<%}%>
<%
}
//留言结果
else {	
	String tip = (String)request.getAttribute("tip");
	ForumContentBean forumContent = (ForumContentBean)request.getAttribute("forumContent");
	String pageIndex=(String)request.getAttribute("pageIndex");
	//失败
	if("failure".equals(result)){
%>
<%=tip%><br/>
<%if(forumContent!=null){%>
<a href="/jcforum/viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;pageIndex=<%=pageIndex%>">返回主题</a><br/>
<%}%>
<a href="/jcforum/index.jsp">返回论坛首页</a><br/>
<%}else {
%>
发表成功！<br/>
<a href="/jcforum/viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;pageIndex=<%=pageIndex%>">返回主题</a><br/>
<a href="/jcforum/index.jsp">返回论坛首页</a><br/>
<%}
}%>
<br/>
<%--<%= PositionUtil.getLastModuleUrl(request, response)%><br/>--%>
</body>
</html>