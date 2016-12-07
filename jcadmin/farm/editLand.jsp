<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

			LandMapBean land =null;
			land = world.getLand(id);
			if (null != request.getParameter("add")) {
				String name = action.getParameterString("name");
				int width = action.getParameterInt("width");
				int height = action.getParameterInt("height");
				String grid = action.getParameterString("grid");
				int rank = action.getParameterInt("rank");
				int pos = action.getParameterInt("pos");
				String item1 = action.getParameterString("item1");
				String item1Grid = action.getParameterString("item1Grid");
				String item2 = action.getParameterString("item2");
				String item2Grid = action.getParameterString("item2Grid");
				if (!name.equals("") ) {
		                land.setName(name);
			            land.setRank(rank);
			            land.setWidth(width);
			            land.setHeight(height);
			            land.setGrid(grid);
			            land.setItem1(item1);
			            land.setItem2(item2);
			            land.setItem1Grid(item1Grid);
			            land.setItem2Grid(item2Grid);
			            FarmWorld.nodeMoveObj(land.getPos(), pos, land);
			            land.setPos(pos);
			            land.init();
						world.updateLand(land);
                        response.sendRedirect("farmLand.jsp");
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
	<form method="post" action="editLand.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=land.getName()%>">
			</td>
				<tr>
				<td>
					需要等级
				</td>
				<td>
			<input type=text name="rank" size="20" value="<%=land.getRank()%>">
			    </td>
			    
				<tr>
				<td>
					宽
				</td>
				<td>
			<input type=text name="width" size="20" value="<%=land.getWidth()%>">
			</td>
				<tr>
				<td>
					高
				</td>
				<td>
			<input type=text name="height" size="20" value="<%=land.getHeight()%>">
			</td>
				<tr>
				<td>
					位置
				</td>
				<td>
			<input type=text name="pos" size="20" value="<%=land.getPos()%>">
			</td>
				<tr>
				<td>
					网格
				</td>
				<td>
			<textarea name="grid" cols="60" rows="2"><%=land.getGrid()%></textarea>
			</td>
				<tr>
				<td>
					格1
				</td>
				<td>
			<input type=text name="item1" size="20" value="<%=land.getItem1()%>">
			</td>
				<tr>
				<td>
					格1内容
				</td>
			<td>
			<input type=text name="item1Grid" size="20" value="<%=land.getItem1Grid()%>">
			</td>
			<tr>
				<td>
					格2
				</td>
			<td>
			<input type=text name="item2" size="20" value="<%=land.getItem2()%>">
			</td>
			<tr>
				<td>
					格2内容
				</td>
			<td>
			<input type=text name="item2Grid" size="20" value="<%=land.getItem2Grid()%>">
			</td>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>