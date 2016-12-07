<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.member();
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

TeamBean team = (TeamBean)request.getAttribute("team");
List userList = team.getUserList();%>
<%=StringUtil.toWml(team.getName())%>(<%=team.getCount()%>人)<br/>
<%
for(int i = 0;i < userList.size();i++){
TeamUserBean tu = (TeamUserBean)userList.get(i);
%><a href="/chat/post.jsp?toUserId=<%=tu.getUserId()%>"><%=tu.getName()%></a><br/>
<%}%>
<a href="chat.jsp">聊天</a>|
<a href="info.jsp">返回</a><br/>
<%}%>
</p>
</card>
</wml>