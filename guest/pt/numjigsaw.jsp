<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@page import="net.joycool.wap.util.RandomUtil,jc.guest.*"%><%@ page import="java.util.*,jc.guest.pt.*" %>
<%response.setHeader("Cache-Control","no-cache"); %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<wml>
<card title="智慧拼图">
<p align="left">

<%
response.setHeader("Cache-Control","no-cache");
GuestHallAction gaction = new GuestHallAction(request,response);
GuestUserInfo guestUser = gaction.getGuestUser();
JigsawAction action=new JigsawAction(request,response);

JigsawBean jb=action.numInit();
if(request.getParameter("s") != null && request.getParameter("s").equals("5")){
	response.sendRedirect("instructions.jsp");return ;
}
if(jb==null){%>
很抱歉,您的游币不够了,无法进行游戏.<br/>
*温馨提示:<a href="/register.jsp">注册</a>乐酷正式用户后会获得10000游币,每天上线可以多获得200游币哟~<br/>
<a href="instructions.jsp">返回智慧拼图首页</a><br/>
<%}else{
	if(request.getParameter("c")!=null&&"y".equals(request.getParameter("c"))){%>
		放弃闯关后,当前闯关记录将被清空,您确定要放弃么?<br/>
		<a href="numjigsaw.jsp?s=5">确定</a>&#160;<a href="numjigsaw.jsp">返回</a><br/>
	<%}else{
		int userPreLevel = 0;
		if(guestUser!=null){
			userPreLevel=guestUser.getLevel();
		}
		boolean level=action.checkEnd(jb,0);
		JigsawUserBean jub=action.getUserDetails();
		
		int score = 2;
		if(jb.getNumGameLevel() > 3 && jb.getNumGameLevel() < 7){
			score = 3;
		} else if (jb.getNumGameLevel() > 6){
			score = 5;
		}
	%>
		第<%=jb.getNumGameLevel()%>关<br/>
		<% if(level){ if(jb.getNumGameLevel()==9){%>
			恭喜您完成了全部关卡,获得通关积分:<%=score%>.<br/>
			<%}else{ %>
			恭喜您完成第<%=jb.getNumGameLevel()%>关,获得奖励积分:<%=score%>.<br/>
			<%}%>
			<%if(guestUser==null){ %>
			很抱歉,您还不是注册用户,我们无法为您保存战绩哦.想保存战绩?只需两步就可以成功注册,请点<a href="/guest/nick.jsp">设置昵称</a><br/>
			<%}else if(jub!=null){
				if(userPreLevel < GuestHallAction.point.length){
			%>
				您获得1点经验,<%if(userPreLevel+1 == guestUser.getLevel()){%>恭喜您等级升为<%=guestUser.getLevel()%>级.<%}%><%if(guestUser.getLevel()< GuestHallAction.point.length ){ %>距离提升到下一等级,还差<%=GuestHallAction.point[guestUser.getLevel()]-guestUser.getPoint()%>点经验.<%}%>每升1级,每天上线将会多领取50游币.<br/>
			<%}%>
			<%}%>
			<%if(jb.getNumGameLevel()<9){%><a href="numjigsaw.jsp?s=2">开始下一关</a><%if(guestUser!=null){%>&#160;需花费1游币<%}%><br/><%}%>
			<%if(jb.getNumGameLevel()==9){%><a href="numjigsaw.jsp?s=6">重新闯关</a><%if(guestUser!=null){%>&#160;需花费1游币<%}%><br/><%}%>
		<%}else{%>
			您已移动<%=jb.getNumCountMove()%>次<br/>
			<% int picShow[][]= jb.getPinNum();// 拼图显示 %>
			<% 
				for(int i=0;i<picShow.length;i++){
				for(int j=0;j<picShow[0].length;j++){
					
					if(picShow[i][j]==0){
					jb.setNumNullRow(i);
					jb.setNumNullCol(j);
					session.setAttribute("jigsawBean",jb);
					%>空&#160;<%
					}else{
					if(jb.getNumGameState()==0){%>
						<%if(jb.near(i, j,0)){%><a href="numjigsaw.jsp?r=<%=i%>&amp;c=<%=j%>"><%=picShow[i][j]<10?"0"+picShow[i][j]:picShow[i][j]+""%></a>&#160;<%}else{%><%=picShow[i][j]<10?"0"+picShow[i][j]:picShow[i][j]+""%>&#160;<%}%>
						<%}else{%><%=picShow[i][j]<10?"0"+picShow[i][j]:picShow[i][j]+""%>&#160;
				<%}}%>
			<%}%><br/>
			<%}%>
			<a href="numjigsaw.jsp?s=2">重玩本关</a><%if(guestUser!=null){%>&#160;需花费1游币<%}%><br/>
			<a href="numjigsaw.jsp?c=y">放弃闯关</a><br/>
		<%}%>
<a href="instructions.jsp">返回智慧拼图首页</a><br/>	
<%=JigsawAction.winString==null?"还没有人玩过一关哦,快来做第一个吧!":JigsawAction.winString%><br/>
<%} %>
<%} %>
<%=BaseAction.getBottomShort(request,response)%></p>
</card>
</wml>