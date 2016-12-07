<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%//@ page errorPage="../failure.jsp"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
UserAction action = new UserAction(request);
action.shortcut2Result();
if(!action.isResult("failure")){
	response.sendRedirect(("shortcut2.jsp?p="+action.getParameterInt("p")));
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="快捷通道设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="<%=("shortcutList2.jsp")%>">添加链接</a><br/>
<a href="<%=("shortcut2.jsp")%>">查看已经加链接</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>