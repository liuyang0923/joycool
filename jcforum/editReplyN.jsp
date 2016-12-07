<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
String contentId=request.getParameter("contentId");
int pageIndex =  StringUtil.toInt((String) request.getParameter("p"));
if(pageIndex == -1) {
	pageIndex = 0;
}
ForumContentBean forumContent = ForumCacheUtil.getForumContent(StringUtil.toInt(contentId));
session.setAttribute("forumreply","true");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发表回帖">
<p align="left">
<%=BaseAction.getTop(request, response)%>
每次回复增加1点经验值<br/>
内容:<input name="content"  maxlength="1000" value=""/><br/>
 <anchor title="确定">匿名回帖
    <go href="viewContent.jsp?contentId=<%=contentId%>&amp;p=<%=pageIndex%>" method="post">
    <postfield name="content" value="$content"/>
    <postfield name="n" value="n"/>
    </go>
    </anchor>
<a href="/jcforum/replyAttach.jsp?contentId=<%=contentId%>&amp;p=<%=pageIndex%>">贴图发表</a><br/>
<%if(forumContent!=null){%>
<a href="viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;p=<%=pageIndex%>">返回主题</a><br/>
<%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>