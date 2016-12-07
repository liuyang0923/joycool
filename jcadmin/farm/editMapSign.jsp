<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");

	MapSignBean mapSign = world.getMapSign(id);
	if (null != request.getParameter("add")) {
		String name = (String)request.getParameter("name");
		String info = (String)request.getParameter("info");
		if (!name.equals("")) {
			mapSign.setName(name);
			mapSign.setInfo(info);
			mapSign.setPosId(action.getParameterInt("posId"));
			mapSign.setDistance(action.getParameterInt("distance"));
			mapSign.setFlag(action.getParameterFlag("flag"));
			mapSign.setNode(world.getMapNode(mapSign.getPosId()));
			mapSign.init();
        world.updateMapSign(mapSign);
        response.sendRedirect("farmMapSign.jsp");
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
	<form method="post" action="editMapSign.jsp?add=1&id=<%=id%>">
<tr>
		<td>
			名称
		</td>
		<td>
		<input type=text name="name" size="20" value="<%=mapSign.getName()%>">
		</td>
</tr>
<tr>
		<td>
			描述
		</td>
		<td>
		<textarea name="info" cols="60" rows="2"><%=mapSign.getInfo()%></textarea>
		</td>
</tr>
<tr>
	<td>
		距离^2
	</td>
	<td>
		<input type=text name="distance" size="20" value="<%=mapSign.getDistance()%>">
	</td>
</tr>
<tr>
	<td>
		地图位置
	</td>
	<td>
		<input type=text name="posId" size="20" value="<%=mapSign.getPosId()%>">
		<% MapNodeBean mapNode = FarmWorld.getWorld().getMapNode(mapSign.getPosId());
		if(mapNode==null){%>无<%}else{%>
		<a href="editMapNode.jsp?id=<%=mapNode.getId()%>"><%=mapNode.getName()%></a>
		<%}%>		
	</td>
</tr>
<tr>
		<td>
			标志位
		</td>
		<td>
	<%for(int flag=0;flag<MapSignBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>" <%if(mapSign.isFlag(flag)){%>checked<%}%>><%=MapSignBean.flagString[flag]%>
	 <%}%><br/>
	</td></tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	<a href="farmMapSign.jsp">返回上一级</a>
	</body>
</html>