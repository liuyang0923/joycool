<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.OperUserAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="java.net.URLEncoder"%><%
response.setHeader("Cache-Control","no-cache");
OperUserAction.operUser(request, response);
String title = (String) request.getAttribute("title");
String action = (String) request.getAttribute("action");
String returnUrl = (String)session.getAttribute("pagebeforeclick");
if(returnUrl==null){
	returnUrl = "ViewFriends.do";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//邀请聊天
if("inviteChat".equals(action)){
	String goTo = (String) request.getAttribute("goTo");
	if(returnUrl!=null){
		goTo = (returnUrl.replace("&", "&amp;"));
	}
%>
<card title="邀请聊天" ontimer="<%=response.encodeURL(goTo)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
邀请聊天<br/>
--------<br/>
邀请成功！您先去聊天室吧坐坐等着吧，他/她马上就来<br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
//邀请游戏
else if("inviteGame".equals(action)){
	//进入页
	String result = (String) request.getAttribute("result");
	//进入页
	if("failure".equals(result))
	{
	String tip = (String) request.getAttribute("tip");
	%>
	<card title="邀请游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<%
	}
	else if(result == null){
		String toUserId = (String) request.getAttribute("toUserId");
		String backTo = returnUrl;
		backTo = (backTo.replace("&", "&amp;"));
%>
<card title="邀请游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
选择游戏<br/>
--------<br/>
<a href="/wgamepk/lgj/pkStart.jsp?userId=<%=toUserId%>" title="进入">PK老虎杠子鸡</a><br/>
<a href="/wgamepk/basketball/pkStart.jsp?userId=<%=toUserId%>" title="进入">PK篮球飞人</a><br/>
<a href="/wgamepk/jsb/pkStart.jsp?userId=<%=toUserId%>" title="进入">PK剪刀石头布</a><br/>
<a href="/wgamepk/dice/pkStart.jsp?userId=<%=toUserId%>" title="进入">PK掷骰子</a><br/>
<a href="/wgamepk/3gong/pkStart.jsp?userId=<%=toUserId%>" title="进入">PK三公</a><br/>
<a href="/wgamepk/football/pkStart.jsp?userId=<%=toUserId%>" title="进入">PK射门</a><br/>
<a href="/wgamehall/football/invite.jsp?userId=<%=toUserId%>" title="进入">点球决战</a><br/>
<a href="/wgamehall/othello/invite.jsp?userId=<%=toUserId%>" title="进入">黑白棋</a><br/>
<a href="/wgamehall/gobang/invite.jsp?userId=<%=toUserId%>" title="进入">五子棋</a><br/>
<a href="/wgamehall/jinhua/pkinvit.jsp?userId=<%=toUserId%>" title="进入">砸金花</a><br/>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="<%=backTo%>" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
    //处理页
	else {
		String goTo = (String) request.getAttribute("goTo");
		if(returnUrl!=null){
			goTo = (returnUrl.replace("&", "&amp;"));
		}
%>
<card title="邀请游戏" ontimer="<%=response.encodeURL(goTo)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
邀请游戏<br/>
--------<br/>
邀请成功！您先进去吧，他/她马上就来<br/>
<br/>
<%
if(returnUrl!=null){
%>
<a href="<%= (returnUrl.replace("&", "&amp;"))%>" title="进入">返回</a><br/>
<%
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
	}
}
//送乐币给对方
else if("sendMoney".equals(action)){
	String result = (String) request.getAttribute("result");
	//进入页
	if(result == null){
		String toUserId = (String) request.getAttribute("toUserId");
		UserStatusBean us = (UserStatusBean) request.getAttribute("us");
		//封禁 wucx 2006-10-20 被禁言的用户不能送乐币 start
		UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        String un = loginUser.getUserName();
        int count=0;
        String user=null;
        Vector postList = ContentList.postList;
        if(postList != null){
	    count=postList.size();
	    for(int i=0;i<count;i++){
		 user=(String)postList.get(i);
	    if(un.equals(user)){
	    	out.clearBuffer();
			response.sendRedirect(("/chat/hall.jsp"));
			return;
		}
	}
}
//封禁 wucx 2006-10-20 被禁言的用户不能送乐币 end
%>
<card title="送乐币">
<p align="left">
<%=BaseAction.getTop(request, response)%>
送乐币<br/>
--------<br/>
乐币数:(您还有<%=us.getGamePoint()%>个乐币)<br/>
<input name="money" format="*N" maxlength="10" value="100"/><br/>
<anchor title="确定">确定
    <go href="operUser.jsp?action=sendMoney&amp;toUserId=<%=toUserId%>" method="post">
    <postfield name="money" value="$money"/>
    </go>
    </anchor>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<% 
if(returnUrl!=null){
%>
<a href="<%= (returnUrl.replace("&", "&amp;"))%>" title="进入">返回</a><br/>
<%
}
%>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<a href="/user/ViewFriends.do">返回好友列表</a><br/>
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
<%
	}
    //处理页
    else {
		String toUserId = (String) request.getAttribute("toUserId");
		UserStatusBean us = (UserStatusBean) request.getAttribute("us");
		String tip = (String) request.getAttribute("tip");
%>
<card title="送乐币" ontimer="<%=response.encodeURL(returnUrl.replace("&", "&amp;"))%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
送乐币<br/>
--------<br/>
<%
        if("failure".equals(result)){
%>
<%=tip%><br/>
<%
        } else {
       // session.setAttribute("d",99);
%>
赠送成功，您还有<%=us.getGamePoint()%>个乐币)<br/>
(3秒后自动跳转。)<br/>
<%
        }
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%
returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl!=null){
%>
<a href="<%= (returnUrl.replace("&", "&amp;"))%>">返回</a><br/>
<%
}
%>
<a href="/user/ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<a href="/user/ViewFriends.do">返回好友列表</a><br/>
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
<%
	}
}
%>
</wml>