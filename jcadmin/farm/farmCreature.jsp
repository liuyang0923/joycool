<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List creatureTList = world.creatureTList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,creatureTList.size(),20);

int j = 0;
String prefixUrl = "farmCreature.jsp";

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

%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = action.getParameterString("name");
	String info = action.getParameterString("info");
	if (!name.equals("")) {
			CreatureTBean creature = new CreatureTBean();
			creature.setId(id);
			creature.setName(name);
			creature.setInfo(info);
			creature.setHp(action.getParameterInt("hp"));
			creature.setMp(action.getParameterInt("mp"));
			creature.setAttack(action.getParameterInt("attack"));
			creature.setLevel(action.getParameterInt("level"));
			creature.setLevelRange(action.getParameterInt("levelRange"));
			creature.setDefense(action.getParameterInt("defense"));
			creature.setType(action.getParameterInt("type"));
			creature.setDrops(request.getParameter("drops"));
			creature.setFlag(action.getParameterFlag("flag"));	
			creature.setCooldown(action.getParameterInt("cooldown"));					
			creature.init();
			world.addCreatureT(creature);
      response.sendRedirect("farmCreature.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	//List vec = service.getMapNodeList
	//		(" 1=1 order by id limit "
	//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
	List vec = creatureTList.subList(paging.getStartIndex(),paging.getEndIndex());
	CreatureTBean creature = null;

	%>
<html>
<link href="common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
怪物模板后台
<br />

<table width="100%">
	<tr>
		<td>
			id
		</td>
		<td>
			名称
		</td>
		<td>
			描述
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
			类型
		</td>
		<td>
			掉落
		</td>
		<td>
			攻速
		</td>
		<td>
			标志
		</td>
		<td>
			操作
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		creature = (CreatureTBean) vec.get(i);
%>
	<tr>
		<form method="post" action="farmCreature.jsp?delete=<%=creature.getId()%>">
		<td>
			<%=creature.getId()%>
		</td>
		<td>
			<a href="editCreature.jsp?id=<%=creature.getId()%>"><%=creature.getName()%></a>
			<input type="hidden" id="id" name="id" value="<%=creature.getId() %>">
		</td>
		<td>
			<%=creature.getInfo()%>
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
			<%=creature.getLevel()%>-<%=creature.getLevel()+creature.getLevelRange()-1%><%if(creature.getLevel()<16){%>(<%=(int)Math.sqrt(creature.getHp()*creature.getAttack()/60/(1-FarmWorld.calcDefenseRate(creature.getDefense(),creature.getLevel())))*2000/creature.getCooldown()%>)
			<%}else{%>(<%=(int)Math.sqrt(creature.getHp()*creature.getAttack()/70/(1-FarmWorld.calcDefenseRate(creature.getDefense(),creature.getLevel())))*2000/creature.getCooldown()%>)<%}%>
		</td>
		<td>
			<%=creature.getDefense()%>(<%=FarmWorld.formatNumber(100*FarmWorld.calcDefenseRate(creature.getDefense(),creature.getLevel()))%>%)
		</td>
		<td>
			<%=creature.getTypeName()%>
		</td>
		<td>
			<%=FarmWorld.creatureDropString(creature.getDropList())%>
		</td>
		<td>
			<%=FarmWorld.formatNumber((float)creature.getCooldown()/1000)%>s
		</td>
		<td>
	<%for(int flag=0;flag<CreatureTBean.FLAG_COUNT;flag++){%>
	 <%if(creature.isFlag(flag)){%><%=CreatureTBean.flagString[flag]%>&nbsp;<%}%>
	 <%}%>
		</td>
		<%/*%><td>
			<a href="farmCreature.jsp?delete=<%=creature.getId()%>">删除</a>
		</td>
		<%*/%>
		<td>
			<a href="findCreature.jsp?id=<%=creature.getId()%>">查找</a>
		</td>
	</tr>
	</form>
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response,20)%>
<%@include file="bottom.jsp"%>
<br />
<form method="post" action="farmCreature.jsp?add=1">
	id：<input name="id"><br/>
	名称：<input name="name"><br/>
	描述：<textarea name="info" cols="60" rows="2"></textarea><br/>
hp：<input name="hp">&nbsp;mp：<input name="mp"><br/>
攻击力：<input name="attack">&nbsp;防御：<input name="defense"><br/>
级别：<input name="level">&nbsp;浮动：<input name="levelRange" value="1"><br/>
类型：<input name="type"><br/>
标志：<%for(int flag=0;flag<CreatureTBean.FLAG_COUNT;flag++){%>
	<input type=checkbox name="flag" value="<%=flag%>"><%=CreatureTBean.flagString[flag]%>
<%}%><br/>
特定掉落：<input name="drops">&nbsp;攻击间隔：<input name="cooldown" value="2000"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<br/>
</body>
</html>
