<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

			MapPickBean pick =null;
			pick = world.getPick(id);
			if (null != request.getParameter("add")) {
				String name = action.getParameterString("name");
				String info = action.getParameterString("info");
				String items = action.getParameterString("items");
				int pos = action.getParameterInt("pos");
				if (!name.equals("") ) {
			            pick.setName(name);
			            pick.setInfo(info);
			            pick.setItems(items);
			            pick.setQuestId(action.getParameterInt("questId"));
			            FarmWorld.nodeMoveObj(pick.getPos(), pos, pick);
			            pick.setPos(pos);
			            pick.init();
						world.updatePick(pick);
                        response.sendRedirect("farmPick.jsp");
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
	<form method="post" action="editPick.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=pick.getName()%>">
			</td>
				<tr>
				<td>
					描述
				</td>
				<td>
			<textarea name="info" cols="60" rows="2"><%=pick.getInfo()%></textarea>
			    </td>
				<tr>
				<td>
					物品
				</td>
				<td>
			<input type=text name="items" size="20" value="<%=pick.getItems()%>">
			</td>
				<tr>
				<td>
					位置
				</td>
				<td>
			<input type=text name="pos" size="20" value="<%=pick.getPos()%>">
			</td>
				<tr>
				<td>
					任务
				</td>
				<td>
			<input type=text name="questId" size="20" value="<%=pick.getQuestId()%>">
			</td>
				
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>