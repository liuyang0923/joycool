<%@ page contentType="application/vnd.wap.xhtml+xml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.ForbidUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.DateUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.rule();
String result =(String)request.getAttribute("result");
String url=("/jcforum/index.jsp");
%><html xmlns="http://www.w3.org/1999/xhtml">
<head><title>论坛版规</title></head><body>
<%if(result.equals("failure")){%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<font color="brown">
<%=BaseAction.getBottom(request, response)%>
</font>
</p>
<%}else if(result.equals("op")){
ForumBean forum =(ForumBean)request.getAttribute("forum");
url=("/jcforum/ruleX.jsp?forumId="+forum.getId());
%>

<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/jcforum/ruleX.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%>版规</a><br/>
<a href="/jcforum/forumX.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<font color="brown">
<%=BaseAction.getBottom(request, response)%>
</font>
</p>
<%}else{
ForumBean forum =(ForumBean)request.getAttribute("forum");
%><p align="left">
<%=BaseAction.getTop(request, response)%>
==本版版规==<br/>
<%if(forum.getRule().length()==0){%>(暂无)<%}else{%><%=StringUtil.toWml(forum.getRule())%><%}%><br/>
!!<a href="forumRule.jsp">乐酷论坛须知</a><br/>
<%if(request.getAttribute("admin")!=null){%>==重写版规==<br/>
版规:(最多250字)<br/>
<form action="<%=response.encodeURL("ruleX.jsp?forumId="+forum.getId())%>" method="post">
<textarea name="rule" rows="6" cols=40 maxlength="1000"/><%=forum.getRule()%></textarea><br/>
<input type="submit" value="提交版规"><br/>
</form>
<%}%><br/>
<a href="/jcforum/forumX.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<font color="brown">
<%=BaseAction.getBottom(request, response)%>
</font>
</p>
<%}%>
</body>
</html>