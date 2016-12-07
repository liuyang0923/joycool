<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
int userId = action.getParameterInt("id");
UserBean user = null;
if(userId>0)
	user = UserInfoUtil.getUser(userId);
if(user==null){
response.sendRedirect("index.jsp");
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>
<%=user.getNickNameWml()%>的圈子<br/><%
List teamList = net.joycool.wap.util.SqlUtil.getIntList("select team_id from team_user where user_id=" + userId, 3);
if(teamList.size()==0){%>(暂无)<br/><%}else{
for(int i = 0;i < teamList.size();i++){
TeamBean team = action.getTeam(((Integer)teamList.get(i)).intValue());
if((team.getFlag()&4)!=0) continue;
%><%=i+1%>.<%if(team.isFlagOutBrowse()){%><a href="chat.jsp?ti=<%=team.getId()%>"><%=StringUtil.toWml(team.getName())%></a><%}else{%><%=StringUtil.toWml(team.getName())%><%}%>-
<a href="info.jsp?ti=<%=team.getId()%>">查看</a><br/>
<%}}%>

<%}%>
<a href="all.jsp">查看所有圈子</a><br/>
<%if(action.getCurrentTeam()!=null){%><a href="chat.jsp?to=<%=userId%>">返回聊天</a><br/><%}%>
<a href="index.jsp">返回圈子首页</a><br/>
<a href="/home/home2.jsp?userId=<%=user.getId()%>">返回<%=user.getNickNameWml()%>的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>