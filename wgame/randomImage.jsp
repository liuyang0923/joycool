<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.action.wgame.WGameAction"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("randImage") == null){
	//response.sendRedirect(("index.jsp"));
	BaseAction.sendRedirect("/wgame/index.jsp", response);
	return;
}
session.removeAttribute("randImage");
WGameAction wa = new WGameAction();
wa.getRandImg(request);
String fileUrl = (String) request.getAttribute("fileUrl");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌场">
<p align="center">
<img src="<%=fileUrl%>" alt="loading..."/><br/>
<a href="<%=fileUrl%>" alt="下载">下载</a><br/>
<br/>
<anchor title="back"><prev/>返回</anchor>
</p>
</card>
</wml>