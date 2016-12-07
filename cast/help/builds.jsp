<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%!
static int[] list1 = {8,18,22,5,23,24,10,6,7,31,35,36};
static int[] list2 = {3,9,2,1,13,14,15,16,19,11,12,40,41};
static int[] list3 = {4,17,25,21,27,20,32,30,33,37,39,38};
%><%
	
	
	CustomAction action = new CustomAction(request);
	BuildingTBean[] so = ResNeed.getBuildingTs();
	int type = action.getParameterInt("t");
	int[] list = list1;
	if(type == 1)
		list = list2;
	else if(type == 2)
		list = list3;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="建筑介绍"><p>
【建筑介绍】<br/>
<%if(type==0){%>军事<%}else{%><a href="builds.jsp">军事</a><%}%>|
<%if(type==1){%>资源<%}else{%><a href="builds.jsp?t=1">资源</a><%}%>|
<%if(type==2){%>基础<%}else{%><a href="builds.jsp?t=2">基础</a><%}%><br/>
<%for(int i=0;i < list.length;i++){
	BuildingTBean s = so[list[i]];
	if(s==null) continue;
%><%=i+1%>.<a href="build.jsp?t=<%=list[i]%>"><%=s.getName()%></a><br/>
<%}%>
<a href="help.jsp">返回详细游戏指南</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>