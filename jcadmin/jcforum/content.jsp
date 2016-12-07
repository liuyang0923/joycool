<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%!
static JcForumServiceImpl service = new JcForumServiceImpl();
%><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}%>
<%
//恢复删除帖子	
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
	String forumId = request.getParameter("forumId").trim();
	ForumBean forum=ForumCacheUtil.getForumCache(StringUtil.toInt(forumId));
		if(forum==null){%>
			<script>
			alert("论坛版块不存在!");
			window.navigate("content.jsp?forumId="+forumId);
			</script>
			<%return;
		}
	//恢复删除帖子数据
	service.updateForumContent("mark1=0,mark2=0,del_mark=0","id="+forumContent.getId());
	SqlUtil.executeUpdate("delete from jc_forum_del where content_id=" + forumContent.getId(),2);
	//恢复删除帖子回复数据
//	service.updateForumReply("del_mark=0","content_id="+forumContent.getId());
	//版块总帖子数量加一数据
	service.updateForum("total_count=total_count+1","id="+forum.getId());
	forum.setTotalCount(forum.getTotalCount()+1);
	ForumCacheUtil.addContentIds(forumContent);
	%>
	<script>
	alert("恢复成功!");
	window.navigate("content.jsp?forumId="+<%=forumId%>);
	</script>
	<%return;
	}
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId==-1){
response.sendRedirect("index.jsp");
return;
}
%>
<html>
	<head>
	 <script language="javascript" >
function operate2(){
     if (confirm('将还原被删除帖子,你确定要还原吗？')) {
       return true;
       } else {
        return false;
       }
      }
</script>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<H1 align="center">论坛内容后台</H1>
		<hr>
		<br/>
		<table width="95%" border="1" align="center">
			<th >编号</th>
			<th>标题</th>
			<th>内容</th><th>时间</th>
			<th>发言者</th>
			<th>删除者</th>
			<%--<th>回复</th>--%>
			<th>操作</th>
			<%List vec = service.getForumContentList2("select a.user_id duser,b.* from jc_forum_del a,jc_forum_content b where a.content_id=b.id and a.forum_id="+forumId);
				for (int i = 0; i < vec.size(); i++) {
					ForumContentBean forumContent = (ForumContentBean) vec.get(i);%>
			<tr>
					<td>
						<%=i + 1%>
					</td>
					<td>
						<%=forumContent.getTitle()%>
					</td>
					<td>
						<%=forumContent.getContent()%>
					</td>
					<td>
						<%=forumContent.getCreateDatetime()%>
					</td>
					<td>
						<%UserBean user = UserInfoUtil.getUser(forumContent.getUserId());
						if(user!=null){%>
						<%=user.getNickName()%>
						<%}else{%>匿名用户<%}
						%>
					</td>
					<td>
						<%UserBean delUser = UserInfoUtil.getUser(forumContent.getDUserId());
						if(delUser!=null){%>
						<%=delUser.getNickName()%>
						<%}
						%>
					</td>
					<%--<td>
						<a href="reply.jsp?contentId=<%=forumContent.getId()%>&amp;forumId=<%=forumId%>">查看</a>
					</td>--%>
					<td>
						<a href="content.jsp?restore=1&amp;forumId=<%=forumId%>&amp;contentId=<%=forumContent.getId()%>" onClick="return operate2()">还原</a>
					</td>
			</tr>
				<%}%>
		</table>
		<p align="center">
		<a href="/jcadmin/jcforum/index.jsp">返回论坛管理后台</a><br />
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		</p>
		<br/>
	</body>
</html>
