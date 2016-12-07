<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.user.SendAction" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
SendAction action=new SendAction(request);
action.groupSendMsg(request);
UserBean user=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
Vector vecOnlineFriend=(Vector)request.getAttribute("onlineFriendsList");
Vector vecOfflineFriend=(Vector)request.getAttribute("offlineFriendsList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友信息群发器">
<p align="left">
<%=BaseAction.getTop(request, response)%>
好友信息群发器:<br/>
内容:<input name="content" value="今天你酷了吗." maxlength="100"/><br/>
添加好友(每位10000乐币)：<br/>
在线好友：(<%=vecOnlineFriend.size()%>)<br/>
<%if(vecOnlineFriend.size()>0){%>
<select multiple="true"  name="onlineFriends" >
<%for(int i=0;i<vecOnlineFriend.size();i++){
	user=(UserBean)vecOnlineFriend.get(i);
	if(user==null)continue;
%>
<option  value="<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></option> 
<%}%>
</select><br/>
<%}%>
离线好友：(<%=vecOfflineFriend.size()%>)<br/>
<%if(vecOfflineFriend.size()>0){%>
<select multiple="true"  name="offlineFriends" >
<%for(int i=0;i<vecOfflineFriend.size();i++){
	user=(UserBean)vecOfflineFriend.get(i);
	if(user==null)continue;
%>
<option  value="<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></option> 
<%}%>
</select><br/>
<%}%>
<anchor>提交发送
<go href="/user/groupSendMsgResult.jsp" method="post">
<postfield name="onlineFriends" value="$onlineFriends" />
<postfield name="offlineFriends" value="$offlineFriends" />
<postfield name="content" value="$content" />
</go>
</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>