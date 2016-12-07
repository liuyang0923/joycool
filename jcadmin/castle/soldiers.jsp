<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	
	CustomAction action = new CustomAction(request);
	int type = action.getParameterInt("t")+1;
	SoldierResBean[] so = ResNeed.getSoldierTs(type);
%><html>
	<head>
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body>
兵种介绍<br/>
<%if(type==1){%>拜索斯<%}else{%><a href="soldiers.jsp">拜索斯</a><%}%>|
<%if(type==2){%>伊斯<%}else{%><a href="soldiers.jsp?t=1">伊斯</a><%}%>|
<%if(type==3){%>杰彭<%}else{%><a href="soldiers.jsp?t=2">杰彭</a><%}%>|
<%if(type==4){%>自然界<%}else{%><a href="soldiers.jsp?t=3">自然界</a><%}%>|
<%if(type==5){%>纳塔<%}else{%><a href="soldiers.jsp?t=4">纳塔</a><%}%><br/>
<%=ResNeed.getRaceInfo(type)%><br/>
<%for(int i=1;i < so.length;i++){
	SoldierResBean s = so[i];
	if(s==null) continue;
	int imgId = s.getId();
	if(imgId%10==4||imgId%10==5) imgId++; else if(imgId%10==6) imgId -=2;
%><%=i%>.<img class="unit u<%=imgId%>" src="img/blank.gif">&emsp;<a href="soldier.jsp?t=<%=i%>&r=<%=type%>"><%=s.getSoldierName()%></a><br/>
<%}%>
<br/>
<%@include file="bottom.jsp"%>
</html>