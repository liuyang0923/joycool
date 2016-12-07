<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
int pos = StringUtil.toInt(request.getParameter("p"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(pos >= 0){
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(loginUser.getId()));
ModuleBean pb = PositionUtil.getModule(pos);
if(pb==null) {
	pos = 0;
	pb = PositionUtil.getModule(pos);
}
if(onlineUser!=null)
	onlineUser.setPositionId(pos);	
%>
<card title="脱机状态(<%=pb.getPositionName()%>)" ontimer="<%=response.encodeURL("idle.jsp?p="+pos)%>">
<timer value="300"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
在本页面停留的时候,其他玩家看到你的状态为(<%=pb.getPositionName()%>)<br/>
<a href="idle.jsp">重新设置</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
<%}else{%>

<card title="脱机状态设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请设置你的脱机状态:<br/>
<a href="idle.jsp?p=5">闲逛</a>| 
<a href="idle.jsp?p=11">聊天</a>|
<a href="idle.jsp?p=40">论坛</a>|
<a href="idle.jsp?p=44">帮会</a><br/>

<a href="idle.jsp?p=103">吃饭</a>|
<a href="idle.jsp?p=107">洗澡</a>|
<a href="idle.jsp?p=108">电视</a>|
<a href="idle.jsp?p=102">睡觉</a><br/>

<a href="idle.jsp?p=104">工作</a>|
<a href="idle.jsp?p=105">学习</a>| 
<a href="idle.jsp?p=109">游戏</a>|
<a href="idle.jsp?p=110">约会</a><br/>

<a href="idle.jsp?p=106">忙碌</a>|
<a href="idle.jsp?p=100">离开</a>| 
<a href="idle.jsp?p=101">发呆</a>|
<a href="idle.jsp?p=111">外出</a><br/>
<br/>
<a href="userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>