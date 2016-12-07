<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="jc.util.SimpleChatLog2"%><%@ page import="jc.family.game.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
String[] showName = vsGame.weapons;
if (action.hasParam("h")){
	if ("1".equals(action.getParameterString("h")))
		vsUser.setHidden(true);
	else if ("2".equals(action.getParameterString("h")))
		vsUser.setHidden(false);
}
LinkedList infoList = vsGame.getInformationList();
Object[][] show = vsGame.getShow(vsUser,true);
int countMyFm = vsGame.getCountFmA();
int countEnemyFm = vsGame.getCountFmB();
String friends = "剑枪弓";
String enemys = "斧矛弩";
if (vsUser != null && vsUser.getSide() == 1) {
	countMyFm = vsGame.getCountFmB();
	countEnemyFm = vsGame.getCountFmA();
}
boolean canFresh = true;
if(vsGame.getState() == vsGame.gameEnd)canFresh = false;

int timeRemain = (int)(vsGame.getLastRoundTime() - System.currentTimeMillis())/1000 + 20;
if(timeRemain<0)timeRemain=0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(vsGame.getState()==2){%><a href="over.jsp">比赛已结束</a><br/><%} else if (vsUser != null && vsUser.getBlood() == 0) {%><a href="die.jsp">您已经阵亡</a><br/><%}%>
第<%=vsGame.getRound()%>回合<%if (vsGame.isStart()) {%>(剩余<%=timeRemain%>秒)<%}%><br/>
<%if (vsUser != null) {
	if (!vsUser.isDeath()) {
	%>[<%=showName[vsUser.getWeapon()]%>]生命<%=vsUser.getBlood()%><br/><% 
	}
	%><%=countMyFm%>友:<%=friends%>,<%=countEnemyFm%>敌:<%=enemys%><br/><% 
} else {
%><%=countMyFm%>攻:<%=friends%>,<%=countEnemyFm%>守:<%=enemys%><br/><% 
}
%>战斗讯息:<% 
if (vsUser != null) {if (vsUser.isHidden()){%><a href="check.jsp?h=2">显示</a>|<%} else {%><a href="check.jsp?h=1">隐藏</a>|<%}
}%><a href="fgtinfo.jsp">全部</a><br/><%
if (infoList.size() > 0 && (vsUser == null || !vsUser.isHidden())){
	Iterator iter = infoList.iterator();
	for (int i = 0; i < 3;i++){
		if (!iter.hasNext()) break;
		FightInformationBean fib = (FightInformationBean) iter.next();
		%><%=i+1%>.<%=fib.getInformation()%><br/><%
	}
} %><%
if (canFresh){%><a href="check.jsp">刷新</a><%}else{%>刷新<%}%>|<%if(vsGame.getState()!=2){%><a href="move.jsp">查看</a><%} else {%>查看<%}%>|<a href="chat.jsp">聊天</a><br/>
<%
if (vsUser != null && !vsUser.isDeath()) {
	if (vsUser.getMapState() == 3) {
	%>本回合已下达指令<br/><%
	} else {
	%>请选择要到达的位置:<br/><%
	}
}
for (int i=0;i<show.length;i++) {
	for (int j=0;j<show[i].length;j++) {
		boolean canWalk = false;
		if (vsUser != null && !vsUser.isDeath() && canFresh) {
			if (Math.abs(vsUser.getCurrI() - i) + Math.abs(vsUser.getCurrJ() - j) < 3) {
				canWalk = true;
			}
		}
		BoxShowBean boxShowBean = (BoxShowBean) show[i][j];
		int weap = vsGame.checkWeap(boxShowBean, vsUser);
		if (canWalk) {
		%><a href="attack.jsp?aimI=<%=i%>&amp;aimJ=<%=j%>"><%=showName[weap]%></a><%
		} else {
		%><%=showName[weap]%><%
		}
	}
	%><br/><%
}
if (canFresh){%><a href="check.jsp">刷新</a><%}else{%>刷新<%}%>|<%if(vsGame.getState()!=2){%><a href="move.jsp">查看</a><%} else {%>查看<%}%>|<a href="chat.jsp">聊天</a><br/>
<a href="tows.jsp">查看对战双方</a><br/>
<%if(vsUser==null){%>
<%=vsGame.getChat().getChatString(0, 3)%><a href="chat2.jsp">更多聊天信息</a><br/>
<%}else{%>
聊天信息&gt;&gt;家族|<a href="chat2.jsp">公共</a><br/>
<%=vsGame.getChat(vsUser.getSide()).getChatString(0, 3)%>
<input name="fchat"  maxlength="100"/>
<anchor>发言<go href="chat.jsp" method="post"><postfield name="content" value="$fchat"/></go></anchor><br/>
<a href="chat.jsp">更多聊天信息</a><br/>
<%}%>
<a href="/fm/index.jsp">&lt;&lt;返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>