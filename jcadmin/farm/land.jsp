<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");
LandMapBean land = world.getLand(id);
if(action.hasParam("grow"))
world.processLand(land);
%>
			<html>
	<head>
	</head>
	采集场<br/><br/>
	<body>
	
<%if(land!=null){%>

<%for(int y=0;y<land.getHeight();y++) {
for(int x=0;x<land.getWidth();x++) {
LandNodeBean node = land.getNode(x,y);
if(node.hasItem())
out.write("●");
else
out.write("○");
}%><br/>
<%}%><br/>

<a href="land.jsp?grow=1&amp;id=<%=id%>">生长</a>
<%}%>
<br/>
<a href="index.jsp">返回</a><br/>
	</body>
</html>