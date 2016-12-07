<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction,net.joycool.wap.bean.chat.RoomInviteBean,net.joycool.wap.bean.UserStatusBean"%>
<%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request,response);
String tip = "";
UserBean inviteUser = null;
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	out.clearBuffer();
	response.sendRedirect("/user/login.jsp");
	return;
}
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
if (us == null){
	out.clearBuffer();
	response.sendRedirect("/user/login.jsp");
	return;
}
RoomInviteBean roomInvite = action.getMyInvite();
if (roomInvite != null){
	inviteUser = UserInfoUtil.getUser(roomInvite.getUserId());
}
int type = action.getParameterInt("t");
if (type < 0 || type > 1){type=0;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>欢迎介绍更多朋友来乐酷.朋友越多越好玩!还有大礼送!<br/>
<a href="invite.jsp">立即邀请</a><br/>
<a href="help.jsp">奖励规则</a><br/>
您总共邀请<%=action.totalInvite()%>位朋友,其中本周<%=action.inviteInThisWeek()%>位.<br/>
<a href="my.jsp">查看我所有邀请来的好友</a><br/>
<%if (type == 0){
if (roomInvite != null){
%>您是被<%=inviteUser.getNickNameWml()%>邀请来的.<br/><%
} else {
	if (us.getOnlineTime() <= 1){
		%>您还未填写您的邀请者呢!请点击<a href="confirm.jsp">填写</a>.<br/><%
	}
}
}%>
<a href="/bottom.jsp">返回上一页</a><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>