<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List cropList = world.cropList;
int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,cropList.size(),20);

			String prefixUrl = "farmCrop.jsp";

			FarmService service = new FarmService();
			%>
<%if (null != request.getParameter("add")) {
                int id = action.getParameterInt("id");
				String name = action.getParameterString("name");
				int rank = action.getParameterInt("rank");
				int growTime = action.getParameterInt("growTime");
				int rotTime = action.getParameterInt("rotTime");
				int product = action.getParameterInt("product");
				int actInterval = action.getParameterInt("actInterval");
				int productCount = action.getParameterInt("productCount");
				int proId = action.getParameterInt("proId");
				if (!name.equals("")) {
						FarmCropBean crop = new FarmCropBean();
			            crop.setName(name);
			            crop.setRank(rank);
			            crop.setGrowTime(growTime);
			            crop.setRotTime(rotTime);
			            crop.setProduct(product);
			            crop.setActInterval(actInterval);
			            crop.setProductCount(productCount);
			            crop.setProId(proId);
						world.addCrop(crop);
                     response.sendRedirect("farmCrop.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = cropList.subList(paging.getStartIndex(),paging.getEndIndex());
			FarmCropBean crop = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		采集的作物后台
		<br />
		<br />
		<form method="post" action="farmCrop.jsp?add=1">
			作物名称：
			<input id="name" name="name"><br/>
			需要等级：
			 <input id="rank" name="rank"><br/>
			生长需要的时间：
			 <input id="growTime" name="growTime"><br/>
			多少时间后腐烂：
			 <input id="rotTime" name="rotTime"><br/>
			 产品：
			 <input id="product" name="product"><br/>
			 灌溉周期：
			 <input id="actInterval" name="actInterval"><br/>
			 收获：
			 <input id="productCount" name="productCount"><br/>
			 对应的专业：
			 <input id="proId" name="proId"><br/>
			 
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>


		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					作物名称
				</td>
				<td>
					需要等级
				</td>
				<td>
					生长需要的时间
				</td>
				<td>
					多少时间后腐烂
				</td>
				<td>
					产品
				</td>
				<td>
					灌溉周期
				</td>
				<td>
					收获
				</td>
				<td>
					对应的专业
				</td>
				
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				crop = (FarmCropBean) vec.get(i);
%>
			<tr>
				<td>
					<%=crop.getId()%>
				</td>
				<td>
					<%=crop.getName()%>
					<input type="hidden" id="id" name="id" value="<%=crop.getId() %>">
				</td>
				<td>
					<%=crop.getRank()%>
				</td>
				<td>
					<%=crop.getGrowTime()%>
				</td>
				<td>
					<%=crop.getRotTime()%>
				</td>
				<td>
					<%=crop.getProduct()%>
				</td>
				<td>
					<%=crop.getActInterval()%>
				</td>
				<td>
					<%=crop.getProductCount()%>
				</td>
				<td>
					<%=crop.getProId()%>
				</td>
				
				<td>
					<a href="editCrop.jsp?id=<%=crop.getId()%>">编辑</a>
				</td>
			</tr>
			
			<%}%>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
