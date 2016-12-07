<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.jcforum.*,java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl" %><%response.setHeader("Cache-Control", "no-cache");
			if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}%>
<%!static IJcForumService service = ServiceFactory.createJcForumService(); %>
<html>
	<head>
<%
//增加版块
if (null != request.getParameter("a")) {
	CustomAction action = new CustomAction(request);
	
	int uid = action.getParameterInt("uid");
	int forumId = action.getParameterInt("forumId");
	
	ForumUserBean userBean = ForumCacheUtil.getForumUser(uid);
	userBean.setMForumId(forumId);
	service.updateForumUser(" m_forum_id = " + forumId, " user_id = " + uid);
%>
<script type="text/javascript">
alert("设置成功");
</script>
<%
}
%>
	</head>
	<body>
	<form action="editUser.jsp?a=1" method="post">
		用户ID:<input name="uid" value=""/>
		板块ID:<input name="forumId" value=""/>
		<input type="submit" value="设置"/>
	</form>
	</body>
</html>
