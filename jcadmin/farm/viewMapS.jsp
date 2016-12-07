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
int h = action.getParameterInt("h");
int w = action.getParameterInt("w");
int sel = action.getParameterInt("sel");
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
.grid{line-height:100%;position:absolute;z-index:-1;overflow:hidden;width:17;height:17;padding:2px;}
</style>
<script src='js/Integer.js' type='text/javascript'></script>
<script src='js/ArrayList.js' type='text/javascript'></script>
	<link href="common.css" rel="stylesheet" type="text/css">
<script>
function fixdiv() {
document.all.move.style.top=document.body.scrollTop;
document.all.move.style.left=document.body.scrollLeft;
}
var sel=<%=sel%>;
function edit(id){
	if(sel==0)
		window.location='editMapNode.jsp?id='+id;
	else if(sel==1)
		window.location='viewMapS.jsp?id='+id+'&w=<%=w%>&h=<%=h%>&sel=1';
	else if(sel==2){
		var iid = new Integer(id);
		var curdiv = document.getElementById('n'+id);
		if(idArray.contains(iid)){
			idArray.remove(idArray.indexOf(iid));
			curdiv.style.backgroundColor='';
		} else {
			idArray.add(iid);
			curdiv.style.backgroundColor='#FFBBFF';
		}
	}
	return false;
}
function clearall(){
if(!confirm('确认清空?'))return false;
for(var i=0;i<idArray.size();i++){
	var curdiv = document.getElementById('n'+idArray.get(i));
	if(curdiv)
		curdiv.style.backgroundColor='';
}
idArray.clear();
return false;
}
function save(){
var idliststr='';
for(var i=0;i<idArray.size();i++){
	if(i>0)
		idliststr+=',';
	idliststr+=idArray.get(i);
}
clipboardData.setData('text',idliststr);
return false;
}
<%if(distribute.size()>1){%>
var idList=new Array(<%
Iterator it = distribute.iterator();
while(it.hasNext()){
%><%=it.next()%><%if(it.hasNext()){%>,<%}}%>);
<%}else{%>
var idList = new Array();
<%if(distribute.size()==1){%>idList[0]=<%=distribute.iterator().next()%><%}%>
<%}%>
var idArray = new ArrayList();
for(var i=0;i<idList.length;i++)
	idArray.add(new Integer(idList[i]));
</script>
<body style="font-size:12px;" onScroll="fixdiv()">
<%
MapNodeBean[][] nodess;
if(h>0&&w>0)
	nodess = world.getRoundNode(mapNode,1000, w,h);
else
	nodess = world.getRoundNode(mapNode,1000);
for(int x = 0;x < nodess.length;x++) {
	MapNodeBean[] nodes = nodess[x];
	for(int y = 0;y < nodes.length;y++) {
		MapNodeBean nc = nodes[y];
		if(nc == null) continue;
		MapNodeBean[] links = nc.getLinks();
		String borderColor=(String)borderColors.get(Integer.valueOf(nc.getMapId()));
		if(borderColor==null)borderColor="gray";
		if(distribute.contains(Integer.valueOf(nc.getId())))borderColor = "solid " + borderColor + " 1px;";else borderColor = "solid " + borderColor + " 1px";
		String cr=nc.getName()+"("+nc.getId()+")&#13;"+ nc.getInfo();
		%>
<div id=n<%=nc.getId()%> class=grid style="left:<%=x*19+startX%>;top:<%=y*19+200+startY%>;border:<%=borderColor%>;">
<a title="<%=cr%>" href="#" onclick="return edit(<%=nc.getId()%>)"><%if(nc.getName().length()>1){%><%=nc.getName().substring(0,1)%><%}else{%><%=nc.getName()%><%}%></a></div><%
}}%>
<script>
for(var i=0;i<idList.length;i++){
	var idiv = document.getElementById('n'+idList[i]);
	if(idiv)
		idiv.style.backgroundColor='#FFBBFF';
}
</script>
<div id="move" style="width:80px;height:80px;font-size:14px;position:absolute;background-color:#DDDDDD;border:solid 1px #00BB00;">
<input name="sel" type=radio onclick="sel=0" value="0" <%if(sel==0){%>checked<%}%> >编辑<br/>
<input name="sel" type=radio onclick="sel=1" value="1" <%if(sel==1){%>checked<%}%> >移动<br/>
<input name="sel" type=radio onclick="sel=2" value="2" <%if(sel==2){%>checked<%}%> >选择<br/>
<a href="#" onclick="return save()">复制id列表</a><br/>
<a href="#" onclick="return clearall();">清空id列表</a><br/>
</div>
	</body>
</html>