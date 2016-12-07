<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
if(action.isMethodGet()){
	response.sendRedirect("list.jsp");
	return;
}
int act = action.getParameterInt("a");
if(act == 1){	// 安装
	action.install();
} else if(act == 2){	// 卸载
	action.install();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="组件">
<p align="left">
<%if(action.isResult("tip")){%>
	<%=action.getTip()%><br/>
<%}%>
<a href="my.jsp">返回我的组件</a><br/>
<a href="index.jsp">组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
