<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*,java.io.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.job.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.bean.job.CardBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
int forumId=StringUtil.toInt(request.getParameter("forumId"));
int replyId=StringUtil.toInt(request.getParameter("replyId"));
if(replyId<=0)
{
	BaseAction.sendRedirect("/jcadmin/jcforum/index.jsp", response);
	return;
}
ForumReplyBean forumReply=(ForumReplyBean)ForumCacheUtil.getForumReply(replyId);
if(forumReply==null)
{
	BaseAction.sendRedirect("/jcadmin/jcforum/index.jsp", response);
	return;
}

%>
<html >
<head>
<title>修改论坛</title>
</head>
<H1 align="center">论坛内容后台</H1>
<hr>
<table align="center" border="1" >
<form method="post" action="replyResult.jsp">
<tr>
	<th>ID</th>
	<td><%=forumReply.getId()%>
	<input type="hidden" name="replyId" value="<%=forumReply.getId()%>" />
	</td>
</tr>
<tr>
	<th>内容</th>
	<td>
		<%--<input type="text" name="content" value="" />--%>
		<textarea name=content rows=5 cols=60><%=forumReply.getContent()%></textarea>	
	</td>
</tr>
<tr>
	<td  align="center" colspan="2" ><input type="submit" name="submit" value="确定"/>
	<input type="reset" name="reset" value="重置"/>
	</td>
</tr>
</form>
</table>
<p align="center">
<a href="http://wap.joycool.net/jcadmin/jcforum/contentDetail.jsp?forumId=<%=forumId%>">返回上一级</a><br />
<a href="http://wap.joycool.net/jcadmin/jcforum/index.jsp">返回论坛管理后台</a><br />
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
</p>
</html>