<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
List list = new ArrayList(action.getAppUserMap().keySet());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="组件">
<p align="left">
【我的组件列表】<br/>
<%if(list.size()==0){%>(暂无)<br/><%}else{%>
<%
for(int i = 0;i < list.size();i++){
Integer iid = (Integer)list.get(i);
AppBean app = AppAction.getApp(iid.intValue());
if(app.isFlagHide())
	continue;
%><img src="img/<%if(app.isFlagIcon()){%><%=app.getId()%><%}else{%>0<%}%>.gif" alt="x"/><a href="<%=app.getDirFull()%>"><%=app.getName()%></a>-<a href="app.jsp?id=<%=app.getId()%>">资料</a><br/>
<%}%>
<%}%>
<a href="list.jsp">&gt;&gt;添加组件</a><br/>
<a href="index.jsp">返回组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
