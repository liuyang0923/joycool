<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,jc.family.game.pvz.*,java.util.*,jc.util.SimpleChatLog2,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.*"%><%!
static int NUMBER_OF_PAGE = 3;%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%
// 用于辨别用户，在界面上选择是敌对家族，还是野生果园的查看列表
int own=1;
if(request.getParameter("o")!=null){
	session.setAttribute("chooseOG",request.getParameter("o"));
}
if(session.getAttribute("chooseOG")!=null){
	if(session.getAttribute("chooseOG").equals("2")){
		own=2;
	}
}
// 用于查看用户是否打开了，水果动态
int isOpen=1;
if(request.getParameter("fs")!=null){
	session.setAttribute("fruitAction",request.getParameter("fs"));
}
if(session.getAttribute("fruitAction")!=null){
	if(session.getAttribute("fruitAction").equals("2")){
		isOpen=2;
	}
}

int oid=3;
String []mark={"△","■","○","▲"};
String []abc={"A","B","C","D","E","F","G","H","I"};

int state = vsGame.getState();
int count = 0,vscount = 0;
int  vsFmSide = 0;
OrchardBean [][]areaType=vsGame.getOrchardBean();
if(state == FruitGameBean.gameInit){String time=vsGame.getLeftStartTime(); %>
水果战准备中...<%=time %><br/>
<%}else if(state == FruitGameBean.gameEnd){ %>
比赛已结束!<a href="end.jsp">查看挑战统计</a><br/>
<%} %>
<%if(vsUser!=null){

if(vsUser.getSide() == 0){count = vsGame.getOrchardACount();vscount=vsGame.getOrchardBCount();vsFmSide = 1;
}else if(vsUser.getSide() == 1){count = vsGame.getOrchardBCount();vscount = vsGame.getOrchardACount();vsFmSide = 0;
}%>
<a href="fmO.jsp"> <%if(vsUser.getSide()==0){ %>■<%}else if(vsUser.getSide()==1){%>▲<%}%>本家族(<%=count%>)</a>|<a href="info.jsp">战场详情</a>|<a href="game.jsp">刷新</a><br/>
<%List fmList=action.getOrchardLists(vsUser.getSide());
if (fmList != null && fmList.size() > 0){
	for(int i = 0;i<fmList.size();i++){
		OrchardBean o=(OrchardBean)fmList.get(i);%>
		<a href="o.jsp?x=<%=o.getX_Point()%>&amp;y=<%=o.getY_Point() %>"><%=o.getOrchardName() %>(<%=o.getFruitCount()%>)</a>|<%}%><br/><%
}%>
<%if(own == 2){%><a href="game.jsp?cmd=o&amp;o=1">敌对(<%=vscount %>)</a><%}else{%>敌对(<%=vscount %>)<%}%>|<%if(own == 1){%><a href="game.jsp?cmd=o&amp;o=2">野生(<%=vsGame.getOrchardWCount()%>)</a><%}else{%>野生(<%=vsGame.getOrchardWCount()%>)<%}%><br/>
<%if(own==1){oid=vsFmSide;}
List oList=action.getOrchardLists(oid); 
	if(oList!=null){
		if(oList.size()>0){
		for(int i=0;i<oList.size();i++){ OrchardBean o=(OrchardBean)oList.get(i);%>
		<a href="o.jsp?x=<%=o.getX_Point()%>&amp;y=<%=o.getY_Point() %>"><%=o.getOrchardName() %>(<%if(own==1){%>?<%}else{%><%=o.getFruitCount()%><%}%>)</a>|<%}%><br/>
		<%}%>
	<%}
}else{ %>
<a href="game.jsp">刷新</a><br/>
<!-- 
<a href="fmO.jsp?s=0">挑战方■:<%=vsGame.getFmANameWml() %>家族</a><br/>
<a href="fmO.jsp?s=1" >接受方▲:<%=vsGame.getFmBNameWml() %>家族</a><br/>
-->
■:<%=vsGame.getFmANameWml() %>家族vs▲:<%=vsGame.getFmBNameWml() %>家族<br/>
<a href="info.jsp">战场详情</a><br/>
<%}
// 显示地图
for(int i=0;i<areaType.length;i++){
for(int j=0;j<areaType[1].length;j++){
if(areaType[i][j]==null){%>
<%=mark[2]%>
<%}else if(areaType[i][j].getSide()==3){%>
<%=mark[0]%>
<%}else if(areaType[i][j].getSide()==0){%>
<%=mark[1]%>
<%}else if(areaType[i][j].getSide()==1){%>
<%=mark[3]%><%}%><%}%>|<%=i+1 %><br/><%}%><%for(int i=0;i<abc.length;i++){%><%=abc[i]%><%if(i<abc.length-1){%>|<%}}%><br/>

水果动态:<%if(isOpen == 1){ %> <a href="game.jsp?fs=2">隐藏</a><%}else if(isOpen == 2){%><a href="game.jsp?fs=1">显示</a><%} %>|<a href="aMore.jsp">更多</a><br/>
<% if(isOpen==1){%>
<% List actionList=action.getSubList2(vsGame.getLogList(),NUMBER_OF_PAGE,"","",false); 
int pageNum=actionList.size();
if(actionList.size()>3){pageNum=3;}
if(actionList!=null){
if(actionList.size()>0){
int tmp = 0;
for(int i=0;i<pageNum;i++){
%><%=i+1%>.<%=actionList.get(i)%><br/>
<%}%><%}}%>
<%}%>
<a href="fmO.jsp?cmd=s">挑战双方信息</a><br/>
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