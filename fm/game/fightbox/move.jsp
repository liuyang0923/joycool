<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
//if (vsUser == null || vsUser.isDeath() || vsGame.getState() != vsGame.gameStart) {response.sendRedirect("check.jsp");return;}
String[] showName = vsGame.weapons;
Object[][] boxmap = vsGame.getShow(vsUser,true);
int countMyFm = vsGame.getCountFmA();
int countEnemyFm = vsGame.getCountFmB();
if(vsUser != null && vsUser.getSide()==1) {
	countMyFm = vsGame.getCountFmB();
	countEnemyFm = vsGame.getCountFmA();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="移动"><p align="left"><%=BaseAction.getTop(request, response)%>
请选择要查看的人:<br/><%
for (int i=0;i<boxmap.length;i++) {
	for (int j=0;j<boxmap[i].length;j++) {
		BoxShowBean boxShowBean = (BoxShowBean) boxmap[i][j];
		int weap = vsGame.checkWeap(boxShowBean, vsUser);
		if (boxShowBean!=null&&boxShowBean.getSide()>=0) {
		%><a href="boxinfo.jsp?i=<%=i%>&amp;j=<%=j%>"><%=showName[weap]%></a><%
		} else {
		%><%=showName[weap]%><%
		}
	}
	%><br/><%
}
%>
<a href="check.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>