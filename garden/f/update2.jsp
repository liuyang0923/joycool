<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int errorNum = action.getParameterInt("err");
%><wml><card title="花之境" ontimer="<%=response.encodeURL("back.jsp?b=1")%>"><timer value="30"/><p>
<%=BaseAction.getTop(request, response)%>
<% if (errorNum == 1){
		%>你的金币不够,不足以升级你的养殖地!<%
   } else if (errorNum == 2){
   		%>你的成就值不够,不足以升级你的养殖地!<%
   } else if (errorNum == 3){
   		%>此土地非你所有!<%
   } else if (errorNum == 4){
   		%>地里有花没采摘,不能升级.<%
   } else {
   		%>错误.<%
   }%>(3秒后自动<a href="fgarden.jsp">返回</a>)<br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>