<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
static String[] status = {
"无","免扰","闲","忙","代管","非本人","暂用","小号","开心","得意","郁闷","怒"
};
%><%
response.setHeader("Cache-Control","no-cache");
int pos = StringUtil.toInt(request.getParameter("p"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(pos >= 0 && pos < status.length){
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
loginUser = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(loginUser.getId()));
if(pos==0)
	loginUser.status = null;
else
	loginUser.status = status[pos];
%>
<card title="设置状态">
<p align="left">
<%=BaseAction.getTop(request, response)%>
其他玩家看到你的状态为(闲逛中<%if(loginUser.status!=null){%>|<%=loginUser.status%><%}%>)<br/>
<a href="status.jsp">重新设置</a><br/>
<a href="userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>

<card title="设置状态">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请设置你的状态:<br/>
<%for(int i=0;i<status.length;i++){
%><a href="status.jsp?p=<%=i%>"><%=status[i]%></a><%if(i%4!=3){%>|<%}else{%><br/><%}%><%}%>
<br/>
<a href="userInfo.jsp">返回用户设置</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
<%}%>
</wml>