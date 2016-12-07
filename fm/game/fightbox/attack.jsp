<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsUser.isDeath() || vsGame.getState() != vsGame.gameStart) {response.sendRedirect("check.jsp");return;}
String[] showName = vsGame.weapons;
if (action.hasParam("aimI")) {
	int i = action.getParameterInt("aimI");
	int j = action.getParameterInt("aimJ");
	if (Math.abs(vsUser.getCurrI() - i) + Math.abs(vsUser.getCurrJ() - j) < 3) {
		vsUser.setAimI(i);
		vsUser.setAimJ(j);
	}
	vsUser.setMapState(2);
	vsUser.setAimAtack(0);
}
Object[][] show = vsGame.getShow(vsUser,false);
int countMyFm = vsGame.getCountFmA();
int countEnemyFm = vsGame.getCountFmB();
String friends = "剑枪弓";
String enemys = "斧矛弩";
if (vsUser != null && vsUser.getSide() == 1) {
	countMyFm = vsGame.getCountFmB();
	countEnemyFm = vsGame.getCountFmA();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="攻击"><p align="left"><%=BaseAction.getTop(request, response)%><% 
%><%=countMyFm%>友:<%=friends%>,<%=countEnemyFm%>敌:<%=enemys%><br/><% 
%>请选择攻击方向:<br/><%
	if (vsUser.getMapState() == 2) {
		for (int i=0;i<show.length;i++) {
			for (int j=0;j<show[i].length;j++) {
				BoxShowBean boxShowBean = (BoxShowBean) show[i][j];
				int weap = vsGame.checkWeap(boxShowBean, vsUser);
				if (FightBoxAction.canAtack(vsUser,i,j)) {
				%><a href="hitinfo.jsp?i=<%=i%>&amp;j=<%=j%>"><%=showName[weap]%></a><%
				} else {
				%><%=showName[weap]%><%
				}
			}
			%><br/><%
		}
	}		
%>
<a href="check.jsp">重新移动</a><br/><a href="check.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>