<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.apply();
TeamBean team = (TeamBean)request.getAttribute("team");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

%>
<%=StringUtil.toWml(team.getName())%>(<%=team.getCount()%>人)<br/>
<%=StringUtil.toWml(team.getInfo())%><br/>
分类:（无）<br/>
<a href="set.jsp">修改设定</a>|
<a href="chat.jsp">聊天</a>|
<a href="member.jsp">成员明细</a><br/>

<%}%>
<a href="info.jsp?id=<%=team.getId()%>">返回</a><br/>
</p>
</card>
</wml>