<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.user.*,net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.inviteTong();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(action.isResult("success")){%>
<card title="邀请成功">
<%=BaseAction.getTop(request, response)%>
<p align="left">
<%=action.getTip()%><br/><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="邀请失败">
<%=BaseAction.getTop(request, response)%>
<p align="left">
<%=action.getTip()%><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>