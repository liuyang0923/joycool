<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%
response.setHeader("Cache-Control","no-cache");
AppAction action = new AppAction(request);
List list = AppAction.service.getAppList("1");
Map map = action.getAppUserMap();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="组件集中营">
<p align="left">
【热门组件】<a href="my.jsp">我的组件</a><br/>
<%
int count = 0;
for(int i = 0;i < list.size();i++){
AppBean app = (AppBean)list.get(i);
if(app.isFlagHide())
	continue;
if(count>4) break;
count++;
%>
<img src="img/<%if(app.isFlagIcon()){%><%=app.getId()%><%}else{%>0<%}%>.gif" alt="x"/><a href="app.jsp?id=<%=app.getId()%>"><%=app.getName()%></a><%if(map == null || !map.containsKey(new Integer(app.getId()))){%>-<a href="appi.jsp?id=<%=app.getId()%>">添加</a><%}else{%>(已添加)<%}%><br/>
<%}%>
【组件分类】<%
List list2 = AppAction.getTypeList();
for(int i=0;i<list2.size();i++){
AppTypeBean at = (AppTypeBean)list2.get(i);
%>.<a href="type.jsp?id=<%=at.getId()%>"><%=at.getName()%>(<%=at.getCount()%>)</a><%}%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
