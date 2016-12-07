<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.SendAction" %><%@ page import="net.joycool.wap.util.*" %><%
response.setHeader("Cache-Control","no-cache");
SendAction action=new SendAction(request);
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("chat",loginUser.getId());
if(forbid!=null){
	action.doTip("failure","已经被禁止发言");
}else{
	action.groupSendMsgResult(request);
}
String result=(String)request.getAttribute("result");
String url=("/user/ViewFriends.do");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect((url));
return;
}else if(result.equals("failure")){%>
<card title="好友消息群发">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/user/groupSendMsg.jsp">好友群发</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("timeError")){%>
<card title="好友消息群发" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转好友列表)<br/>
<a href="<%=url%>">返回好友列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="好友消息群发" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
发送成功!(3秒后跳转好友列表)<br/>
<a href="/user/groupSendMsg.jsp">返回好友列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>