<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

			PickTBean pick =null;
			pick = world.getPickT(id);
			if (null != request.getParameter("add")) {
				String items = action.getParameterString("items");
				if (!items.equals("") ) {
			            pick.setItems(items);
			            pick.setPos(request.getParameter("pos").replace("，",","));
			            pick.setCooldown(action.getParameterInt("cooldown")*1000);
			            pick.init();
						world.updatePickT(pick);
                        response.sendRedirect("farmPick2.jsp");
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
	<h3>编辑：地图物品</h3>
	<table width="100%">
	<form method="post" action="editPick2.jsp?add=1&id=<%=id%>">
				<tr>
				<td>
					物品
				</td>
				<td>
			<input type=text name="items" size="20" value="<%=pick.getItems()%>">
			</td></tr>
				<tr>
				<td>
					位置
				</td>
				<td>
			<input type=text name="pos" size="20" value="<%=pick.getPos()%>">
			</td></tr>
				<tr>
				<td>
					刷新间隔
				</td>
				<td>
			<input type=text name="cooldown" size="20" value="<%=pick.getCooldown()/1000%>">
			</td>
		</tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>