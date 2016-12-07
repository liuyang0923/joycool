<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.chat.*,net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%!
public void searchkickUser(HttpServletRequest request) {
	IChatService chatService = ServiceFactory.createChatService();
	int roomId=action.getParameterInt("roomId");
	String kickId = (String) request.getParameter("kickId");
	int userId = StringUtil.toInt(kickId);
	if (userId < 0) {
		request.setAttribute("roomId", roomId);
		request.setAttribute("result", "failure");
		request.setAttribute("tip", "请输入用户ID");
		return;
	}
	String condition = " room_id=" + roomId + " and user_id=" + kickId;
	JCRoomBean room = chatService.getJCRoom(" id=" + roomId);
	UserBean user = null;
	if (room.getGrantMode() == 1) {
		// 授权用户
		// user = userService
		// .getUser(" join jc_room_user on user_info.id=jc_room_user.user_id
		// where "
		// + condition + " and status=1");
		// zhul 2006-10-16 优化用户信息查询 start
		RoomUserBean roomUser = chatService.getJCRoomUser(condition
				+ " and status=1");
		user = UserInfoUtil.getUser(roomUser.getUserId());
		// zhul 2006-10-16 优化用户信息查询 end
	} else {// 在线用户
		// user = userService
		// .getUser(" join jc_room_online on
		// user_info.id=jc_room_online.user_id
		// where "
		// + condition);
		// zhul 2006-10-16 优化用户信息查询 start
		JCRoomOnlineBean online = chatService.getOnlineUser(condition);
		if(online!=null){
		    user = UserInfoUtil.getUser(online.getUserId());
		}    
		// zhul 2006-10-16 优化用户信息查询 end
	}
	request.setAttribute("roomId", roomId);
	request.setAttribute("user", user);
	request.setAttribute("result", "success");
}
%>
<%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
searchkickUser(request); //action.searchkickUser(request);
String result=(String) request.getAttribute("result");
String roomId=(String) request.getAttribute("roomId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if("failure".equals(result)){%>
<card title="踢人">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/chat/kick.jsp?roomId=<%=roomId%>">返回上一级</a><br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
UserBean user=(UserBean)request.getAttribute("user");
%>
<card title="踢人">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(user!=null){%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName()) %><a href="kickInfo.jsp?status=0&amp;roomId=<%=roomId%>&amp;kickIndex=0&amp;onlineIndex=0&amp;userId=<%=user.getId()%>&amp;userName=<%=StringUtil.toWml(user.getNickName())%>">踢出</a><br/>
<%}else{%>该用户不存在<%}%><br/>
<a href="/chat/kick.jsp?roomId=<%=roomId%>">返回上一级</a><br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>