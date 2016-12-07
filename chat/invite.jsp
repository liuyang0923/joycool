<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.*,net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%
JCRoomChatAction action = new JCRoomChatAction(request);

net.joycool.wap.bean.UserBean loginUser = action.getLoginUser();
int roomId = 0;
if (request.getParameter("roomId") != null)
	roomId = net.joycool.wap.util.StringUtil.toId(request.getParameter("roomId"));
if (loginUser==null||!action.isManager(roomId, loginUser.getId())) {
	BaseAction.sendRedirect("/chat/hall.jsp", response);
	return;
}

action.invite(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:notPresent name="tip">
<card title="邀请成功">
<%=BaseAction.getTop(request, response)%>
<p align="left">
邀请成功<br/>
---------<br/>
此用户已经被你邀请到聊天室！<br/>
并扣除你10000乐币.<br/>
<a href="inviteFriends.jsp?roomId=<%=roomId%>">继续邀请</a><br/>
<a href="hall.jsp?roomId=<%=roomId%>">返回聊天室</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>

<logic:present name="tip">
<card title="邀请失败">
<%=BaseAction.getTop(request, response)%>
<p align="left">
邀请失败<br/>
---------<br/>
<bean:write name="tip"/><br/>
<a href="hall.jsp?roomId=<%=roomId%>">返回聊天室</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:present>
</wml>