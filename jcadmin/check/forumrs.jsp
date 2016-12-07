<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.jcforum.*"%><%@ page import="net.joycool.wap.action.jcforum.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static JcForumServiceImpl service = new JcForumServiceImpl();
static int COUNT_PER_PAGE = 30;
%><%

CustomAction action = new CustomAction(request);

int del = action.getParameterIntS("d");
if(del>=0){
	ForumCacheUtil.deleteMyForumReply(null,0,del);
}

int userId = action.getParameterInt("id");
PagingBean paging = new PagingBean(action, 10000, COUNT_PER_PAGE, "p");
String limit = " limit " + paging.getStartIndex() + "," + COUNT_PER_PAGE;
List list = service.getForumReplyList("del_mark=0 and ctype!=1 and content!='(内容被删除)' order by id desc" + limit);
%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table width="98%" align=center>
<%

for(int i = 0;i < list.size();i++){
ForumReplyBean con = (ForumReplyBean)list.get(i);
%><tr><td width="100"><a href="/jcadmin/user/forumr.jsp?id=<%=con.getContentId()%>"><%=con.getId()%></a><br/><%

%></td><td><%=StringUtil.toWml(StringUtil.limitString(con.getContent(),200))%><br/></td>
<td width="80"><%=DateUtil.formatSqlDatetime(con.getCreateTime())%></td>
<%
%><td width="100"><a href="/jcadmin/user/queryUserInfo.jsp?id=<%=con.getUserId()%>"><%=con.getUserId()%></a></td>

<td>
<a href="forumrs.jsp?d=<%=con.getId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认删除？')">删</a>
</td>
</tr>
<%}%>
</table><%=paging.shuzifenye("forumrs.jsp", false, "|", response, 10)%>
<body>
</html>