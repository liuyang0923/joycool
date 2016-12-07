<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="jc.guest.*,jc.guest.farmer.*,net.joycool.wap.framework.*"%><%
FarmAction action=new FarmAction(request,response);
GuestUserInfo guestUser = action.getGuestUser();
FarmGameBean fgbean = (FarmGameBean) session.getAttribute("fgbean");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="完美农夫"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fgbean != null) {
	int steps = fgbean.getSteps();
	%>您总共播种了<%=steps%>块麦田，获得评分：<%if(steps == 30){%>完美<%}else if(steps >= 25 && steps < 30){%>优秀<%}else if (18 <= steps && steps < 25){%>良好<%}else{%>糟糕<%}%>!<br/><%
	if (fgbean.getUid() > 0) {
		if (guestUser != null) {
			int lvl = guestUser.getLevel();
			if ("yes".equals(session.getAttribute("start"))) {
				// 加经验
				action.addPoint(guestUser,1);
				action.smtFarmer(fgbean);
				session.removeAttribute("start");
			}
			if(guestUser.getLevel() < GuestHallAction.point.length ){
				int nowLvl = guestUser.getLevel();
				int remainExp = GuestHallAction.point[guestUser.getLevel()]-guestUser.getPoint();
				%>您获得1点经验,<%if (nowLvl - lvl == 1) {%>恭喜您等级升为<%=nowLvl%>级，<%}%>距离提升到下一等级，还差<%=remainExp%>点经验。每升1级，每天上线将会多领取50游币。<br/><%
			}
		}
	} else {
	%>很抱歉，您还不是注册用户，我们无法为您保存战绩哦。想保存战绩？只需两步就可以成功注册，请点此<a href="/guest/nick.jsp">设置昵称</a><br/><%
	}
	session.removeAttribute("fgbean");
} else {
	%>游戏内容为空!<br/><%
}
%><a href="farm.jsp?rep=1">重开一局</a><%if (guestUser != null) {
%>(需花费1游币)<%
}%><br/><a href="index.jsp">返回完美农夫首页</a><br/><%
FarmerBean notice = action.getNotice();
if (notice != null) {
	GuestUserInfo tempGuest = action.getGuestUser(notice.getUid());
	%>恭喜<%=tempGuest.getUserNameWml()%>获得了1次完美评分!<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>