<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsGame.getState() != vsGame.gameEnd) {
	response.sendRedirect("view.jsp");
	return;
}
int over = vsGame.getGameResult();
int side = 0;
int killed;
String killdSide;
String result;
String fmName;
List userList;
if (vsUser != null) {side = vsUser.getSide();} else {side = vsGame.getZombieSide();}
if (action.hasParam("s")) {side = action.getParameterInt("s");}
if (side < 0 || side > 1) {side = 0;}
if (side == vsGame.getPlantSide()) {
	result = over == side?"植物获胜":"植物失败";
	killdSide = "消灭僵尸:";
	killed = vsGame.getZombieDieNum();
	userList = vsGame.getPlantUserList();
} else {
	result = over == side ? "僵尸获胜":"僵尸失败";
	killdSide = "消灭植物:";
	killed = vsGame.getPlantDieNum();
	userList = vsGame.getZombieUserList();
}
if (side == 0) {fmName = vsGame.getFmANameWml();} else {fmName = vsGame.getFmBNameWml();}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%>
挑战统计|<a href="view.jsp">查看战场</a><br/>
<%=result%>:<%=fmName%>家族<br/>
参与人数:<%=userList.size()%><br/>
用时:<%=GameAction.getFormatDifferTime(vsGame.getEndTime() - vsGame.getStartTime())%><br/>
<%=killdSide%><%=killed%><br/>
成员|操作<br/><%
for (int i = 0; i < userList.size();i++) {
	PVZUserBean pvzUser = (PVZUserBean) userList.get(i);
	%><%=pvzUser.getNickNameWml()%>|<%=pvzUser.getOperNum()%><br/><%
}
%><a href="over.jsp?s=<%=1-side%>">查看对方家族</a><br/><a href="/fm/index.jsp">返回家族首页</a><br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>