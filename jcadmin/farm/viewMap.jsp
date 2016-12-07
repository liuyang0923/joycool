<%@ page contentType="text/html;charset=utf-8"%><%!static int startX=20,startY=-200;%>
<%@include file="top.jsp"%><%!
static String addStyle="style=\"background-color:#88FF88;width:20;height:20;float:left;\"";
static String delStyle="style=\"background-color:#FF8888;width:20;height:20;float:left;\"";
static String delErrStyle="style=\"background-color:#8888FF;width:20;height:20;float:left;\"";
static String addClick="onclick=\"return confirm('确认添加连接？')\"";
static String delClick="onclick=\"return confirm('确认删除连接？')\"";
static HashMap borderColors = new HashMap();
static{
borderColors.put(new Integer(0), "#DDDD00");
borderColors.put(new Integer(1), "red");
borderColors.put(new Integer(2), "green");
borderColors.put(new Integer(3), "blue");
borderColors.put(new Integer(4), "orange");
borderColors.put(new Integer(5), "green");
borderColors.put(new Integer(6), "orange");
borderColors.put(new Integer(7), "blue");
borderColors.put(new Integer(8), "red");
borderColors.put(new Integer(9), "#FF00FF");
borderColors.put(new Integer(10), "DDDD00");
borderColors.put(new Integer(11), "orange");
borderColors.put(new Integer(14), "blue");
borderColors.put(new Integer(17), "red");
borderColors.put(new Integer(20), "#FF00FF");
borderColors.put(new Integer(26), "DDDD00");
borderColors.put(new Integer(47), "FF00FF");
borderColors.put(new Integer(48), "FF00FF");
borderColors.put(new Integer(49), "FF00FF");
borderColors.put(new Integer(35), "red");
borderColors.put(new Integer(38), "green");
borderColors.put(new Integer(41), "#642614");
borderColors.put(new Integer(45), "#0000AA");
borderColors.put(new Integer(50), "green");
borderColors.put(new Integer(54), "red");
borderColors.put(new Integer(57), "blue");
borderColors.put(new Integer(60), "yellow");
borderColors.put(new Integer(63), "orange");
}
%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");
HashSet distribute = (HashSet)session.getAttribute("distribute");	// 分布区域特殊标志
if(distribute==null)
	distribute = new HashSet();
MapNodeBean mapNode = world.getMapNode(id);
MapBean map = world.getMap(mapNode.getMapId());
	if (null != request.getParameter("add")) {
		String name = (String)request.getParameter("name").trim();
		String info = (String)request.getParameter("info").trim();
		String link = (String)request.getParameter("link").trim();
		if (!name.equals("")) {
		mapNode.setName(name);
		mapNode.setMapId(action.getParameterInt("mapId"));
		mapNode.setInfo(info);
		mapNode.setLink(link);
		mapNode.initLink(world);
		mapNode.setExp(action.getParameterInt("exp"));
        world.updateMapNode(mapNode);
        if(!"确定".equals(request.getParameter("submit")))
            response.sendRedirect("farmMapNode.jsp");
        else
        	response.sendRedirect("editMapNode.jsp?id="+id);
		} else {%>
    <script>
	alert("请填写正确各项参数！");
	</script>
        <%}}%>
<html>
	<head>
	</head>
	<link href="common.css" rel="stylesheet" type="text/css">
	<body style="font-size:12px;">
<%
MapNodeBean[][] nodess = world.getRoundNode(mapNode,500);
for(int x = 0;x < nodess.length;x++) {
	MapNodeBean[] nodes = nodess[x];
	for(int y = 0;y < nodes.length;y++) {
		MapNodeBean nc = nodes[y];
		if(nc == null) continue;
		MapNodeBean[] links = nc.getLinks();
		String borderColor=(String)borderColors.get(Integer.valueOf(nc.getMapId()));
		if(borderColor==null)borderColor="gray";
		if(distribute.contains(Integer.valueOf(nc.getId())))borderColor = "solid " + borderColor + " 1px;background-color:#FFCCFF;";else borderColor = "solid " + borderColor + " 1px";

		%>
<div style="line-height:100%;position:absolute;z-index:-1;left:<%=x*82+startX%>;top:<%=y*42+200+startY%>;width:70;height:33;border:<%=borderColor%>;padding-top:2px;" align="center">
<%if(nc==mapNode){ 
MapNodeBean to = null;int dir = 0;
%>
<font color="red"><b><%=nc.getName()%><%if(nc.getObjs().size()>0){%>@<%}%><%if(nc.getPlayers().size()>0){%>#<%}%></b><br/>[<%=nc.getId()%>]</font>
<div style="position:absolute;z-index:-2;overflow:hidden;left:27;top:-13;width:15;height:15;">
	<% if(y>0){to = nodess[x][y-1]; dir=1;%>
	<%if(links[dir]!=null){%>
		<%if(to==links[dir]){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delStyle%> <%=delClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delErrStyle%> <%=delClick%>></a>
		<%}%>
	<%}else{%>
		<%if(to!=null){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=2&id=<%=to.getId()%>" <%=addStyle%> <%=addClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>" <%=addStyle%>></a>
		<%}%>
	<%}}%>
</div>
<div style="position:absolute;z-index:-2;overflow:hidden;left:27;top:29;width:15;height:15;">
	<% if(y<=nodess[0].length-2){to = nodess[x][y+1]; dir=7;%>
	<%if(links[dir]!=null){%>
		<%if(to==links[dir]){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delStyle%> <%=delClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delErrStyle%> <%=delClick%>></a>
		<%}%>
	<%}else{%>
		<%if(to!=null){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=2&id=<%=to.getId()%>" <%=addStyle%> <%=addClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>" <%=addStyle%>></a>
		<%}%>
	<%}}%>
</div>
<div style="position:absolute;z-index:-2;overflow:hidden;left:-14;top:8;width:15;height:15;">
	<% if(x>0){to = nodess[x-1][y]; dir=3;%>
	<%if(links[dir]!=null){%>
		<%if(to==links[dir]){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delStyle%> <%=delClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delErrStyle%> <%=delClick%>></a>
		<%}%>
	<%}else{%>
		<%if(to!=null){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=2&id=<%=to.getId()%>" <%=addStyle%> <%=addClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>" <%=addStyle%>></a>
		<%}%>
	<%}}%>
</div>
<div style="position:absolute;z-index:-2;overflow:hidden;left:66;top:8;width:15;height:15;">
	<% if(x<=nodess.length-2){to = nodess[x+1][y]; dir=5;%>
	<%if(links[dir]!=null){%>
		<%if(to==links[dir]){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delStyle%> <%=delClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=3&id=<%=to.getId()%>" <%=delErrStyle%> <%=delClick%>></a>
		<%}%>
	<%}else{%>
		<%if(to!=null){%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>&add=2&id=<%=to.getId()%>" <%=addStyle%> <%=addClick%>></a>
		<%}else{%>
			<a href="addMapNode.jsp?from=<%=nc.getId()%>&dir=<%=dir%>" <%=addStyle%>></a>
		<%}%>
	<%}}%>
</div>
<%}else{%>
<a title="<%=nc.getInfo()%>" href="editMapNode.jsp?id=<%=nc.getId()%>"><%=nc.getName()%></a><font color=red><b><%if(nc.getObjs().size()>0){%>@<%}%><%if(nc.getPlayers().size()>0){%>#<%}%></b></font><br/>[<%=nc.getId()%>]
<%}%>
</div>
<%if(links[1]!=null){%>
	<div style="position:absolute;overflow:hidden;left:<%=x*82+39+startX%>;top:<%=y*42+200-10+startY%>;width:3;height:11;border:solid gray 1px;"></div>
<%}%>
<%if(links[3]!=null){%>
	<div style="position:absolute;overflow:hidden;left:<%=x*82-13+startX%>;top:<%=y*42+200+20+startY%>;width:14;height:3;border:solid gray 1px;"></div>
<%}%>
<%if(links[5]!=null){%>
	<div style="position:absolute;overflow:hidden;left:<%=x*82-13+82+startX%>;top:<%=y*42+200+11+startY%>;width:14;height:3;border:solid gray 1px;"></div>
<%}%>
<%if(links[7]!=null){%>
	<div style="position:absolute;overflow:hidden;left:<%=x*82+29+startX%>;top:<%=y*42+200-10+42+startY%>;width:3;height:11;border:solid gray 1px;"></div>
<%}%>
<%}}%>
	</body>
</html>