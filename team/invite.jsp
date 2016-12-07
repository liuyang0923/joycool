<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.invite();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

TeamBean team = (TeamBean)request.getAttribute("team");%>
<%=team.getName()%>(<%=team.getCount()%>人)<br/>
<%=team.getInfo()%><br/>
分类:（无）<br/>
<a href="set.jsp">修改设定</a>|
<a href="chat.jsp">聊天</a>|
<a href="member.jsp">成员明细</a><br/>

<%}%>
<a href="chat.jsp">聊天</a>|<a href="info.jsp">返回</a><br/>
<a href="index.jsp">返回圈子首页</a><br/>
</p>
</card>
</wml>