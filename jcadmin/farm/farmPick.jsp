<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List pickList = world.pickList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,pickList.size(),20);

	String prefixUrl = "farmPick.jsp";

	%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = action.getParameterString("name");
	String info = action.getParameterString("info");
	String items = action.getParameterString("items");
	int pos = action.getParameterInt("pos");
	if (!name.equals("")) {
			MapPickBean pick = new MapPickBean();
			pick.setId(id);
            pick.setName(name);
            pick.setInfo(info);
            pick.setItems(items);
            pick.setQuestId(action.getParameterInt("questId"));
            FarmWorld.nodeMoveObj(pick.getPos(), pos, pick);
            pick.setPos(pos);
            pick.init();
			world.addPick(pick);
         response.sendRedirect("farmPick.jsp");
	} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = pickList.subList(paging.getStartIndex(),paging.getEndIndex());
			MapPickBean pick = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		任务宝箱
		<br />

		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					名称
				</td>
				<td>
					描述
				</td>
				<td>
					物品
				</td>
				<td>
					位置
				</td>
				<td>
					任务
				</td>
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				pick = (MapPickBean) vec.get(i);
%>
			<tr>
				<td>
					<%=pick.getId()%>
				</td>
				<td>
					<%=pick.getName()%>
				</td>
				<td>
					<%=pick.getInfo()%>
				</td>
				<td>
					<%=pick.getItems()%>
				</td>
				<td>
					<%=pick.getPos()%>
				</td>
				<td>
					<%=pick.getQuestId()%>
				</td>
				<td>
					<a href="editPick.jsp?id=<%=pick.getId()%>">编辑</a>
				</td>
			</tr>
			
			<%}%>
			<tr>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
		<br />
		<form method="post" action="farmPick.jsp?add=1">
			名称：
			<input id="name" name="name"><br/>
			描述：
			 <textarea name="info" cols="60" rows="2"></textarea><br/>
			物品：
			 <input id="items" name="items"><br/>
			位置：
			 <input id="pos" name="pos"><br/>
			任务：
			 <input id="questId" name="questId"><br/>
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>
		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
