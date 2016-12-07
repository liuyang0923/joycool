<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction,net.joycool.wap.bean.jcforum.ForumRcmdBean"%><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
ForumAction action = new ForumAction(request);
String tip = "";
boolean isSee = false;
ForumBean forumBean = null;
ForumRcmdBean rcmdBean = null;
ForumContentBean contentBean = null;
int add = action.getParameterInt("add");
int del = action.getParameterInt("del");
int see = action.getParameterInt("see");
if (add > 0){
	contentBean = ForumCacheUtil.getForumContent(add);
	if (contentBean == null){
		tip = "您查找的帖子不存在或已被删除.";
	} else {
		if (ForumAction.rcmdMap.containsKey(new Integer(add))){
			tip = "此ID已存在.";
		} else {
			int lastId = 0;
			rcmdBean = new ForumRcmdBean();
			rcmdBean.setContentId(contentBean.getId());
			rcmdBean.setRcmdTime(System.currentTimeMillis());
			lastId = action.getForumService().addRcmd(rcmdBean);
			if (lastId > 0){
				rcmdBean.setId(lastId);
				ForumAction.rcmdMap.put(new Integer(rcmdBean.getContentId()),rcmdBean);
			} else {
				tip  = "添加错误.";
			}
		}
	}
}
if (del > 0){
	rcmdBean = ForumAction.getRcmd(del);
	if (rcmdBean == null){
		tip = "此帖子没有被推荐.";
	} else {
		contentBean = ForumCacheUtil.getForumContent(rcmdBean.getContentId());
		if (contentBean == null){
			tip = "您要删除的帖子不存在.";
		} else {
			SqlUtil.executeUpdate("delete from jc_forum_rcmd where id=" + rcmdBean.getId(),2);
			ForumAction.rcmdMap.remove(new Integer(rcmdBean.getContentId()));
		}
	}
}
if (see > 0){
	rcmdBean = ForumAction.getRcmd(see);
	if (rcmdBean == null){
		tip = "没有找到该精帖.";
	} else {
		contentBean = ForumCacheUtil.getForumContent(rcmdBean.getContentId());
		if (contentBean == null){
			tip = "您要删除的帖子不存在.";
		} else {
			isSee = true;
		}
	}
}
List list = ForumAction.getRcmdList();
%>
<html>
	<head>
		<title>精帖推荐</title>
	</head>
	<body>
		<H1 align="center">精帖推荐</H1>
		<%=tip%><br/>
<%if (isSee){
%><a href="rcmd.jsp">返回</a><br/><%
if (contentBean != null){
UserBean user = UserInfoUtil.getUser(contentBean.getUserId());
%>标题:<%=StringUtil.toWml(contentBean.getTitle())%><br/>
(<%=contentBean.getCount()%>阅|<%=contentBean.getReply()%>回)<br/>
作者:<%=user!=null?user.getNickNameWml():"游客"%><br/>
时间:<%=DateUtil.formatSqlDatetime(contentBean.getCreateTime())%><br/>
内容:<%=StringUtil.toWml(contentBean.getContent())%><br/>
<%
}
} else {
%><form action="rcmd.jsp" method="post">
	Id=<input type="text" name="add" />
	<input type="submit" value="添加精帖" />
</form>
<table border = "1">
	<tr>
		<td>序号</td>
		<td>数据库ID</td>
		<td>所属论坛板块</td>
		<td>帖子id</td>
		<td>帖子标题</td>
		<td>添加时间</td>
		<td>操作</td>
		<td>操作</td>
	</tr>
	<% if (list != null && list.size() > 0){
		for (int i = 0 ; i < list.size() ; i++){
			rcmdBean = ForumAction.getRcmd(((Integer)list.get(i)).intValue());
			if (rcmdBean != null){
				contentBean = ForumCacheUtil.getForumContent(rcmdBean.getContentId());
				if (contentBean != null){
					forumBean = ForumCacheUtil.getForumCache(contentBean.getForumId());
					if (forumBean != null){
						%><tr>
							<td><%=i+1%></td>
							<td><%=rcmdBean.getId()%></td>
							<td><%=StringUtil.toWml(forumBean.getTitle())%></td>
							<td><%=rcmdBean.getContentId()%></td>
							<td><%=StringUtil.toWml(contentBean.getTitle())%></td>
							<td><%=DateUtil.formatSqlDatetime(rcmdBean.getRcmdTime())%></td>
							<td><a href="rcmd.jsp?see=<%=rcmdBean.getContentId()%>">查看</a></td>
							<td><a href="rcmd.jsp?del=<%=rcmdBean.getContentId()%>" onclick="return confirm('真的要删除<%=rcmdBean.getContentId()%>')">删除</a></td>
						</tr><%
					}
				}
			}
		}
	}%>
</table>
<%
}
%>
	</body>
</html>
