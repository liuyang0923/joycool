<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%
UserResBean us = action.getUserResBean();
CastleUserBean castleUser = action.getCastleUser();
if(castleUser == null) {
	response.sendRedirect("/cast/s.jsp");
	return;
}
long nowTime = System.currentTimeMillis();
int curQuest = castleUser.getQuest();
boolean haveQuest = (curQuest<ResNeed.getQuestCount());
%><%=BaseAction.getTop(request, response)%>
<%if(haveQuest&&curQuest<6){%><a href="quest.jsp">**新手任务**</a><br/><%}%>
<%if(action.curPage==1){%>资源<%}else{%><a href="base.jsp">资源</a><%}%>|<%if(action.curPage==2){%>建筑<%}else{%><a href="ad.jsp">建筑</a><%}%>|<%if(action.curPage==3){%>地图<%}else{%><a href="around.jsp">地图</a><%}%>|<%if(action.curPage==4){%>信息<%}else{%><a href="amsg.jsp">信息<%if(castleUser.getUnread()>0){%><%=castleUser.getUnread()%><%}%></a><%}%><%if(haveQuest&&curQuest>=6){%>|<a href="quest.jsp">任务</a><%}%><%if(castleUser.getCastleCount()>1){%>|<%if(action.curPage==5){%>城堡<%}else{%><a href="castle.jsp">城堡</a><%}%><%}%><br/>
<%if(haveQuest){%>资源:<%}%>木<%=us.getWood(nowTime)%>|石<%=us.getStone(nowTime)%>|铁<%=us.getFe(nowTime)%>|粮<%=us.getGrain(nowTime)%><br/>
