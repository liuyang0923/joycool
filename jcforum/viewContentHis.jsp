<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
int contentId = action.getParameterInt("contentId");
ForumContentBean con = ForumCacheUtil.getForumContent(contentId);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(request.getParameter("title")!=null){
if(loginUser.getId() != con.getUserId()&&!ForumAction.isSuperAdmin(loginUser.getId())){
response.sendRedirect(("index.jsp"));
return;}
}
action.viewContentHis(request,response);
PagingBean paging = (PagingBean)request.getAttribute("page");
int order = 0;
if(loginUser != null && loginUser.getUserSetting().isFlagVorder())
	order = 1;
List replyList = (List)request.getAttribute("replyList");
String popedom = (String) request.getAttribute("popedom");
//ForumContentBean con =(ForumContentBean)request.getAttribute("con");
String prefixUrl = (String) request.getAttribute("prefixUrl");
String isDel = (String) request.getAttribute("isDel");
String result = (String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(con==null){%>
<card title="论坛">
<%}else{%>
<card title="<%=StringUtil.toWml(con.getTitle())%>">
<%}%>
<%if(result!=null){
if(result.equals("success")&&!action.hasParam("cType")){%>
<onevent type="onenterforward">
<refresh>
<setvar name="title" value=""/>
<setvar name="content" value=""/>
</refresh>
</onevent>
<%}}
%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result!=null){%>
<%=request.getAttribute("tip") %><br/>
<a href="viewContentHis.jsp?contentId=<%=con.getId()%>">返回帖子</a><br/>
<%}else if(con!=null){
UserBean authorUser = UserInfoUtil.getUser(con.getUserId());
if(authorUser!=null){
ForumUserBean forumUser = ForumCacheUtil.getForumUser(authorUser);
if(authorUser.getId()==100){%>系统信息
<%} else {
if(forumUser != null && forumUser.getExp() >= 10){%><%=forumUser.getRankName()%><%}else{%>楼主<%}%>:
<%if(true){%>
<a href="/user/ViewUserInfo.do?userId=<%=authorUser.getId()%>" ><%=StringUtil.toWml(authorUser.getNickName())%></a>
		<%}else{%>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=authorUser.getId()%>" ><%=StringUtil.toWml(authorUser.getNickName())%></a>
		<%}}
}else{%>游客<%}%>|<%=con.getCreateDatetime()%><br/>
<%if(!con.isReadonly()){%>
<a href="editReply.jsp?contentId=<%=con.getId()%>&amp;p=<%=paging.getCurrentPageIndex()%>">回复</a>
<%} 
if(loginUser!=null){%>
|<a href="result.jsp?contentId=<%=con.getId()%>&amp;cart=1">收藏</a>
<%}%>
<%if("manager".equals(popedom)){%>
|<a href="forum.jsp?del=<%=con.getId()%>&amp;forumId=<%=con.getForumId()%>">删除</a>
<%if(con.getType()>0){	// 投票帖子，结束后type重置为0%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ev=1">结束投票</a>
|<%}%>
<a href="editTitle.jsp?contentId=<%=con.getId()%>">编辑</a><%if(con.getUserId()==loginUser.getId()){%>|<a href="contentWrite.jsp?contentId=<%=con.getId()%>">续写</a><%}%>
<%}else if("super".equals(popedom)){%>
|<a href="forum.jsp?del=<%=con.getId()%>&amp;forumId=<%=con.getForumId()%>">删除</a>
|<%if(con.getType()>0){	// 投票帖子，结束后type重置为0%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ev=1">结束投票</a>
|<%}%>
<a href="editTitle.jsp?contentId=<%=con.getId()%>">编辑</a><%if(con.getUserId()==loginUser.getId()){%>|<a href="contentWrite.jsp?contentId=<%=con.getId()%>">续写</a><%}%>
<%}else if("mycontent".equals(popedom)){%>
|<a href="forum.jsp?del=<%=con.getId()%>&amp;forumId=<%=con.getForumId()%>">删除</a>
<%if(!DateUtil.timepast(con.getCreateTime(),300)){%>
|<a href="editTitle.jsp?contentId=<%=con.getId()%>">编辑</a>
<%}%>
|<a href="contentWrite.jsp?contentId=<%=con.getId()%>">续写</a>
<%}else if("normal".equals(popedom)){%>
<%}else{%>
  <%} if(paging.getCurrentPageIndex()+1 < paging.getTotalPageCount()) {%>
|<a href="viewContentHis.jsp?contentId=<%=con.getId()%>&amp;p=<%=paging.getTotalPageCount()%>">尾页</a><%}%><br/>
<% if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
   <%}
  	if(con!=null && paging.getCurrentPageIndex()==0 && !action.hasParam("r")){%>
--------------<br/>
<%=ForumAction.speContent(response,action.contentTransform(con,request,response))%>
<%if(con.getType()!=0){%>已经有<%=ForumCacheUtil.getVoteCount(con.getId())%>人参与了投票<br/><%}%>
<%}else{%><%}
  if(replyList!=null){%>
==回复列表==<br/>
<%for(int i=0;i<replyList.size();i++){
   ForumReplyBean forum1=(ForumReplyBean)replyList.get(i);
   if(forum1!=null){%><%if(order==0){%><%=i+1+paging.getCurrentPageIndex()*ForumAction.NUMBER_PER_PAGE%><%}else{%><%=paging.getTotalCount()-i-paging.getCurrentPageIndex()*ForumAction.NUMBER_PER_PAGE%><%}%>.<%UserBean user=(UserBean)UserInfoUtil.getUser(forum1.getUserId());
   if(user!=null){
if(user.getId()==100){%>系统信息
    <%}else if(true){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
     <%}else{%>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
<%}  }else{%>
游客<%}%>:<%if(forum1.getCType()==1){%><img src="../img/jcforum/action/<%=forum1.getContent()%>.gif" alt=""/>
<%}else{%><%=ForumAction.speContent(response,StringUtil.toWml(forum1.getContent()))%><%}%>
<%if(forum1.getAttach()!=null & !("").equals(forum1.getAttach())){%>
<%if(loginUser==null||loginUser.isShowImg()){%>
<img src="<%=Constants.IMG_ROOT_URL%><%=forum1.getAttach()%>" alt="o"/>
<%}else{%>
(附件)
<%}%>
<a href="<%=Constants.IMG_ROOT_URL%><%=forum1.getAttach()%>">下载</a><br/>
<%}%>(<%=forum1.getCreateDatetime()%>)
<%if("super".equals(popedom) || "manager".equals(popedom) || (loginUser!=null && forum1.getUserId()==loginUser.getId())){%>
<a href="viewContentHis.jsp?del=<%=forum1.getId()%>&amp;contentId=<%=con.getId()%>">删除</a>
<%}%><br/><%}
  }
 }%>
<%=paging.shuzifenye("viewContentHis.jsp?contentId=" + con.getId(), true, "|", response)%>
<%if(paging.getTotalPageCount()>5){%>
跳到<input name="index"  maxlength="5" format="*N" value="1"/>页
<anchor title="确定">GO
<go href="viewContentHis.jsp?forumId=<%=con.getForumId()%>&amp;contentId=<%=con.getId()%>" method="post">
<postfield name="go" value="$index"/>
</go>
</anchor><br/>
<%}%>
<%
}else{%>
 该主题不存在!<br/>
<%}
	if(con!=null){
  ForumBean forum=ForumCacheUtil.getForumCache(con.getForumId());
  if(forum!=null){%>
<a href="history.jsp?forumId=<%=con.getForumId()%>">返回<%=StringUtil.toWml(forum.getTitle())%>历史</a><%if(con.getMark1()==1){%>|<a href="forum.jsp?prime=1&amp;forumId=<%=con.getForumId()%>">回精华区</a><%}%><br/>
<%}}%>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>