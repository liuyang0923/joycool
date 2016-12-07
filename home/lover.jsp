<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendBean"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	if(user == null) {
		response.sendRedirect("/");
		return;
	}
	if (user.getGender() == 1){
		// 男的？转回男性家园
		response.sendRedirect("home.jsp");
		return;
	}
	int uid = user.getId();//LoginUser.getId();
	
//	HomePlayer player = homeService.getPlayer(" user_id = " + uid);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园">
<p>
<%=BaseAction.getTop(request, response)%>
★★今日情人★★<br/>
1.刘鸿飞5钻石.6金条.0珍珠.1000铜板<br/>
2.王聪5钻石.3金条.10珍珠.500铜板<br/>
3.马莉5钻石.6金条.0珍珠.1000铜板<br/>
4.邱毅5钻石.6金条.0珍珠.1000铜板<br/>
<a href="#">>>更多</a><br/>
<a href="#">返回上一级</a><br/>
---------------------------<br/>
往日情人<br/>
12月1号：刘鸿飞5钻石.6金条.0珍珠.1000铜板<br/>
11月30号：马莉5钻石.6金条.0珍珠.1000铜板<br/>
<a href="#">>>更多</a><br/>
<a href="#">返回上一级</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>