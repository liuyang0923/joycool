<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.getLastWeekRank(request);
int day=StringUtil.toInt((String)request.getAttribute("day"));
String date=(String)request.getAttribute("date");
Vector rankList=(Vector)request.getAttribute("rankList");
int rank = StringUtil.toInt((String)request.getAttribute("rank"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请榜">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=date%>王冠获得者<br/>
------------<br/>
<%
RoomInviteRankBean rankBean=null;
for(int i=0;i<rankList.size();i++){
	rankBean=(RoomInviteRankBean)rankList.get(i);
	UserStatusBean us=UserInfoUtil.getUserStatus(rankBean.getUserId());
%>
<%=(i+1)%>.<%if(us!=null){%><%=us.getHatShow()%><%}%>
<a href="/user/ViewUserInfo.do?userId=<%=rankBean.getUserId()%>"><%=StringUtil.toWml(rankBean.getNickName())%></a>(邀请<%=rankBean.getCount()%>位)<br/>
<%}%>
<%for(int i=1;i<=7;i++){%><%if(i!=1){%>|<%}%><%
	if(i!=day){
%><a href="lastRank.jsp?day=<%=(-i)%>"><%=i%></a><%}else{%><%=i%><%}}%><br/>
<a href="inviteRank.jsp">查看今天的排名</a><br/>
【邀请榜说明】<br/>
1.按照用户今天邀请好友的数量决定排名<a href="inviteRoom.jsp">马上开始邀请>></a><br/>
2.当日排名前7者可得一顶独特的乐酷王冠，第二日生效<br/>
3.获得王冠的用户可以持有7天<br/>
4.7天后重新通过邀请好友的排名来抢王冠<br/>
<a href="inviteRoom.jsp">马上开始邀请好友抢王冠</a><br/>
==其他龙虎榜==<br/>
<%@include file="/top/bottom.jsp"%>
</p>
</card>
</wml>
