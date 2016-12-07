<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.ForbidUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.bUser(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
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
url=("/jcforum/bUser.jsp?forumId="+forum.getId());
%>

<card title="论坛" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回封禁列表)<br/>
<a href="/jcforum/bUser.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
ForumBean forum =(ForumBean)request.getAttribute("forum");
ForbidUtil.ForbidGroup fgroup = (ForbidUtil.ForbidGroup)request.getAttribute("fgroup");
List bUserSet = fgroup.getForbidList();
%>
<card title="论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
用户ID:<input name="userId2" format="*N" maxlength="9"/><br/>
理由:<input name="bak" maxlength="100"/><br/>
期限:<select name="per">
<option value="60">1小时</option>
<option value="300">5小时</option>
<option value="1440">1天</option>
<option value="10080">1星期</option>
<option value="43200">1个月</option>
</select><br/>
<anchor title="确定">确定封禁该ID
  <go href="bUser.jsp?op=add&amp;forumId=<%=forum.getId()%>" method="post">
    <postfield name="userId" value="$userId2"/>
    <postfield name="bak" value="$bak"/>
    <postfield name="per" value="$per"/>
  </go>
</anchor><br/>
----------<br/>
<input name="userId" format="*N" maxlength="9"/>
<anchor title="确定">查询封禁ID
  <go href="bUser.jsp?forumId=<%=forum.getId()%>" method="post">
    <postfield name="search" value="$userId"/>
  </go>
</anchor><br/>
<br/>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>