<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.friendVote(request);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="交友中心">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/top/index.jsp">去看排行榜</a><br/>
<br/>
<a href="/chat/hall.jsp">返回聊天室</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="交友中心">
<p align="left">
<%=BaseAction.getTop(request, response)%>
投票完成,感谢您的参与!<br/>
<a href="/top/index.jsp">去看排行榜</a><br/>
<br/>
<a href="/chat/hall.jsp">返回聊天室</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>