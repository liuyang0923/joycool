<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.job.CardBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
int contentId =StringUtil.toInt(request.getParameter("contentId"));
String title = request.getParameter("title");
String content = request.getParameter("content");
 if (contentId<=0){%>
	<script>
	alert("回复帖子id有误！");
	window.navigate("index.jsp");
	</script>
<%}else if (title == null || title.equals("")) {%>
	<script>
	alert("帖子标题不能为空！");
	window.navigate("index.jsp");
	</script>
<%}else if (content == null || content.equals("")) {%>
	<script>
	alert("帖子内容不能为空！");
	window.navigate("index.jsp");
	</script>
<%}
ForumContentBean forumContent = ForumCacheUtil.getForumContent(contentId);
	if(forumContent==null){%>
			<script>
			alert("帖子不存在!");
			window.navigate("index.jsp");
			</script>
			<%return;
	}
int forumId=forumContent.getForumId();
ForumCacheUtil.updateForumContent("title='"+title+"',content='"+content+"'",
				"id=" + forumContent.getId(), forumContent.getId());
%>
	<script>
	alert("更新成功!");
	window.navigate("contentDetail.jsp?forumId=<%=forumId%>");
	</script>
<%return;%>