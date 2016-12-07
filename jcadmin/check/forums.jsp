<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.jcforum.*"%><%@ page import="net.joycool.wap.action.jcforum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static JcForumServiceImpl service = new JcForumServiceImpl();
static int COUNT_PER_PAGE = 30;
%><%

CustomAction action = new CustomAction(request);

int del = action.getParameterIntS("d");
String tip = null;
if(del>=0){
	ForumContentBean cons = ForumCacheUtil.getForumContent(del);
	if(cons!=null){
		if(cons.getMark1() != 0 || cons.getMark2() != 0){
			tip = "无法删除精华帖、置顶帖、新闻贴";
		}else{
			ForumBean forum = ForumCacheUtil.getForumCache(cons.getForumId());
			ForumCacheUtil.deleteForumContent(cons, forum, 100);
			ForumUserBean fu = ForumCacheUtil.getForumUser(cons.getUserId());
			if(fu != null) {
				fu.decExp(5);
				if(fu.getThreadCount() > 0)
					fu.setThreadCount(fu.getThreadCount() - 1);
				ForumCacheUtil.updateForumUser(fu);
			}
			tip = "删除成功";
		}
	}else{
		tip = "删除失败,没有找到该帖";
	}
}

int forumId = 0;
PagingBean paging = new PagingBean(action, 1000000, COUNT_PER_PAGE, "p");
String limit = " limit " + paging.getStartIndex() + "," + COUNT_PER_PAGE;
List list = service.getForumContentList("del_mark=0 order by id desc" + limit);
%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<%if(tip!=null){%><font color=red size="6"><%=tip%></font><%}%>
<table width="98%" align=center>
<%

for(int i = 0;i < list.size();i++){
ForumContentBean con = (ForumContentBean)list.get(i);
%><tr><td width="100"><%=con.getId()%><br/><%
if(forumId==0){
ForumBean f = ForumCacheUtil.getForumCache(con.getForumId());
if(f!=null){
%><%=StringUtil.toWml(f.getTitle())%><%}%>(<%=con.getForumId()%>)<%}%></td><td><%if(con.isPeak()){%><font color=red>[顶]</font><%} if(con.getMark1()==1){%><font color=red>[精]</font><%} if(con.isNews()){%><font color=red>[新闻]</font><%}%><%=StringUtil.toWml(con.getTitle())%><br/><%=con.getReply()%>/<%=con.getCount()%></td>
<td><%=StringUtil.toWml(StringUtil.limitString(con.getContent(),200))%></td>
<td width="80"><%=DateUtil.formatSqlDatetime(con.getCreateTime())%></td>
<%
UserBean user = UserInfoUtil.getUser(con.getUserId());
%><td width="100"><%if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%}else{%><%=con.getUserId()%><%}%></td>

<td>
<a href="forums.jsp?d=<%=con.getId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认删除？')">删</a>
</td>
</tr>
<%}%>
</table><%=paging.shuzifenye("forums.jsp", false, "|", response, 10)%><body>
</html>