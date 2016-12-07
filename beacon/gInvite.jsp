<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.spec.buyfriends.*,net.joycool.wap.spec.*"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
	response.setHeader("Cache-Control","no-cache");
	CustomAction customAction = new CustomAction(request, response);
	
	int sendUid = customAction.getParameterInt("uid");
	
	if(sendUid == 0) {
		response.sendRedirect("/invite.jsp");
		return;
	}
	
	UserBean sendUser = UserInfoUtil.getUser(sendUid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
	  UserBean user = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
  	  if(user != null) {  	  
%><card title="邀请"><p>
<img src="/img/invite.gif"/><br/>
<%=user.getNickNameWml()%>:你的好友[<a href="/home/home2.jsp?uid=<%=(sendUid)%>"><%=sendUser.getNickNameWml()%></a>]在此恭候您的到来。你成功的被邀请到<%=sendUser.getGenderText()%>的朋友买卖游戏中，你现在已经是<%=sendUser.getGenderText()%>的临时奴隶了。你将为<%=sendUser.getGenderText()%>打苦工1整天，他不只可以获得300元的好处费，还可以从你身上赚取相应的报酬。<br/>
首先要记好您的ID和密码：<br/>
ID：<%=user.getId()%><br/>
密码：<%=net.joycool.wap.util.Encoder.decrypt(user.getPassword()) %><br/>
<%--a href="/beacon/sms.jsp">>>获取短信保存ID和密码</a><br/--%>
---------------<br/>
想不想摆脱他的魔掌，也把<%=sendUser.getNickNameWml()%>买做自己的奴隶呢？那就赶快加入游戏吧<br/>
<a href="/beacon/bFri/myInfo.jsp">&gt;&gt;去朋友买卖</a><br/>
<a href="/home/inputRegisterInfo.jsp">创建我的家园</a><br/>
<a href="/Column.do?columnId=9438">新手帮助</a>|<a href="/">了解乐酷</a><br/>
<a href="/team/chat.jsp?ti=106">乐酷新人讲堂</a>
<br/><%=BaseAction.getBottom(request, response)%></p></card>
<%} else { %>
<card title="邀请" ontimer="<%=response.encodeURL("/invite.jsp")%>">
<timer value="50"/><p><%if(request.getAttribute("tip") != null) {%><%=request.getAttribute("tip") %><br/><%}%>5秒后自动返回<br/>
<a href="/invite.jsp">返回重填</a>
<br/><%=BaseAction.getBottom(request, response)%></p></card>
<%} %>
</wml>