<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.jcforum.*"%><%@ page import="net.joycool.wap.action.jcforum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static JcForumServiceImpl service = new JcForumServiceImpl();
static int COUNT_PER_PAGE = 30;
%><%

CustomAction action = new CustomAction(request);
int userId = action.getParameterInt("id");
int forumId = action.getParameterInt("fid");
PagingBean paging = new PagingBean(action, 10000, COUNT_PER_PAGE, "p");
String limit = " limit " + paging.getStartIndex() + "," + COUNT_PER_PAGE;
List list;
if(userId!=0)
	list = service.getForumContentList("user_id="+userId + " and del_mark=0 order by id desc" + limit);
else if(forumId!=0)
	list = service.getForumContentList("forum_id="+forumId + " and del_mark=0 order by last_re_time desc" + limit);
else
	list = service.getForumContentList("del_mark=0 order by id desc" + limit);
%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<table width="98%" align=center>
<%

for(int i = 0;i < list.size();i++){
ForumContentBean con = (ForumContentBean)list.get(i);
%><tr><td width="100"><%=con.getId()%><br/><%
if(forumId==0){
ForumBean f = ForumCacheUtil.getForumCache(con.getForumId());
if(f!=null){
%><a href="forums.jsp?fid=<%=con.getForumId()%>"><%=StringUtil.toWml(f.getTitle())%></a><br/><%=f.getTodayCount()%>/<%=f.getTotalCount()%><%}%>(<%=con.getForumId()%>)<%}%></td><td><%if(con.isPeak()){%><font color=red>[顶]</font><%} if(con.getMark1()==1){%><font color=red>[精]</font><%} if(con.isNews()){%><font color=red>[新闻]</font><%}%><a href="forumr.jsp?id=<%=con.getId()%>"><%=StringUtil.toWml(con.getTitle())%></a><br/><%=con.getReply()%>/<%=con.getCount()%></td>
<td><%=StringUtil.limitString(con.getContent(),200)%></td>
<td width="80"><%=DateUtil.formatSqlDatetime(con.getCreateTime())%></td>
<%
UserBean user = UserInfoUtil.getUser(con.getUserId());
%><td width="100"><%if(user!=null){%><a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%}else{%><%=con.getUserId()%><%}%></td></tr>
<%}%>
</table><%if(userId!=0){
%><%=paging.shuzifenye("forums.jsp?id=" + userId, true, "|", response, 10)%><%
	}else if(forumId!=0){
%><%=paging.shuzifenye("forums.jsp?fid=" + forumId, true, "|", response, 10)%><%
	}else{
%><%=paging.shuzifenye("forums.jsp", false, "|", response, 10)%><%
	}
%><body>
</html>