
<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
//如果不是管理员，则返回聊天大厅
action.isManager(request,response);
action.getUserList2(request,response);
UserBean user=null;
int totalManager=Integer.parseInt((String)request.getAttribute("totalManager"));
//int managerIndex=Integer.parseInt((String)request.getAttribute("managerIndex"));
//int friendIndex=Integer.parseInt((String)request.getAttribute("friendIndex"));
//int onlineIndex=Integer.parseInt((String)request.getAttribute("onlineIndex"));
String managerPagination=(String)request.getAttribute("managerPagination");
String friendPagination=(String)request.getAttribute("friendPagination");
String onlinePagination=(String)request.getAttribute("onlinePagination");
Vector vecManager=(Vector)request.getAttribute("vecManager");
Vector vecFriend=(Vector)request.getAttribute("vecFriend");
Vector vecOnline=(Vector)request.getAttribute("vecOnline");
int roomId=0;
if (request.getParameter("roomId") != null)
	roomId = Integer.parseInt(request.getParameter("roomId"));
//String backTo="roomManager.jsp?managerIndex="+managerIndex+"&amp;friendIndex="+friendIndex+"&amp;onlineIndex="+onlineIndex;
String navigateTo=(String)request.getAttribute("navigateTo");
//System.out.println("navigateTo="+navigateTo);
//String backTo="/chat/hall.jsp?&amp;roomId="+request.getParameter("roomId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="设置管理员">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您可以批准任意多的人，成为您房间的管理员。管理员有踢人、审批进入权限。但只有房主有删除管理员权限<br/>
现有管理员：<%=totalManager%>人<br/>
你自己<br/>
<%
	for(int i=0;i<vecManager.size();i++){
		user=(UserBean)vecManager.get(i);
%>
<a href="<%=navigateTo+"&amp;userId="+user.getId()+"&amp;deleteOrAdd=0" %>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%>(撤销)</a><br/>
<%}if(!managerPagination.equals("")){ %>
<%=managerPagination %><br/><%}%>
好友列表<br/>
<%
	for(int i=0;i<vecFriend.size();i++){
		user=(UserBean)vecFriend.get(i);
%>
<a href="<%=navigateTo+"&amp;userId="+user.getId()+"&amp;deleteOrAdd=1" %>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName()) %>(设为管理员)</a><br/>
<%}if(!friendPagination.equals("")){ %>
<%=friendPagination %><br/><%}%>
在线用户<br/>
<%
	for(int i=0;i<vecOnline.size();i++){
		user=(UserBean)vecOnline.get(i);
%>
<a href="<%=navigateTo+"&amp;userId="+user.getId()+"&amp;deleteOrAdd=1" %>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName()) %>(设为管理员)</a><br/>
<%}if(!onlinePagination.equals("")){  %>
<%=onlinePagination %><br/><%}%>
<a href="/chat/hall.jsp?roomId=<%=roomId%>" title="进入">返回聊天室</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>