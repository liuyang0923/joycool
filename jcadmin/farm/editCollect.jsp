<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");

	CollectBean collect = world.getCollect(id);
	if (null != request.getParameter("add")) {
		String name = (String)request.getParameter("name");
		String info = (String)request.getParameter("info");
		if (!name.equals("")) {
			collect.setName(name);
			collect.setInfo(info);
			collect.setType(action.getParameterInt("type"));
			collect.setRank(action.getParameterInt("rank"));
			collect.setPrice(action.getParameterInt("price"));
			collect.setItems(request.getParameter("items").replace("，",","));
			collect.init();
        world.updateCollect(collect);
        world.initCollectItem();
        response.sendRedirect("farmCollect.jsp");
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
	<form method="post" action="editCollect.jsp?add=1&id=<%=id%>">
<tr>
		<td>
			名称
		</td>
		<td>
		<input type=text name="name" size="20" value="<%=collect.getName()%>">
		</td>
</tr>
<tr>
		<td>
			描述
		</td>
		<td>
		<textarea name="info" cols="60" rows="2"><%=collect.getInfo()%></textarea>
		</td>
</tr>
<tr>
	<td>
		类型
	</td>
	<td>
		<input type=text name="type" size="20" value="<%=collect.getType()%>">
	</td>
</tr>
<tr>
	<td>
		价格
	</td>
	<td>
		<input type=text name="price" size="20" value="<%=collect.getPrice()%>">
	</td>
</tr>
<tr>
	<td>
		级别
	</td>
	<td>
		<input type=text name="rank" size="20" value="<%=collect.getRank()%>">
	</td>
</tr>
<tr>
	<td>
		包含的内容
	</td>
	<td>
		<input type=text name="items" size="20" value="<%=collect.getItems()%>">
	</td>
</tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	<a href="farmCollect.jsp">返回上一级</a>
	</body>
</html>