<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.set2();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

TeamBean team = (TeamBean)request.getAttribute("team");
TeamUserBean tu = (TeamUserBean)request.getAttribute("tu");%>
<%=team.getName()%><br/>
<a href="set2.jsp?flag=0">有新消息时发送通知</a>(<%if(tu.isFlag(0)){%>否<%}else{%>是<%}%>)<br/>
<a href="set2.jsp?flag=1">聊天页面自动刷新</a>(<%if(tu.isFlag(1)){%>是<%}else{%>否<%}%>)<br/>
注意：以上修改只在本圈子有效<br/>
<a href="chat.jsp">聊天</a>|<a href="info.jsp">圈子信息</a><br/>
<a href="index.jsp">返回圈子首页</a><br/>
<%}%>
</p>
</card>
</wml>