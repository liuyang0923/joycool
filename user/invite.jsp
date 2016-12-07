<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.spec.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request, response);
	
	boolean flag = true;
	int sendUid = customAction.getParameterInt("uid");
	
	if(sendUid == 0) {
		response.sendRedirect("/invite.jsp");
		return;
	}
	
	UserBean sendUser = UserInfoUtil.getUser(sendUid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(flag) { 
  	  UserBean user = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
%><card title="注册成功"><p>
<%=user.getNickNameWml()%>:你的好友【<%=sendUser.getNickNameWml()%>】在此恭候您的到来。你的加盟让他获得1点积分。而你在乐酷也将享受到网游的虚真，豪赌的刺激，情感论坛中别人的喜怒哀乐，与百万人在线交友的兴奋。赶紧开始你的乐酷之旅<br/>
---------------<br/>
首先要记好您的ID和密码：<br/>
ID：<%=user.getId() %><br/>
密码：<%=net.joycool.wap.util.Encoder.decrypt(user.getPassword()) %><br/>
<a href="/beacon/sms.jsp">>>获取短信保存ID和密码</a><br/>
---------------<br/>
想不想看看<%=sendUser.getNickNameWml()%>的日记和照片，偷窥一下他正在做什么？<br/>
<a href="/home/home2.jsp?uid=<%=(sendUid)%>">去<%=sendUser.getNickNameWml()%>的家园看看</a>
<br/><%=BaseAction.getBottom(request, response)%></p></card>
<%} else { %>
<card title="邀请" ontimer="<%=response.encodeURL("/invite.jsp")%>">
<timer value="50"/><p><%=request.getAttribute("tip") %><br/>5秒后自动返回<br/>
<a href="<%="http://"+sendUid+".joycool.net/invite.jsp"%>">返回重填</a>
<br/><%=BaseAction.getBottom(request, response)%></p></card><%} %></wml>