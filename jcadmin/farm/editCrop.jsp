<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");
			FarmCropBean crop =null;
			crop = world.getCrop(id);
			if (null != request.getParameter("add")) {
				String name = action.getParameterString("name");
				int rank = action.getParameterInt("rank");
				int growTime = action.getParameterInt("growTime");
				int rotTime = action.getParameterInt("rotTime");
				int product = action.getParameterInt("product");
				int actInterval = action.getParameterInt("actInterval");
				int productCount = action.getParameterInt("productCount");
				int proId = action.getParameterInt("proId");
				if (!name.equals("") ) {
		                crop.setName(name);
			            crop.setRank(rank);
			            crop.setGrowTime(growTime);
			            crop.setRotTime(rotTime);
			            crop.setProduct(product);
			            crop.setActInterval(actInterval);
			            crop.setProductCount(productCount);
			            crop.setProId(proId);
						world.updateCrop(crop);
                        response.sendRedirect("farmCrop.jsp");
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
	<form method="post" action="editCrop.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					作物名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=crop.getName()%>">
			</td>
				<tr>
				<td>
					需要等级
				</td>
				<td>
			<input type=text name="rank" size="20" value="<%=crop.getRank()%>">
			    </td>
			    
				<tr>
				<td>
					生长需要的时间
				</td>
				<td>
			<input type=text name="growTime" size="20" value="<%=crop.getGrowTime()%>">
			</td>
				<tr>
				<td>
					多少时间后腐烂
				</td>
				<td>
			<input type=text name="rotTime" size="20" value="<%=crop.getRotTime()%>">
			</td>
				<tr>
				<td>
					产品
				</td>
				<td>
			<input type=text name="product" size="20" value="<%=crop.getProduct()%>">
			</td>
				<tr>
				<td>
					灌溉周期
				</td>
				<td>
			<input type=text name="actInterval" size="20" value="<%=crop.getActInterval()%>">
			</td>
				<tr>
				<td>
					收获
				</td>
			<td>
			<input type=text name="productCount" size="20" value="<%=crop.getProductCount()%>">
			</td>
			<tr>
				<td>
					对应的专业
				</td>
			<td>
			<input type=text name="proId" size="20" value="<%=crop.getProId()%>">
			</td>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>