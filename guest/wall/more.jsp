<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.wall.*"%>
<% response.setHeader("Cache-Control","no-cache");
WallAction action = new WallAction(request);
String tip = "";
WallBean bean = null;
UserBean user = null;
int id = action.getParameterInt("id");
if (id <= 0){
	tip = "要查看的信息不存在.";
} else {
	bean = WallAction.service.getWallBean(" id=" + id);
	if (bean == null || bean.getVisible() == 0){
		tip = "要查看的信息不存在.";
	} else {
		user = UserInfoUtil.getUser(bean.getUid());
	}
}
List list = action.getWallRandom(2);	// 随机找2个墙
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐酷用户墙"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>★<%=bean.getTitleWml()%>★<br/>
<%if (user != null){%>文/<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><br/><%}%>
<%=bean.getContentWml()%><br/>
==========<br/>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		bean = (WallBean)list.get(i);
		if (bean != null){
			user = UserInfoUtil.getUser(bean.getUid());
			%>★<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>:<%=bean.getShortContWml()%><a href="more.jsp?id=<%=bean.getId()%>">详</a><br/><%
		}
	}
}%>
<%
} else {
	%><%=tip%><br/><%
}%>==<a href="index.jsp">返回用户墙</a>==<br/><a href="/chat/hall.jsp">去乐酷论坛</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>