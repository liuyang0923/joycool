<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.util.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.user.SendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
SendAction action = new SendAction(request);
UserBean loginUser = action.getLoginUser();
action.send(request);
//得到房间号
int roomId=action.getParameterInt("roomId");
//封禁 zhul 2006-10-09 被禁言的用户不能发动作 start
String un = loginUser.getUserName();
int count=0;
String user=null;
Vector postList = ContentList.postList;
if(postList != null){
	count=postList.size();
	for(int i=0;i<count;i++){
		user=(String)postList.get(i);
	    if(un.equals(user)){
		//response.sendRedirect(("/chat/hall.jsp?roomId="+roomId));
		BaseAction.sendRedirect("/chat/hall.jsp?roomId="+roomId, response);
		return;
		}
	}
}
//封禁 zhul 2006-10-09 被禁言的用户不能发动作 end
if(roomId==null){
roomId="0";
}

int userId = action.getParameterInt("toUserId");
UserBean toUser = null;
if(userId > 0)
	toUser = UserInfoUtil.getUser(userId);
int use = 0;
if(UserBagCacheUtil.getUserBagItemCount(SendAction.ACTION_A_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(SendAction.ACTION_A_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x1;		//套餐A
	}
	
}
if(UserBagCacheUtil.getUserBagItemCount(SendAction.ACTION_B_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(SendAction.ACTION_B_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x2;		//套餐B
	}
}
if(UserBagCacheUtil.getUserBagItemCount(SendAction.ACTION_C_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(SendAction.ACTION_C_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x4;		//套餐C
	}
}
if(UserBagCacheUtil.getUserBagItemCount(SendAction.ACTION_D_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(SendAction.ACTION_D_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x8;		//套餐D
	}
}
if(UserBagCacheUtil.getUserBagItemCount(SendAction.ACTION_E_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(SendAction.ACTION_E_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x10;		//套餐E
	}
}
SendAction.CuffUser cuffUser = null;
boolean flag = false;
	synchronized(SendAction.handcuff) {
	cuffUser= (SendAction.CuffUser)SendAction.handcuff.get(new Integer(loginUser.getId()));
	if(cuffUser == null) {
		flag = true;
	} else if(cuffUser.time < System.currentTimeMillis()) {
		flag = true;
		SendAction.handcuff.remove(new Integer(loginUser.getId()));
	}
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="动作卡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
发送对象:<%if(toUser != null) {if(toUser.getUs2()!=null){%><%=toUser.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(toUser.getNickName())%><%}else { %>所有人<%} %><br/>
<%if(flag) {

%>
<%if((use & 0x1) != 0) {%>动作套餐A：<br/>
<%
List actionList = SendAction.getRankActionList(SendAction.ACTION_A_ITEM);
PagingBean paging = new PagingBean(action, actionList.size(), 5, "pa");
RankActionBean rankAction=null;
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
rankAction=(RankActionBean)actionList.get(i);
if(toUser != null) {%>
<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId())%>"><%=rankAction.getActionName()%></a>
|<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId() + "&amp;isPrivate=1")%>">悄悄发</a><br/>
<%} else {%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;actionId=<%=rankAction.getId() %>"><%=rankAction.getActionName()%></a><%}%>
<%}if(userId>0){
	%><%=paging.shuzifenye("action.jsp?toUserId="+userId, true, "|", response)%>
	<%} else {
	%>
	<%=paging.shuzifenye("action.jsp", false, "|", response)%>
	<%}
}%>

<%if((use & 0x2) != 0) {%>动作套餐B：<br/>
<%
List actionList = SendAction.getRankActionList(SendAction.ACTION_B_ITEM);
PagingBean paging = new PagingBean(action, actionList.size(), 5, "pb");
RankActionBean rankAction=null;
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
rankAction=(RankActionBean)actionList.get(i);
if(toUser != null) {%>
<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId())%>"><%=rankAction.getActionName()%></a>
|<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId() + "&amp;isPrivate=1")%>">悄悄发</a><br/>
<%} else {%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;actionId=<%=rankAction.getId() %>"><%=rankAction.getActionName()%></a><%}%>
<%}if(userId>0){
	%><%=paging.shuzifenye("action.jsp?toUserId="+userId, true, "|", response)%>
	<%} else {
	%>
	<%=paging.shuzifenye("action.jsp", false, "|", response)%>
	<%}
}%>

<%if((use & 0x4) != 0) {%>动作套餐C：<br/>
<%
List actionList = SendAction.getRankActionList(SendAction.ACTION_C_ITEM);
PagingBean paging = new PagingBean(action, actionList.size(), 5, "pc");
RankActionBean rankAction=null;
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
rankAction=(RankActionBean)actionList.get(i);
if(toUser != null) {%>
<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId())%>"><%=rankAction.getActionName()%></a>
|<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId() + "&amp;isPrivate=1")%>">悄悄发</a><br/>
<%} else {%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;actionId=<%=rankAction.getId() %>"><%=rankAction.getActionName()%></a><%}%>
<%}if(userId>0){
	%><%=paging.shuzifenye("action.jsp?toUserId="+userId, true, "|", response)%>
	<%} else {
	%>
	<%=paging.shuzifenye("action.jsp", false, "|", response)%>
	<%}
}%>

<%if((use & 0x8) != 0) {%>动作套餐D：<br/>
<%
List actionList = SendAction.getRankActionList(SendAction.ACTION_D_ITEM);
PagingBean paging = new PagingBean(action, actionList.size(), 5, "pd");
RankActionBean rankAction=null;
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
rankAction=(RankActionBean)actionList.get(i);
if(toUser != null) {%>
<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId())%>"><%=rankAction.getActionName()%></a>
|<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId() + "&amp;isPrivate=1")%>">悄悄发</a><br/>
<%} else {%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;actionId=<%=rankAction.getId() %>"><%=rankAction.getActionName()%></a><%}%>
<%}if(userId>0){
	%><%=paging.shuzifenye("action.jsp?toUserId="+userId, true, "|", response)%>
	<%} else {
	%>
	<%=paging.shuzifenye("action.jsp", false, "|", response)%>
	<%}
}%>

<%if((use & 0x10) != 0) {%>动作套餐E：<br/>
<%
List actionList = SendAction.getRankActionList(SendAction.ACTION_E_ITEM);
PagingBean paging = new PagingBean(action, actionList.size(), 5, "pe");
RankActionBean rankAction=null;
for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
rankAction=(RankActionBean)actionList.get(i);
if(toUser != null) {%>
<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId())%>"><%=rankAction.getActionName()%></a>
|<a href="<%=("post.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId() + "&amp;isPrivate=1")%>">悄悄发</a><br/>
<%} else {%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;actionId=<%=rankAction.getId() %>"><%=rankAction.getActionName()%></a><%}%>
<%}if(userId>0){
	%><%=paging.shuzifenye("action.jsp?toUserId="+userId, true, "|", response)%>
	<%} else {
	%>
	<%=paging.shuzifenye("action.jsp", false, "|", response)%>
	<%}
}%>

<%} else { 
	long left = cuffUser.time - System.currentTimeMillis();
%>
你被别人铐住了手，<%=left / 1000 %>秒钟后才能发动作<br/>
<%} %>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%if(toUser != null) {%>
<a href="ViewUserInfo.do?userId=<%=toUser.getId()%>">返回用户信息</a><br/>
<%} %>
<a href="ViewFriends.do">返回好友列表</a><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>