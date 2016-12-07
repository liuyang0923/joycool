<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%!
static int[] list1 = {8,18,22,5,23,24,10,6,7,26,31,34,37,35,36,26,28,29,42,43};
static int[] list2 = {3,9,2,1,13,14,15,16,19,11,12,40,41};
static int[] list3 = {4,17,25,21,27,20,32,30,33,39,38};
%><%
	response.setHeader("Cache-Control","no-cache");
	
	CustomAction action = new CustomAction(request);
	BuildingTBean[] so = ResNeed.getBuildingTs();
	int type = action.getParameterInt("t");
	int[] list = list1;
	if(type == 1)
		list = list2;
	else if(type == 2)
		list = list3;
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
建筑介绍<br/>
<%if(type==0){%>军事<%}else{%><a href="builds.jsp">军事</a><%}%>|
<%if(type==1){%>资源<%}else{%><a href="builds.jsp?t=1">资源</a><%}%>|
<%if(type==2){%>基础<%}else{%><a href="builds.jsp?t=2">基础</a><%}%><br/>
<%for(int i=0;i < list.length;i++){
	BuildingTBean s = so[list[i]];
	if(s==null) continue;
%><%=i+1%>.<a href="build.jsp?t=<%=list[i]%>"><%=s.getName()%></a><br/>
<%}%>
<br/>
<%@include file="bottom.jsp"%>
</html>