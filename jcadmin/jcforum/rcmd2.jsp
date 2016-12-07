<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction,net.joycool.wap.bean.jcforum.ForumRcmdBean,net.joycool.wap.bean.jcforum.ForumRcmdBean2"%>
<%! static String weekStr[] = {"","周一","周二","周三","周四","周五","周六","周日",}; %>
<%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
ForumAction action = new ForumAction(request);
String tip = "";
boolean isModify = false;
ForumBean forumBean = null;
ForumRcmdBean2 rcmdBean2 = null;
ForumContentBean contentBean = null;
int modify = action.getParameterInt("m");
int editId = action.getParameterInt("eid");
if (modify > 0){
	int fid = action.getParameterInt("fid");
	String content = action.getParameterNoEnter("c");
	rcmdBean2 = ForumAction.getRcmd2(modify);
	if (fid <=0 ){
		tip = "论坛:" + fid + "不存在.";
	} else if (content == null || (content.length() < 0 || content.length() > 20)){
		tip = "没有写说明文字,或说明文字超过20字.";
	} else if (rcmdBean2 == null){
		tip = "记录不存在";
	} else {
		forumBean = ForumCacheUtil.getForumCache(fid); 
		if (forumBean == null){
			tip = "论坛:" + fid + "不存在.";
		} else {
			SqlUtil.executeUpdate("update jc_forum_rcmd2 set forum_id=" + forumBean.getId() + ",content='" + StringUtil.toSql(content) + "',rcmd_time=now() where id=" + rcmdBean2.getId(),2);
			rcmdBean2.setForumId(forumBean.getId());
			rcmdBean2.setContent(content);
			ForumAction.rcmdMap2.put(new Integer(rcmdBean2.getId()),rcmdBean2);
		}
	}
}
if (editId > 0){
	forumBean = ForumCacheUtil.getForumCache(editId);
	isModify = true;
}
List list = ForumAction.getRcmdList2();
%>
<html>
	<head>
		<title>论坛推荐管理</title>
	</head>
	<body>
		<H1 align="center">论坛推荐管理</H1>
<%=tip%><br/>
<%if (isModify){
%><a href="rcmd2.jsp">返回</a><br/>
修改论坛内容:<br/>
<form action="rcmd2.jsp?m=<%=editId%>" method="post">
论坛ID:<br/><input type="text" name="fid" /><br/>
文字说明(20字):<br/><input type="text" name="c" size="100%" maxlength="20"/><br/>
<input type="submit" value="确认修改" />
</form>
<%
} else {
%>
<table border = "1">
	<tr>
		<td>序号</td>
		<td>星期</td>
		<td>论坛id</td>
		<td>推荐论坛板块</td>
		<td>说明文字</td>
		<td>操作</td>
	</tr>
	<% if (list != null && list.size() > 0){
		for (int i = 0 ; i < list.size() ; i++){
			rcmdBean2 = ForumAction.getRcmd2(((Integer)list.get(i)).intValue());
			if (rcmdBean2 != null){
				forumBean = ForumCacheUtil.getForumCache(rcmdBean2.getForumId());
					%><tr>
						<td><%=rcmdBean2.getId()%></td>
						<td><%=weekStr[rcmdBean2.getWeek()]%></td>
						<td><%=rcmdBean2.getForumId()%></td>
						<td><%=forumBean==null?"没有选择":StringUtil.toWml(forumBean.getTitle())%></td>
						<td><%=rcmdBean2.getContentWml()%></td>
						<td><a href="rcmd2.jsp?eid=<%=rcmdBean2.getId()%>">修改</a></td>
					</tr><%
			}
		}
	}%>
</table>
<%
}
%>
	</body>
</html>