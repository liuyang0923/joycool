<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List skillList = world.skillList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,skillList.size(),20);

	String prefixUrl = "farmSkill.jsp";

	FarmService service = new FarmService();

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
		int rank = action.getParameterInt("rank");
		int price = action.getParameterInt("price");
		int proId = action.getParameterInt("proId");
		long cooldown = action.getParameterLong("cooldown");
		int cooldownId = action.getParameterInt("cooldownId");
		String material = action.getParameterString("material");
		String product = action.getParameterString("product");
		if (!name.equals("")) {
				FarmSkillBean skill = new FarmSkillBean();
				skill.setId(id);
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
				skill.setFlag(action.getParameterFlag("flag"));
				skill.setCost(request.getParameter("cost"));
				skill.setEffect(request.getParameter("effect"));
				skill.init();
				world.addSkill(skill);
             response.sendRedirect("farmSkill.jsp");
		} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			//List vec = service.getMapNodeList
			//		(" 1=1 order by id limit "
			//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
			List vec = skillList.subList(paging.getStartIndex(),paging.getEndIndex());
			FarmSkillBean skill = null;

			%>
<html>
	<head>
<link href="common.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		专业的技能后台
		<br />

<table width="1000" border="1">
	<tr>
		<td>
			id
		</td>
		<td>
			名称
		</td>
		<td>
			介绍
		</td>
		<td>
			等级
		</td>
		<td>
			费用
		</td>
		<td>
			专业
		</td>
		<td>
			职业
		</td>
		<td>
			五行
		</td>
		<td>
			冷却
		</td>
		<td>
			冷却id
		</td>
		<td>
			材料
		</td>
		<td>
			产品
		</td>
		<td>
			消耗
		</td>
		<td>
			效果
		</td>
		<td>
			标志
		</td>
		<td>
			操作
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		skill = (FarmSkillBean) vec.get(i);
%>
	<tr>
		<td>
			<%=skill.getId()%>
		</td>
		<td>
			<%=skill.getName()%>
			<input type="hidden" id="id" name="id" value="<%=skill.getId() %>">
		</td>
		<td width=150>
			<%=skill.getInfo()%>
		</td>
		<td>
			<%=skill.getRank()%>
		</td>
		<td>
			<%=FarmWorld.formatMoney(skill.getPrice())%>
		</td>
		<td>
			<%FarmProBean pro = world.getPro(skill.getProId());%>
			<%=pro==null?"(无)":pro.getName()%>
		</td>
		<td>
			<%=FarmUserBean.class1Name[skill.getClass1()]%>
		</td>
		<td>
			<%=FarmUserBean.elementName[skill.getElement()]%>
		</td>
		<td>
			<%=skill.getCooldownMinute()%>
		</td>
		<td>
			<%=skill.getCooldownId()%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(skill.getMaterialList())%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(skill.getProductList())%>
		</td>
		<td>
			<%=FarmWorld.getSkillCostString(skill.getCostList())%>
		</td>
		<td width=100>
			<%=FarmWorld.getSkillEffectString(skill.getEffectList())%>
		</td>
		<td>
			<%for(int flag=0;flag<FarmSkillBean.FLAG_COUNT;flag++){%>
			 <%if(skill.isFlag(flag)){%><%=FarmSkillBean.flagString[flag]%>&nbsp;<%}%>
			 <%}%><br/>
		</td>
		<%/*%><td>
			<a href="farmSkill.jsp?delete=<%=skill.getId()%>">删除</a>
		</td>
		<%*/%>
		<td>
			<a href="editSkill.jsp?id=<%=skill.getId()%>">编辑</a>
		</td>
	</tr>
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
<br />
<form method="post" action="farmSkill.jsp?add=1">
    id：
	<input id="id" name="id"><BR>
	技能名称：
	<input id="name" name="name"><br/>
	技能介绍：
	 <textarea id="info" name="info"cols="60" rows="2"></textarea><br/>
	需要等级：
	 <input id="rank" name="rank" value="0"><br/>
	 学习需要的钱：
	 <input id="price" name="price" value="0"><br/>
	 所属专业：<input name="proId" value="0">&nbsp;
	 所属职业：<input name="class1" value="0"><br/>
	 冷却时间：
	 <input id="cooldown" name="cooldown" value="0"><br/>
	 冷却id：
	 <input id="cooldownId" name="cooldownId" value="0"><br/>
	 材料：<input id="material" name="material"><br/>
	 产品：<input id="product" name="product"><br/>
消耗：<input name="cost">&nbsp;效果：<input name="effect"><br/>
	标志：
	<%for(int flag=0;flag<FarmSkillBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>"><%=FarmSkillBean.flagString[flag]%>
	 <%}%><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
</body>
</html>
