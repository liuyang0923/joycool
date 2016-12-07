<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction,net.joycool.wap.bean.chat.RoomInviteBean,net.joycool.wap.bean.UserStatusBean,net.joycool.wap.bean.PagingBean"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request,response);
String tip = "";
int total = 0;
UserBean user = null;
RoomInviteBean roomInvite = null;
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	out.clearBuffer();
	response.sendRedirect("/user/login.jsp");
	return;
}
Vector list = action.getInviteList(" user_id=" + loginUser.getId() + " and send_datetime>=" + DateUtil.getFirstDayOfWeek() + " order by id desc");
if (list != null && list.size() > 0){
	total = list.size();
}
PagingBean paging = new PagingBean(action,total, COUNT_PRE_PAGE, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>您本周邀请了<%=action.inviteInThisWeek()%>位好友.加油就有皇冠戴哦.<br/>
您总共邀请了:<%=total%>位好友,感谢您对我们的大力支持呀^_^<br/>
以下是您邀请过的朋友:<br/>
<%
if (list != null && list.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		roomInvite = (RoomInviteBean)list.get(i);
		if (roomInvite != null){
			user = UserInfoUtil.getUser(roomInvite.getInviteeId());
			if (user != null){
				%><%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><br/><%	
			}
		}
	}
	%><%=paging.shuzifenye("my.jsp",false,"|",response)%><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="invite.jsp">立即邀请好友</a><br/>
<a href="index.jsp">返回邀请好友页</a><br/>
<a href="/bottom.jsp">返回ME</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>