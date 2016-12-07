<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}%>
<%
IJcForumService service = ServiceFactory.createJcForumService();
//增加版主	
if (null != request.getParameter("del")) {
	int replyId= StringUtil.toInt(request.getParameter("replyId"));
	if(replyId<=0){%>
		<script>
		alert("回复帖子id有误！");
		window.navigate("index.jsp");
		</script>
	<%return;
	}
	ForumReplyBean forumReply = ForumCacheUtil.getForumReply(replyId);
	if(forumReply==null){%>
			<script>
			alert("回复帖子不存在!");
			window.navigate("index.jsp");
			</script>
			<%return;
	}
	ForumContentBean forumContent = ForumCacheUtil.getForumContent(forumReply.getContentId());
	if(forumContent==null){%>
			<script>
			alert("帖子不存在!");
			window.navigate("index.jsp");
			</script>
			<%return;
	}
	//删除一条回复
	ServiceFactory.createJcForumService().delForumReply("id="+forumReply.getId());
	ForumCacheUtil.updateForumContent("reply=reply-1", "id=" + forumReply.getContentId(),
				forumReply.getContentId());
	//清空缓存
	//ForumCacheUtil.flushForumReply(forumReply.getId());
	// 清空缓存
	//ForumCacheUtil.flushForumContent(forumReply.getContentId());
	//清空论坛缓存
	//OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP);
	int contentId= forumContent.getId();
	%>
	<script>
	alert("删除成功!");
	window.navigate("replyDetail.jsp?contentId=<%=contentId%>");
	</script>
	<%return;
}
int contentId=StringUtil.toInt(request.getParameter("contentId"));
if(contentId==-1){
response.sendRedirect("index.jsp");
return;
}
ForumContentBean forumContent  = ForumCacheUtil.getForumContent(contentId);
if(forumContent==null){
response.sendRedirect("index.jsp");
return;
}
int forumId= forumContent.getForumId();
%>
<html>
	<head>
	 <script language="javascript" >
function operate2(){
     if (confirm('将删除回复,你确定吗？')) {
       return true;
       } else {
        return false;
       }
      }
</script>
	</head>
	<body>
		<H1 align="center">论坛内容后台</H1>
		<hr>
		<br/>
		<table border="1" align="center">
			<th >编号</th>
			<th>帖子id</th>
			<th>回复内容</th>
			<th>发言者</th>
			<th>操作</th>
			<th>操作</th>
			<%List vec = service.getForumReplyList("content_id="+contentId+" order by id asc");
				for (int i = 0; i < vec.size(); i++) {
					ForumReplyBean forumReply = (ForumReplyBean) vec.get(i);%>
			<tr>
					<td>
						<%=i + 1%>
					</td>
					<td>
						<%=forumReply.getContentId()%>
					</td>
					<td>
						<%=forumReply.getContent()%>
					</td>
					<td>
						<%UserBean user = UserInfoUtil.getUser(forumReply.getUserId());
						if(user!=null){%>
						<%=user.getNickName()%>
						<%}else{%>匿名<%}
						%>
					</td>
					<td>
						<a href="editReply.jsp?replyId=<%=forumReply.getId()%>&amp;forumId=<%=forumId%>&amp;contentId=<%=forumReply.getContentId()%>">更新</a>
					</td>
					<td>
						<a href="replyDetail.jsp?del=true&amp;replyId=<%=forumReply.getId()%>&amp;forumId=<%=forumId%>&amp;contentId=<%=forumReply.getContentId()%>" onClick="return operate2()">删除</a>
					</td>
			</tr>
				<%}%>
			<tr>
		</table>
		<p align="center">
		<a href="http://wap.joycool.net/jcadmin/jcforum/contentDetail.jsp?forumId=<%=forumId%>">返回上一级</a><br />
		<a href="http://wap.joycool.net/jcadmin/jcforum/index.jsp">返回论坛管理后台</a><br />
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		</p>
		<br/>
	</body>
</html>
