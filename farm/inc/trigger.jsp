<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
if(farmUser.getCurTrigger().size() > 0) {
TriggerBean trigger = (TriggerBean)farmUser.getCurTrigger().getFirst();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=FarmWorld.replaceInfo(StringUtil.toWml(trigger.getInfo()),farmUser)%><br/>
<%=FarmWorld.getActionDetail(trigger.getActionList())%><br/>
<a href="/farm/inc/vt.jsp?id=<%=trigger.getId()%>">确定</a><br/>
<%@include file="../bottom.jsp"%></p>
</card>
</wml><%
return;
}
%>