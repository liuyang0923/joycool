<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.action.jcforum.ForumxAction,net.joycool.wap.action.jcforum.NewsTypeBean"%><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
CustomAction action = new CustomAction(request);
String tip = "";
int add = action.getParameterInt("a");
if (add == 1){
	String title = action.getParameterNoEnter("title");
	if (title==null || "".equals(title) || title.length() > 20){
		tip = "没有输入论坛标题或标题太长.";
	} else {
		int lastId = 0;
		net.joycool.wap.util.db.DbOperation db = new net.joycool.wap.util.db.DbOperation(2);
		db.executeUpdate("insert into jc_forum (title,bad_user,mark,type) values ('" + StringUtil.toSql(title) + "','',0,0)");
		lastId = db.getLastInsertId();
		db.release();
		// 清掉缓存
		OsCacheUtil.flushGroup("forum");
		tip = "插入成功。论坛名:" + StringUtil.toWml(title) + ",论坛ID:" + lastId;
	}
}
List vec = ForumCacheUtil.getForumListCache();

%>
<html>
	<head>
		<title>添加论坛</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%=tip%><br/>
		<form action="addForum.jsp?a=1" method="post">
			请输入新论坛名称:<input type="text" name="title" />
			<input type="submit" value="确定" onclick="return confirm('确定增加论坛？')"/>
		</form>
		<table width="100%" border="1" align="center">
			<th >ID</th>
			<th>名称</th>
			<%for (int i = 0; i < vec.size(); i++) {
					ForumBean notice = (ForumBean) vec.get(i);
					if (notice != null){
						%><tr>
							<td><%=notice.getId()%></td>
							<td><%=StringUtil.toWml(notice.getTitle())%></td>						
						  </tr><%
					}
			  }%>
			
		</table>
	</body>
</html>