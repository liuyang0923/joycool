<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.getState() != vsGame.gameStart) {
	response.sendRedirect("view.jsp");
	return;
}
int i = action.getParameterInt("i");
int j = action.getParameterInt("j");
Object[][] view = vsGame.getGameMap();
if (action.hasParam("n")) {action.checkZombieMove(vsGame,vsUser);}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (i >= 0 && j >= 0 && i < view.length && j < view[0].length) {
	if (request.getAttribute("tip") != null) {%><%=request.getAttribute("tip")%><br/><%}
	%><a href="move.jsp?i=<%=i%>&amp;j=<%=j%>">刷新</a>|当前第<%=j+1%>排<br/><%
	PVZBean pvzBean = (PVZBean) view[i][j];
	LinkedList zombieList = pvzBean.getZombieList();
	PlantBean plantBean = pvzBean.getPlantBean();
	if (plantBean != null) {%>当前植物：<br/><%=plantBean.getProto().getName()%>|血<%=plantBean.getHp()%><br/><%}
	if (zombieList.size() > 0) {
		for (int n=0;n<zombieList.size();n++) {
			ZombieBean zombieBean = (ZombieBean) zombieList.get(n);
			if (zombieBean != null) {
				%><%=zombieBean.getProto().getName()%>|血<%=zombieBean.getHp()%>|<%
				boolean canAction = zombieBean.getActionTime() < System.currentTimeMillis() ? true : false;
				boolean canAttack = action.getCanAttack(zombieBean,plantBean);
				String show = canAttack ? "攻击" : "前进";
				if (canAction) {
					%><a href="move.jsp?i=<%=i%>&amp;j=<%=j%>&amp;n=<%=n%>"><%=show%></a><%
				} else {
					%><%=show%><%
				}
				%><br/><%
			} else {
			%>僵尸数据错误,请通知管理员!<br/><%
			}
		}
	} else {
		%>此地点已没有僵尸<br/><%
	}
} else {
	%>参数错误<br/><%
}
%><a href="view.jsp">返回战场</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>