<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();
int id = action.getParameterInt("id");
CreatureTBean creature = world.getCreatureT(id);
			if (null != request.getParameter("add")) {
				String name = action.getParameterString("name");
				String info = action.getParameterString("info");
				if (!name.equals("") ) {
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
						world.updateCreatureT(creature);
                        response.sendRedirect("farmCreature.jsp");
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
	<form method="post" action="editCreature.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=creature.getName()%>">
			</td></tr>
				<tr>
				<td>
					介绍
				</td>
				<td>
			<textarea name="info" cols="60" rows="2"><%=creature.getInfo()%></textarea>
			    </td>
			    </tr>
				<tr>
				<td>
					hp
				</td>
				<td>
			<input type=text name="hp" size="20" value="<%=creature.getHp()%>">
			</td></tr>
				<tr>
				<td>
					mp
				</td>
				<td>
			<input type=text name="mp" size="20" value="<%=creature.getMp()%>">
			</td></tr>
				<tr>
				<td>
					攻击力
				</td>
				<td>
			<input type=text name="attack" size="20" value="<%=creature.getAttack()%>">
			</td></tr>
				<tr>
				<td>
					防御力
				</td>
				<td>
			<input type=text name="defense" size="20" value="<%=creature.getDefense()%>">
			</td></tr>
				<tr>
				<td>
					级别
				</td>
			<td>
			<input type=text name="level" size="20" value="<%=creature.getLevel()%>">
			</td></tr>
			<tr>
				<td>
					级别浮动
				</td>
			<td>
			<input type=text name="levelRange" size="20" value="<%=creature.getLevelRange()%>">
			</td></tr>
			<tr>
				<td>
					类型
				</td>
			<td>
			<input type=text name="type" size="20" value="<%=creature.getType()%>">&nbsp;<%=creature.getTypeName()%>
			</td></tr>
			<tr>
				<td>
					特殊掉落
				</td>
			<td>
			<input type=text name="drops" size="20" value="<%=creature.getDrops()%>">
			</td></tr>
			<tr>
				<td>
					攻击间隔
				</td>
			<td>
			<input type=text name="cooldown" size="20" value="<%=creature.getCooldown()%>">
			</td></tr>
			<tr>
				<td>
					标志
				</td>
			<td>
			<%for(int flag=0;flag<CreatureTBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox name="flag" value="<%=flag%>" <%if(creature.isFlag(flag)){%>checked<%}%>><%=CreatureTBean.flagString[flag]%>
		 <%}%>
			</td></tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
<%@include file="bottom.jsp"%>
	</body>
</html>