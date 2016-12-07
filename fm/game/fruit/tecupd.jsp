<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,net.joycool.wap.util.*"%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="水果科技研究所"><p align="left">
<%OrchardBean bean = action.getOneOrchards();
if(vsUser != null){
if( bean == null ){%>
海市蜃楼出现啦，你看到的果园是梦幻效果!<br/>
<%}else{
if(action.hasParam("t")){
	int type=action.getParameterInt("t");
	int update = action.updateFamilyFruitDetails(type);
	if(update == 0){%>
		已升满,无法再次升级!<br/>
		<a href="tecupd.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y")%>">继续升级</a><br/>
	<%}else if(update == 1){%>
		阳光量不足,升级失败!<br/>
		<a href="tecupd.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y")%>">继续升级</a><br/>
	<%}else if(update == 2){%>
		升级成功!<br/>
		<a href="tecupd.jsp?x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y")%>">继续升级</a><br/>
	<%}%>	
	
<%}else{
int side=bean.getSide();
FruitFamilyBean ffb = action.getFruitFamilyBean(vsUser.getSide());
%>
<a  href="game.jsp">返回水果战</a><br/>
=<%=bean.getOrchardName()%>果园=<br/>
阳光总量:<%if(vsGame.getState() == VsGameBean.gameInit || vsGame.getState() == VsGameBean.gameEnd){%><%=bean.getSunCount()%><%}else{ %><%=bean.getSunCount(System.currentTimeMillis()) %><%} %>|采集率:<%=bean.getSunCaptureRate()%><br/>
现有水果:<%=bean.getFruitCount()%><br/>
水果科技:<br/>
<a href="tecIntro.jsp?t=1&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">尖刺果皮:</a><%if(side==3){%>0<%}else{%><%=ffb.getFruitATKGrade() %><%}%>级<%if(side!=3 && vsGame.getState() == 1){%><a href="tecupd.jsp?cmd=up&amp;t=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">升级</a><%if(ffb.getFruitATKGrade()<10){%><br/>下一等级需:阳光<%=50+25*ffb.getFruitATKGrade()%><%}%><%}%><br/>
<a href="tecIntro.jsp?t=2&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">加厚果皮:</a><%if(side==3){%>0<%}else{%><%=ffb.getFruitHP() %><%}%>级<%if(side!=3 && vsGame.getState() == 1){%><a href="tecupd.jsp?cmd=up&amp;t=3&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">升级</a><%if(ffb.getFruitHP()<10){%><br/>下一等级需:阳光<%=50+25*ffb.getFruitHP()%><%}%><%}%><br/>
<a href="tecIntro.jsp?t=3&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">果影分身:</a><%if(side==3){%>0<%}else{%><%=ffb.getFruitYield() %><%}%>级<%if(side!=3 && vsGame.getState() == 1){%><a href="tecupd.jsp?cmd=up&amp;t=5&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">升级</a><%if(ffb.getFruitYield()<10){%><br/>下一等级需:阳光<%=50+50*ffb.getFruitYield()%><%}%><%}%><br/>
<a href="tecIntro.jsp?t=4&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">喷气水果:</a><%if(side==3){%>0<%}else{%><%=ffb.getFruitSpeed() %><%}%>级<%if(side!=3 && vsGame.getState() == 1){%><a href="tecupd.jsp?cmd=up&amp;t=4&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">升级</a><%if(ffb.getFruitSpeed()<4){%><br/>下一等级需:阳光<%=100+100*ffb.getFruitSpeed() %><%}%><%}%><br/>
<a href="tecIntro.jsp?t=5&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">阳光透镜:</a><%=bean.getSunGlassGrade()%>级<%if(side!=3 && vsGame.getState() == 1){%><a href="tecupd.jsp?cmd=up&amp;t=6&amp;x=<%=request.getParameter("x") %>&amp;y=<%=request.getParameter("y") %>">升级</a><%if(bean.getSunGlassGrade()<10){%><br/>下一等级需:阳光<%=25+25*bean.getSunGlassGrade() %><%}%><%}%><br/>
<%}%>
<%}%><%}else{ %>
您没有访问权限!<br/>
<%} %>
<a href="game.jsp">返回水果战</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>