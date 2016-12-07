<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction,net.joycool.wap.bean.UserStatusBean"%>
<%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request,response);
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
String tip = "";
int type = action.getParameterInt("t");	// 0:填写邀请 1:失败 2:成功 3:成功提示
int invite = action.getParameterInt("i");
int uid = action.getParameterInt("uid");
UserBean user = null;
//先看用户登陆是否超过了1小时(3600000)。如果超过，则不可再确定邀请关系。
if (us.getOnlineTime() > 1){
	tip = "您已登陆超过1小时,不可再确认邀请关系.";
} else if (action.getMyInvite() != null){
	tip  = "您已经填写过邀请信息了.";
} else {
	user = UserInfoUtil.getUser(uid);
	if (type == 1 && user == null){
		type = 2;
	} else {
		if (invite == 1){
			if (uid <= 0){
				type = 2;
			} else {
				type = action.confirm(uid);
			}
		}
	}
	if (type < 0 || type > 3){type = 0;}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
switch (type){
case 0:{
// 填写邀请信息
%>请输入邀请您来乐酷的朋友的乐酷ID:<br/><%
break;
}
case 1:{
// 确认
%>您确认是ID:<%=user.getId()%>,<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>邀请您来的吗?<br/>
<a href="confirm.jsp?uid=<%=user.getId()%>&amp;i=1">确认</a>|<a href="confirm.jsp">返回上一页</a><br/>
一旦确认,不得更改哦.<br/>
<%
break;
}
case 2:{
// 失败
%>失败!没有这个用户!请输入邀请您来乐酷的朋友的乐酷ID:<br/><%
break;
}
case 3:{
// 成功
%>确认邀请者成功!您是<%=user.getId()%>邀请来乐酷的!<br/><%
break;
}
}
if (type == 0 || type == 2){
%><input name="uid" format="*N"/><br/>
<anchor>
	确认
	<go href="confirm.jsp?t=1" method="post">
		<postfield name="uid" value="$uid" />
	</go>
</anchor><br/>
一旦确认,不得更改哦.<br/>
<a href="/lswjs/index.jsp">没有邀请人,我自己来的!</a><br/><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回邀请好友页</a><br/><a href="/bottom.jsp">返回ME</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>