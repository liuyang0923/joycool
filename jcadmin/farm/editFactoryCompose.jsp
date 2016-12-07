<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();
int id = action.getParameterInt("id");

			FactoryComposeBean factoryCompose =null;
			factoryCompose = world.getFactoryCompose(id);
			if (null != request.getParameter("add")) {
				String name = action.getParameterString("name");
				String info = action.getParameterString("info");
				int factoryId = action.getParameterInt("factoryId");
				int rank = action.getParameterInt("rank");
				int price = action.getParameterInt("price");
				int time = action.getParameterInt("time");
				String material = action.getParameterString("material");
				String product = action.getParameterString("product");
				if (!name.equals("") ) {
			            factoryCompose.setName(name);
			            factoryCompose.setRank(rank);
			            factoryCompose.setInfo(info);
			            factoryCompose.setFactoryId(factoryId);
			            factoryCompose.setPrice(price);
			            factoryCompose.setTime(time);
			            factoryCompose.setMaterial(material);
			            factoryCompose.setProduct(product);
			            factoryCompose.init();
						world.updateFactoryCompose(factoryCompose);
                        response.sendRedirect("farmFactoryCompose.jsp?factoryId="+factoryId);
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
	<form method="post" action="editFactoryCompose.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=factoryCompose.getName()%>">
			</td>
			<tr>
				<td>
					对应的工厂
				</td>
				<td>
			<input type=text name="factoryId" size="20" value="<%=factoryCompose.getFactoryId()%>">
			</td>
			
				<tr>
				<td>
					介绍
				</td>
				<td>
			<textarea name="info" cols="60" rows="2"><%=factoryCompose.getInfo()%></textarea>
			    </td>
			    
				<tr>
				<td>
					需要等级
				</td>
				<td>
			<input type=text name="rank" size="20" value="<%=factoryCompose.getRank()%>">
			</td>
				<tr>
				<td>
					需要的铜板
				</td>
				<td>
			<input type=text name="price" size="20" value="<%=factoryCompose.getPrice()%>">
			</td>
				<tr>
				<td>
					需要的时间
				</td>
				<td>
			<input type=text name="time" size="20" value="<%=factoryCompose.getTime()%>">
			</td>
				<tr>
				<td>
					材料
				</td>
				<td>
			<input type=text name="material" size="20" value="<%=factoryCompose.getMaterial()%>">
			</td>
			<tr>
				<td>
					产品
				</td>
				<td>
			<input type=text name="product" size="20" value="<%=factoryCompose.getProduct()%>">
			</td>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>