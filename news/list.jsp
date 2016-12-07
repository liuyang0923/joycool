<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumxAction action=new ForumxAction(request);

	int id = action.getParameterInt("id");
	NewsTypeBean type;
	if(id == 0) {
		type = action.getCurrentNewsType();
	} else {
		type = action.getNewsType(id);
		session.setAttribute("ntId",Integer.valueOf(id));
	}
	if(type == null){
		response.sendRedirect(("index.jsp"));
		return;
	}
	int count = SqlUtil.getIntResult("select count(id) from forum_news where type="+type.getId(),2);
	PagingBean paging = new PagingBean(action, count, 15, "p");
	List list = SqlUtil.getIntList("select id from forum_news where type="+type.getId()+" order by id desc limit "+paging.getStartIndex()+","+paging.getCountPerPage(), 2);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=type.getName()%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
【<%=type.getName()%>】<%=id==7?"<a href=\"nba/index.jsp\">【NBA专题】</a>":"" %><br/>
<%for(int i=0;i<list.size();i++){
Integer iid = (Integer)list.get(i);
ForumContentBean con = ForumCacheUtil.getForumContent(iid.intValue());
if(con==null) continue;
%><%if(con.getAttach()!=null & !("").equals(con.getAttach())){%>@<%}%><a href="news.jsp?id=<%=con.getId()%>&amp;tid=<%=type.getId()%>"><%=StringUtil.toWml(StringUtil.limitString(con.getTitle(),27))%></a>[<%=con.getCreateDatetime()%>]<br/>
<%}%>
<%=paging.shuzifenye("list.jsp", false, "|", response)%>
<% if (type.getType() != 1) {
	if(type.getColumnId()!=0){
		%><a href="index.jsp">新闻首页</a>&gt;<a href="/Column.do?columnId=<%=type.getColumnId()%>"><%=type.getColumnName()%></a><%
	}else{
		%><a href="index.jsp">返回新闻首页</a><%
	}
} else if (type.getType() == 1){
	%><a href="index2.jsp">返回八卦首页</a><br/><%
%><a href="list.jsp?id=21">情感</a>|<a href="list.jsp?id=23">八卦</a>|<a href="list.jsp?id=24">美体</a>|<a href="list.jsp?id=22">时尚</a><br/>
<a href="list.jsp?id=25">运势</a>|<a href="list.jsp?id=26">幽默</a>|<a href="list.jsp?id=27">心理</a>|<a href="list.jsp?id=28">化妆</a>
<%}%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>