<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();		
int id = action.getParameterInt("id");

	FarmCarBean car = world.getCar(id);
	if (null != request.getParameter("add")) {
		String name = (String)request.getParameter("name");
		String info = (String)request.getParameter("info");
		if (!name.equals("")) {
			car.setName(name);
			car.setInfo(info);
			car.setQuestId(action.getParameterInt("questId"));
			car.setCooldown(action.getParameterInt("cooldown")*1000);
			car.setMoney(action.getParameterInt("money"));
			car.setLine(request.getParameter("line"));
			car.setFlag(action.getParameterFlag("flag"));
			car.init();
        world.updateCar(car);
        response.sendRedirect("farmCar.jsp");
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
	<form method="post" action="editCar.jsp?add=1&id=<%=id%>">
<tr>
		<td>
			名称
		</td>
		<td>
		<input type=text name="name" size="20" value="<%=car.getName()%>">
		</td>
</tr>
<tr>
		<td>
			描述
		</td>
		<td>
		<textarea name="info" cols="60" rows="2"><%=car.getInfo()%></textarea>
		</td>
</tr>
<tr>
	<td>
		移动间隔
	</td>
	<td>
		<input type=text name="cooldown" size="20" value="<%=car.getCooldown()/1000%>">秒
	</td>
</tr>
<tr>
	<td>
		价钱
	</td>
	<td>
		<input type=text name="money" size="20" value="<%=car.getMoney()%>">
	</td>
</tr>
<tr>
	<td>
		任务id
	</td>
	<td>
		<input type=text name="questId" size="20" value="<%=car.getQuestId()%>">
	</td>
</tr>
<tr>
	<td>
		路线
	</td>
	<td>
		<textarea name="line" cols="60" rows="2"><%=car.getLine()%></textarea>
	</td>
</tr>
<tr>
	<td>
		起点-终点
	</td>
	<td>
		<% MapNodeBean mapNode = FarmWorld.getWorld().getMapNode(car.getStartPos());
		if(mapNode==null){%>无<%}else{%>
		<a href="editMapNode.jsp?id=<%=mapNode.getId()%>"><%=mapNode.getName()%></a>
		<%}%>-
		<% MapNodeBean mapNode2 = FarmWorld.getWorld().getMapNode(car.getEndPos());
		if(mapNode2==null){%>无<%}else{%>
		<a href="editMapNode.jsp?id=<%=mapNode2.getId()%>"><%=mapNode2.getName()%></a>
		<%}%>
	</td>
</tr>
		<tr>
		<td>
			标志位
		</td>
		<td>
	<%for(int flag=0;flag<FarmCarBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>" <%if(car.isFlag(flag)){%>checked<%}%>><%=FarmCarBean.flagString[flag]%>
	 <%}%><br/>
	</td></tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	<a href="farmCar.jsp">返回上一级</a>
	</body>
</html>