<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List pickList = world.pickTList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,pickList.size(),20);

			String prefixUrl = "farmPick2.jsp";

			%>
<%if (null != request.getParameter("add")) {
                int id = action.getParameterInt("id");
				String items = action.getParameterString("items");
				if (!items.equals("")) {
						PickTBean pick = new PickTBean();
						pick.setId(id);
			            pick.setItems(items);
			            pick.setPos(request.getParameter("pos"));
			            pick.setCooldown(action.getParameterInt("cooldown")*1000);
			            pick.init();
						world.addPickT(pick);
                     response.sendRedirect("farmPick2.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = pickList.subList(paging.getStartIndex(),paging.getEndIndex());
			PickTBean pick = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
	大地图采集
	<br />

	<table width="100%">
		<tr>
			<td>
				id
			</td>
			<td>
				物品
			</td>
			<td>
				位置
			</td>
			<td>
				刷新间隔
			</td>
			<td>
				操作
			</td>
		</tr>
		<%for (int i = 0; i < vec.size(); i++) {
			pick = (PickTBean) vec.get(i);
%>
		<tr>
			<td>
				<%=pick.getId()%>
			</td>
			<td>
<% List itemList = pick.getItemList();
for(int j=0;j<itemList.size();j++){
Integer iid = (Integer)itemList.get(j);
LandItemBean li = world.getLandItem(iid.intValue());
%><%if(j>0){%>,<%}%><%=li.getName()%><%}%>
			</td>
			<td>
				<%=pick.getPos()%>
			</td>
			<td>
				<%=pick.getCooldown()/1000%>秒
			</td>
			<td>
				<a href="editPick2.jsp?id=<%=pick.getId()%>">编辑</a>
			</td>
		</tr>
		
		<%}%>
	</table>
	<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
	<br />
	<form method="post" action="farmPick2.jsp?add=1">
		物品：
		 <input name="items"><br/>
		位置：
		 <input name="pos" value="1"><br/>
		刷新间隔：
		 <input name="cooldown" value="600"><br/>
		<input type="submit" id="add" name="add" value="增加">
		<br />
	</form>
	<a href="index.jsp">返回新手管理首页</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
	<br />
</body>
</html>
