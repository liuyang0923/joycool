<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.service.impl.ChatServiceImpl"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.bean.chat.JCRoomOnlineBean" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser == null){
	response.sendRedirect("hall2.jsp");
	return;
}
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 start
String reURL=request.getRequestURL().toString();
String queryStr=request.getQueryString();
session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 end
JCRoomChatAction action = new JCRoomChatAction(request);
int hidePublic = action.getParameterIntS("hide");
if(hidePublic==0) session.removeAttribute("hide_public");
else if(hidePublic==1) session.setAttribute("hide_public","");
action.hall(request,response);
String viewNum=(String)request.getAttribute("viewNum");//zhul 2006-09-13 获取当前聊天室要显示的人数
UserStatusBean us=null;
if(loginUser!=null){
us=UserInfoUtil.getUserStatus(loginUser.getId());
}
String result=(String) request.getAttribute("result");
int roomId=action.getParameterInt("roomId");		
//获取用户自建聊天室、托管聊天室或人气最高聊天室
action.getRooms(request,String.valueOf(roomId));
int[] roomList=(int[])request.getAttribute("roomList");	

//zhul　2006-06-28　end
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("waiting".equals(result)){
%>
<card title="聊天大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("close".equals(result)){
%>
<card title="聊天大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="payment.jsp?roomId=<%=roomId%>">续费房间</a><br/><br/>
<a href="hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("kick".equals(result) || "apply".equals(result)){
%>
<card title="聊天大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="apply.jsp?roomId=<%=roomId%>">申请进入</a><br/><br/>
<a href="hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("full".equals(result)){
String roomName=(String)request.getAttribute("roomName");
Vector roomCacheList=(Vector)request.getAttribute("roomCacheList");
//int[] roomArray ={6,8};
//String roomUrl=("hall.jsp?roomId="+roomArray[RandomUtil.nextInt(roomArray.length)]);
String roomUrl=("roomList.jsp");
%>
<card title="<%=roomName%>" ontimer="<%=response.encodeURL(roomUrl)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后跳转聊天广场)<br/>
<%for(int i =0;i<roomCacheList.size();i++){
JCRoomBean room = (JCRoomBean)roomCacheList.get(i);%>
<a href="hall.jsp?roomId=<%=room.getId()%>"><%=StringUtil.toWml(room.getName())%></a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
Vector pml = (Vector)request.getAttribute("pml");
Vector ml = (Vector)request.getAttribute("ml");
String prefixUrl1="";
String backTo1="";
//初始化公聊分页参数
PagingBean paging = (PagingBean)request.getAttribute("paging");
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
PagingBean paging1 = null;
if(pml!=null){
//私聊分页设置
paging1 = (PagingBean)request.getAttribute("paging1");
prefixUrl1 = (String) request.getAttribute("prefixUrl1");
backTo1 = (String) request.getAttribute("backTo1");
}
//得到公聊信息的数量
int count = ml.size();
JCRoomContentBean content = null;
String url = prefixUrl;
if(paging.getCurrentPageIndex() > 0) url += "&amp;p=" + paging.getCurrentPageIndex();
if(paging1!=null&&paging1.getCurrentPageIndex() > 0) url += "&amp;p1=" + paging1.getCurrentPageIndex();
url = (url);
String postUrl = ("post.jsp?roomId="+roomId);
backTo = (backTo);
//取得聊天室名称
JCRoomBean room=(JCRoomBean)request.getAttribute("room");
String roomName=room.getName();
int countList=0;
//countList=action.getOnlinecount("room_id="+roomId);
//countList=room.getCurrentOnlineCount();
if(loginUser!=null)	
	UserInfoUtil.getUser(loginUser.getId()).notice[0]=0;	// 清空新聊天数量

%><card title="<%=roomName%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="500"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(roomId==2){%>
<a href="/stock2/index.jsp">进入乐酷股市</a><br/>
<%}
if(loginUser != null && loginUser.getNickName() != null && loginUser.getNickName().startsWith("乐客")){
%>
风韵尤存的迎宾女金香玉媚笑道：哎唷官人，您可算来了，不过好像您还没有名字哦，快来给您自己<a href="/user/userInfo.jsp">定个江湖名号</a><br/>
<%
}if(session.getAttribute("hasEnterChatHall") == null && loginUser != null){
//欢迎语
Vector messagelist=(Vector) request.getAttribute("Message");
	int countren=messagelist.size();
	if(messagelist!=null && countren==2){
    if(loginUser.getGender()==1){
    String messagen=(String)messagelist.get(0);%>
	<%=messagen%><br/>
	<%}else{
		String messagev=(String)messagelist.get(1);%>
		<%=messagev%><br/>
		<%}
 session.setAttribute("hasEnterChatHall", "1");
 }
}
if(roomId==2){
%>
激荡股市风云，交流赚钱心得，体验看盘乐趣，炫耀分红快感，大家来聊哦～<br/>
<%
} else if(roomId==4){
%>
广东嘅朋友入来饮茶啦，嗨呢度大家可以讲白话，搵到同城嘅老乡同埋朋友！<br/>
<%
}
%>
<%
//判断是否有私人信息，如果存在显示聊天记录
if(pml!=null && loginUser!=null){
%>
==<%=StringUtil.toWml(roomName)%>(<%=viewNum%>人)==<br/>
<%
    boolean hasReturnUrl = false;
    String returnUrl = "";
    boolean isOtherRoom = false;
    JCRoomBean returnRoom = null;/*
    String lastModule = StringUtil.convertNull((String)session.getAttribute(Constants.LAST_MODULE));
    String lastModuleUrl = StringUtil.convertNull((String)session.getAttribute(Constants.LAST_MODULE_URL));
    if(!lastModule.equals("")){
    	String currentModule = StringUtil.convertNull((String)session.getAttribute(Constants.CURRENT_MODULE));
    	String lastRoomId = StringUtil.convertNull((String)session.getAttribute("lastroomid"));
		String currentRoomId = StringUtil.convertNull((String)session.getAttribute("currentroomid"));		
    	if(currentModule.equalsIgnoreCase("chat") && !lastRoomId.equals(currentRoomId) && !lastRoomId.equals("")){
    		hasReturnUrl = true;
    		isOtherRoom = true;
    		returnUrl = "/chat/hall.jsp?roomId=" + lastRoomId;
    		int returnRoomId = 0;
    		try{
    			returnRoomId = Integer.parseInt(lastRoomId);
    		}catch(Exception e)
    		{}
    		returnRoom = RoomUtil.getRoomById(returnRoomId);
    	}
    	else if(!lastModule.equals(currentModule)){     	
        	hasReturnUrl = true;
        	returnUrl = lastModuleUrl; 
        }
    }
	returnUrl = returnUrl.replace("&", "&amp;");*/
	returnUrl = PositionUtil.getLastModuleUrl(request,response);
%>
<% if(returnUrl.length()!=0){ 
       if(!isOtherRoom){     
%>
<a href="/user/ViewFriends.do?roomId=<%=roomId%>">好友
<%int friendCount=UserInfoUtil.getUserOnlineFriendsCount(loginUser.getId());
if(friendCount>0){%>
<%=friendCount%>
<%}%>
</a> |
<%=returnUrl%><br/>
<%     
       }else{
    	   if(returnRoom!=null){
%>
<a href="/user/ViewFriends.do?roomId=<%=roomId%>">好友
<%int friendCount=UserInfoUtil.getUserOnlineFriendsCount(loginUser.getId());
if(friendCount>0){%>
<%=friendCount%>
<%}%></a>
|<%=returnUrl%><br/>
<%    	   
           }
       }
   }
for(int i = 0; i < pml.size(); i ++){
	content = (JCRoomContentBean)pml.get(i);
%>
<%=(i + 1)%>.<%=action.getPrivateMessageDisplay(content, request, response)%><br/>
<%}%>
<%=paging1.shuzifenye(prefixUrl1, true, "|", response)%>
<%
LinkedList linkManList = RoomCacheUtil.getLinkManByIdList(loginUser.getId());
if(linkManList.size()>0){%>
<a href="linkMan.jsp?roomId=<%=roomId%>">最近联系人</a>
<%
ListIterator iter = linkManList.listIterator();
int limit = 0;
while(iter.hasNext()&&++limit<4){
	int userId = ((Integer)iter.next()).intValue();
	UserBean user=UserInfoUtil.getUser(userId);
	if(user==null)continue;%>
-<a href="post.jsp?roomId=<%=roomId %>&amp;toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getShortNickName())%></a>
<%}%><br/><%}
}
%>
==<%=room.getName()%><%if(pml!=null){
if(session.getAttribute("hide_public")==null){
%>(<a href="hall.jsp?hide=1&amp;roomId=<%=roomId%>">隐藏</a>)<%}else{
%>(<a href="hall.jsp?hide=0&amp;roomId=<%=roomId%>">显示</a>)<%}
}%>==<br/>
<%
if(JCRoomChatAction.speakerList.size() > 0) {
synchronized(JCRoomChatAction.speakerList) {
	ChatSpeakerBean bean = (ChatSpeakerBean)JCRoomChatAction.speakerList.getFirst();
	long now = System.currentTimeMillis();
	if(bean != null) {
		if((bean.getCreateTime().getTime() + 60*1000) <= now) {
			JCRoomChatAction.speakerList.removeFirst();
			ChatServiceImpl chatService = new ChatServiceImpl();
			chatService.updateChatSpeaker(bean.getId());
		}
%><a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=bean.getUid() %>"><%=StringUtil.toWml(bean.getName()) %></a>大喇叭:<%=StringUtil.toWml(bean.getContent()) %><br/>
<%}}} else {
	ChatServiceImpl chatService = new ChatServiceImpl();
	chatService.getChatSpeaker("mark = 0 order by create_time asc");
}%>
<%if(session.getAttribute("hide_public")==null){%>
<a href="<%=postUrl%>">发言</a>|<a href="<%=url%>">刷新</a>|<a href="/user/viewLoginUserInfo.jsp">登录</a><br/>
<%
for(int i = 0; i < ml.size(); i ++){
	content = (JCRoomContentBean)ml.get(i);
	if(content==null)continue;
	try{
%>
<%=(i + 1 )%>.<%=action.getMessageDisplay(content, request, response)%><br/>
<%
	}catch(Exception e){
	}
}%>
<%=paging.shuzifenye(prefixUrl, true, "|", response)%>
<%}%>
==<%
//zhul_获取5个聊客_2006-09-16 start
if(loginUser==null||loginUser.getGender()==0){%><a href="onlineListMan.jsp?roomId=<%=roomId%>">聊天室聊客</a><%}else{%><a href="onlineListWoman.jsp?roomId=<%=roomId%>">聊天室聊客</a><%}%>==<br/>
<%if(loginUser!=null){%>
<a href="/user/userInfo.jsp">修改我的资料</a><br/>
<%}
for(int i=0;i<roomList.length;i++)
{
	if(i>1) break;
	JCRoomBean jcRoom=RoomUtil.getRoomById(roomList[i]);
%><a href="hall.jsp?roomId=<%=jcRoom.getId()%>"><%=StringUtil.toWml(jcRoom.getName())%></a>&nbsp;<%
}
%><a href="roomList.jsp">更多</a><br/><%
//zhul 2006-06-28 start 如果是聊天大厅，下面有特别显示
if(loginUser!=null){
boolean isManager=action.isManager(roomId,loginUser.getId());
boolean isSuperManager=action.isSuperManager(roomId,loginUser.getId());
if(isManager){%>
<a href="inviteFriends.jsp?roomId=<%=roomId%>">邀请</a>
<a href="kick.jsp?roomId=<%=roomId%>">踢人</a>
<a href="manager.jsp?roomId=<%=roomId%>">审批</a><br/>
<%}if(isSuperManager){%>
<a href="roomManager.jsp?roomId=<%=roomId%>">加管理员</a><br/>
<%}}%>
<%--=BaseAction.getAdver(26,response)--%>
&gt;&gt;<a href="/jcforum/index.jsp">论坛</a>.<%if(loginUser!=null){%><a href="/user/messageIndex.jsp">信箱</a>.<%}%><a href="/user/onlineManager.jsp?forumId=355">管理员</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
<%}%>
</wml>