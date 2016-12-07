<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.roomList(request);
int roomId=action.getParameterInt("roomId");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Vector top = (Vector)request.getAttribute("top");
Vector room1 = (Vector)request.getAttribute("room1");
//if(room1!=null){
//得到不需要认证房间的分页参数
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
String backTo1 = (String) request.getAttribute("backTo1");
//}
Vector room2 = (Vector)request.getAttribute("room2");
//if(room2!=null){
//得到需要认证房间的分页参数
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
//所有房间展示，后台配置房间显示顺序
Vector room3 = (Vector)request.getAttribute("room3");
int totalPageCount3 = ((Integer) request.getAttribute("totalPageCount3")).intValue();
int pageIndex3 = ((Integer) request.getAttribute("pageIndex3")).intValue();
String prefixUrl3 = (String) request.getAttribute("prefixUrl3");
String backTo3 = (String) request.getAttribute("backTo3");

//在线人数
int onlineCount = JoycoolSpecialUtil.getRealOnlineUserCount(request);
JCRoomBean room=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="聊天大广场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/chatCenter.gif" alt="loading"/><br/>
聊天总人数：<%=onlineCount%>人<br/>
<%--＝推荐房屋＝<br/>
<%
for(int i = 0; i < top.size(); i ++){
	room = (JCRoomBean)top.get(i);
%>
<%=i+1%><a href="hall.jsp?roomId=<%=room.getId()%>"><%=StringUtil.toWml(room.getName())%></a>(<%=RoomUtil.getDisplayNum(String.valueOf(room.getId()))%>/<%=room.getMaxOnlineCount()%>)<br/>
<%}%> 
<%if(room1.size()>0){%>
＝人气聊天室＝<br/>
<%
for(int i = 0; i < room1.size(); i ++){
	room = (JCRoomBean)room1.get(i);
%>
<%=i+1%><a href="hall.jsp?roomId=<%=room.getId()%>"><%=room.getName()%></a>(<%=RoomUtil.getDisplayNum(String.valueOf(room.getId()))%>/<%=room.getMaxOnlineCount()%>)<br/>
<%}
String fenye1 = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, false, "|", response);
if(!"".equals(fenye1)){ 
%>
<%=fenye1%><br/>
<%}}
if(room2.size()>0){%>
＝需要主人认证的＝<br/>
<%
for(int i = 0; i < room2.size(); i ++){
	room = (JCRoomBean)room2.get(i);
%>
<%=i+1%><a href="hall.jsp?roomId=<%=room.getId()%>"><%=StringUtil.toWml(room.getName())%></a>(<%=RoomUtil.getDisplayNum(String.valueOf(room.getId()))%>人在线)<br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%}}%> --%>
<%
//int room1Count=0;
//int room1MaxCount=0;
if(room3.size()>0){%>
<%--==所有聊天室==<br/> --%>
<%int j = 1;
for(int i = 0; i < room3.size(); i ++){
	room = (JCRoomBean)room3.get(i);
	if(room.getId()==1){
	continue;
	//room1Count=RoomUtil.getDisplayNum(room.getId());
	//room1MaxCount=room.getMaxOnlineCount();
	}
%>
<%=j%>.
<a href="hall.jsp?roomId=<%=room.getId()%>"><%=StringUtil.toWml(room.getName())%></a>(<%=RoomUtil.getDisplayNum(room.getId())%>/<%=room.getMaxOnlineCount()%>):<br/>
<%--<%=StringUtil.toWml(room.getDescription())%>
<%if(loginUser.getGender()==0){%><a href="onlineListMan.jsp?roomId=<%=room.getId()%>">本室聊客</a>
<%}else{%><a href="onlineListWoman.jsp?roomId=<%=room.getId()%>">本室聊客</a><%}%>
<a href="hall.jsp?roomId=<%=room.getId()%>"><%=StringUtil.toWml("进入>>")%></a><br/>--%>
<%j++;}
String fenye3 = action.shuzifenyeNew(totalPageCount3,pageIndex3, prefixUrl3, false, "|", response); 
if(!"".equals(fenye3)){
%>
<%=fenye3%><br/>
<%}}%>
<br/>
<%--搜索聊天室：<br/>
聊天室名称：
<input name="roomName" maxlength="20" value="v"/>
      <anchor title ="search roomName">GO
      <go href="search.jsp" method="post">
          <postfield name="roomName" value="$(roomName)"/>
      </go>
</anchor><br/>
创建人昵称：
<input name="nickName" maxlength="20" value="v"/>
     <anchor title ="search nickName">GO
     <go href="search.jsp" method="post">
          <postfield name="nickName" value="$(nickName)"/>
     </go>
</anchor><br/><br/>
<a href="<%=("roomApply.jsp")%>">申请建立聊天室</a><br/>       --%>
<%--
<a href="addHall.do" >我也建个房间！</a><br/>
<a href="hall.jsp?roomId=<%=roomId%>">返回聊天大厅</a><br/>
<a href="/lswjs/index.jsp">返回钓鱼岛赌场</a><br/>
--%>
<%--聊天拘留所：<a href="hall.jsp?roomId=1">小黑屋</a>(<%=room1Count%>/<%=room1MaxCount%>)<br/>--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>