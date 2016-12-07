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
 <anchor title="确定">回帖
    <go href="viewContent.jsp?contentId=<%=contentId%>&amp;p=<%=pageIndex%>" method="post">
    <postfield name="content" value="$content"/>
    </go>
    </anchor>
<a href="/jcforum/replyAttach.jsp?contentId=<%=contentId%>&amp;p=<%=pageIndex%>">贴图发表</a><br/>
发表情：<br/>
<a href="viewContent.jsp?cType=1&amp;content=1&amp;contentId=<%=contentId%>">愤怒</a>
<a href="viewContent.jsp?cType=1&amp;content=2&amp;contentId=<%=contentId%>">吃惊</a>
<a href="viewContent.jsp?cType=1&amp;content=3&amp;contentId=<%=contentId%>">呕吐</a>
<a href="viewContent.jsp?cType=1&amp;content=4&amp;contentId=<%=contentId%>">调皮</a><br/>
<a href="viewContent.jsp?cType=1&amp;content=5&amp;contentId=<%=contentId%>">大哭</a>
<a href="viewContent.jsp?cType=1&amp;content=6&amp;contentId=<%=contentId%>">难过</a>
<a href="viewContent.jsp?cType=1&amp;content=7&amp;contentId=<%=contentId%>">鲜花</a>
<a href="viewContent.jsp?cType=1&amp;content=8&amp;contentId=<%=contentId%>">害怕</a><br/>
<a href="viewContent.jsp?cType=1&amp;content=9&amp;contentId=<%=contentId%>">厉害</a>
<a href="viewContent.jsp?cType=1&amp;content=10&amp;contentId=<%=contentId%>">害羞</a>
<a href="viewContent.jsp?cType=1&amp;content=11&amp;contentId=<%=contentId%>">支持</a>
<a href="viewContent.jsp?cType=1&amp;content=12&amp;contentId=<%=contentId%>">握手</a><br/>
<a href="viewContent.jsp?cType=1&amp;content=13&amp;contentId=<%=contentId%>">大笑</a>
<a href="viewContent.jsp?cType=1&amp;content=14&amp;contentId=<%=contentId%>">色狼</a>
<a href="viewContent.jsp?cType=1&amp;content=15&amp;contentId=<%=contentId%>">沉思</a>
<a href="viewContent.jsp?cType=1&amp;content=16&amp;contentId=<%=contentId%>">不屑</a><br/>
<a href="viewContent.jsp?cType=1&amp;content=17&amp;contentId=<%=contentId%>">疑问</a>
<a href="viewContent.jsp?cType=1&amp;content=18&amp;contentId=<%=contentId%>">冷汗</a>
<a href="viewContent.jsp?cType=1&amp;content=19&amp;contentId=<%=contentId%>">疲倦</a>
<a href="viewContent.jsp?cType=1&amp;content=20&amp;contentId=<%=contentId%>">狗屎</a><br/>
<br/>
<%if(forumContent!=null){%>
<a href="viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;p=<%=pageIndex%>">返回主题</a><br/>
<%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>