<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*,net.joycool.wap.bean.chat.*,java.util.*,net.joycool.wap.action.chat.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page errorPage=""%><%
response.setHeader("Cache-Control","no-cache");
//zhul 自建聊天室7 显示自建聊天室和自己为管理员的聊天室_接收参数 2006-06-27 start
JCRoomChatAction roomAction=new JCRoomChatAction(request);
roomAction.getChatRoom(request);
/**int ownerTotalPage= Integer.parseInt((String)request.getAttribute("ownerTotalPage"));
int pageIndex=Integer.parseInt((String)request.getAttribute("pageIndex"));
Vector roomList=(Vector)request.getAttribute("roomList");

int manaTotalPage = Integer.parseInt((String)request.getAttribute("manaTotalPage"));
int pageIndex1 = Integer.parseInt((String)request.getAttribute("pageIndex1"));
Vector manaRoomList=(Vector)request.getAttribute("manaRoomList");

String ownerPrefixURL="mySpace.jsp?pageIndex1="+pageIndex1;
String manaPrefixURL="mySpace.jsp?pageIndex="+pageIndex;
*/
//zhul 自建聊天室7 显示自建聊天室和自己为管理员的聊天室_接收参数 2006-06-27 end
Vector roomList=(Vector)request.getAttribute("roomList");
Vector manaRoomList=(Vector)request.getAttribute("manaRoomList");
Vector nearlyRoomList=(Vector)request.getAttribute("nearlyRoomList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人功能选项">
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
<p align="left">功能选项</p>
<p align="left">
<a href="/user/userInfo.jsp?backTo=<%=PageUtil.getBackTo(request)%>">个人资料</a><br/>
<a href="/user/ViewFriends.do?backTo=<%=PageUtil.getBackTo(request)%>">好友管理</a><br/>
<a href="/user/messageIndex.jsp?backTo=<%=PageUtil.getBackTo(request)%>">信箱管理</a><br/>
<a href="/mycart.jsp?backTo=<%=PageUtil.getBackTo(request)%>">我的收藏夹</a><br/>
<!--zhul 自建聊天室7 显示自建聊天室和自己为管理员的聊天室 2006-06-27 start-->
<%
if(nearlyRoomList!=null ){
    if(nearlyRoomList.size()>0){
%>
==我最近去过的聊天室==<br/>
<%
	for(int i=0;i<nearlyRoomList.size();i++)
	{
		JCRoomBean jcRoom=(JCRoomBean)nearlyRoomList.get(i);
%>
	<%=StringUtil.toWml(jcRoom.getName()+"("+jcRoom.getCurrentOnlineCount()+"/"+jcRoom.getMaxOnlineCount()+")")%> <a href="/chat/hall.jsp?roomId=<%=jcRoom.getId()%>&amp;backTo=<%=PageUtil.getBackTo(request)%>">进入</a><br/>
<%}}
}else{%>
<%
int rooms=roomList.size();
if(rooms>0){
%>
==我的房产==<br/>
<%
	for(int i=0;i<rooms;i++)
	{
		JCRoomBean jcRoom=(JCRoomBean)roomList.get(i);
	%>
	<%=StringUtil.toWml(jcRoom.getName()+"("+jcRoom.getCurrentOnlineCount()+"/"+jcRoom.getMaxOnlineCount()+")")%> <a href="/chat/hall.jsp?roomId=<%=jcRoom.getId()%>&amp;backTo=<%=PageUtil.getBackTo(request)%>">进入</a>&nbsp;<a href="/chat/editHallLink.jsp?roomId=<%=jcRoom.getId()%>">修改</a><br/>
  <%}
}%>
<%--<%=PageUtil.shuzifenye(ownerTotalPage,pageIndex,(ownerPrefixURL),true," ",response)%><br/>--%>
<!--如果有托管聊天室则显示如下-->
<%
int manaRoom=manaRoomList.size();
if(manaRoom>0){
    IChatService chatService=ServiceFactory.createChatService();
%>
==托管房产==<br/>
<%
	for(int i=0;i<manaRoom;i++)
	{
		RoomManagerBean roomMana=(RoomManagerBean)manaRoomList.get(i);
		JCRoomBean room=chatService.getJCRoom("id="+roomMana.getRoomId());
%>
	<%=StringUtil.toWml(room.getName()+"("+room.getCurrentOnlineCount()+"/"+room.getMaxOnlineCount()+")")%> <a href="/chat/hall.jsp?roomId=<%=room.getId()%>&amp;backTo=<%=PageUtil.getBackTo(request)%>">进入</a>&nbsp;<a href="/chat/editHallLink.jsp?roomId=<%=room.getId()%>">修改</a><br/>
  <%}
  }
}%>
<%--<%=roomAction.shuzifenye(manaTotalPage,pageIndex1,(manaPrefixURL),true," ",response)%><br/>--%>
<!--zhul 自建聊天室7 显示自建聊天室和自己为管理员的聊天室 2006-06-27 end-->
<anchor><prev/>返回</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>