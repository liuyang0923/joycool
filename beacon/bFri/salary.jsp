<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	ActionBuyFriend action = new ActionBuyFriend(request);
	action.salary();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="朋友买卖"><p><%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
===补助金说明==<br/>
如果现金不足,也不用担心会失去参与游戏的机会,我们会每天发放一笔补助金,这会使你的游戏更简单.<br/>
补助金每天只能领取一次,但不会累计,如果当天不来领取,将失去当天的补助金.<br/>
<a href="myInfo.jsp">返回朋友买卖首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>

