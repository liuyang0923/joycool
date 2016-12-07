<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.registerFriend(request);
String result=(String) request.getAttribute("result");
String url = ("/friend/editPhotoTemp.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
url = ("/friend/inputRegisterInfo.jsp");
%>
<card title="注册结果" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>,3秒后自动返回!<br/>
<a href="/friend/inputRegisterInfo.jsp">返回注册页面</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
%>
<card title="注册结果" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您已经开通交友中心,记得增加个性照片哟!(3秒后自动跳转交友中心个人相片页面) <br/>
<a href="/friend/friendCenter.jsp">返回交友中心首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>