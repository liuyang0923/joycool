<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.spec.buyfriends.BeanVisit"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserAction"%><%!
static String[] scoreMark = {"☆","★","★★","★★★","★★★★","★★★★★"};
%><%
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
%>【<%=app.getName()%>】<%if(app.isFlagTest()){%>(测试中)<%}%><br/>
作者:<%=app.getAuthor()%><br/>
评分:<%=scoreMark[(int)Math.round(app.getAveScore())]%>(<%=app.getAveScoreString()%>)<br/>
简介:<%=app.getInfo()%><br/>
<%if(!added){%>
	<%if(app.isFlagClose()){%>该组件暂时无法安装<br/>
	<%}else{%>
	<a href="appi.jsp?id=<%=app.getId()%>">&gt;&gt;添加组件</a><br/>
	<%}%>
<%}else{%>

	<%if(app.isFlagOffline()){%>!!该组件正在维护中<br/><%if(app.getOffline().length()>0){%><%=app.getOffline()%><br/><%}%>
	<%}else{%>
	<a href="/<%=app.getDir()%>/">&gt;&gt;进入组件</a><br/>
	<%}%>
	
<%}%>
<a href="content.jsp?id=<%=action.getParameterInt("id")%>">交流区(<%=app.getReplyCount()%>)</a>|<a href="score.jsp?id=<%=action.getParameterInt("id")%>">评分(<%=app.getScoreCount() %>)</a><br/>
<%if(added){%>
<a href="del.jsp?id=<%=action.getParameterInt("id")%>">×删除组件×</a><br/>
<%}%>

<%}%>
<a href="my.jsp">返回我的组件</a><br/>
<a href="index.jsp">返回组件集中营</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>