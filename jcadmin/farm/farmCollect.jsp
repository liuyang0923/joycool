<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List collectList = world.collectList;

PagingBean paging = new PagingBean(action,collectList.size(),20,"p");

	String prefixUrl = "farmCollect.jsp";

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
	String name = request.getParameter("name").trim();
	String info = request.getParameter("info").trim();
	if (!name.equals("")) {
			CollectBean collect = new CollectBean();
			collect.setId(id);
			collect.setName(name);
			collect.setInfo(info);
			collect.setType(action.getParameterInt("type"));
			collect.setRank(action.getParameterInt("rank"));
			collect.setPrice(action.getParameterInt("price"));
			collect.setItems(request.getParameter("items"));
			collect.init();
			world.addCollect(collect);
			world.initCollectItem();
         response.sendRedirect("farmCollect.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	List vec = collectList.subList(paging.getStartIndex(),paging.getEndIndex());
	CollectBean collect = null;

	%>
<html>
<head>
</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
新手地图后台
<br />

<table width="100%" class="farmTable">
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
			类型
		</td>
		<td>
			级别
		</td>
		<td>
			价格
		</td>
		<td>
			包含的内容
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		collect = (CollectBean) vec.get(i);
%>
	<tr>
		<form method="post" action="farmCollect.jsp?delete=<%=collect.getId()%>">
		<td>
			<%=collect.getId()%>
		</td>
		<td>
			<a href="editCollect.jsp?id=<%=collect.getId()%>"><%=collect.getName()%></a>
		</td>
		<td>
			<%=collect.getInfo()%>
		</td>
		<td>
			<%=collect.getTypeName()%>
		</td>
		<td>
			<%=collect.getRank()%>
		</td>
		<td>
			<%=FarmWorld.formatMoney(collect.getPrice())%>
		</td>
		<td>
		<%if(collect.getType()==0){%>
			<%
			for(int i2 = 0;i2 < collect.getCount();i2++) {
			DummyProductBean item = FarmWorld.getItem(((Integer)collect.getItemList().get(i2)).intValue());
			if(item==null) continue;
			%><a href="editItem.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>,
			<%}%>
		<%}else if(collect.getType()==1){%>
			<%
			for(int i2 = 0;i2 < collect.getCount();i2++) {
			CreatureTBean creature = FarmNpcWorld.one.getCreatureT(((Integer)collect.getItemList().get(i2)).intValue());
			if(creature==null) continue;
			%><a href="editCreature.jsp?id=<%=creature.getId()%>"><%=creature.getName()%></a>,
			<%}%>
		<%}%>
		</td>
	</tr>
	</form>
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
<form method="post" action="farmCollect.jsp?add=1">
    id：<input id="id" name="id">&nbsp;
	名称：<input id="name" name="name"><br/>
	描述：<textarea id="info" name="info"cols="60" rows="2"></textarea><br/>
	类型：<input name="type"><br/>
	等级：<input name="rank"><br/>
	价格：<input name="price"><br/>
	包括的内容：<input name="items"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<br />
</body>
</html>
