<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();
int id = action.getParameterInt("id");
	
	FarmStoneBean stone =null;
	stone = world.getStone(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		if (!name.equals("") ) {
	            stone.setName(name);
	            stone.setType(action.getParameterInt("type"));
	            stone.setInfo(request.getParameter("info"));
	            stone.setValue(request.getParameter("value"));
	            int pos = action.getParameterInt("pos");
	            FarmWorld.nodeMoveObj(stone.getPos(), pos, stone);
				world.updateStone(stone);
                response.sendRedirect("farmStone.jsp");
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
<h3>编辑：七彩石</h3>
<table width="100%">
<form method="post" action="editStone.jsp?add=1&id=<%=id%>">
	<tr>
		<td>
			名称
		</td>
		<td>
	<input type=text name="name" size="20" value="<%=stone.getName()%>">
	</td></tr>
		<tr>
		<td>
			内容
		</td>
		<td>
	<textarea name="info" cols="60" rows="2"><%=stone.getInfo()%></textarea>
	    </td></tr>
		<tr>
		<td>
			所在地图
		</td>
		<td>
	<input type=text name="pos" size="20" value="<%=stone.getPos()%>">
	</td></tr>
		<tr>
		<td>
			类型
		</td>
		<td>
	<input type=text name="type" size="20" value="<%=stone.getType()%>">
	</td></tr>
		<tr>
		<td>
			参数
		</td>
		<td>
	<input type=text name="value" size="20" value="<%=stone.getValue()%>">
	</td></tr>
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
			<br />
</body>
</html>