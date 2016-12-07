<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
boolean showFaceImg = true, showSendImg = true, confirm=false, xhtml=false;
if(loginUser!=null&&loginUser.getUserSetting()!=null) {
	showFaceImg = !loginUser.getUserSetting().isFlagHideFace();
	showSendImg = !loginUser.getUserSetting().isFlagHideSend();
	xhtml = loginUser.getUserSetting().isFlagXhtml();
}
action.viewContent(request,response);
PagingBean paging = (PagingBean)request.getAttribute("page");
int order = 0;
boolean full = action.hasParam("fu");	// 展开回复列表
boolean rev = action.hasParam("v");
if((loginUser != null && loginUser.getUserSetting().isFlagVorder()) ^ rev)
	order = 1;
List replyList = (List)request.getAttribute("replyList");
String popedom = (String) request.getAttribute("popedom");
ForumContentBean con =(ForumContentBean)request.getAttribute("con");
String prefixUrl = (String) request.getAttribute("prefixUrl");
String isDel = (String) request.getAttribute("isDel");
session.setAttribute("viewContent","true");
String result = (String)request.getAttribute("result");
ForumContentBean prevForumContent=(ForumContentBean)request.getAttribute("prevForumContent");
ForumContentBean nextForumContent=(ForumContentBean)request.getAttribute("nextForumContent");
ForumContentBean prevJHForumContent=(ForumContentBean)request.getAttribute("prevJHForumContent");
ForumContentBean nextJHForumContent=(ForumContentBean)request.getAttribute("nextJHForumContent");
ForumBean forum=null;
if(con!=null) forum=ForumCacheUtil.getForumCache(con.getForumId());
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
<a href="viewContent.jsp?contentId=<%=con.getId()%>">返回帖子</a><br/>
<%}else if(action.isResult("confirm")){
ForumReplyBean reply = (ForumReplyBean)request.getAttribute("reply");
%>
<%=StringUtil.toWml(StringUtil.limitString(reply.getContent(), 50))%><br/>
确定要删除该回复吗?<br/>
<a href="viewContent.jsp?c=1&amp;contentId=<%=con.getId()%>&amp;del=<%=reply.getId()%>">确认删除</a><br/>
<a href="viewContent.jsp?contentId=<%=con.getId()%>">返回帖子</a><br/>
<%}else if(con!=null){

String editUrl;
if(xhtml)
	editUrl = "editTitleX.jsp?contentId="+con.getId();
else
	editUrl = "editTitle.jsp?contentId="+con.getId();

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
}else{%>游客<%}%>(<%=DateUtil.sformatTime(con.getCreateTime())%>)<br/>
<%	if(con!=null && paging.getCurrentPageIndex()==0 && !action.hasParam("r") && !full){%>
<%=ForumAction.speContent(response,action.contentTransform(con,request,response, showSendImg), showFaceImg)%>
<%--if(con.getType()!=0){%>已经有<%=ForumCacheUtil.getVoteCount(con.getId())%>人参与了投票<br/><%}--%>
<%}%>
<%if(!con.isReadonly()&&loginUser!=null){%>
<a href="editReply.jsp?contentId=<%=con.getId()%>&amp;p=<%=paging.getCurrentPageIndex()%>">回复</a>
<%} 
if(loginUser!=null){%>
<%if(ForbidUtil.isForbid("newsa",loginUser.getId())){%>|<a href="addNews.jsp?id=<%=con.getId()%>"><%if(!con.isNews()){%>+<%}%>新闻</a><%}%>
|<a href="result.jsp?contentId=<%=con.getId()%>&amp;cart=1">收藏</a>
<%}%>
<%if("manager".equals(popedom)){%>
<%if(!con.isReadonly()){%>
|<a href="forum.jsp?del=<%=con.getId()%>&amp;forumId=<%=con.getForumId()%>">删除</a>
<%}%>
|<%if(con.getMark1()==1){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delprime=1">去精</a>
|<%}else{%>
<a href="primea.jsp?prime=<%=con.getId()%>">加精</a>
|<%}%>
<%if(con.isPeak()){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delpeak=1">取消置顶</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;peak=1">置顶</a>
|<%}%>
<%if(con.isReadonly()){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delro=1">-锁</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ro=1">+锁</a>
|<%}%>
<%if(con.getType()>0){	// 投票帖子，结束后type重置为0%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ev=1">结束投票</a>|
<%}%>
<%if(!con.isReadonly()){%>
<a href="<%=editUrl%>">编辑</a><%if(con.getUserId()==loginUser.getId()){%>|<a href="contentWrite.jsp?contentId=<%=con.getId()%>">续写</a><%}%>
<%}%>
<%}else if("super".equals(popedom)){%>
|<a href="forum.jsp?del=<%=con.getId()%>&amp;forumId=<%=con.getForumId()%>">删除</a>
|<%if(con.getMark1()==1){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delprime=1">去精</a>
|<%}else{%>
<a href="primea.jsp?prime=<%=con.getId()%>">加精</a>
|<%}%>
<%if(con.isPeak()){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delpeak=1">取消置顶</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;peak=1">置顶</a>
|<%}%>
<%if(con.isTopPeak()){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;deltoppeak=1">取消总置顶</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;toppeak=1">总置顶</a>
|<%}%>
<%if(con.isReadonly()){%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;delro=1">-锁</a>
|<%}else{%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ro=1">+锁</a>
|<%}%>
<%if(con.getType()>0){	// 投票帖子，结束后type重置为0%>
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ev=1">结束投票</a>|
<%}%>
<a href="<%=editUrl%>">编辑</a><%if(con.getUserId()==loginUser.getId()){%>|<a href="contentWrite.jsp?contentId=<%=con.getId()%>">续写</a><%}%>
<%}else if("mycontent".equals(popedom)){%>
<%if(!con.isReadonly()){%>
|<a href="forum.jsp?del=<%=con.getId()%>&amp;forumId=<%=con.getForumId()%>">删除</a>
<%if(!DateUtil.timepast(con.getCreateTime(),300)){%>
|<a href="<%=editUrl%>">编辑</a>
<%}%>
|<a href="contentWrite.jsp?contentId=<%=con.getId()%>">续写</a>
<%}%>
<%}else if("normal".equals(popedom)){%>
<%if(ForbidUtil.isForbid("foruma",loginUser.getId())){%><%if(!con.isReadonly()){%>|
<a href="result.jsp?contentId=<%=con.getId()%>&amp;ro=1">+锁</a>
|<%}%><%}%>
<%}else{%>
  <%} if(paging.getCurrentPageIndex()+1 < paging.getTotalPageCount()) {
String lastPage = "viewContent.jsp?contentId=" + con.getId();
if(!rev)
	lastPage += "&amp;v=1";
  %>
|<a href="<%=(lastPage)%>">尾页</a><%}%><br/>
<% if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
<%}%>
<%
  if(replyList!=null){
String fullUrl = "viewContent.jsp?contentId="+con.getId()+"&amp;fu=1&amp;p="+paging.getCurrentPageIndex();	// 查看全部对应的url
if(rev)
	fullUrl += "&amp;v=1";
for(int i=0;i<replyList.size();i++){
   ForumReplyBean forum1=(ForumReplyBean)replyList.get(i);
   if(forum1!=null){%><%if(order==0){%><%=i+1+paging.getCurrentPageIndex()*ForumAction.NUMBER_PER_PAGE%><%}else{%><%=paging.getTotalCount()-i-paging.getCurrentPageIndex()*ForumAction.NUMBER_PER_PAGE%><%}%>.<%UserBean user=(UserBean)UserInfoUtil.getUser(forum1.getUserId());
   if(user!=null){
if(user.getId()==100){%>系统信息
    <%}else if(true){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
     <%}else{%>
<a href="/chat/post.jsp?roomId=0&amp;toUserId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>
<%}  }else{%>
游客<%}%>:<%if(forum1.getCType()==1){%><%if(showFaceImg){%><img src="../img/jcforum/action/<%=forum1.getContent()%>.gif" alt=""/><%}else{%>表情(<%=ForumAction.getFaceString(forum1.getContent())%>)<%}%>
<%}else{%>
	<%if(full||forum1.getContent().length()<=100){	// 是否显示所有的回复内容，如果没有选择，只显示前100字
		%><%=ForumAction.speContent(response,StringUtil.toWml(forum1.getContent()), showFaceImg)%><%
	}else{
		%><%=StringUtil.toWml(forum1.getContent().substring(0,95))%>(省略<%=forum1.getContent().length()-75%>字)<a href="<%=fullUrl%>">显示全部</a><%
	}%>
<%}%>
<%if(forum1.getAttach()!=null & !("").equals(forum1.getAttach())){%>
<%if(showSendImg){%>
<img src="<%=Constants.IMG_ROOT_URL%><%=forum1.getAttach()%>" alt="o"/>
<%}else{%>
(附件)
<%}%>
<a href="<%=Constants.IMG_ROOT_URL%><%=forum1.getAttach()%>">下载</a><br/>
<%}%>(<%=DateUtil.sformatTime(forum1.getCreateTime())%>)
<%if("super".equals(popedom) || "manager".equals(popedom) || (loginUser!=null && forum1.getUserId()==loginUser.getId())){%>
<a href="viewContent.jsp?del=<%=forum1.getId()%>&amp;contentId=<%=con.getId()%>">删除</a>
<%}%><br/><%}
  }
 }
String prefixURL = "viewContent.jsp?contentId=" + con.getId();
if(rev)
	prefixURL += "&amp;v=1";
 %>
<%=paging.shuzifenye(prefixURL, true, "|", response)%>
<%if(paging.getTotalPageCount()>5){%>
跳到<input name="index"  maxlength="5" format="*N" value="1"/>页
<anchor title="确定">GO
<go href="viewContent.jsp?forumId=<%=con.getForumId()%>&amp;contentId=<%=con.getId()%>" method="post">
<postfield name="go" value="$index"/>
</go>
</anchor><br/>
<%}%>
<%if(!con.isReadonly()&&loginUser!=null){
%>
<input name="content"  maxlength="1000" value=""/><br/>
<anchor title="确定">快速回复|
<go href="viewContent.jsp?contentId=<%=con.getId()%>" method="post">
    <postfield name="content" value="$content"/>
</go></anchor>
<a href="editReply.jsp?contentId=<%=con.getId()%>&amp;p=<%=paging.getCurrentPageIndex()%>">回复</a>.<a href="/admin/report/forum.jsp?id=<%=con.getId()%>">x举报x</a><br/>
<%}%><%

if(prevForumContent==null&&nextForumContent==null){

%><a href="viewContent.jsp?contentId=<%=con.getId()%>&amp;next=1">下一贴</a>|<%
int first = action.getFirstContentId(con.getForumId());
if(first==0||first==con.getId()){%>上一贴<br/><%}else{%><a href="viewContent.jsp?contentId=<%=con.getId()%>&amp;prev=1">上一贴</a><%}

}

String myTopic=(String)request.getAttribute("myTopic");
int sUserId=StringUtil.toInt((String)request.getAttribute("sUserId"));
//显示下一条
if(prevForumContent != null){
	if(myTopic!=null){%>
	
		<a href="<%=("viewContent.jsp?contentId="+prevForumContent.getId()+"&amp;forumId="+con.getForumId()+"&amp;myTopic=true")%>">下一帖<%--:<%=StringUtil.toWml(prevForumContent.getTitle())%>--%></a>
	<%}else if(sUserId>0){%>
		<a href="viewContent.jsp?contentId=<%=prevForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>&amp;sUserId=<%=sUserId%>">下一帖<%--:<%=StringUtil.toWml(prevForumContent.getTitle())%>--%></a>
	<%}else{%>
		<a href="viewContent.jsp?contentId=<%=prevForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>">下一帖<%--:<%=StringUtil.toWml(prevForumContent.getTitle())%>--%></a>
	<%}%>
<%}
//显示上一条
if(nextForumContent != null){
	if(myTopic!=null){%>
<a href="<%=("viewContent.jsp?contentId="+nextForumContent.getId()+"&amp;forumId="+con.getForumId()+"&amp;myTopic=true")%>">上一帖<%--:<%=StringUtil.toWml(nextForumContent.getTitle())%>--%></a>
	<%}else if(sUserId>0){%>
<a href="viewContent.jsp?contentId=<%=nextForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>&amp;sUserId=<%=sUserId%>">上一帖<%--:<%=StringUtil.toWml(nextForumContent.getTitle())%>--%></a>
	<%}else{%>
<a href="viewContent.jsp?contentId=<%=nextForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>">上一帖<%--:<%=StringUtil.toWml(nextForumContent.getTitle())%>--%></a>
	<%}
}
%><br/><%if(con.getMark1()==1&&forum.getPrimeCat()==0&&nextJHForumContent != null||prevJHForumContent != null){//显示精华上一条
// 如果是分类精华，不显示上下精华帖
if(nextJHForumContent != null){%><a href="viewContent.jsp?contentId=<%=nextJHForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>">下一精华</a><%}
%><%//显示精华下一条
if(prevJHForumContent != null){%>|<a href="viewContent.jsp?contentId=<%=prevJHForumContent.getId()%>&amp;forumId=<%=con.getForumId()%>">上一精华</a>
<%}%><br/><%
}
}else{%>
 该主题不存在!<br/>
<%}
	if(con!=null){
  
  if(forum!=null){%>
<a href="forum.jsp?forumId=<%=con.getForumId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><%if(con.getMark1()==1){%>|<%if(forum.getPrimeCat()==0){%><a href="forum.jsp?type=1&amp;forumId=<%=con.getForumId()%>">回精华区</a><%}else{%><a href="prime.jsp?cat=<%=forum.getPrimeCat()%>">分类精华</a><%}%><%}%><br/>
<%}}%>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>