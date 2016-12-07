<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");

MapBean mapNode =null;
mapNode = world.getMap(id);
if (null != request.getParameter("add")) {
	String name = (String)request.getParameter("name");
	String info = (String)request.getParameter("info");
	if (!name.equals("")) {
			mapNode.setName(name);
			mapNode.setInfo(info);
			mapNode.setX(action.getParameterInt("x"));
			mapNode.setY(action.getParameterInt("y"));
			mapNode.setFlag(action.getParameterFlag("flag"));
			mapNode.setExp(action.getParameterInt("exp"));
			mapNode.setRank(action.getParameterInt("rank"));
			mapNode.setSp(action.getParameterInt("sp"));
			mapNode.setCooldown(action.getParameterInt("cooldown"));
			mapNode.setAttackCount(action.getParameterInt("attackCount"));
			mapNode.setEntryNode(action.getParameterInt("entryNode"));
			mapNode.setMaxPlayer(action.getParameterInt("maxPlayer"));
			mapNode.setParent(action.getParameterInt("parent"));
			mapNode.setCondition(request.getParameter("condition"));
			mapNode.init();
        world.updateMap(mapNode);
        response.sendRedirect("farmMap.jsp");
		} else {%>
    <script>
	alert("请填写正确各项参数！");
	</script>
        <%}}%>
	<html>
	<head>
	</head>
	<link href="common.css" rel="stylesheet" type="text/css">
	<body>
	<table width="100%">
	<form method="post" action="editMap.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=mapNode.getName()%>">
			</td>
				<tr>
				<td>
					描述
				</td>
				<td>
			<textarea name="info" cols="60" rows="2"><%=mapNode.getInfo()%></textarea>
			</td>
				<tr>
				<td>
					x坐标
				</td>
				<td>
			<input type=text name="x" size="20" value="<%=mapNode.getX()%>">
			</td>
				<tr>
				<td>
					y坐标
				</td>
				<td>
			<input type=text name="y" size="20" value="<%=mapNode.getY()%>">
			</td></tr>
				<tr>
				<td>
					入口id
				</td>
			<td>
			<input type=text name="entryNode" size="20" value="<%=mapNode.getEntryNode()%>">
			</td>
			</tr>
<tr>
			<td>
				标志位
			</td>
			<td>
		<%for(int flag=0;flag<MapBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox name="flag" value="<%=flag%>" <%if(mapNode.isFlag(flag)){%>checked<%}%>><%=MapBean.flagString[flag]%>
		 <%}%><br/>
		</td></tr>
				<tr>
				<td>
					经验值
				</td>
			<td>
			<input type=text name="exp" size="20" value="<%=mapNode.getExp()%>">
			</td>
			</tr>
				<tr>
				<td>
					体力
				</td>
			<td>
			<input type=text name="sp" size="20" value="<%=mapNode.getSp()%>">
			</td>
			</tr>
<tr>
	<td>
		冷却
	</td>
	<td>
		<input type=text name="cooldown" size="20" value="<%=mapNode.getCooldown()%>">
	</td>
</tr>
<tr>
	<td>
		等级
	</td>
	<td>
		<input type=text name="rank" size="20" value="<%=mapNode.getRank()%>">
	</td>
</tr>
<tr>
	<td>
		条件
	</td>
	<td>
		<input type=text name="condition" size="20" value="<%=mapNode.getCondition()%>">&nbsp;<%=FarmWorld.getConditionString(mapNode.getConditionList())%>
	</td>
</tr>
<tr>
	<td>
		主动攻击怪物数
	</td>
	<td>
	<input type=text name="attackCount" size="20" value="<%=mapNode.getAttackCount()%>">
	</td>
</tr>
<tr>
	<td>
		玩家数量
	</td>
	<td>
	<input type=text name="maxPlayer" size="20" value="<%=mapNode.getMaxPlayer()%>">
	</td>
</tr>
<tr>
	<td>
		父地图
	</td>
	<td>
	<input type=text name="parent" size="20" value="<%=mapNode.getParent()%>">
	<% if(mapNode.getParent()>0){
	MapBean mapP = world.getMap(mapNode.getParent());
	if(mapP!=null){
	%>&nbsp;<a href="editMap.jsp?id=<%=mapP.getId()%>"><%=mapP.getName()%></a>
	<%}}%>
	</td>
</tr>
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
<img src="/farm/img/map/<%=mapNode.getId()%>.gif"><br />
<a href="farmMap.jsp">返回上一级</a>&nbsp;<a href="farmMapNode.jsp?mapId=<%=mapNode.getId()%>">查看结点</a>
</body>
</html>