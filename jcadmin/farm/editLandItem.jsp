<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");
			LandItemBean landItem =null;
			landItem = world.getLandItem(id);
			if (null != request.getParameter("add")) {
				int itemId = action.getParameterInt("itemId");
				String name = action.getParameterString("name");
				int rank = action.getParameterInt("rank");
				int proId = action.getParameterInt("proId");
				int min = action.getParameterInt("min");
				int max = action.getParameterInt("max");
				if (!name.equals("") ) {
			           landItem.setName(name);
			            landItem.setRank(rank);
			            landItem.setItemId(itemId);
			            landItem.setProId(proId);
			            landItem.setMin(min);
			            landItem.setMax(max);
						world.updateLandItem(landItem);
                        response.sendRedirect("farmLandItem.jsp");
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
	<form method="post" action="editLandItem.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					拣起来之前的名字
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=landItem.getName()%>">
			</td>
				<tr>
				<td>
					得到的物品id
				</td>
				<td>
			<input type=text name="itemId" size="20" value="<%=landItem.getItemId()%>">
			    </td>
			    
				<tr>
				<td>
					需要等级
				</td>
				<td>
			<input type=text name="rank" size="20" value="<%=landItem.getRank()%>">
			</td>
				<tr>
				<td>
					物品的最小个数
				</td>
				<td>
			<input type=text name="min" size="20" value="<%=landItem.getMin()%>">
			</td>
				<tr>
				<td>
					物品的最大个数
				</td>
				<td>
			<input type=text name="max" size="20" value="<%=landItem.getMax()%>">
			</td>
			<tr>
				<td>
					需要的采集专业
				</td>
				<td>
			<input type=text name="proId" size="20" value="<%=landItem.getProId()%>">
			</td>
				
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>