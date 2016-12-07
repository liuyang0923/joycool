<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
int forumId=StringUtil.toInt(request.getParameter("forumId"));
if(forumId<=0){
forumId=1;
}
ForumAction action=new ForumAction(request);
ForumUserBean forumUser = action.getForumUser();
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
session.setAttribute("forumcontent","true");
int optionCount=StringUtil.toInt(request.getParameter("o"));
if(optionCount < 4) optionCount = 4;
if(optionCount > 20) optionCount = 20;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发起投票">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(forumUser==null||forumUser.getExp()<300){%>
论坛经验值至少要300才能发起投票.<br/>
<%}else{%>
发起投票于[<%=StringUtil.toWml(forum.getTitle())%>]<br/>
标题:<input name="title"  maxlength="30" value=""/><br/>
内容:<input name="content"  maxlength="1000" value=""/><br/>
<%for(int i = 0;i < optionCount;i++){%>
选项<%=i+1%>:<input name="option<%=i%>"  maxlength="100" value=""/><br/>
<%}%>
<anchor title="确定">发起投票
    <go href="forum.jsp?t=1&amp;forumId=<%=forumId%>" method="post">
    <postfield name="content" value="$content"/> 
<%for(int i = 0;i < optionCount;i++){%><postfield name="option" value="$option<%=i%>"/><%}%>
    <postfield name="title" value="$title"/>
    </go>
</anchor>
<a href="editVote.jsp?forumId=<%=forumId%>&amp;o=<%=(optionCount+2)%>">增加选项</a><br/>

<%}%>
<a href="forum.jsp?forumId=<%=forumId%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>