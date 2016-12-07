<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request, response);
action.land();
LandUserBean landUser = action.getLandUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
LandNodeBean[] nodes = (LandNodeBean[])request.getAttribute("nodes");
if(nodes[0] == null) {
	landUser.setX(0);
	landUser.setY(0);
	out.clearBuffer();
	action.redirect("map.jsp");
	return;
}
%>
<%out.write("　　");if(nodes[1]!=null){%><a href="land.jsp?d=3">↑</a><%}else{%><%}%><br/>
<%if(nodes[3]!=null){%><a href="land.jsp?d=1">←</a><%}else{out.print('　');}%>
<%=nodes[0].getName()%>
<%if(nodes[4]!=null){%><a href="land.jsp?d=2">→</a><%}else{%><%}%><br/>
<%out.write("　　");if(nodes[2]!=null){%><a href="land.jsp?d=4">↓</a><%}else{%><%}%><br/>
<%
LandItemBean landItem = nodes[0].getItem();
if(landItem!=null){%>
<a href="landPick.jsp"><%=landItem.getName()%></a><br/>
<%}%>
<a href="land.jsp">刷新</a>|<a href="help/land.jsp">??</a><br/>
<%}%>
<a href="map.jsp">离开采集场</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>