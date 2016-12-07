<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
int index=action.getParameterInt("i");
List chooseList;
if (vsUser.isPlant()) {
	chooseList = vsGame.getPlantProtoList();
} else {
	chooseList = vsGame.getZombieProtoList();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (action.hasParam("p")) {
	chooseList = vsGame.getPlantProtoList();
	if (index>=0 && index < chooseList.size()) {
		PlantProtoBean plantProto = (PlantProtoBean) chooseList.get(index);
		%><%=plantProto.getName()%>:<br/><img src="/rep/family/pvz/p<%=plantProto.getId()%>.gif" /><br/><%=plantProto.getDescribe()%><br/><a href="base.jsp?i=<%=index%>">选择此植物</a><br/><%
	} else {
	%>不存在此植物<br/><%
	}
} else if (action.hasParam("z")) {
	chooseList = vsGame.getZombieProtoList();
	if (index>=0 && index < chooseList.size()) {
		ZombieProtoBean zombieProto = (ZombieProtoBean) chooseList.get(index);
		%><%=zombieProto.getName()%>:<br/><img src="/rep/fm/z<%=zombieProto.getId()%>.gif" /><br/><%=zombieProto.getDescribe()%><br/><a href="base.jsp?i=<%=index%>">选择此僵尸</a><br/><%
	} else {
	%>不存在此僵尸<br/><%
	}
}%><a href="base.jsp">返回</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>