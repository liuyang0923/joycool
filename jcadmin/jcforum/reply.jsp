<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}%>
<%IJcForumService service = ServiceFactory.createJcForumService();
//增加版主	
if (null != request.getParameter("restore")) {
		String content = request.getParameter("contentId");
	int contentId = StringUtil.toInt(content);
	if(contentId<=0){%>
		<script>
		alert("帖子id有误！");
		window.navigate("index.jsp");
		</script>
	<%return;
	}
	ForumContentBean forumContent = service.getForumContent("id="+contentId);
	if(forumContent==null){%>
			<script>
			alert("帖子不存在!");
			window.navigate("content.jsp?forumId="+forumId);
			</script>
			<%return;
	}
	String forumId = request.getParameter("forumId");
	ForumBean forum=ForumCacheUtil.getForumCache(StringUtil.toInt(forumId));
		if(forum==null){%>
			<script>
			alert("论坛版块不存在!");
			window.navigate("content.jsp?forumId="+forumId);
			</script>
			<%return;
		}
	String reply = request.getParameter("replyId");	
	int replyId = StringUtil.toInt(reply);
	if(replyId<=0){%>
		<script>
		alert("回复帖子id有误！");
		window.navigate("index.jsp");
		</script>
	<%return;
	}
	ForumReplyBean forumReply = service.getForumReply("id="+replyId);
	if(forumReply==null){%>
			<script>
			alert("回复帖子不存在!");
			window.navigate("content.jsp?forumId="+forumId);
			</script>
			<%return;
	}
	//恢复删除帖子回复数据
	service.updateForumReply("del_mark=0","id="+forumReply.getId());
	//清空缓存
	OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP);
	%>
	<script>
	alert("恢复成功!");
	window.navigate("content.jsp?forumId="+forumId);
	</script>
	<%return;
}
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId==-1){
response.sendRedirect("index.jsp");
return;
}
int contentId=StringUtil.toInt(request.getParameter("contentId"));
if(contentId==-1){
response.sendRedirect("index.jsp");
return;
}
%>
<html>
	<head>
	 <script language="javascript" >
function operate2(){
     if (confirm('将还原被删除回复,你确定要还原吗？')) {
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
		<table width="800" border="1" align="center">
			<th >编号</th>
			<th>帖子id</th>
			<th>回复内容</th>
			<th>发言者</th>
			<th>操作</th>
			<%List vec = service.getForumReplyList("content_id="+contentId+" and del_mark=1");
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
						<%}
						%>
					</td>
					<td>
						<a href="content.jsp?restore=1&amp;replyId=<%=forumReply.getId()%>&amp;forumId=<%=forumId%>&amp;contentId=<%=forumReply.getContentId()%>" onClick="return operate2()">还原删除回复</a>
					</td>
			</tr>
				<%}%>
			<tr>
		</table>
		<p align="center">
		<a href="http://wap.joycool.net/jcadmin/jcforum/content.jsp?forumId=<%=forumId%>">返回上一级</a><br />
		<a href="http://wap.joycool.net/jcadmin/jcforum/index.jsp">返回论坛管理后台</a><br />
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		</p>
		<br/>
	</body>
</html>
