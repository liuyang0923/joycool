<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
Object[][] view = vsGame.getGameMap();
if (action.hasParam("c")) {request.setAttribute("tip","植物已铲除!");}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (vsGame.getState() == vsGame.gameEnd) {
%>比赛已结束!<a href="over.jsp">查看挑战统计!</a><br/><%
} else if (vsGame.getState() == vsGame.gameInit) {
%>游戏将于<%=GameAction.getFormatDifferTime(vsGame.getStartTime()-System.currentTimeMillis())%>后开始<br/><%
}
if (request.getAttribute("tip")!= null) {%><%=request.getAttribute("tip")%><br/><%}
if (vsUser != null && vsGame.getState() == vsGame.gameStart) {
	if (vsUser.isPlant()) {
	%><a href="setplant.jsp">放置植物</a>|<%
	} else if (vsUser.isZombie()){
	%><a href="setzombie.jsp">放置僵尸</a>|<%
	}
}
%><a href="view.jsp">刷新</a><%if (vsUser != null) {%>(<%if (vsUser.isPlant()) {%><%=vsGame.getPlantSun()%><%} else {%><%=vsGame.getZombieSun()%><%}%>太阳)<%}%><br/><%
long now = System.currentTimeMillis();
for(int i = 0; i < view.length;i++){
	for (int j = 0; j < view[i].length; j++) {
		PVZBean pvzBean = (PVZBean) view[i][j];
		if (pvzBean.getState() == 1) {
			%>大门<%
		} else if (pvzBean.getState() == 2) {	
			%>一一<%
		} else {
			LinkedList zombieList = pvzBean.getZombieList();
			PlantBean plantBean = pvzBean.getPlantBean();
			if (zombieList.size() > 0) {
				if (plantBean != null) {
					if (vsUser != null) {
						if (vsUser.isPlant()) {
							PlantBean tempPlant = pvzBean.getPlantBean();
							if (tempPlant.getActionTime() < now) {
							%><a href="attack.jsp?i=<%=i%>&amp;j=<%=j%>">厮杀</a><%
							} else {
							%>厮杀<%
							}
						} else {
						%><a href="move.jsp?i=<%=i%>&amp;j=<%=j%>">厮杀</a><%
						}
					} else {
						%>厮杀<%
					}	
				} else {
					if (vsUser == null || vsUser.isPlant() || vsGame.getState() != vsGame.gameStart){
						%><%=((ZombieBean) zombieList.get(0)).getProto().getShortName()%><%
					} else {
						ZombieBean tempZombie = (ZombieBean) zombieList.get(0);
						if (zombieList.size() > 1 || tempZombie.getActionTime() < now) {
						%><a href="move.jsp?i=<%=i%>&amp;j=<%=j%>"><%=tempZombie.getProto().getShortName()%></a><%
						} else {
						%><%=tempZombie.getProto().getShortName()%><%
						}
					}
				}
			} else if (plantBean != null) {
				if (vsUser == null || vsUser.isZombie() || plantBean.getActionTime() > now || vsGame.getState() != vsGame.gameStart) {
					%><%=plantBean.getProto().getShortName()%><%
				} else {
					%><a href="attack.jsp?i=<%=i%>&amp;j=<%=j%>"><%=plantBean.getProto().getShortName()%></a><% 
				}
			} else {
				if(i==9){
					%>墓地<%
				}else{
					%>空地<%	
				}
			}
		}
		if (j != view[0].length - 1) {%>.<%}
	}
	%><br/><%
}
%>战斗信息:<a href="fif.jsp">更多</a><br/><%
LinkedList infoList = vsGame.getFightInformationList(); 
for (int i=0;i<infoList.size();i++){
	%><%=i+1%>.<%=infoList.get(i)%><br/><%
	if (i >= 2) break;
}
%><a href="tows.jsp">挑战双方信息</a><br/>
<%if(vsUser==null){%>
<%=vsGame.getChat().getChatString(0, 3)%><a href="chat2.jsp">更多聊天信息</a><br/>
<%}else{%>
聊天信息&gt;&gt;家族|<a href="chat2.jsp">公共</a><br/>
<%=vsGame.getChat(vsUser.getSide()).getChatString(0, 3)%>
<input name="fchat"  maxlength="100"/>
<anchor>发言<go href="chat.jsp" method="post"><postfield name="content" value="$fchat"/></go></anchor><br/>
<a href="chat.jsp">更多聊天信息</a><br/>
<%}%><a href="/fm/index.jsp">&lt;&lt;返回家族首页</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>