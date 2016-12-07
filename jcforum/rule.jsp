<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.ForbidUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.DateUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.rule();
String result =(String)request.getAttribute("result");
String url=("/jcforum/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="论坛" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回乐酷论坛)<br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("op")){
ForumBean forum =(ForumBean)request.getAttribute("forum");
url=("/jcforum/rule.jsp?forumId="+forum.getId());
%>

<card title="论坛" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后自动返回)<br/>
<a href="/jcforum/rule.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%>版规</a><br/>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
ForumBean forum =(ForumBean)request.getAttribute("forum");
%><card title="论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==本版版规==<br/>
<%if(forum.getRule().length()==0){%>(暂无)<%}else{%><%=StringUtil.toWml(forum.getRule())%><%}%><br/>
!!<a href="forumRule.jsp">乐酷论坛须知</a><br/>
<%if(request.getAttribute("admin")!=null){%>==重写版规==<br/>
版规:(最多250字)<br/>
<input name="rule" maxlength="250"/>
<anchor title="确定">提交版规
  <go href="rule.jsp?forumId=<%=forum.getId()%>" method="post">
    <postfield name="rule" value="$rule"/>
  </go>
</anchor><br/>
<%}%><br/>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>