<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.action.jcforum.*,net.joycool.wap.action.jcforum.NewsTypeBean,net.joycool.wap.bean.PagingBean"%>
<%! static int COUNT_PRE_PAGE = 15; %>
<%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
ForumxAction action = new ForumxAction(request);
int id = action.getParameterInt("id");	//分类id
int del = action.getParameterInt("del");
NewsTypeBean typeBean = null;
PagingBean paging = new PagingBean(action,10000,COUNT_PRE_PAGE,"p");
String tip = "";
List newsList = null;
ForumContentBean con = null;
typeBean = action.getNewsType(id);
if (typeBean == null){
	tip = "查找的新闻分类不存在.";
} else {
	tip = StringUtil.toWml(typeBean.getName());
	if (del > 0){
		SqlUtil.executeUpdate("delete from forum_news where id=" + del + " and type=" + id,2);
		if(!SqlUtil.exist("select id from forum_news where id="+del,2)) {	// 如果还有这个帖子的新闻存在，不去除标签
			ForumContentBean con2 = ForumCacheUtil.getForumContent(del);
			if(con2!=null){
				con2.cancelNews();
				ForumAction.getForumService().updateForumContent("mark2="+con2.getMark2(), "id=" + con2.getId());
			}
		}
		ForumxAction.latestNews.clear();
	}
	newsList = SqlUtil.getIntList("select id from forum_news where type=" + typeBean.getId() + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE, 2);
}
%>
<html>
	<head>
		<title>新闻管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
			<a href="news.jsp">返回上一页</a><br/>
			<%=tip %><br/>
			<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>ID</td>
					<td align=center>标题</td>
					<td align=center>操作</td>
				</tr>
				<%if (newsList != null && newsList.size() > 0){
					for (int i = 0 ; i < newsList.size() ; i++){
						int iid = ((Integer)newsList.get(i)).intValue();
						con = ForumCacheUtil.getForumContent(iid);
						%><tr>
							<td><%=i+paging.getStartIndex()+1%></td>
							<td><a href="/jcadmin/user/forumr.jsp?id=<%=iid%>"><%if(con!=null){%><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),24))%><%}%>(<%=iid%>)</a></td>
							<td><a href="readNews.jsp?id=<%=typeBean.getId()%>&del=<%=iid%>" onclick="return confirm('真要删除？')">删除</a></td>
						  </tr><%
					}
				}%>
			</table>
			<%=paging.shuzifenye("readNews.jsp?id=" + (typeBean != null ? typeBean.getId() : 0) , true, "|", response)%>
	</body>
</html>
