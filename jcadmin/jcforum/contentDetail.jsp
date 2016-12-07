<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}%>
<%
IJcForumService service = ServiceFactory.createJcForumService();
//恢复删除帖子	
if (null != request.getParameter("del")) {
	String content = request.getParameter("contentId");
	int contentId = StringUtil.toInt(content);
	if(contentId<=0){%>
		<script>
		alert("帖子id有误！");
		window.navigate("index.jsp");
		</script>
	<%return;
	}
	ForumContentBean forumContent = ForumCacheUtil.getForumContent(contentId);
	if(forumContent==null){%>
			<script>
			alert("帖子不存在!");
			window.navigate("index.jsp");
			</script>
			<%return;
	}
	//删除论坛帖子回复后删除帖子内容
	if (ForumCacheUtil.deleteReplyID(contentId)) {
		ForumCacheUtil.deleteContentID(contentId) ;
		}
	// 更新点击记录(前台)
	//if (forumContent.getCreateDatetime().substring(0, 10).equals(
	//		DateUtil.getToday())) {
	//	ForumCacheUtil.updateForum("today_count=today_count-1,total_count=total_count-1",
	//			"id=" + forumContent.getForumId(), forumContent.getForumId());
	//} else
	//	ForumCacheUtil.updateForum("total_count=total_count-1", "id="
	//			+ forumContent.getForumId(), forumContent.getForumId());
	//清空缓存
	ForumCacheUtil.flushForumContent(contentId);
	int forumId = forumContent.getForumId();
	%>
	<script>
	alert("删除成功!");
	window.navigate("contentDetail.jsp?forumId=<%=forumId%>");
	</script>
	<%return;
	}
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId==-1){
response.sendRedirect("index.jsp");
return;
}
//设置分页参数
int numberPage = 20;
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
	pageIndex = 0;
}
String prefixUrl = "contentDetail.jsp?forumId="+forumId;
int totalCount = service.getForumContentCount("forum_id="+forumId); 
int totalPageCount = totalCount / numberPage;
if (totalCount % numberPage != 0) {
	totalPageCount++;
}
if (pageIndex > totalPageCount - 1) {
	pageIndex = totalPageCount - 1;
}
if (pageIndex < 0) {
	pageIndex = 0;
}
// 取得要显示的消息列表
int start = pageIndex * numberPage;
int end = numberPage;
List vec = service.getForumContentList("forum_id="+forumId+" order by last_re_time desc , id desc limit "+ start + ", " + end);
%>
<html>
	<head>
	 <script language="javascript" >
function operate2(){
     if (confirm('将删除帖子,你确定吗？')) {
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
			<th >帖子ID</th>
			<th>标题</th>
			<th>内容</th>
			<th>发言者</th>
			<%--<th>删除者</th>--%>
			<th>状态</th>
			<th>回复</th>
			<th>操作</th>
			<th>操作</th>
			<%
				for (int i = 0; i < vec.size(); i++) {
					ForumContentBean forumContent = (ForumContentBean) vec.get(i);%>
			<tr>
					<td>
						<%=i + 1%>
					</td>
					<td>
						<%=forumContent.getId()%>
					</td>
					<td>
						<%=forumContent.getTitle()%>
					</td>
					<td>
						<%=forumContent.getContent()%>
					</td>
					<td>
						<%UserBean user = UserInfoUtil.getUser(forumContent.getUserId());
						if(user!=null){%>
						<%=user.getNickName()%>
						<%}else{%>匿名用户<%}
						%>
					</td>
					<%--<td>
						<%UserBean delUser = UserInfoUtil.getUser(forumContent.getDUserId());
						if(delUser!=null){%>
						<%=delUser.getNickName()%>
						<%}else{%>空<%}
						%>
					</td>--%>
					<td>
						<%if(forumContent.getDelMark()==1){%>已删除<%}else{%>正常<%}%>
					</td>
					<td>
						<a href="replyDetail.jsp?contentId=<%=forumContent.getId()%>&amp;forumId=<%=forumId%>">查看</a>
					</td>
					<td>
						<%if(forumContent.getDelMark()==1){%>不能操作<%}else{%><a href="editContent.jsp?contentId=<%=forumContent.getId()%>">更新</a><%}%>
						
					</td>
					<td>
						<a href="contentDetail.jsp?del=true&amp;contentId=<%=forumContent.getId()%>" onClick="return operate2()">删除</a>
					</td>
			</tr>
				<%}%>
			<tr>
		</table>
		<p align="center">
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
</P><br>
		<p align="center">
		<a href="http://wap.joycool.net/jcadmin/jcforum/index.jsp">返回论坛管理后台</a><br />
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		</p>
		<br/>
	</body>
</html>
