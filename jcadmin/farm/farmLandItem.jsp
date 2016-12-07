<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List landItemList = world.landItemList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,landItemList.size(),20);

			String prefixUrl = "farmLandItem.jsp";

			FarmService service = new FarmService();
			%>
<%if (null != request.getParameter("add")) {
                int itemId = action.getParameterInt("itemId");
				String name = action.getParameterString("name");
				int rank = action.getParameterInt("rank");
				int proId = action.getParameterInt("proId");
				int min = action.getParameterInt("min");
				int max = action.getParameterInt("max");
				
				if (!name.equals("")) {
						LandItemBean landItem = new LandItemBean();
						landItem.setId(action.getParameterInt("id"));
			            landItem.setName(name);
			            landItem.setRank(rank);
			            landItem.setItemId(itemId);
			            landItem.setProId(proId);
			            landItem.setMin(min);
			            landItem.setMax(max);
						world.addLandItem(landItem);
                     response.sendRedirect("farmLandItem.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = landItemList.subList(paging.getStartIndex(),paging.getEndIndex());
			LandItemBean landItem = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		采集的物品后台
		<br />


		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					得到的物品id
				</td>
				<td>
					等级
				</td>
				<td>
					拣起来之前的名字
				</td>
				<td>
					需要的采集专业
				</td>
				<td>
					物品的最小个数
				</td>
				<td>
					物品的最大个数
				</td>
				
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				landItem = (LandItemBean) vec.get(i);
%>
			<tr>
				<td>
					<%=landItem.getId()%>
				</td>
				<td>
					<%=landItem.getItemId()%>
				</td>
				<td>
					<%=landItem.getRank()%>
				</td>
				<td>
					<%=landItem.getName()%>
					<input type="hidden" id="id" name="id" value="<%=landItem.getId() %>">
				</td>
				
				<td>
					<%=landItem.getProId()%>
				</td>
				<td>
					<%=landItem.getMin()%>
				</td>
				<td>
					<%=landItem.getMax()%>
				</td>
				<td>
					<a href="editLandItem.jsp?id=<%=landItem.getId()%>">编辑</a>
				</td>
			</tr>
			
			<%}%>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
				<br />
		<form method="post" action="farmLandItem.jsp?add=1">
		id:<input name="id"><br/>
		     得到的物品id：
			 <input name="itemId"><br/>
			需要等级：
			 <input id="rank" name="rank"><br/>
			 拣起来之前的名字：
			<input id="name" name="name"><br/>
			需要的采集专业：
			 <input id="proId" name="proId"><br/>
			 物品的最小个数：
			<input id="min" name="min">
			<br/>
			 物品的最大个数：
			 <input id="max" name="max"><br/>
			 
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>
		<br />
	</body>
</html>
