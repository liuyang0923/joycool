<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null) {response.sendRedirect("check.jsp");return;}//观众可以直接进入
if (vsUser.getWeapon() == 0 && !action.hasParam("weap")) {response.sendRedirect("chweap.jsp");return;}
String[] weaps = vsGame.weapons;
int weapon = action.getParameterInt("weap");
long now = System.currentTimeMillis();
if(weapon != 0) {
	if (vsGame.getState() == vsGame.gameStart){
		if (vsUser.getWeapon() == 0) {
			vsUser.setWeapon(weapon);
		}
	} else {
		vsUser.setWeapon(weapon);
	}
	
	if (vsUser.getBlood()==0 && vsUser.getLife()>0) {
		if (vsGame.getStopEnter() != 0 && now - vsGame.getStopEnter() > 120000) {
			vsUser.setLife(0);
			vsUser.setDeath(true);
		} else {
			vsUser.decLife();
			vsUser.setBlood(10);
			int[] iniLocation = action.getIniLocation(vsUser.getSide());
			vsUser.setCurrI(iniLocation[0]);
			vsUser.setCurrJ(iniLocation[1]);
			BoxBean box = (BoxBean) vsGame.getScene()[iniLocation[0]][iniLocation[1]];
			List aBoxUserList = box.getBoxUserList();
			List gameUserList = vsGame.getBoxUserList();
			synchronized (aBoxUserList) {
				aBoxUserList.add(vsUser);
			}
			synchronized (gameUserList) {
				gameUserList.add(vsUser);
			}
			if (vsUser.getSide() == 0) {
				vsGame.setCountFmA(vsGame.getCountFmA() + 1);
			} else {
				vsGame.setCountFmB(vsGame.getCountFmB() + 1);
			}
			vsGame.setShow(null);
		}
	}
}
long coolTime = vsGame.getStartTime() - now;
if (vsUser.getWeapon() != 0 && (vsGame.isStart() || vsGame.isEnd())) {
	response.sendRedirect("check.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%><% 
if (coolTime <= 0) {
	%>游戏已开始，冲啊！勇士们！为了荣誉！<br/>您已选了武器:<%=weaps[vsUser.getWeapon()]%><br/><a href="check.jsp">开始游戏</a><br/><%
} else {%>
距开始时间:<%=GameAction.getFormatDifferTime(coolTime)%>，请耐心等待。<a href="wait.jsp">刷新</a><br/>您已选了武器:<%=weaps[vsUser.getWeapon()]%><br/><a href="chweap.jsp?c=1">更换武器</a><br/>开始游戏<br/>
<%}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>