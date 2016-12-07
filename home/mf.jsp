<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.action.home.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	
	HomeAction homeAction = new HomeAction(request);
	IFriendAdverService faService = ServiceFactory.createFriendAdverService();
	int userId = homeAction.getParameterInt("uid");
	String tip = "";
	if (userId > 0){
		UserBean user = UserInfoUtil.getUser(userId);
		if (user == null){
			tip = "用户不存在.";
		} else {
			FriendAdverBean friendAdver = faService.getFriendAdver(" user_id=" + userId);
			if (friendAdver == null){
				tip = "用户没有发布交友信息,无法与TA交友.";
			} else {
				response.sendRedirect("../friendadver/friendAdverMessage.jsp?id=" + friendAdver.getId());
				return;
			}
		}
	} else {
		tip = "用户不存在.";
	}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家园"><p>
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/><a href="home.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>