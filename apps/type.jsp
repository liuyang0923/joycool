<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
int type = action.getParameterInt("id");
List list = AppAction.service.getAppList("type=" + type);
Map map = action.getAppUserMap();
List list2 = AppAction.getTypeList();
AppTypeBean appType = AppAction.getType(type);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="组件分类">
<p align="left">
【<%if(appType!=null){%><%=appType.getName()%><%}else{%>??<%}%>】<br/><%
for(int i = 0;i < list.size();i++){
AppBean app = (AppBean)list.get(i);
if(app.isFlagHide())
	continue;
%>
<img src="img/<%if(app.isFlagIcon()){%><%=app.getId()%><%}else{%>0<%}%>.gif" alt="x"/><a href="app.jsp?id=<%=app.getId()%>"><%=app.getName()%></a><%if(map == null || !map.containsKey(new Integer(app.getId()))){%>-<a href="appi.jsp?id=<%=app.getId()%>">添加</a><%}else{%>(已添加)<%}%><br/>
<%}%>
<%
for(int i=0;i<list2.size();i++){
AppTypeBean at = (AppTypeBean)list2.get(i);
%><%if(i!=0){%>.<%}%><%if(at.getId()!=type){%><a href="type.jsp?id=<%=at.getId()%>"><%=at.getName()%>(<%=at.getCount()%>)</a><%}else{%><%=at.getName()%>(<%=at.getCount()%>)<%}%><%}%><br/>
<a href="index.jsp">返回组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
