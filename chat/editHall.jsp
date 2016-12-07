<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.JCRoomBean"%><%@ page import="net.joycool.wap.util.Constants"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%

String editType = (String)request.getAttribute("editType");
if (editType == null){
	// 从jsp页面进时(修改成功页进入其它页后的返回)
	 editType = request.getParameter("editType");
}
if(editType == null){
	editType = "1";
}

int roomId=action.getParameterInt("roomId");
JCRoomBean room = (JCRoomBean)session.getAttribute("Room");
if (!"5".equals(editType)){
	// editType为5是保存成功页，不需要检查上面两个参数
	if(roomId==null){
		BaseAction.sendRedirect("/failure.jsp", response);
		return;
	}
	if (room == null){
		BaseAction.sendRedirect("/failure.jsp", response);
		return;
	}
}

// 当前页url
String currentUrl = request.getRequestURL().toString();
if (request.getAttribute("update") == null){
	currentUrl = URLEncoder.encode(currentUrl+"?editType="+editType+"&amp;roomId="+roomId,"UTF-8");
}else{
	currentUrl = URLEncoder.encode(currentUrl+"?editType="+editType+"&amp;roomId="+roomId+"&amp;update=1","UTF-8");
}

if ("5".equals(editType)){
	// 修改成功页，从其它页返回该页是不必调用action
	currentUrl = "editHall.jsp?editType=5";
}

// 后台参数验证提示信息
String message = "";
if (request.getAttribute("message") != null){
	message = "<font color='red'>"+(String)request.getAttribute("message")+"</font>";
}

if (("1").equals(editType)){
%>
<card title="修改聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
您当前聊天室名称是[<b><%=StringUtil.toWml(room.getName())%></b>]<br/>
请修改聊天室名称：(不超过10个字)<br/>
<input type="text" name="name"  maxlength="10" value="<%=StringUtil.toWml(room.getName())%>"/><br/>
    <anchor title="确定提交">确定
    <go href="EditHall.do?editType=1&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="name" value="$name"/>
    </go>
    </anchor>
<br/><br/>
    <a href="editHallLink.jsp?roomId=<%=room.getId()%>" title="返回上一级">返回上一级</a><br/>
    <a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 

<br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("2").equals(editType)){
%>
<card title="修改聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%
if (!"".equals(message)){
	if (message.indexOf("乐币不够") != -1){
	%>
	<%=message%> <a href="payment.jsp?roomId=<%=roomId%>&amp;backTo=<%=currentUrl%>" title="我要冲值">我要冲值</a><br/>
	<%
	}else{
	%>
	<%=message%><br/>
	<%
	}
}

String payWayName = "";
if (room.getPayWay() == 0){
	payWayName = "购买";
}else if (room.getPayWay() == 1){
	payWayName = "租用";
}
%>
您当前房间付款方式是[<b><%=payWayName%></b>]<br/>
面积是[<%=room.getMaxOnlineCount()%>]平米<br/>
<%
if (room.getPayWay() == 1){
%>
租用时间是[<%=room.getPayDays()%>]<br/>
<%
}
%>
您要改变房间大小还是付款方式？<br/>
<input type="text" name="maxOnlineCount" maxlength="10" value="<%=room.getMaxOnlineCount()%>" size="14"/> 平米<br/>
(最多这么多人同时在)<br/>
付费方式：<br/>
购买(每平米10万)
    <anchor title="选择">选择
    <go href="EditHall.do?editType=2&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="maxOnlineCount" value="$maxOnlineCount"/>
    <postfield name="payDays" value=""/>
    <postfield name="payWay" value="0"/>
    </go>
    </anchor>
<br/>
租用(每天每平米1千，至少5天)
<br/>
<%
if (room.getPayWay() == 0){
%>
租用时间<input type="text" name="payDays" maxlength="8" value="" size="14"/> 天 
<%
}else{
%>
租用时间<input type="text" name="payDays" maxlength="8" value="<%=room.getPayDays()%>" size="14"/> 天
<%
}
%>
    <anchor title="选择"> 选择
    <go href="EditHall.do?editType=2&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="maxOnlineCount" value="$maxOnlineCount"/>
    <postfield name="payDays" value="$payDays"/>
    <postfield name="payWay" value="1"/>
    </go>
    </anchor>
    
<br/><br/>
<a href="editHallLink.jsp?roomId=<%=room.getId()%>" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("3").equals(editType)){
%>
<card title="修改聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
<%
if ( room.getThumbnail() != null && !"".equals(room.getThumbnail()) ){
%>
您当前房屋的图片是<img src="<%=Constants.CHAT_IMG_PATH%><%=room.getThumbnail()%>" alt="loading..."/><br/>
<%
}else {
%>
您当前房屋[<b>没有图片</b>]<br/>
<%
}
%>
要升级房屋的图片吗？ 
    <anchor title="放弃">放弃
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
	<postfield name="thumbnail" value=""/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>1.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="1.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>2.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="2.gif"/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>3.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="3.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>4.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="4.gif"/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>5.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="5.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>6.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="6.gif"/>
    </go>
    </anchor>
<br/>
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>7.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="7.gif"/>
    </go>
    </anchor>
    
    <anchor title="选择"><img src="<%=Constants.CHAT_IMG_PATH%>8.gif" alt="loading..."/>
    <go href="EditHall.do?editType=3&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="thumbnail" value="8.gif"/>
    </go>
    </anchor>
<br/>
<a href="editHallLink.jsp?roomId=<%=room.getId()%>" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("4").equals(editType)){

String grantModeName = "";
if (room.getGrantMode() == 0){
	grantModeName = "自由进入";
}else if (room.getGrantMode() == 1){
	grantModeName = "需管理员批准";
}
%>
<card title="修改聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
您当前房间授权情况是[<b><%=grantModeName%></b>]<br/>
您的房间允许其他用户自由进入，还是需要您的批准？<br/>
    <anchor title="自由进入">1、自由进入
    <go href="EditHall.do?editType=4&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="grantMode" value="0"/>
    </go>
    </anchor>
<br/>
    <anchor title="需管理员批准">2、需管理员批准
    <go href="EditHall.do?editType=4&amp;roomId=<%=room.getId()%>&amp;update=1&amp;backTo=<%=currentUrl%>" method="post">
    <postfield name="grantMode" value="1"/>
    </go>
    </anchor>
    
<br/>
<a href="editHallLink.jsp?roomId=<%=room.getId()%>" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("5").equals(editType)){
	JCRoomBean newRoom = (JCRoomBean)session.getAttribute("NewRoom");
	if (newRoom == null){
		BaseAction.sendRedirect("/failure.jsp", response);
		return;
	}
%>
<card title="修改聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%=message%>
<br/>
您的房间已经修改成功！您以后可以在个人资料里面管理自己的房间。<br/>
您现在可以邀请朋友来您的聊天室啦！<br/>
房间编号：<%=newRoom.getId()%><br/>
房间名称：<%=StringUtil.toWml(newRoom.getName())%><br/>
房间大小：<%=newRoom.getMaxOnlineCount()%><br/>
<%
if ( newRoom.getThumbnail() != null && !"".equals(newRoom.getThumbnail()) ){
%>
房间图片：<img src="<%=Constants.CHAT_IMG_PATH%><%=newRoom.getThumbnail()%>" alt="loading..."/><br/>
<%
}
%>

<a href="editHallLink.jsp?roomId=<%=newRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="继续修改">继续修改</a><br/>
<a href="http://wap.joycool.net/chat/hall.jsp?roomId=<%=newRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="进入聊天室">进入聊天室</a><br/>
<a href="http://wap.joycool.net/chat/inviteFriends.jsp?roomId=<%=newRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="邀请别人来">邀请别人来</a><br/>
<a href="roomManager.jsp?roomId=<%=newRoom.getId()%>&amp;backTo=<%=currentUrl%>" title="设置管理员">设置管理员</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<br/><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>