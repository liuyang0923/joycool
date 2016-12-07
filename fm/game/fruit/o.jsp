<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,net.joycool.wap.util.*"%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<a href="game.jsp">返回水果战</a><br/>
<% 
OrchardBean bean = action.getOneOrchards();
if(vsUser!=null ){ // 用户不是精英
if(bean!=null){// 存在果园
	// 生产水果
	if(action.hasParam("cmd") && action.getParameterString("cmd").equals("fp")){
		if(bean.getSide()==vsUser.getSide() && vsGame.getState() == 1){// 必须是自己家族的果园，且在比赛中
			action.productFruits();
			response.sendRedirect("o.jsp?x="+request.getParameter("x")+"&y="+request.getParameter("y"));return;//防刷新
		}
	}
	// 变更丢水果比例
	if(action.hasParam("cmd") && action.getParameterString("cmd").equals("cp")){
		if(vsGame.getState() < 2){
			action.updateFamilyFruitDetails(1);
		}
	}%>
=<%=bean.getOrchardName()%>果园=<br/>
<%
FruitFamilyBean ffb = action.getFruitFamilyBean(vsUser.getSide());
int side = vsUser.getSide();
int vsSide = 3;
if(side==0){vsSide=1;}else if(side==1){vsSide=0;}
	if(bean.getSide() == vsUser.getSide()){// 自己家族的果园
	%>
		阳光总量:<%if(vsGame.getState() == VsGameBean.gameInit || vsGame.getState() == VsGameBean.gameEnd){%><%=bean.getSunCount()%><%}else{ %><%=bean.getSunCount(System.currentTimeMillis()) %><%} %>|采集率:<%=bean.getSunCaptureRate() %><br/>
		现有水果:<%=bean.getFruitCount() %><%if(vsGame.getState()==1){%><a href="throw.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>&amp;t=1">生产水果</a><%}%><br/>
		<a  href="tecupd.jsp?x=<%=request.getParameter("x")%>&amp;y=<%=request.getParameter("y") %>">水果科技研究所</a><br/>
		扔水果比例:<%=ffb.getFruitThrowProportion() %>0%<%if(vsGame.getState()<2){%><a  href="throw.jsp?t=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">变更</a><%} %><br/>
		选择目标:<br/>
		<%if(vsGame.getState()==1){
			int objective=1;
			int ownSide = 0;
			if(request.getParameter("o")!=null){
				objective = action.getParameterInt("o");
			}
			if(objective==1){
				ownSide = vsSide;
			}else if(objective==2){
				ownSide = vsUser.getSide();
			}else if(objective==3){
				ownSide = 3;
			}
			List oList = action.getOrchardLists(ownSide) ;
		%>
			<%if(objective==1){%>敌对(<%if(vsSide == 0){%><%=vsGame.getOrchardACount()%><%}else{%><%=vsGame.getOrchardBCount() %><%}%>)<%}else{%><a href="o.jsp?cmd=o&amp;o=1&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">敌对(<%if(vsSide == 0){%><%=vsGame.getOrchardACount()%><%}else{%><%=vsGame.getOrchardBCount() %><%}%>)</a><%} %>|<%if(objective==2){%>本家族(<%if(side == 0){%><%=vsGame.getOrchardACount()%><%}else{%><%=vsGame.getOrchardBCount() %><%}%>)<%}else{%><a href="o.jsp?cmd=o&amp;o=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">本家族(<%if(side == 0){%><%=vsGame.getOrchardACount()%><%}else{%><%=vsGame.getOrchardBCount() %><%}%>)</a><%} %>|<%if(objective==3){%>野生(<%=vsGame.getOrchardWCount()%>)<%}else{ %><a href="o.jsp?cmd=o&amp;o=3&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">野生(<%=vsGame.getOrchardWCount()%>)</a><%} %><br/>
		<% 
			if(oList!=null){
				if(oList.size()>0){
					for(int i=0;i<oList.size();i++){OrchardBean o=(OrchardBean)oList.get(i);%>
						<a href="throw.jsp?t=3&amp;x1=<%=request.getParameter("x")%>&amp;y1=<%=request.getParameter("y") %>&amp;x2=<%=o.getX_Point() %>&amp;y2=<%=o.getY_Point() %>"><%=o.getOrchardName() %>(<%if(objective==1){%>?<%}else{%><%=o.getFruitCount()%><%}%>)</a>|<%}%><br/><%}%>
			<%}%>
		<%}else if(vsGame.getState() == 0){ String time=vsGame.getLeftStartTime();%>
			<%=time %>后开始战斗!<br/>
		<%}else if(vsGame.getState() == 2){ %>
			挑战赛已经结束!<a href="end.jsp">查看挑战统计</a><br/>
		<%} %>
	<%}else if(bean.getSide() == vsSide || bean.getSide() == 3){%>
		水果数量:<%if(bean.getSide() == 3){%><%=bean.getFruitCount() %><%}else{ %>?<%} %>|采集率:<%=bean.getSunCaptureRate() %><br/>
		扔水果比例:<%=ffb.getFruitThrowProportion()%>0%<%if(vsGame.getState()<2){%><a  href="throw.jsp?t=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">变更</a><%} %><br/>
		从哪里扔水果:<br/>
		<%if(vsGame.getState() == 0){
			String time=vsGame.getLeftStartTime();
		%>
		<%=time %>后开始战斗!<br/>
		<%}else if(vsGame.getState() == 1){%>
		<%List oList = action.getOrchardLists(vsUser.getSide()) ;
			if(oList!=null){
				if(oList.size()>0){
					for(int i=0;i<oList.size();i++){OrchardBean o=(OrchardBean)oList.get(i);%>
						<a href="throw.jsp?t=3&amp;x2=<%=request.getParameter("x")%>&amp;y2=<%=request.getParameter("y") %>&amp;x1=<%=o.getX_Point() %>&amp;y1=<%=o.getY_Point() %>"><%=o.getOrchardName() %>(<%=o.getFruitCount()%>)</a>|<%}%><br/><%}%>
		<%}%>
		<%}else if(vsGame.getState() == VsGameBean.gameEnd){ %>
		挑战赛已经结束!<a href="end.jsp">查看挑战统计</a><br/>
		<%} %>
	<%}%>

<%}else {// 果园不存在或者果园不是他们家族 的%>
海市蜃楼出现啦，你看到的果园是梦幻效果!<br/>
<%}%>
<%}else{%>您无权限访问,来发动你自己的挑战吧!<br/><%}%>
<a href="game.jsp">返回水果战</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>