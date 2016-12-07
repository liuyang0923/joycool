<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="net.joycool.wap.bean.*,java.util.*,jc.guest.*,jc.guest.farmer.*,net.joycool.wap.framework.*"%><%
FarmAction action=new FarmAction(request,response);
GuestUserInfo guestUser = null;
GuestUserInfo guestLogin = action.getGuestUser();
FarmerBean fmBean = null;
int uid = action.getParameterInt("uid");
if (uid > 0) {
	guestUser = action.getGuestUser(uid);
	fmBean = FarmAction.service.getFarmerBean("uid="+uid);
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="完美农夫排行榜"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (guestUser != null) {
	if (fmBean != null) {
		if (guestLogin == null || guestLogin.getId() != uid) {
		%><a href="/guest/info.jsp?uid=<%=uid%>"><%=guestUser.getUserNameWml()%></a><br/>
		<%}%>
		完美次数:<%=fmBean.getLv1()%><br/>
		优秀次数:<%=fmBean.getLv2()%><br/>
		良好次数:<%=fmBean.getLv3()%><br/>
		糟糕次数:<%=fmBean.getLv4()%><br/><%
	} else {
	%>用户暂无战绩!<br/><%
	}
} else {
	%>无此用户!<br/><%
}
%><a href="index.jsp">返回完美农夫首页</a><br/><%
FarmerBean notice = action.getNotice();
if (notice != null) {
	GuestUserInfo tempGuest = action.getGuestUser(notice.getUid());
	%>恭喜<%=tempGuest.getUserNameWml()%>获得了1次完美评分!<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>