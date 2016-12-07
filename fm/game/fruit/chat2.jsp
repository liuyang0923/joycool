<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="jc.util.SimpleChatLog2"%><%@ page import="jc.family.game.fruit.*,net.joycool.wap.framework.BaseAction"%><%!
	static int NUMBER_OF_PAGE = 10;
%><%
FruitAction action = new FruitAction(request,response);
%><%@include file="inc.jsp"%><%
SimpleChatLog2 sc = vsGame.getChat();

	String content = action.getParameterNoEnter("content");
	if(content != null && action.isCooldown("chat", 5000)) {		// 发言
		UserBean loginUser = action.getLoginUser();
		sc.add(loginUser.getId(),loginUser.getNickNameWml(),StringUtil.toWml(content));
	}

PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="发言"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(vsUser!=null){%><a href="chat.jsp">家族</a>|公共<br/><%}%>
<input name="fchat2"  maxlength="100"/>
<anchor>发言<go href="chat2.jsp" method="post"><postfield name="content" value="$fchat2"/></go></anchor>|<a href="chat2.jsp">刷新</a>|<a href="game.jsp">返回</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chat2.jsp", false, "|", response)%>
<a href="game.jsp">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>