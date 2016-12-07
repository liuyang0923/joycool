<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
TorchAction action = new TorchAction(request);
action.pass();
int torchId = action.getParameterInt("t");
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="奥运火炬手">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
List userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
List onlineFriendsList = new ArrayList();

for(int i=0;i<userFriends.size();i++)
{
	String userIdKey=(String)userFriends.get(i);
	if(OnlineUtil.isOnline(userIdKey))
		onlineFriendsList.add(userIdKey);
}
%>
=传递火炬给在线好友=<br/>
<%
for(int i = 0; i < onlineFriendsList.size(); i ++){
	String userId = (String) onlineFriendsList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;%>
<a href="pass.jsp?t=<%=torchId%>&amp;u=<%=user.getId()%>">传递火炬-<%=user.getNickNameWml()%></a><br/>
<%}%>

<%}%>
<br/>
<a href="index.jsp">返回奥运火炬首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>