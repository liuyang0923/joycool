<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String forumId =(String)request.getParameter("forumId");
if(forumId==null){
forumId="1";
}
ForumBean forum = ForumCacheUtil.getForumCache(StringUtil.toInt(forumId));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="论坛搜索">
<p align="left">
请输入要查找的作者ID：<br/>
<input name="userId" format="*N"  maxlength="9" value=""/><br/>
<anchor title="确定">搜索作者
  <go href="searchUserRs.jsp" method="post">
    <postfield name="userId" value="$userId"/>
    <postfield name="forumId" value="<%=forumId%>"/>
  </go>
</anchor><br/>
请输入要搜索的标题：<br/>
<input name="key" maxlength="20" value=""/><br/>
<select name="per">
<option value="0">最近<%=ForumAction.PERIOD_INTERVAL_DAY%>天</option>
<%for(int i = 1;i<6;i++){%><option value="<%=i%>"><%=ForumAction.PERIOD_INTERVAL_DAY*i%>~<%=ForumAction.PERIOD_INTERVAL_DAY*(i+1)%>天</option><%}%>
</select>
<anchor title="确定">搜索标题
<go href="searchUserRs.jsp?forumId=<%=forumId%>" method="post">
	<postfield name="key" value="$key"/><postfield name="per" value="$per"/>
</go>
</anchor><br/>
<br/>
<%if(forum!=null){%>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%}%>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>

