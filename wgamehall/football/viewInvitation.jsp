<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgamehall.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FootballAction action = new FootballAction(request);
action.viewInvitation(request);
String result = (String) request.getAttribute("result");
WGameHallBean football = (WGameHallBean) request.getAttribute("football");
UserStatusBean enemyUs = (UserStatusBean) request.getAttribute("enemyUs");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="查看玩家邀请">
<p align="center">查看玩家邀请</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//失败
if("failure".equals(result)){
%>
对方已经取消邀请<br/>
<%
}
//应邀或者取消
else if(football != null){
	UserStatusBean us=null;
	us=UserInfoUtil.getUserStatus(football.getLeftUserId());
%>
<%if(us!=null){%><%=us.getHatShow()%><%}%><%=StringUtil.toWml(football.getLeftNickname())%>想跟您玩点球决战,他现在有<%=enemyUs.getGamePoint()%>个乐币.<br/>
<a href="/wgamehall/football/acceptInvitation.jsp?gameId=<%=football.getId()%>">接受邀请</a>|<a href="/wgamehall/football/denyInvitation.jsp?gameId=<%=football.getId()%>">拒绝邀请</a><br/>
<%
}
%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamehall/football/index.jsp">返回上一级</a><br/>
<a href="/wgameh/hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>