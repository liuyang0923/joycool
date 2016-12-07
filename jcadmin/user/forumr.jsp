<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.jcforum.*"%><%@ page import="net.joycool.wap.action.jcforum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static JcForumServiceImpl service = new JcForumServiceImpl();
static int COUNT_PER_PAGE = 30;
%><%

CustomAction action = new CustomAction(request);
int id = action.getParameterInt("id");
ForumContentBean fc = service.getForumContent("id="+id);
if(fc==null)
	return;
ForumBean forum=ForumCacheUtil.getForumCache(fc.getForumId());
if(action.hasParam("restore")){
	//恢复删除帖子数据
	service.updateForumContent("mark1=0,mark2=0,del_mark=0","id="+fc.getId());
	SqlUtil.executeUpdate("delete from jc_forum_del where content_id=" + fc.getId(),2);
	//版块总帖子数量加一数据
	service.updateForum("total_count=total_count+1","id="+forum.getId());
	forum.setTotalCount(forum.getTotalCount()+1);
	ForumCacheUtil.addContentIds(fc);
	fc.setDelMark(0);
}
List typeList = null;
if(fc.isNews())
	typeList = SqlUtil.getObjectsList("select b.id,b.name from forum_news a,forum_news_type b where a.id="+id + " and a.type=b.id",2);
PagingBean paging = new PagingBean(action, 10000, COUNT_PER_PAGE, "p");
String limit = " limit " + paging.getStartIndex() + "," + COUNT_PER_PAGE;
List list = service.getForumReplyList("content_id="+id + " order by id" + limit);
UserBean user2 = UserInfoUtil.getUser(fc.getUserId());
%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<table width="98%" align=center>
<tr><td><%=fc.getTitle()%>(<%=DateUtil.formatSqlDatetime(fc.getCreateTime())%>)<%if(typeList!=null)for(int i=0;i<typeList.size();i++){Object[] objs = (Object[])typeList.get(i);%>《<a href="../jcforum/readNews.jsp?id=<%=objs[0]%>"><font color=red><%=objs[1]%></font></a>》<%}%></td><td><a href="forums.jsp?fid=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a></td><td><a href="queryUserInfo.jsp?id=<%=user2.getId()%>"><%if(user2!=null){%><%=user2.getNickNameWml()%><%}%></a>(<%=fc.getUserId()%>)</td></tr>
<tr><td><%if(paging.getCurrentPageIndex()==0){%><%=fc.getContent()%><%}else{%><%}%></td><td><%if(fc.getDelMark()==1){%>已删除-<a href="forumr.jsp?id=<%=id%>&restore=1" onclick="return confirm('确认还原改贴?')">还原</a><%}%></td><td><%if(fc.getDelMark()==1){%><%=SqlUtil.getIntResult("select user_id from jc_forum_del where content_id="+id,2)%><%}%></td></tr>
</table>
<table width="98%" align=center>
<%

for(int i = 0;i < list.size();i++){
ForumReplyBean con = (ForumReplyBean)list.get(i);
%><tr><td width="30"><%=paging.getStartIndex()+i+1%><br/></td>
<td><%=con.getContent()%></td>
<td width="80"><%=DateUtil.formatSqlDatetime(con.getCreateTime())%></td>
<%
UserBean user = UserInfoUtil.getUser(con.getUserId());
%><td width="100"><%if(user!=null){%><a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%}else{%><%=con.getUserId()%><%}%></td></tr>
<%}%>
</table>
<%=paging.shuzifenye("forumr.jsp?id=" + id, true, "|", response, 10)%>
<body>
</html>