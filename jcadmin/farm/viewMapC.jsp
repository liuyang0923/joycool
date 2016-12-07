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
<style>
a{text-decoration:none;}
</style>
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
		String cr;
		if(nc.getObjCount() > 0){
			cr = nc.getObjNames();
		} else {
			cr = nc.getInfo();
		}
		%>
<div style="line-height:100%;position:absolute;z-index:-1;left:<%=x*40+startX%>;top:<%=y*20+200+startY%>;width:37;height:17;border:<%=borderColor%>;padding:1px;">
<a title="<%=cr%>" href="editMapNode.jsp?id=<%=nc.getId()%>"><%if(nc.getName().length()>2){%><%=nc.getName().substring(0,2)%><%}else{%><%=nc.getName()%><%}%></a><%if(nc.getObjs().size()>0){%><font color=red><b>@</b></font><%}%></div>
<%}}%>
	</body>
</html>