<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List creatureSpawnList = world.creatureSpawnList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,creatureSpawnList.size(),20);

int j = 0;
String prefixUrl = "farmCreatureSpawn.jsp";

NpcService service = new NpcService();

if (null != request.getParameter("delete")) {/*
	int id = StringUtil.toInt(request.getParameter("delete"));
	dbOp = new DbOperation();
	dbOp.init();
	dbOp
			.executeUpdate("delete from farm_map_node where id="
					+ id);
	dbOp.release();*/
}

if (null != request.getParameter("add")) {
    int templateId = action.getParameterInt("templateId");
	if (templateId>0) {
			CreatureSpawnBean cs = new CreatureSpawnBean();
			cs.setTemplateId(templateId);
			cs.setPos(request.getParameter("pos"));
			cs.setCount(action.getParameterInt("count"));
			cs.setCooldown(action.getParameterInt("cooldown")*1000);
			cs.setFlag(action.getParameterFlag("flag"));
			cs.init();
			world.addCreatureSpawn(cs);
      response.sendRedirect("farmCreatureSpawn.jsp");
	} else {%>
<script>
alert("请填写正确各项参数！");
</script>
<%}
}
//List vec = service.getMapNodeList
//		(" 1=1 order by id limit "
//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
List vec = creatureSpawnList.subList(paging.getStartIndex(),paging.getEndIndex());
CreatureSpawnBean creature = null;

%>
<html>
<link href="common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
怪物分布后台
<br />

<table width="100%">
<tr>
	<td>
		id
	</td>
	<td>
		怪物
	</td>
	<td>
		分布区域
	</td>
	<td>
		结点数量
	</td>
	<td>
		数量
	</td>
	<td>
		刷新间隔
	</td>
	<td>
		标志
	</td>
	<td>
		操作
	</td>
</tr>
<%for (int i = 0; i < vec.size(); i++) {
	creature = (CreatureSpawnBean) vec.get(i);
	CreatureTBean ct = world.getCreatureT(creature.getTemplateId());
%>
<tr>
	<form method="post" action="farmCreatureSpawn.jsp?delete=<%=creature.getId()%>">
	<td>
		<%=creature.getId()%>
	</td>
	<td>
		<%if(ct!=null){%><a href="editCreature.jsp?id=<%=ct.getId()%>"><%=ct.getName()%></a><%}else{%>(未知)<%}%>
	</td>
	<td width="500">
		<%=creature.getPos()%>
	</td>
	<td>
		<%=creature.getPosList().size()%>
	</td>
	<td>
		<%=creature.getCount()%>
	</td>
	<td>
		<%=creature.getCooldown()/1000%>秒
	</td>
	<%/*%><td>
		<a href="farmcreature.jsp?delete=<%=creature.getId()%>">删除</a>
	</td>
	<%*/%>
<td width="75">
<%for(int flag=0;flag<CreatureSpawnBean.FLAG_COUNT;flag++){%>
 <%if(creature.isFlag(flag)){%><%=CreatureSpawnBean.flagString[flag]%>&nbsp;<%}%>
 <%}%><br/>
</td>
	<td>
		<a href="editCreatureSpawn.jsp?id=<%=creature.getId()%>">编辑</a>&nbsp;
		<%if(creature.getPosList().size()>0){%>
		<a href="editMapNode.jsp?id=<%=creature.getPosList().get(0)%>&amp;distribute=<%=creature.getPos()%>">分布</a>-
		<a href="editCreatureSpawn.jsp?refresh=1&id=<%=creature.getId()%>" onclick="return confirm('确认强制生长?')">强制生长</a>
		<%}%>
	</td>
</tr>
</form>
<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
<%@include file="bottom.jsp"%>
	<br />
	<form method="post" action="farmCreatureSpawn.jsp?add=1">
		怪物id：
		<input name="templateId"><br/>
		生长区域：
		 <textarea name="pos" cols="60" rows="2"></textarea><br/>
		数量：<input name="count"><br/>
		刷新间隔：<input name="cooldown" value="60"><br/>
标志：
<%for(int flag=0;flag<CreatureSpawnBean.FLAG_COUNT;flag++){%>
 <input type=checkbox name="flag" value="<%=flag%>"><%=CreatureSpawnBean.flagString[flag]%>
 <%}%><br/>
		<input type="submit" id="add" name="add" value="增加">
		<br />
	</form>
	<br />
</body>
</html>
