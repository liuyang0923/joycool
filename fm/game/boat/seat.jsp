<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.*,jc.family.*,jc.family.game.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
String[] seatNum = {"左1","右1","左2","右2","左3","右3","左4","右4","左5","右5","掌舵人"};
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="龙舟比赛"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean == null){
	out.println("你没有加入家族");
}else{
Integer inmid = (Integer)session.getAttribute("mid");
int uid = action.getLoginUser().getId();
int mid = 0;
if(inmid != null){
	mid = inmid.intValue();
}
MatchBean matchBean = (MatchBean)BoatAction.matchCache.get(new Integer(mid));
if(matchBean == null){
	response.sendRedirect("/fm/game/fmgame.jsp");
	return;
}
if(matchBean.getState() == 2){
	response.sendRedirect("game.jsp");
	return;
}
BoatGameBean bean = (BoatGameBean)matchBean.getGameMap().get(new Integer(fmbean.getFm_id()));
if(bean != null){
BoatBean boatBean = bean.getBoat();
%><%=StringUtil.toWml(bean.getFmName())%>家族|<a href="boatinfo2.jsp?bid=<%=boatBean.getId()%>"><%=StringUtil.toWml(boatBean.getName())%></a><br/>
<%
action.sit(bean,uid);
boolean onboat = false;
for(int i=0;i<bean.getSeat().length;i++){
	MemberBean mb = (MemberBean)bean.getSeat()[i];
	if(mb != null && mb.getUid()==uid){
		onboat = true;
	}
}
for(int i=0;i<bean.getSeat().length;i++){
	MemberBean mb = (MemberBean)bean.getSeat()[i];
	%><%=seatNum[i]%>:<%
	if(i == 10){
	%><%if(mb != null && mb.getUid() > 0){%><%=UserInfoUtil.getUser(mb.getUid()).getNickNameWml()%><%}%><%
	}
	if(mb == null || mb.getUid() == 0){
		if(onboat){
		%>坐下<%
		}else{
		%><a href="game.jsp?a=1&amp;i=<%=i%>">坐下</a><%
		}
	}else{
		if(mb.getUid()==uid){
		%><a href="seat.jsp?a=2&amp;i=<%=i%>">站起</a><%
		}else{
		%>已坐<%
		}
	}
	if(i%2 == 1){
	%><br/><%
	}else if(i != bean.getSeat().length-1){
	%>||<%
	}
}
%><br/><%
}else{
%>赛事不存在<br/><%
}%><a href="game.jsp">返回赛事</a><br/><%
}%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>