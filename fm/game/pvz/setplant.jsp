<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.getState() != vsGame.gameStart) {
	// 跳转
	response.sendRedirect("view.jsp");
	return;
}
List plantProtoList = vsUser.getPlantList();

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (plantProtoList != null) {
	%>植物园|<a href="setplant.jsp">刷新</a><br/>家族公用太阳<%=vsGame.getPlantSun()%>.<br/>请选择您要的植物:<br/><%
	long now = System.currentTimeMillis();
	for (int i=0;i<plantProtoList.size();i++) {
		PlantProtoBean bean = (PlantProtoBean) plantProtoList.get(i);
		if (bean != null) {
			if(bean.getPrice()>vsGame.getPlantSun()){
				%><%=bean.getName()%>,<%=bean.getPrice()%>阳光(不足)<br/><%
			} else if(bean.getBuildTime()<now){
				%><a href="setplant2.jsp?x=<%=i%>"><%=bean.getName()%></a>,<%=bean.getPrice()%>阳光<br/><%
			}else{
				%><%=bean.getName()%>,<%=bean.getPrice()%>阳光(<%=(bean.getBuildTime()-now)/1000+1%>秒)<br/><%
			}
		}
	}
} else {
	%>没有可选植物,请通知管理员添加!<br/><%	
}
%><a href="view.jsp">返回战场</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>