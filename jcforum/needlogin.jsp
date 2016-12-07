<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
int forumId = action.getParameterInt("forumId");
ForumBean forum = null;
if(forumId>0)
	forum = ForumCacheUtil.getForumCache(forumId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您需要<a href="/user/login.jsp">登陆</a>才能执行这个操作！<br/>
<%if(forum != null){%>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%}%>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>