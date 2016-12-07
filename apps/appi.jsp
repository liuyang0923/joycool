<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
action.view();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="组件">
<p align="left">
<%if(action.isResult("tip")){%>
	<%=action.getTip()%><br/>
<%}else{
	AppBean app = (AppBean)request.getAttribute("appBean");
	Map map = action.getAppUserMap();
	boolean added = (map != null && map.containsKey(new Integer(app.getId())));
	
%>【<a href="app.jsp?id=<%=app.getId()%>"><%=app.getName()%></a>】<br/>
确认要添加这个组件吗?<br/>
<%if(app.isFlagClose()){%>该组件暂时无法安装<br/>
<%}else if(!added){%>
如果添加了[<%=app.getName()%>],将允许该组件获取你在乐酷的个人信息、好友信息等.<br/>
<anchor>同意<go href="mapp.jsp?a=1&amp;id=<%=app.getId()%>" method="post"></go></anchor>|<a href="app.jsp?id=<%=app.getId()%>">取消</a><br/>
<%}else{%>
<a href="/<%=app.getDir()%>/">&gt;&gt;进入组件</a><br/>
<%}%>
<%if(app.isFlagOffline()){%>该组件暂时无法使用<br/><%}%>
<%}%><br/>
<a href="my.jsp">返回我的组件</a><br/>
<a href="index.jsp">返回组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>