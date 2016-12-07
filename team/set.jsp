<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.set();
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
<a href="set.jsp?flag=0">允许陌生人浏览</a>(<%if(team.isFlagOutBrowse()){%>是<%}else{%>否<%}%>)<br/>
<a href="set.jsp?flag=1">允许陌生人发言</a>(<%if(team.isFlagOutChat()){%>是<%}else{%>否<%}%>)<br/>
圈子名称:<input name="name" maxlength="6" value="<%=StringUtil.toWmlQ(team.getName())%>"/><br/>
<anchor title="确定">确认修改
<go href="set.jsp" method="post">
    <postfield name="name" value="$name"/>
</go></anchor><br/>
圈子介绍:<input name="info" maxlength="100" value="<%=StringUtil.toWmlQ(team.getInfo())%>"/><br/>
<anchor title="确定">确认修改
<go href="set.jsp" method="post">
    <postfield name="info" value="$info"/>
</go></anchor><br/>
圈子分类:<select name="class1" value="<%=team.getClass1()%>">
<option value="0">无</option>
<%
List classList = TeamAction.getTeamClassList();
for(int i = 0;i < classList.size();i++){
TeamClassBean tc = (TeamClassBean)classList.get(i);
%>
<option value="<%=tc.getId()%>"><%=tc.getName()%></option>
<%}%>
</select><br/>
<anchor title="确定">确认修改
<go href="set.jsp" method="post">
    <postfield name="class1" value="$class1"/>
</go></anchor><br/>

<a href="chat.jsp">聊天</a>|
<a href="member.jsp">成员明细</a><br/>
<a href="info.jsp">返回</a><br/>
<%}%>
</p>
</card>
</wml>