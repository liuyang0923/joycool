<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.contentWrite(request);
ForumContentBean content=(ForumContentBean)request.getAttribute("content");
String result = (String)request.getAttribute("result");
String url=("/jcforum/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%if(content!=null){%><a href="/jcforum/forum.jsp?forumId=<%=content.getForumId()%>">返回论坛</a><br/><%}%>
<a href="/jcforum/index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(content.getContent())%><br/>
续写:<input name="content"  maxlength="5000" value=""/><br/>
 <anchor title="确定">确定
    <go href="contentResult.jsp?contentId=<%=content.getId()%>" method="post">
    <postfield name="content" value="$content"/>
    </go>
    </anchor><br/>
<a href="/jcforum/forum.jsp?forumId=<%=content.getForumId()%>">返回论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
