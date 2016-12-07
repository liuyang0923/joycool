<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.ForbidUtil" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
ForumUserBean forumUser = action.getForumUser();
int forumId = action.getParameterInt("forumId");
if(forumId==6){
response.sendRedirect("index.jsp");
return;
}
if(forumId==0) forumId=1;
ForumBean forum = ForumCacheUtil.getForumCache(forumId);
if(forumId==1&&request.getParameter("content")!=null){
if(forumUser==null||forumUser.getExp()<200){
request.setAttribute("tip","你还不能在这里发表主题");
}
}

if(request.getAttribute("tip")==null)
	action.forum(request,response);
int type = action.getParameterInt("type");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(forum==null){%>
<card title="论坛">
<%}else{%>
<card title="<%=StringUtil.toWml(forum.getTitle())%>(<%=forum.getTodayCount()%>新)">
<%}%>
<%
String result = (String)request.getAttribute("result");
if(result!=null){
if(result.equals("setvar")){%>
<onevent type="onenterforward">
<refresh>
<setvar name="title" value=""/>
<setvar name="content" value=""/>
</refresh>
</onevent>
<%}}%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
boolean isAdmin = false;
if(loginUser!=null)
	isAdmin=ForbidUtil.isForbid("admin", loginUser.getId());
boolean xhtml = false;
if(loginUser!=null && loginUser.getUserSetting()!=null){
	xhtml=loginUser.getUserSetting().isFlagXhtml();
}
if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
<a href="editContent.jsp?forumId=<%=forumId%>">发表主题</a><br/>
<a href="forum.jsp?forumId=<%=forumId%>">返回论坛</a><br/>
<%}
else if(action.isResult("confirm")){
ForumContentBean con = (ForumContentBean)request.getAttribute("cons");
%>
<%=StringUtil.toWml(con.getTitle())%><br/>
确定要删除该主题吗?<br/>
<a href="forum.jsp?c=1&amp;forumId=<%=forumId%>&amp;del=<%=con.getId()%>">确认删除</a><br/>
<a href="forum.jsp?forumId=<%=forumId%>">返回论坛</a><br/>
<%}else if(forum!=null){
List contentList = (List)request.getAttribute("contentList");
PagingBean paging = (PagingBean)request.getAttribute("page");
String prefixUrl = (String) request.getAttribute("prefixUrl");
String popedom = (String) request.getAttribute("popedom");
%>
发表<a href="<%if(xhtml){%>editContentX.jsp?forumId=<%=forum.getId()%><%}else{%>editContent.jsp?forumId=<%=forum.getId()%><%}%>">主题</a><%if(loginUser!=null){%>/<a href="editVote.jsp?forumId=<%=forum.getId()%>">投票</a>|<a href="myTopic.jsp?f=<%=forum.getId()%>">我的主题</a><%}%><br/>
  <%if(contentList!=null){
   for(int i=0;i<contentList.size();i++){
   ForumContentBean forum1=(ForumContentBean)contentList.get(i);
   if(forum1!=null){
   	 	if(forum1.getAttach()!=null && !("").equals(forum1.getAttach())){%>@<%}%>
<%if(forum1.isPeak()||forum1.isTopPeak()){%>[顶]<%}else if(forum1.getMark1()==1){%>[精]<%}else{%><%=i+1%>.<%}%>
<a href="viewContent.jsp?contentId=<%=forum1.getId()%>"><%=StringUtil.toWml(StringUtil.limitString(forum1.getTitle(),30))%></a>(<%if(forum1.getReply()>0){%>回<a href="viewContent.jsp?r=1&amp;contentId=<%=forum1.getId()%>&amp;forumId=<%=forum1.getForumId()%>"><%=forum1.getReply()%></a><%}%>阅<%=forum1.getCount()%>)
<%      if(isAdmin){%>|<a href="forum.jsp?del=<%=forum1.getId()%>&amp;forumId=<%=forum1.getForumId()%>">删除</a><%}%><br/><%}
    }
   }%>
<%=paging.shuzifenye(prefixUrl, true, "|", response)%>
<a href="forum.jsp?forumId=<%=forumId%>">按时间</a>|
<%if(type!=2){%><a href="forum.jsp?type=2&amp;forumId=<%=forumId%>">按人气</a><%}else{%>按人气<%}%>|
<%if(type!=1){%><%if(forum.getPrimeCat()==0){%><a href="forum.jsp?type=1&amp;forumId=<%=forumId%>">精华区</a><%}else{%><a href="prime.jsp?cat=<%=forum.getPrimeCat()%>">分类精华</a><%}%>
<%}else{%>精华区<%}%>
<%if(popedom!=null){%>|<a href="bUser.jsp?forumId=<%=forumId%>">封禁列表</a><%}%><br/>
<%if(paging.getTotalPageCount()>5){%>
跳到<input name="index"  maxlength="2" format="*N" value="1"/>页
<anchor title="确定">GO
  <go href="forum.jsp?forumId=<%=forum.getId()%>" method="post">
    <postfield name="go" value="$index"/>
  </go>
</anchor><br/><%}%>
<%if(forum.getTongId()>=20000){%>
    <a href="/fm/myfamily.jsp?id=<%=forum.getTongId()%>">&gt;&gt;返回家族</a>&gt;<a href="/fm/chat/chat.jsp?fid=<%=forum.getTongId()%>">聊天</a><br/>
<%}else if(forum.getTongId()>0){%>
    <a href="/tong/tong.jsp?tongId=<%=forum.getTongId()%>">&gt;&gt;返回帮会</a><br/>
<%}else if(forum.getId()==2878){%>
	<a href="/farm/index.jsp">&gt;&gt;返回桃花源首页</a><br/>
<%}else if(forum.getId()==12260){%>
	<a href="/farm2/index.jsp">&gt;&gt;返回桃花源2首页</a><br/>
<%}else if(forum.getId()==5545){%>
	<a href="/cast/index.jsp">&gt;&gt;返回城堡战争首页</a><br/>
<%}else if(forum.getId()==12212){%>
	<a href="/city/index.jsp">&gt;&gt;返回七国风云首页</a><br/>
<%}else if(forum.getId()==9066){%>
	<a href="/garden/island.jsp">&gt;&gt;返回采集岛首页</a><br/>
<%}else if(forum.getId()==11090||forum.getId()==11091){%>
	<a href="/friend/match/index.jsp">&gt;&gt;返回超级乐后选拔赛</a><br/>
<%}else if(forum.getId()==9389){%>
	<a href="/wgame/texas/index.jsp">&gt;&gt;返回德州扑克大厅</a><br/>
<%}else if(forum.getId()==11799){%>
	<a href="/fm/index.jsp">&gt;&gt;返回家族首页</a><br/>
<%}%>
    <a href="searchUser.jsp?forumId=<%=forum.getId()%>">搜索</a>|
    <a href="rule.jsp?forumId=<%=forum.getId()%>">版规</a>|
    <a href="viewAdmin.jsp?forumId=<%=forum.getId()%>">版主</a>|
    <a href="history.jsp?forumId=<%=forum.getId()%>">历史</a><br/>
    <%
}
else{%>
 该论坛不存在!<br/>
<%}%>
<a href="index.jsp">论坛首页</a><%--
<select name="nav">
<option onpick="forum.jsp?forumId=1">乐酷特快</option>
<option onpick="forum.jsp?forumId=3">情感倾诉</option>
<option onpick="forum.jsp?forumId=1985">游戏交易</option>
<option onpick="forum.jsp?forumId=4">随便灌水区</option>
<option onpick="forum.jsp?forumId=1548">围炉夜话</option>
<option onpick="forum.jsp?forumId=1802">新人学堂</option>
<option onpick="forum.jsp?forumId=671">贴图秀场</option>
<option onpick="forum.jsp?forumId=6">两性夜语</option>
</select>--%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>