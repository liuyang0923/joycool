<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.getState() != vsGame.gameStart) {
	// 跳转
	response.sendRedirect("view.jsp");
	return;
}
List zombieList = vsUser.getZombieList();

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (zombieList != null) {
	%>僵尸墓地|<a href="setzombie.jsp">刷新</a><br/>家族公用太阳<%=vsGame.getZombieSun()%>.<br/>请选择您要的僵尸:<br/><%
	long now = System.currentTimeMillis();
	for (int i=0;i<zombieList.size();i++) {
		ZombieProtoBean bean = (ZombieProtoBean) zombieList.get(i);
		if (bean != null) {
			if(bean.getPrice()>vsGame.getZombieSun()){
				%><%=bean.getName()%>,<%=bean.getPrice()%>阳光(不足)<br/><%
			} else if(bean.getBuildTime()<now){
				%><a href="setzombie2.jsp?x=<%=i%>"><%=bean.getName()%></a>,<%=bean.getPrice()%>阳光<br/><%
			}else{
				%><%=bean.getName()%>,<%=bean.getPrice()%>阳光(<%=(bean.getBuildTime()-now)/1000+1%>秒)<br/><%
			}
		}
	}
} else {
	%>没有可选僵尸,请通知管理员添加!<br/><%		
}
%><a href="view.jsp">返回</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>