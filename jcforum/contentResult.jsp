<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.contentResult(request);
String result = (String)request.getAttribute("result");
String url=("/jcforum/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="帖子续写" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/jcforum/index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
ForumContentBean content=(ForumContentBean)request.getAttribute("content");
url=("/jcforum/viewContent.jsp?contentId="+content.getId()+"&amp;forumId="+content.getForumId());
%>
<card title="帖子续写" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转)<br/>
<a href="/jcforum/viewContent.jsp?contentId=<%=content.getId()%>&amp;forumId=<%=content.getForumId()%>">返回</a><br/>    
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
