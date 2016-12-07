<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
RoomUserBean roomUser=null;
UserBean user=null;
JCRoomChatAction action=new JCRoomChatAction(request);
//如果不是管理员，则返回聊天大厅
action.isManager(request,response);
//获取用户列表＝目前聊天室里面的人＋踢出用户列表
action.getUserList(request,response);
int kickIndex=Integer.parseInt((String)request.getAttribute("kickIndex"));
int onlineIndex=Integer.parseInt((String)request.getAttribute("onlineIndex"));
int roomId=0;
if (request.getParameter("roomId") != null)
	roomId = Integer.parseInt(request.getParameter("roomId"));
Vector vecOnlineUsers=(Vector)request.getAttribute("onlineUsers");
Vector vecKicUsers=(Vector)request.getAttribute("kickUsers");
String onlinePagination=(String)request.getAttribute("onlinePagination");
String kickPagination=(String)request.getAttribute("kickPagination");
//String backTo="/chat/hall.jsp?roomId="+request.getParameter("roomId");
String backTo=request.getParameter("backTo");

	
//System.out.println("backto="+backTo);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="要踢出哪个人？">
<p align="left">
<%=BaseAction.getTop(request, response)%>
踢出的人，将再也无权进入聊天室哦。<br/>
目前聊天室的人：<br/>
<%for(int  i=0;i<vecOnlineUsers.size();i++){
	user=(UserBean)vecOnlineUsers.get(i);
%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName()) %><a href="<%=("kickInfo.jsp?status=0&amp;roomId="+roomId+"&amp;kickIndex="+kickIndex+"&amp;onlineIndex="+onlineIndex+"&amp;userId="+user.getId()+"&amp;userName="+StringUtil.toWml(user.getNickName()))%>">踢出</a><br/>
<%}if(!onlinePagination.equals("")){%>
<%=onlinePagination %><br/><%}%>
已经踢出的人：<br/>
<%for(int  i=0;i<vecKicUsers.size();i++){

	roomUser=(RoomUserBean)vecKicUsers.get(i);
//	user=action.getUser(" id="+roomUser.getUserId());
	//zhul 2006-10-12_优化用户信息查询
	user = UserInfoUtil.getUser(roomUser.getUserId());
	%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%><a href="<%=("kickInfo.jsp?status=1&amp;roomId="+roomId+"&amp;kickIndex="+kickIndex+"&amp;onlineIndex="+onlineIndex+"&amp;userId="+user.getId()+"&amp;userName="+StringUtil.toWml(user.getNickName()))%>">取消踢出</a><br/>
<%}if(!kickPagination.equals("")){%>
<%=kickPagination %><br/><%}%>
用户ID:<input name="kickId"  maxlength="10" value="v"/>
    <anchor title="确定">搜索
    <go href="searchKickUser.jsp?roomId=<%=roomId%>" method="post">
    <postfield name="kickId" value="$kickId"/>
    </go>
    </anchor>
<br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>" title="进入">返回聊天室</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>