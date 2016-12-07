<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.addApply(request);
String result=(String)request.getAttribute("result");
String url=("/chat/myApply.jsp");
String url1=("/chat/roomApply.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if("exist".equals(result)){
%>
<card title="提交" ontimer="<%=response.encodeURL(url1)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("failure".equals(result)){
%>
<card title="我要加入" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
%>
<card title="提交" ontimer="<%=response.encodeURL(url1)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
申请已提交！赶紧去招纳您的聊天室成员吧！（3秒钟返回）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>