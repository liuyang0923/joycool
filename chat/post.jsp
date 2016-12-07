<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%!
static IUserService userService = ServiceFactory.createUserService();%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
UserBean loginUser = action.getLoginUser();
int toUserId=action.getParameterInt("toUserId");
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
UserSettingBean set = loginUser.getUserSetting();
String un = String.valueOf(loginUser.getId());
int roomId=action.getParameterInt("roomId");
//禁言
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("chat",loginUser.getId());
if(forbid!=null&&!action.isMethodGet()){
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发言">
<p align="left">
已经被禁止发言<br/>
<%=forbid.getBak()%><br/>
<a href="hall.jsp">返回聊天室</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>
<%
return;
}
//macq_2006-12-15_判断用户是否被别人使用臭鸡蛋,不能发言_start
if(UserBagAction.stopSpeakMap.containsKey(new Integer(loginUser.getId()))){
	out.clearBuffer();
	response.sendRedirect("hall.jsp?roomId="+roomId);
	return;
}
//macq_2006-12-15_判断用户是否被别人使用臭鸡蛋,不能发言_end
action.post(request);
String enterMode = (String)request.getAttribute("enterMode");
String result = (String)request.getAttribute("result");
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
String chatRoomId = (String)session.getAttribute("currentroomid");
if(chatRoomId==null || chatRoomId.equals("")){chatRoomId = "0";}
if(returnUrl==null){
	returnUrl = "/chat/hall.jsp";
}
if(request.getParameter("actionId") != null) {
	int userId = action.getParameterInt("toUserId");
	if(userId > 0) {
		returnUrl = "/chat/hall.jsp?toUserId="+userId;
	}else{
		returnUrl = "/chat/hall.jsp";
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(!"get".equals(enterMode) && "success".equals(result)){
%>
<card title="发言" ontimer="<%=response.encodeURL(returnUrl.replace("&", "&amp;"))%>">
<%if(request.getParameter("actionId") == null){%>
<onevent type="onenterforward">
<refresh>
<setvar name="chat" value=""/>
</refresh>
</onevent>
<%}%>
<timer value="10"/>
<p align="left">
<%	
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(toUserId));
%>
发言成功！<br/>
<%
String isPrivate=request.getParameter("isPrivate");
String privateNotice=(String)request.getAttribute("privateNotice"); if(isPrivate==null&&privateNotice!=null){%><%=privateNotice%><br/><%}
if(onlineUser!=null){%>
<% String positionName = PositionUtil.getPositionName(onlineUser.getId() + "", onlineUser.getPositionId()); %>
<%if(onlineUser.noticeMark()){%>对方设置了免打扰(<%= positionName %>),没有注意到你的发言<br/><%}else{%>
对方在<%= positionName %>,<%= ("发呆".equals(positionName))?"可能已经离开了,因此无法回复":"请耐心等待回复" %>(1秒跳转)<br/>
<%}%>
<%}%>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/>   
</p>
</card>
<%}else if (!"get".equals(enterMode) && "failure".equals(result)){
%>
<card title="发言" ontimer="<%=response.encodeURL(returnUrl.replace("&", "&amp;"))%>">
<timer value="10"/>
<p align="left">
<%	
String tip = (String)request.getAttribute("tip");
%>
<%=tip%><br/>
<anchor title="back"><prev/>返回发言</anchor><br/>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/>   
</p>
</card>
<%}else {%>
<card title="发言">
<p align="left">
<%
String target = (String)request.getAttribute("target");
	//有接收者
	if("notNull".equals(target)){
	    UserBean toUser = (UserBean) request.getAttribute("toUser");
		if(toUser == null){
%>
对方已下线。<br/>
<%
		}
        else {
        	//liuyi 2006-09-07 start 修改用户在线状态判断
        	String key = String.valueOf(toUser.getId());
            UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(key);
        	//liuyi 2006-09-07 end

UserFriendBean friendBean = userService.getUserFriend(loginUser.getId(), toUser.getId());
UserFriendBean friend2Bean = userService.getUserFriend(toUser.getId(),loginUser.getId());
UserStatusBean userStatus=UserInfoUtil.getUserStatus(loginUser.getId());
UserStatusBean touserStatus=UserInfoUtil.getUserStatus(toUser.getId());
boolean male = (loginUser.getGender()==1);
boolean male2 = (toUser.getGender()==1);
UserNoteBean note = UserInfoUtil.getUserNote(loginUser.getId(), toUser.getId());
ForbidUtil.AdminUser au = ForbidUtil.getAdminUser(toUser.getId());
if(au!=null)
	if(au.isFlag(0))
		out.write("未知");
	else if(au.isFlag(1))
		out.write("管理");
	else
		out.write("监察");
else {
if(friendBean==null){
	if(userStatus.getTong()!=0 && userStatus.getTong()==touserStatus.getTong()) {
		if(userStatus.getTong()<20000)
			out.write("同帮");
		else
			out.write("家族");
	} else
		out.write(String.valueOf(toUser.getId()));
}else if(friendBean.getMark()==0)
	out.write("好友");
else if(friendBean.getMark()==1){
	if(male2)
		out.write("兄弟");
	else if(!male2)
		out.write("姐妹");
}else if(friendBean.getMark()==2){
	if(male)
		out.write("老婆");
	else
		out.write("老公");
}else
	out.write("对方");
}
%>:<%=(set!=null&&set.isFlagHideHat())?toUser.getUserHatText():toUser.getUserHat()%><%=((toUser.getNickName() == null || toUser.getNickName().equals("v") || toUser.getNickName().replace(" ", "").equals("")) ? "乐客"+toUser.getId() : StringUtil.toWml(toUser.getNickName()))%>(<%=LoadResource.getPostionNameByUserId2(toUser.getId())%>)<br/><%
  if(note!=null&&note.getShortNote().length()>0){%><%=StringUtil.toWml(note.getShortNote())%><br/><%}
 // 显示用户信息图片，录入新人卡、旗帜卡等等
String extraInfo = (set!=null&&set.isFlagHideStar()) ? toUser.getExtraInfoText(5) : toUser.getExtraInfo(5);
if(extraInfo!=null){%><%=extraInfo%><br/><%}%>
<%if(forbid!=null){%>(已被禁止发言)<a href="/admin/query.jsp">查询</a><br/><%}else{%>
<input type="text" name="chat" maxlength="100" value=""/><br/>
<anchor title="post"><% if(onlineUser!=null){%>发言<%}else{%>留言<%}%>
<go href="post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUserId%>" method="post">
  <postfield name="content" value="$chat"/>    
</go>
</anchor>|<anchor title="post">只让<%=toUser.getGenderText()%>看见
<go href="post.jsp?isPrivate=1&amp;roomId=<%=roomId%>&amp;toUserId=<%=toUserId%>" method="post">
  <postfield name="content" value="$chat"/>
</go>
</anchor><br/><%}%>
<% if(toUser.getId()!=100){
boolean isABadGuys=userService.isUserBadGuy(loginUser.getId(), toUser.getId());
//boolean isBBadGuys=userService.isUserBadGuy(toUser.getId(), loginUser.getId());
if(!isABadGuys){%>
<a href="/user/sendAction.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUser.getId()%>">发动作</a>|
<%}%><a href="/user/viewNote.jsp?tid=<%=toUser.getId()%>">备注</a> <%= PositionUtil.getLastModuleUrl(request, response)%><br/><%--
<a href="action.jsp?toUserId=<%=toUser.getId()%>">动作卡</a><br/>--%>
<%if(onlineUser!=null){%>
<a href="/user/operUser.jsp?action=inviteGame&amp;toUserId=<%=toUser.getId()%>">邀请<%=toUser.getGenderText()%>玩游戏</a><br/>
<%}%>
<%
action.viewMessage(request,response,"post.jsp");
Vector vecMessages=(Vector)request.getAttribute("messages");
String pagination=(String)request.getAttribute("pagination");
JCRoomContentBean content=null;
for(int i = 0; i < vecMessages.size(); i ++){
	content = (JCRoomContentBean)vecMessages.get(i);
%>
<%=(i + 1)%>.<%=action.getPrivateMessageDisplay(content, request, response)%><br/>
<%}

ArrayList userFriends=UserInfoUtil.getUserFriends(loginUser.getId());
boolean isFriend=userFriends.contains(toUser.getId()+"");
%>
<%=pagination%>
<a href="/user/sendMessage.jsp?toUserId=<%=toUser.getId()%>">写信</a>.<%if(isFriend){%><a href="postPic.jsp?rid=<%=roomId%>&amp;tuid=<%=toUser.getId()%>">贴图</a><%}else{%>贴图<%}%>.<a href="/user/userBagPost.jsp?roomId=<%=roomId%>&amp;userId=<%=toUser.getId()%>">使用道具</a><br/>
<a href="/user/operUser.jsp?action=sendMoney&amp;toUserId=<%=toUser.getId()%>">送乐币</a>.<a href="/user/tradeItem.jsp?userId=<%=toUser.getId()%>">赠送/交易</a>.<a href="/user/tradeItem.jsp?noitem=1&amp;userId=<%=toUser.getId()%>">转帐</a><br/>
<a href="/user/ViewUserInfo.do?roomId=<%=roomId%>&amp;userId=<%=toUser.getId()%>">用户信息</a><%
if(toUser.isFlagFriend()){
%>.<a href="/friend/friendInfo.jsp?userId=<%=toUser.getId()%>">交友资料</a><%}%><br/>
<%if(toUser.getHome()==1){%>
<a href="/home/home.jsp?userId=<%=toUser.getId()%>">进入<%=toUser.getGenderText()%>的家园</a><br/>
<%}%>
<a href="hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/>
————————<br/>
<%
//boolean isFriend=userService.isUserFriend(loginUser.getId(), toUser.getId());
//zhul 2006-10-17 优化好友判断
//liuyi 2006-10-30 交友系统 start
%>
<% if(friendBean==null || friendBean.getMark()==0){
if(isFriend){%>
<a href="/user/dfriend.jsp?id=<%=toUser.getId()%>">删除好友</a>
<%}else{%>
<%if(!isABadGuys){%><a href="/user/OperFriend.do?add=1&amp;friendId=<%=toUser.getId()%>">加为好友</a>.<%}%><%}%>
<%if (!isABadGuys) {%>
<%if(!isFriend){%><a href="/user/confirmBadGuy.jsp?add=1&amp;badGuyId=<%=toUser.getId()%>">加黑名单</a><%}%>
<%}else{%>
<a href="/user/OperBadGuy.do?delete=1&amp;badGuyId=<%=toUser.getId()%>">出黑名单</a>
<%}%><br/><%}%>
<%
if(friendBean!=null && friend2Bean!=null){
	//&& friendBean.getLevelValue()>=500&& friend2Bean.getLevelValue()>=500 大于500才可以
	if(userStatus.getMark()==0 && touserStatus.getMark()!=2 && loginUser.getGender()!=toUser.getGender()&& friendBean.getLevelValue()>=500&& friend2Bean.getLevelValue()>=500 ){
%>
<a href="/friend/proposal.jsp?toId=<%=toUser.getId()%>">向<%=toUser.getGenderText()%>求婚</a><br/>
<%   }
	if(friendBean.getMark()==0 && friend2Bean.getMark()==0 && friendBean.getLevelValue()>=Constants.MIN_FRIEND_LEVEL_FOR_JY && friend2Bean.getLevelValue()>=Constants.MIN_FRIEND_LEVEL_FOR_JY){
%>
<a href="/friend/jyProposal.jsp?toId=<%=toUser.getId()%>">与<%=toUser.getGenderText()%>义结金兰</a><br/> 
<% }else if(friendBean.getMark()==1){
%>
<a href="/friend/deleteJy.jsp?toId=<%=toUser.getId()%>">与<%=toUser.getGenderText()%>割袍断义</a><br/> 
<%		
	}
	else if(friendBean.getMark()==2){
%>
<a href="/friend/divorce.jsp?userId=<%=toUser.getId()%>">与<%=toUser.getGenderText()%>离婚</a><br/>
<%		
}} 
//liuyi 2006-10-30 交友系统 end
%> <%}%>
<%}}
//没有接收者
else {
%><a href="onlineList.jsp?roomId=<%=roomId%>">选择某个用户</a><br/><%--
接收者：所有人<br/>
<a href="onlineList.jsp?roomId=<%=roomId%>">选择某个用户</a><br/>
发言内容：
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<input type="text" name="chat" maxlength="100" value=""/><br/>
<anchor title="post">发表<go href="post.jsp?roomId=<%=roomId%>" method="post">
  <postfield name="content" value="$chat"/>
</go></anchor>--%><%--|<a href="action.jsp?roomId=<%=roomId%>">动作卡</a>--%><br/>
<a href="hall.jsp">返回聊天室</a><br/>
<%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
<%}%>
</wml>