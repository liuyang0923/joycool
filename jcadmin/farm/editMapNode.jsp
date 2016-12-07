<%@ page contentType="text/html;charset=utf-8"%><%!static int startX=20,startY=10;%>
<%@include file="top.jsp"%><%!
static String addStyle="style=\"background-color:#88FF88;width:20;height:20;float:left;\"";
static String delStyle="style=\"background-color:#FF8888;width:20;height:20;float:left;\"";
static String delErrStyle="style=\"background-color:#8888FF;width:20;height:20;float:left;\"";
static String addClick="onclick=\"return confirm('确认添加连接？')\"";
static String delClick="onclick=\"return confirm('确认删除连接？')\"";
static HashMap borderColors = new HashMap();
static{
borderColors.put(new Integer(0), "yellow");
borderColors.put(new Integer(1), "red");
borderColors.put(new Integer(2), "green");
borderColors.put(new Integer(3), "blue");
borderColors.put(new Integer(4), "orange");
borderColors.put(new Integer(5), "green");
borderColors.put(new Integer(6), "orange");
borderColors.put(new Integer(7), "blue");
borderColors.put(new Integer(8), "red");
borderColors.put(new Integer(9), "purple");
borderColors.put(new Integer(10), "yellow");
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
String distributeParam = request.getParameter("distribute");

if(distributeParam!=null){
 HashSet distribute2 = new HashSet();
 distribute2.addAll(StringUtil.toInts2(distributeParam));
 session.setAttribute("distribute",distribute2);
}
HashSet distribute = (HashSet)session.getAttribute("distribute");	// 分布区域特殊标志
if(distribute==null)
	distribute = new HashSet();
FarmWorld world = FarmWorld.getWorld();	
int id = action.getParameterInt("id");
if(request.getParameter("calc")!=null){
world.generateMapCoord();
response.sendRedirect("editMapNode.jsp?id="+id);
return;
}
MapNodeBean mapNode = world.getMapNode(id);
if(request.getParameter("clear")!=null){
mapNode.getObjs().clear();
response.sendRedirect("editMapNode.jsp?id="+id);
return;
}
int rp = action.getParameterIntS("rp");
if(rp>=0) {
	mapNode.getPlayers().remove(rp);
	response.sendRedirect("editMapNode.jsp?id="+id);
}
int ro = action.getParameterIntS("ro");
if(ro>=0) {
	mapNode.getObjs().remove(ro);
	response.sendRedirect("editMapNode.jsp?id="+id);
}

MapBean map = world.getMap(mapNode.getMapId());
if(map==null)map=new MapBean();
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
<style type="text/css">
a{text-decoration:none;}
.gv{position:absolute;overflow:hidden;width:3;height:11;border:solid gray 1px;}
.gh{position:absolute;overflow:hidden;width:14;height:3;border:solid gray 1px;}
.gr{line-height:100%;position:absolute;z-index:-1;width:70;height:33;padding-top:2px;text-align:center;}
</style>
	<link href="common.css" rel="stylesheet" type="text/css">
	</head>
	<body style="font-size:12px;">
<form method="post" action="editMapNode.jsp?add=1&id=<%=id%>">
<table width="100%">

	<tr>
		<td>
			id
		</td>
		<td>
	<%=mapNode.getId()%>
	</td><td rowspan="5" width=100 align=center>
<a href="editMap.jsp?id=<%=map.getId()%>"><img src="/farm/img/map/<%=map.getId()%>.gif" height="128px" border=0></a>
	</td>
	<td rowspan="5" width=150 valign=top style="line-height:130%;padding:5;">
<a href="editMapNode.jsp?clear=1&amp;id=<%=mapNode.getId()%>" onclick="return confirm('确认全部删除?')">清除本结点所有物体</a><br/>

<% List objList = mapNode.getObjs();
for(int i = 0;i < objList.size();i++){
Object obj = objList.get(i);%>

<%if(obj instanceof FarmNpcBean){
FarmNpcBean npc = (FarmNpcBean)obj;
%>
N<a href="editNpc.jsp?id=<%=npc.getId()%>"><%=npc.getName()%></a>-
<a href="editMapNode.jsp?id=<%=id%>&ro=<%=i%>" onclick="return confirm('确认删除?')">删</a><br/>

<%}else if(obj instanceof MapDataBean){
MapDataBean data = (MapDataBean)obj;
%>
<%=data.getEditLink(response)%><%if(!data.isVisible()){%>(隐)<%}%>-
<a href="editMapNode.jsp?id=<%=id%>&ro=<%=i%>" onclick="return confirm('确认删除?')">删</a><br/>

<%}%>
<%}%>

<% objList = mapNode.getPlayers();
for(int i = 0;i < objList.size();i++){
Object obj = objList.get(i);%>

<%if(obj instanceof FarmUserBean){
FarmUserBean npc = (FarmUserBean)obj;
%>
<a href="viewUser.jsp?id=<%=npc.getUserId()%>"><%=npc.getNameWml()%></a>-
<a href="editMapNode.jsp?id=<%=id%>&rp=<%=i%>" onclick="return confirm('确认删除?')">删</a><br/>
<%}else if(obj instanceof MapDataBean){
MapDataBean data = (MapDataBean)obj;
%>
<%=data.getEditLink(response)%><br/>
<%}%>
<%}%>

	
	</td>
	</tr>
	<tr>
		<td>
			名称
		</td>
		<td>
	<input type=text name="name" size="20" value="<%=mapNode.getName()%>">
	</td></tr>
		<tr>
		<td>
			说明
		</td>
		<td>
	<textarea name="info" cols="60" rows="2"><%=mapNode.getInfo()%></textarea>
	</td></tr>
		<tr>
		<td>
			所属地图
		</td>
		<td>
	<input type=text name="mapId" size="20" value="<%=mapNode.getMapId()%>"><a href="editMap.jsp?id=<%=map.getId()%>">&nbsp;<%=map.getName()%></a>&nbsp;坐标&nbsp;<%=mapNode.x%>，<%=mapNode.y%>
	<a href="editMapNode.jsp?id=<%=id%>&calc=1">重新计算全地图坐标</a>
	</td></tr>
		<tr>
		<td>
			位置链接
		</td>
		<td>
	<input type=text name="link" size="20" value="<%=mapNode.getLink()%>">
	</td></tr>
</table>
	<input type="submit" name="submit" value="确定">&nbsp;<input type="submit" value="确定并返回">
<a href="farmMapNode.jsp">返回上一级</a>
<a href="viewMapC.jsp?id=<%=id%>">放大怪物</a>
<a href="viewMapS.jsp?id=<%=id%>&w=30&h=20">区域放大</a>
<a href="viewMapS.jsp?id=<%=id%>">全图</a>
	</form>
<%
MapNodeBean[][] nodess = world.getRoundNode(mapNode,20, 6, 7);
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
<div class=gr style="left:<%=x*82+startX%>;top:<%=y*42+200+startY%>;border:<%=borderColor%>;">
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
<%}%></div>
<%if(links[1]!=null){%><div class=gv style="left:<%=x*82+39+startX%>;top:<%=y*42+200-10+startY%>;"></div><%}%>
<%if(links[3]!=null){%><div class=gh style="left:<%=x*82-13+startX%>;top:<%=y*42+200+20+startY%>;"></div><%}%>
<%if(links[5]!=null){%><div class=gh style="left:<%=x*82-13+82+startX%>;top:<%=y*42+200+11+startY%>;"></div><%}%>
<%if(links[7]!=null){%><div class=gv style="left:<%=x*82+29+startX%>;top:<%=y*42+200-10+42+startY%>;"></div><%}%>
<%}}%>
	</body>
</html>