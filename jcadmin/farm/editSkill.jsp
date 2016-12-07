<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");

	FarmSkillBean skill =null;
	skill = world.getSkill(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		String info = action.getParameterString("info");
		int rank = action.getParameterInt("rank");
		int price = action.getParameterInt("price");
		int proId = action.getParameterInt("proId");
		long cooldown = action.getParameterLong("cooldown");
		int cooldownId = action.getParameterInt("cooldownId");
		String material = action.getParameterString("material");
		String product = action.getParameterString("product");
		if (!name.equals("")) {
		        skill.setName(name);
				skill.setInfo(info);
				skill.setRank(rank);
				skill.setPrice(price);
				skill.setProId(proId);
				skill.setCooldown(cooldown);
				skill.setCooldownId(cooldownId);
				skill.setMaterial(material);
				skill.setProduct(product);
				skill.setClass1(action.getParameterInt("class1"));
				skill.setElement(action.getParameterInt("element"));
				skill.setFlag(action.getParameterFlag("flag"));
				skill.setCost(request.getParameter("cost"));
				skill.setEffect(request.getParameter("effect"));
				skill.init();
                world.updateSkill(skill);
                response.sendRedirect("farmSkill.jsp");
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
	<form method="post" action="editSkill.jsp?add=1&id=<%=id%>">
	<tr>
		<td>
			技能名称</td><td><input type=text name="name" size="20" value="<%=skill.getName()%>">
		</td>
	</tr><tr>
		<td>
			技能介绍</td><td><textarea name="info" cols="60" rows="2"><%=skill.getInfo()%></textarea>
		</td>
	</tr><tr>
		<td>
			需要等级</td><td><input type=text name="rank" size="20" value="<%=skill.getRank()%>">
		</td>
	</tr><tr>
		<td>
			学习需要的钱</td><td><input type=text name="price" size="20" value="<%=skill.getPrice()%>">
		</td>
	</tr>
	<tr>
		<td>
			所属专业</td><td><input type=text name="proId" size="20" value="<%=skill.getProId()%>">
		</td>
	</tr>
	<tr>
		<td>
			所属职业</td><td><input type=text name="class1" size="20" value="<%=skill.getClass1()%>">
		</td>
	</tr>
	<tr>
		<td>
			五行元素</td><td><input type=text name="element" size="20" value="<%=skill.getElement()%>">
		</td>
	</tr>
	<tr>
		<td>
			冷却时间</td><td><input type=text name="cooldown" size="20" value="<%=skill.getCooldown()%>">
		</td>
	</tr><tr>
		<td>
			冷却id</td><td><input type=text name="cooldownId" size="20" value="<%=skill.getCooldownId()%>">
		</td>
	</tr><tr>
		<td>
			材料</td><td><input type=text name="material" size="20" value="<%=skill.getMaterial()%>">
		</td>
	</tr>
	<tr>
		<td>
			产品</td><td><input type=text name="product" size="20" value="<%=skill.getProduct()%>">
		</td>
	</tr>
	<tr>
		<td>
			战斗消耗</td><td><input type=text name="cost" size="20" value="<%=skill.getCost()%>">
		</td>
	</tr>
	<tr>
		<td>
			战斗效果</td><td><input type=text name="effect" size="20" value="<%=skill.getEffect()%>">
		</td>
	</tr>
			<tr>
			<td>
				标志位
			</td>
			<td>
		<%for(int flag=0;flag<FarmSkillBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox name="flag" value="<%=flag%>" <%if(skill.isFlag(flag)){%>checked<%}%>><%=FarmSkillBean.flagString[flag]%>
		 <%}%><br/>
		</td></tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>