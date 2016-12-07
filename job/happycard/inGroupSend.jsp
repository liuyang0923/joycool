<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
String info=null;
//判断用户是否提交
if(null!=request.getParameter("onlineFriends")){
String onlineFriends=request.getParameter("onlineFriends");
String offlineFriends=request.getParameter("offlineFriends");
if(onlineFriends.equals("") && offlineFriends.equals("")){
	info="请选择一个好友!";
}
else{
	action.sendInGroup(request);%>
	<%=JCRoomChatAction.getTransferPage(("/job/happycard/sendOk.jsp?inGroupSend=1"))%>
<%
	return;
}
}
action.inGroupSend(request,response);
HappyCardBean card=null;
HappyCardBean nextCard=null;
UserBean user=null;
card=(HappyCardBean)request.getAttribute("card");
nextCard=(HappyCardBean)request.getAttribute("nextCard");
Vector vecOnlineFriend=(Vector)request.getAttribute("onlineFriendsList");
Vector vecOfflineFriend=(Vector)request.getAttribute("offlineFriendsList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发送贺卡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(null!=card){%>
<%if(null!=info){%><%=info%><%}%><br/>
<%=card.getTitle()%>(人气<%=card.getHits()%>)<br/>
<img src="<%=card.getImageUrl()%>" alt="loading..." /><br/>
选择您要发送贺卡的人：<br/>
(一天之内给同一好友最多只能发3条贺卡)<br/>
在线好友：(<%=vecOnlineFriend.size()%>)<br/>
<%if(vecOnlineFriend.size()>0){%>
<select multiple="true"  name="onlineFriends" >
<%for(int i=0;i<vecOnlineFriend.size();i++){
	user=(UserBean)vecOnlineFriend.get(i);
	if(user==null){continue;}
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
	if(user==null){continue;}
%>
<option  value="<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></option> 
<%}%>
</select><br/>
<%}%>
祝福语：<br/>
<%=card.getContent()%><br/>
<anchor>提交发送
<go href="/job/happycard/inGroupSend.jsp" method="post">
<postfield name="onlineFriends" value="$onlineFriends" />
<postfield name="offlineFriends" value="$offlineFriends" />
<postfield name="id" value="<%=card.getId()%>" />
</go>
</anchor><br/>
<%}%>
<%if(nextCard!=null){%>下一张：<a href="/job/happycard/card.jsp?id=<%=nextCard.getId()%>"><%=nextCard.getTitle()%></a><br/><%}%>
<a href="<%=("/job/happycard/index.jsp") %>">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
