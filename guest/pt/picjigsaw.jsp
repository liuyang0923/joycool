<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@page import="net.joycool.wap.util.RandomUtil,jc.guest.*"%><%@ page import="java.util.*,jc.guest.pt.*" %><%response.setHeader("Cache-Control","no-cache"); %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
JigsawAction action=new JigsawAction(request,response);
int picID=action.getParameterInt("id");
if(picID!=0){
	boolean isExits=action.picIsExits(picID);
	if(!isExits){
		response.sendRedirect("instructions.jsp");
		return;
	}
}
JigsawBean jb=action.picInit();
String url=JigsawAction.ATTACH_URL_ROOT;
GuestHallAction gaction = new GuestHallAction(request,response);
GuestUserInfo guestUser = gaction.getGuestUser();
%>
<wml>
<card title="智慧拼图">
<p align="left">
<%
if(jb==null){%>
很抱歉,您的游币不够了,无法进行游戏.<br/>
*温馨提示:<a href="/register.jsp">注册</a>乐酷正式用户后会获得10000游币,每天上线可以多获得200游币哟~<br/>
<a href="instructions.jsp">返回智慧拼图首页</a><br/>
<%}else{
int userPreLevel = 0;
if(guestUser!=null){
userPreLevel=guestUser.getLevel();
}	
boolean isOver=action.checkEnd(jb,1);%>
<%=jb.getPicName()%><br/>
您已移动<%=jb.getPicCountMove()%>次<br/>
<%if(isOver){ %>
恭喜你完成拼图!<br/>
<%if(guestUser==null){ %>
很抱歉,您还不是注册用户,我们无法为您保存战绩哦.想保存战绩?只需两步就可以成功注册,请点<a href="/guest/nick.jsp">设置昵称</a><br/>
<%}else{
			if(userPreLevel < GuestHallAction.point.length){
			%>
				您获得1点经验,<%if(userPreLevel+1 == guestUser.getLevel()){%>恭喜您等级升为<%=guestUser.getLevel()%>级.<%}%><%if(guestUser.getLevel() < GuestHallAction.point.length ){ %> 距离提升到下一等级,还差<%=GuestHallAction.point[guestUser.getLevel()]-guestUser.getPoint()%>点经验.<%}%>每升1级,每天上线将会多领取50游币.<br/>
			<%}%>
			<%}%>
<a href="piclist.jsp">返回选择难度</a><br/>
<%}%>
<% int picShow[][]= jb.getPinPic();// 拼图显示 %>
<% 
			for(int i=0;i<picShow.length;i++){
			for(int j=0;j<picShow[0].length;j++){
				if(picShow[i][j]==0){jb.setPicNullRow(i);jb.setPicNullCol(j);session.setAttribute("jigsawBean",jb);%><%if(picShow.length>4){ %><img alt="o" src="<%=url%>pic_00.gif"/><%}else{%><img alt="o" src="<%=url%>pic_0.gif"/><%}%><%}else{String str=""+picShow[i][j];if(picShow[i][j]<10){str="0"+str;}if(jb.getPicGameState()==0){%><%if(jb.near(i, j,1)){%><a href="picjigsaw.jsp?r=<%=i%>&amp;c=<%=j%>"><img alt="o" src="<%=url%><%=jb.getPicNum()%>_<%=str%>.gif" /></a><%}else{%><img alt="o" src="<%=url%><%=jb.getPicNum()%>_<%=str%>.gif" /><%}%><%}else{%><img alt="o" src="<%=url%><%=jb.getPicNum()%>_<%=str%>.gif"/><%}}%><%}%><br/><%}%>
		查看原图:<%if(jb.getButton()==1){%><a href="picjigsaw.jsp?s=3">隐藏</a><br/>
		<img alt="o" src="<%=url%><%=jb.getPicNum()%>.gif"/><br/>
		<%}else if(jb.getButton()==0){ %> <a href="picjigsaw.jsp?s=1">显示</a><br/><%}%>
		<a href="picjigsaw.jsp?s=2">重玩本关</a><%if(guestUser!=null){%>&#160;需花费1游币<%}%><br/>
		<%if(!isOver){%><a href="piclist.jsp">返回选择难度</a><br/><%}%>
<%=JigsawAction.winString==null?"还没有人玩过一关哦,快来做第一个吧!":JigsawAction.winString%><br/>
<%}%>
<%=BaseAction.getBottomShort(request,response)%></p>
</card>
</wml>