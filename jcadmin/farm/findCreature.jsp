<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List creatureList = new ArrayList(world.objectMap.values());
int id = action.getParameterInt("id");
int show = action.getParameterInt("show");
if(show>0){
Object obj = world.getObject(show);
if(obj!=null) FarmWorld.nodeAddObj((MapDataBean)obj);
}
CreatureTBean template = world.getCreatureT(id);
%>
<html>
<link href="common.css" rel="stylesheet" type="text/css">
	<head>
	</head>
	<body>
		搜索当前存在的怪物后台：<%=template.getName()%>
		<br />

		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					hp
				</td>
				<td>
					mp
				</td>
				<td>
					攻击力
				</td>
				<td>
					级别
				</td>
				<td>
					防御
				</td>
				<td>
					位置
				</td>
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < creatureList.size(); i++) {
			Object obj = creatureList.get(i);
			if(!(obj instanceof CreatureBean)) continue;
				CreatureBean creature = (CreatureBean) creatureList.get(i);
				if(creature.getTemplate().getId()!=id) continue;
%>
			<tr>
				<td>
					<%=creature.getGid()%>
				</td>
				<td>
					<%=creature.getHp()%>
				</td>
				<td>
					<%=creature.getMp()%>
				</td>
				<td>
					<%=creature.getAttack()%>
				</td>
				<td>
					<%=creature.getLevel()%>
				</td>
				<td>
					<%=creature.getDefense()%>
				</td>
				<td>
				<%=creature.getPos()%>
<%
MapNodeBean node = FarmWorld.one.getMapNode(creature.getPos());
if(node!=null){
%>
	<a href="editMapNode.jsp?id=<%=node.getId()%>"><%=node.getName()%></a>
<%}%>
				</td>
				<td>
					<a href="findCreature.jsp?id=<%=id%>&amp;show=<%=creature.getGid()%>">重新显示</a>
				</td>
			</tr>
			<%}%>
		</table>
<%@include file="bottom.jsp"%>
		<br />

	</body>
</html>
