<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.*,jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%!
	static int maxSelect = 4;
%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.isEnd()) { // 观众直接跳转到游戏页
	response.sendRedirect("view.jsp");
	return ;
}
List chooseList;
LinkedList useList;
if (vsUser.isPlant()) {
	chooseList = vsGame.getPlantProtoList();
 	useList = vsUser.getPlantList(); 
} else {
	chooseList = vsGame.getZombieProtoList();
	useList = vsUser.getZombieList();
}
if (vsGame.isStart() && useList.size()>= maxSelect) { // 已经选择过了，直接开始
	response.sendRedirect("view.jsp");
	return ;
}
if (action.hasParam("i")) {
	int index = action.getParameterInt("i");
	if (index >=0 && index < chooseList.size()){
		if (useList.size() < maxSelect) {
			if (vsUser.isPlant()) {
				useList.add(chooseList.get(index));	
			} else {
				useList.add(chooseList.get(index));
			}
			response.sendRedirect("base.jsp");
			return;
		}
	}
}
if (action.hasParam("rc")) {
	useList.clear();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (vsUser.isPlant()) {
 	%>植物园:<br/>家族公用太阳<%=vsGame.getPlantSun()%><br/>已选植物:<%
 	if (useList.size() > 0) {
 		for (int i = 0; i < useList.size();i++) {
 			PlantProtoBean plantProto = (PlantProtoBean) useList.get(i);
			%><%=plantProto.getName()%><%
			if (i != useList.size() - 1) {%>|<%}
 		}
		%><br/><%
		if (useList.size() >= maxSelect) {
 			%><a href="view.jsp">开始游戏!!</a><br/><a href="base.jsp?rc=1">重新选择植物</a><br/><%
		} else {
 		%>您还可以再选择<%=maxSelect-useList.size()%>种植物:<br/><%
		}
 	} else {
 		%>无<br/>您还可以再选择<%=maxSelect%>种植物:<br/><%
 	}
 	if (useList.size() < maxSelect) {
		for (int i = 0; i < chooseList.size(); i++){
	 		PlantProtoBean plantProto = (PlantProtoBean) chooseList.get(i);
	 		if (useList.contains(plantProto)) {
			%><%=plantProto.getName()%><%
	 		} else {
			%><a href="details.jsp?p=1&amp;i=<%=i%>"><%=plantProto.getName()%></a><%
	 		}
			if (i != chooseList.size() - 1) {%>|<%}
	 	}	
		%><br/><%
 	}
} else {
 	%>僵尸墓地:<br/>家族公用太阳<%=vsGame.getZombieSun()%><br/>已选僵尸:<%
	if (useList.size() > 0) {
 		for (int i = 0; i < useList.size(); i++) {
 			ZombieProtoBean zombieProto = (ZombieProtoBean) useList.get(i);
 			%><%=zombieProto.getName()%><%
 			if (i != useList.size() - 1) {%>|<%}
 		} 
		%><br/><%
 		if (useList.size() >= maxSelect) {
 			%><a href="view.jsp">开始游戏!!</a><br/><a href="base.jsp?rc=1">重新选择僵尸</a><br/><%
	 	} else {
 		%>您还可以再选择<%=maxSelect-useList.size()%>种僵尸:<br/><%
 		}
	} else {
		%>无<br/>您还可以再选择<%=maxSelect%>种僵尸:<br/><%
	}
	if (useList.size() < maxSelect) {
		for (int i = 0; i < chooseList.size(); i++){
	 		ZombieProtoBean zombieProto = (ZombieProtoBean) chooseList.get(i);
	 		if (useList.contains(zombieProto)) {
			%><%=zombieProto.getName()%><%
	 		} else {
			%><a href="details.jsp?z=1&amp;i=<%=i%>"><%=zombieProto.getName()%></a><%
	 		}
			if (i != chooseList.size() - 1) {%>|<%}
		}
		%><br/><%
	}
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>