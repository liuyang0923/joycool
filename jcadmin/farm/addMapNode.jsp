<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
FarmWorld world = FarmWorld.getWorld();
CustomAction action = new CustomAction(request, response);
int id = action.getParameterIntS("id");
int from = action.getParameterIntS("from");		// 从哪里来
int dir = action.getParameterIntS("dir");		// 方向
int idir = 8-dir;	//反方向
if(action.hasParam("clearNext")){
	session.removeAttribute("lastnode");
}
String lastnode = "";
if(id<0){
	int nextId = from;
	if(session.getAttribute("lastnode")!=null)
		nextId = ((Integer)session.getAttribute("lastnode")).intValue()+1;

	while(world.getMapNode(nextId)!=null)
		nextId++;
	lastnode = String.valueOf(nextId);

}
MapNodeBean fromNode = world.getMapNode(from);
if(fromNode==null||dir<0){
	response.sendRedirect("farmMapNode.jsp");
	return;
}
MapNodeBean toNode = null;
if ("1".equals(request.getParameter("add"))) {
	if(world.getMapNode(id)==null){
		String name = request.getParameter("name").trim();
		String info = request.getParameter("info").trim();
		if (!name.equals("")) {
			toNode = new MapNodeBean();
			toNode.setId(id);
			toNode.setName(name);
			toNode.setInfo(info);
			toNode.addLink(idir,from);
			toNode.initLink(world);
			toNode.setMapId(action.getParameterInt("mapId"));
			world.addMapNode(toNode);
			fromNode.addLink(dir,toNode.getId());
			fromNode.initLink(world);
			world.updateMapNode(fromNode);
		    session.setAttribute("lastnode",Integer.valueOf(id));
		}
		response.sendRedirect("editMapNode.jsp?id="+fromNode.getId());
		return;
	} else {
		session.setAttribute("addMapTip", "非法操作！输入的ID重复！");
		response.sendRedirect("addMapNode.jsp?from="+from+"&dir="+dir);
		return;
    }
} else if ("2".equals(request.getParameter("add"))) {
	toNode = world.getMapNode(id);
	toNode.addLink(idir,from);
	toNode.initLink(world);
	world.updateMapNode(toNode);
	fromNode.addLink(dir, toNode.getId());
	fromNode.initLink(world);
	world.updateMapNode(fromNode);
    response.sendRedirect("editMapNode.jsp?id="+fromNode.getId());
    return;
} else if ("3".equals(request.getParameter("add"))) {	// 删除
	toNode = world.getMapNode(id);
	toNode.delLink(idir);
	toNode.initLink(world);
	world.updateMapNode(toNode);
	fromNode.delLink(dir);
	fromNode.initLink(world);
	world.updateMapNode(fromNode);
    response.sendRedirect("editMapNode.jsp?id="+fromNode.getId());
    return;
}

%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		
		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
<% String tip = (String)session.getAttribute("addMapTip");
if(tip != null) {
session.removeAttribute("addMapTip");
%><br/><font color=red size=5><b><%=tip%></b></font><%
}%>
<br />
		<form method="post" action="addMapNode.jsp?add=2&from=<%=from%>&dir=<%=dir%>">
		    id：
			<input id="id" name="id">
			<input type="submit" value="确定">
			<br />
		</form>
		<br />
		<hr>
		<form method="post" action="addMapNode.jsp?add=1&from=<%=from%>&dir=<%=dir%>">
		    id：
			<input id="id" name="id" value="<%=lastnode%>">&nbsp;
			名称：
			<input id="name" name="name"><br/>
			内容：
			 <textarea id="info" name="info"cols="60" rows="2"></textarea><br/>
			 所属地图：
			 <input id="mapId" name="mapId" value="<%=fromNode.getMapId()%>"><br/>
			 连接结点:<%=fromNode.getName()%>(<%=fromNode.getId()%>)<br/>
			<input type="submit" value="确定">
			<br />
		</form>
		<br />
		<hr>
		<form method="post" action="addMapNode.jsp?add=1&from=<%=from%>&dir=<%=dir%>">
		    id：
			<input id="id" name="id" value="<%=lastnode%>">&nbsp;
			名称：
			<input id="name" name="name" value="<%=fromNode.getName()%>"><br/>
			内容：
			 <textarea id="info" name="info"cols="60" rows="2"><%=fromNode.getInfo()%></textarea><br/>
			 所属地图：
			 <input id="mapId" name="mapId" value="<%=fromNode.getMapId()%>"><br/>
			 连接结点:<%=fromNode.getName()%>(<%=fromNode.getId()%>)<br/>
			<input type="submit" value="确定">
			<br />
		</form>
		<br/>
		<br/>
		<a href="addMapNode.jsp?clearNext=1&from=<%=from%>&dir=<%=dir%>">重置结点id序列</a>
	</body>
</html>
