<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="邀请朋友"><p><%=BaseAction.getTop(request, response)%>
【邀请他，再把他卖掉】<br/>
每邀请到一个朋友，就会获得300游戏币（专用于买卖好友游戏中），这是你积累财富的最快方法。同时这个好友还要无条件给你当一天临时奴隶哦，嘿嘿嘿<br/>
【如何邀请】<br/>
1.记住属于你个人的网址: <%=loginUser.getId()%>m.joycool.net<br/>
2.把这个网址告诉你的朋友，并提示让他按照这个地址访问<br/>
3.当他访问了你的网址并注册成功后，邀请成功！你会得到300游戏币，而且成功让他成为你的临时奴隶！<a href="h.jsp">细节参见游戏规则</a><br/>
提示：可以通过手机的通讯录对好友进行群发提高效率。发给他们的短信中最好有说服他们访问的理由，如“来看看我的最新照片”之类的话<br/>
<a href="myInfo.jsp">返回朋友买卖首页</a><br/>
<%=BaseAction.getBottom(request, response)%></p></card></wml>