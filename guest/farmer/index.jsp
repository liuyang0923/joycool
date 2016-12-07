<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="jc.guest.*,jc.guest.farmer.*,net.joycool.wap.framework.*"%><%
FarmAction action=new FarmAction(request,response);
GuestUserInfo guestUser = action.getGuestUser();
FarmGameBean fgbean = (FarmGameBean) session.getAttribute("fgbean");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="完美农夫"><p align="left"><%=BaseAction.getTop(request, response)%>
啦啦啦～啦啦啦～我是种田的小农夫～<br/><%
if(fgbean != null) {
	%><a href="farm.jsp">继续游戏</a><br/><%
} 
%><a href="farm.jsp?rep=1">新开一局</a><%if (guestUser != null) {
%>(需花费1游币)<%
}%><br/><a href="rank.jsp">排行榜</a><br/><a href="rule.jsp">游戏说明</a><br/><a href="/guest/index.jsp">返回游乐园</a><br/><%
FarmerBean notice = action.getNotice();
if (notice != null) {
	GuestUserInfo tempGuest = action.getGuestUser(notice.getUid());
	%>恭喜<%=tempGuest.getUserNameWml()%>获得了1次完美评分!<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>