<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.service.impl.ChatServiceImpl"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.bean.chat.JCRoomOnlineBean" %><%
response.setHeader("Cache-Control","no-cache");
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 start
String reURL=request.getRequestURL().toString();
String queryStr=request.getQueryString();
session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 end
JCRoomChatAction action = new JCRoomChatAction(request);
action.hall(request,response);
String viewNum=(String)request.getAttribute("viewNum");//zhul 2006-09-13 获取当前聊天室要显示的人数
String roomId="0";	
//获取用户自建聊天室、托管聊天室或人气最高聊天室
action.getRooms(request,roomId);
int[] roomList=(int[])request.getAttribute("roomList");	

//zhul　2006-06-28　end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
Vector pml = (Vector)request.getAttribute("pml");
Vector ml = (Vector)request.getAttribute("ml");
String prefixUrl1="";
String backTo1="";
//初始化公聊分页参数
PagingBean paging = (PagingBean)request.getAttribute("paging");
String prefixUrl = "hall2.jsp?roomId=0";
String backTo = (String) request.getAttribute("backTo");

//得到公聊信息的数量
int count = ml.size();
JCRoomContentBean content = null;
String url = prefixUrl;
if(paging.getCurrentPageIndex() > 0) url += "&amp;p=" + paging.getCurrentPageIndex();
//取得聊天室名称
JCRoomBean room=(JCRoomBean)request.getAttribute("room");
String roomName=room.getName();
int countList=0;
%><card title="<%=roomName%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="500"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
==<%=room.getName()%>==<br/>
<a href="post.jsp">发言</a>|<a href="<%=url%>">刷新</a>|<a href="/user/viewLoginUserInfo.jsp">登录</a><br/>
<%
for(int i = 0; i < ml.size(); i ++){
	content = (JCRoomContentBean)ml.get(i);
	if(content==null)continue;
	%><%=(i + 1 )%>.<%=action.getMessageDisplay(content, request, response).replace("/chat/post.jsp", "/user/guestViewInfo.jsp")%><br/>
<%

}%>
<%=paging.shuzifenye(prefixUrl, true, "|", response)%>
==
<a href="onlineListMan.jsp?roomId=<%=roomId%>">聊天室聊客</a>==<br/>
<%
for(int i=0;i<roomList.length;i++)
{
	if(i>1) break;
	JCRoomBean jcRoom=RoomUtil.getRoomById(roomList[i]);
%><a href="hall2.jsp?roomId=<%=jcRoom.getId()%>"><%=StringUtil.toWml(jcRoom.getName())%></a>&nbsp;<%
}
%><a href="roomList.jsp">更多</a><br/>
&gt;&gt;<a href="/jcforum/index.jsp">论坛</a>.<a href="/user/onlineManager.jsp?forumId=355">管理员</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>