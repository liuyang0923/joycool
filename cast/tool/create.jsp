<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	int dis = action.getParameterInt("dis");
	if(dis<3) dis=15;
	int[][] map = CastleUtil.getMap();
	int[] sideCount = CastleUtil.sideCount;
	int x = action.getParameterInt("x");
	if(x==0)	x=200;
	int y = action.getParameterInt("y");
	if(y==0)	y=200;
	int add = action.getParameterInt("add");
	CastleBean castle = action.getCastle();
	for(int i=0;i<add;i++)
		CastleUtil.randomCastle(castle);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="周围玩家"><p>
<%
for(int j=y-dis;j <= y+dis;j++){
 for(int i=x-dis;i <= x+dis;i++) if(map[i][j]!=0){if(map[i][j]!=castle.getId()){
%>●<%}else{%><font color="red">●</font><%}%><%}else{%>○<%}%>|<%if(j==y-dis||j==y+dis){%><a href="create.jsp?x=<%=x%>&amp;y=<%=j%>"><%=j%></a><%}else{%><%=j%><%}%><br/><%
}%>
<a href="create.jsp?x=<%=x-dis%>&amp;y=<%=y%>"><%=x-dis%></a>-<%=x%>-<a href="create.jsp?x=<%=x+dis%>&amp;y=<%=y%>"><%=x+dis%></a><br/>
<%for(int i = 1;i < 50;i++){%><%=i%>-<font color="red"><%=sideCount[i]%></font>,<%}%><br/>
<a href="create.jsp?add=1">随机产生+1</a><br/>
<a href="create.jsp?add=5">随机产生+5</a><br/>
<a href="create.jsp?add=20">随机产生+20</a><br/>
<a href="create.jsp?add=100">随机产生+100</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>