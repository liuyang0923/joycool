<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
boolean showFaceImg = true, showSendImg = true;
if(loginUser!=null&&loginUser.getUserSetting()!=null) {
	showFaceImg = !loginUser.getUserSetting().isFlagHideFace();
	showSendImg = !loginUser.getUserSetting().isFlagHideSend();
}
action.viewPrime();
PagingBean paging = (PagingBean)request.getAttribute("page");
int order = 0;
List replyList = (List)request.getAttribute("replyList");
String popedom = (String) request.getAttribute("popedom");
ForumContentBean con =(ForumContentBean)request.getAttribute("con");
String prefixUrl = (String) request.getAttribute("prefixUrl");
String result = (String)request.getAttribute("result");
ForumContentBean prevJHForumContent=(ForumContentBean)request.getAttribute("prevJHForumContent");
ForumContentBean nextJHForumContent=(ForumContentBean)request.getAttribute("nextJHForumContent");
int catId=action.getAttributeInt("cat");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(con==null){%>
<card title="论坛">
<%}else{%>
<card title="<%=StringUtil.toWml(con.getTitle())%>">
<%}
%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result!=null){%>
<%=request.getAttribute("tip") %><br/>
<a href="primev.jsp?id=<%=con.getId()%>">返回帖子</a><br/>
<%}else if(con!=null){


UserBean authorUser = UserInfoUtil.getUser(con.getUserId());
if(authorUser!=null){
ForumUserBean forumUser = ForumCacheUtil.getForumUser(authorUser);
if(authorUser.getId()==100){%>系统信息
<%} else {
	if(con.isNickTopic()) {
%>楼主匿名<%
	} else {
if(forumUser != null && forumUser.getExp() >= 10){%><%=forumUser.getRankName()%><%}else{%>楼主<%}%>:
<%if(true){%>
<a href="/user/ViewUserInfo.do?userId=<%=authorUser.getId()%>" ><%=StringUtil.toWml(authorUser.getNickName())%></a>
		<%}else{%>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=authorUser.getId()%>" ><%=StringUtil.toWml(authorUser.getNickName())%></a>
		<%}}}
}else{%>游客<%}%>(<%=DateUtil.sformatTime(con.getCreateTime())%>)<br/>
<%	if(con!=null && paging.getCurrentPageIndex()==0 && !action.hasParam("r")){%>
<%=ForumAction.speContent(response,action.contentTransform(con,request,response, showSendImg), showFaceImg)%>
<%}%>
<%
if(loginUser!=null){%>
<%--<a href="result.jsp?contentId=<%=con.getId()%>&amp;cart=1">收藏</a>--%>
<%}
if("manager".equals(popedom)){
%>
|<%if(con.getMark1()==1){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delprime=1">去精</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;prime=1">加精</a>
|<%}%>
<%}else if("super".equals(popedom)){%>
|<%if(con.getMark1()==1){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delprime=1">去精</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;prime=1">加精</a>
|<%}%>
<%}
%><br/>
<% if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
<%}%>
<%
  if(replyList!=null){%>
<%for(int i=0;i<replyList.size();i++){
   ForumReplyBean forum1=(ForumReplyBean)replyList.get(i);
   if(forum1!=null){%><%if(order==0){%><%=i+1+paging.getCurrentPageIndex()*ForumAction.NUMBER_PER_PAGE%><%}else{%><%=paging.getTotalCount()-i-paging.getCurrentPageIndex()*ForumAction.NUMBER_PER_PAGE%><%}%>.<%UserBean user=(UserBean)UserInfoUtil.getUser(forum1.getUserId());
   if(user!=null){
if(user.getId()==100){%>系统信息
    <%}else if(!forum1.isNickReply()){
    	if(true){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
     <%}else{%>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
<%}} else {
%>匿名回复<%	
}}else{%>
游客<%}%>:<%if(forum1.getCType()==1){%><%if(showFaceImg){%><img src="../img/jcforum/action/<%=forum1.getContent()%>.gif" alt=""/><%}else{%>表情(<%=ForumAction.getFaceString(forum1.getContent())%>)<%}%>
<%}else if(forum1.isAction()){%><%=forum1.getActionTypeStr() %><%} else {%><%=ForumAction.speContent(response,StringUtil.toWml(forum1.getContent()), showFaceImg)%><%}%>
<%if(forum1.getAttach()!=null & !("").equals(forum1.getAttach())){%>
<%if(showSendImg){%>
<img src="<%=Constants.IMG_ROOT_URL%><%=forum1.getAttach()%>" alt="o"/>
<%}else{%>
(附件)
<%}%>
<a href="<%=Constants.IMG_ROOT_URL%><%=forum1.getAttach()%>">下载</a><br/>
<%}%>(<%=DateUtil.sformatTime(forum1.getCreateTime())%>)
<br/><%}
  }
 }
String prefixURL = "primev.jsp?id=" + con.getId();
 %>
<%=paging.shuzifenye(prefixURL, true, "|", response)%>
<%if(paging.getTotalPageCount()>5){%>
跳到<input name="index"  maxlength="5" format="*N" value="1"/>页
<anchor title="确定">GO
<go href="primev.jsp?id=<%=con.getId()%>" method="post">
<postfield name="go" value="$index"/>
</go>
</anchor><br/>
<%}%>

<%if(con.getMark1()==1&&nextJHForumContent != null||prevJHForumContent != null){//显示精华上一条
if(prevJHForumContent != null){%><a href="primev.jsp?id=<%=prevJHForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>">下一贴</a><%}else{%>下一贴<%}
%>|<%//显示精华下一条
if(nextJHForumContent != null){%><a href="primev.jsp?id=<%=nextJHForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>">上一贴</a><%}else{%>上一贴
<%}%><br/><%
}
}else{%>
 该精华帖不存在!<br/>
<%}
	if(con!=null){
  ForumBean forum=ForumCacheUtil.getForumCache(con.getForumId());
  if(forum!=null){%>
<%if(catId!=0){%><a href="prime.jsp?cat=<%=catId%>">回精华区分类</a><%}else{%><a href="primel.jsp?forumId=<%=forum.getId()%>">回精华区未分类列表</a><%}%><br/>
<a href="forum.jsp?forumId=<%=con.getForumId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<%}}%>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>