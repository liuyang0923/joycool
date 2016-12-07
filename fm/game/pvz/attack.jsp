<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.getState() != vsGame.gameStart) {
	response.sendRedirect("view.jsp");
	return;
}
Object[][] view = vsGame.getGameMap();
int i = action.getParameterInt("i");
int j = action.getParameterInt("j");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (i >= 0 && j >= 0 && i < view.length && j < view[0].length) {
	PVZBean pvzBean = (PVZBean) view[i][j];
	PlantBean plantBean = pvzBean.getPlantBean();
	if (plantBean != null) {
		if (action.hasParam("r")) {
			pvzBean.setPlantBean(null);
			StringBuilder info = new StringBuilder();
			info.append("第");
			info.append(j+1);
			info.append("列的一个");
			info.append(plantBean.getProto().getName());
			info.append("被铲除了!");
			vsGame.getFightInformationList().addFirst(info.toString());
			response.sendRedirect("view.jsp?c=1");
			return;
		} else {
			action.plantAttack(vsGame,vsUser,i,j);
		}
		if (request.getAttribute("tip")!= null) {%><%=request.getAttribute("tip")%><br/><%}
		%><%=plantBean.getProto().getName()%>,血<%=plantBean.getHp()%><br/><%
		if (plantBean.getProto().getType() == 0) { // 阳光
		} else if (plantBean.getProto().getType() == 1) { // 攻击
		} else if (plantBean.getProto().getType() == 3) { // 爆破
		}

	} else {
		%>植物已死亡!<br/><%
	}
		LinkedList zombieList = pvzBean.getZombieList();
		for (int n=0;n<zombieList.size();n++) {
			ZombieBean zombieBean = (ZombieBean) zombieList.get(n);
			if (zombieBean != null) {
				%><%=zombieBean.getProto().getName()%>|血<%=zombieBean.getHp()%><br/><%
			} else {
				%>僵尸数据错误,请通知管理员!<br/><%
			}
		}
		%><a href="view.jsp">返回战场</a><br/>
		<%if (plantBean != null) {%><br/><a href="attack.jsp?i=<%=i%>&amp;j=<%=j%>&amp;r=1">X铲除植物X</a><br/><%}%><%
} else {
	%>参数错误<br/><a href="view.jsp">返回战场</a><br/><%
}
%><%
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>