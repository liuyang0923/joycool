<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.ForbidUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.DateUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*" %><%
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
<a href="/jcforum/bUser.jsp?forumId=<%=forum.getId()%>">返回封禁列表</a><br/>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
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
<a href="/jcforum/bUsera.jsp?forumId=<%=forum.getId()%>">添加封禁玩家</a><br/>
===封禁列表===<br/>
<%
if(bUserSet.size()>0){
PagingBean paging = new PagingBean(action,bUserSet.size(),10,"p");
int start = paging.getStartIndex();
int end = paging.getEndIndex();
Iterator it = bUserSet.iterator();
int searchUser = action.getParameterInt("search");
if(searchUser==0){
int i = 0;
long now = System.currentTimeMillis();
while(it.hasNext()&&i<end){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)it.next();
	int userId=forbid.getValue();
	i++;
	if(i<=start) continue;
	
	UserBean user  = UserInfoUtil.getUser(userId);
	if(user==null){continue;}%>
<%=i%><a href="bUser.jsp?search=<%=user.getId()%>&amp;forumId=<%=forum.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>(<%=DateUtil.formatTimeInterval(forbid.getEndTime()-now)%>)
<a href="bUser.jsp?op=del&amp;userId=<%=user.getId()%>&amp;forumId=<%=forum.getId()%>" >解禁</a><br/>
<%}%>
<%=paging.shuzifenye("bUser.jsp?forumId="+forum.getId(), true, "|", response)%>
<%}else if(fgroup.isForbid(searchUser)){
ForbidUtil.ForbidBean forbid = fgroup.getForbid(searchUser);
UserBean user  = UserInfoUtil.getUser(searchUser);
if(user!=null){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a><br/>
理由:<%=forbid.getBak()%><br/>
封禁自<%=DateUtil.formatDate2(forbid.getStartTime())%><br/>
至<%=DateUtil.formatDate2(forbid.getEndTime())%><br/>
执行人:(隐藏)<br/>
<a href="bUser.jsp?op=del&amp;userId=<%=user.getId()%>&amp;forumId=<%=forum.getId()%>">确认解禁</a><br/>
<a href="bUser.jsp?forumId=<%=forum.getId()%>">返回列表</a><br/>
<%}}else{%>
(没有查询到封禁用户)<br/>
<a href="bUser.jsp?forumId=<%=forum.getId()%>">返回列表</a><br/>
<%}%>
<input name="userId" format="*N" maxlength="9"/>
<anchor title="确定">查询封禁ID
  <go href="bUser.jsp?forumId=<%=forum.getId()%>" method="post">
    <postfield name="search" value="$userId"/>
  </go>
</anchor><br/>
<%}else{%>封禁列表为空！<br/><%}%>
<br/>
<a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="/jcforum/index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>