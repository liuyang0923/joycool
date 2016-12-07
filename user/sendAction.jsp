<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.user.SendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
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

//动作接收者信息
UserBean toUser=(UserBean)request.getAttribute("toUser");
//好友
Integer isFriend=(Integer)request.getAttribute("isFriend");
//黑名单用户
Integer isBadGuy=(Integer)request.getAttribute("isBadGuy");
//用户可以发送得动作list
List actionList=(List)request.getAttribute("actionList");
//升级后用户可以发送得动作list
List updateActionList=(Vector)request.getAttribute("updateActionList");
PagingBean paging = ((PagingBean) request.getAttribute("paging"));
String prefixUrl = (String) request.getAttribute("prefixUrl");

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
<card title="动作列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
发送对象:<%if(toUser.getUs2()!=null){%><%=toUser.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(toUser.getNickName())%><br/>
<%if(flag) {%>
===可选动作===<br/>
<%
RankActionBean rankAction=null;
for(int i=0;i<actionList.size();i++){
rankAction=(RankActionBean)actionList.get(i);
int actionId=rankAction.getId();
if(actionId==4443){%>
<a href="<%=("actionResult.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId()+"&amp;isPrivate=yes")%>"><%=rankAction.getActionName()%><%--(<%=rankAction.getNeedGamePoint()%>)--%>(限私发)</a><br/>
<%}else{
%>
<a href="<%=("actionResult.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId()+"&amp;isPrivate=no")%>"><%=rankAction.getActionName()%><%--(<%=rankAction.getNeedGamePoint()%>)--%></a>
<a href="<%=("actionResult.jsp?roomId="+roomId+"&amp;actionId="+rankAction.getId()+"&amp;toUserId="+toUser.getId()+"&amp;isPrivate=yes")%>">悄悄发</a><br/>
<%}}%>
<%=paging.shuzifenye(prefixUrl, true, "|", response)%>
<%for(int i=0;i<updateActionList.size();i++){
rankAction=(RankActionBean)updateActionList.get(i);%>
升级后还可以:<%=rankAction.getActionName()%><br/>
<%}%>
<%if(toUser.getOnlineStatus() != null){%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUser.getId()%>">与<%=toUser.getGender() == 1? "他" : "她"%>聊天</a>
<%}else{%>
<a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUser.getId()%>">给<%=toUser.getGender() == 1? "他" : "她"%>留言</a>&nbsp; 
<%}%>
<%} else { 
	long left = cuffUser.time - System.currentTimeMillis();
%>
你被别人铐住了手，<%=left / 1000 %>秒钟后才能发表动作<br/>
<%} %>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="ViewUserInfo.do?userId=<%=toUser.getId()%>">返回用户信息</a><br/>
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