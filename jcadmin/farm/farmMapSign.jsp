<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List mapSignList = world.signList;

PagingBean paging = new PagingBean(action,mapSignList.size(),20,"p");

	String prefixUrl = "farmMapSign.jsp";

	if (null != request.getParameter("delete")) {/*
		int id = StringUtil.toInt(request.getParameter("delete"));
		dbOp = new DbOperation();
		dbOp.init();
		dbOp
				.executeUpdate("delete from farm_map_node where id="
						+ id);
		dbOp.release();*/
	}

	%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = request.getParameter("name").trim();
	String info = request.getParameter("info").trim();
	if (!name.equals("")) {
			MapSignBean mapSign = new MapSignBean();
			mapSign.setId(id);
			mapSign.setName(name);
			mapSign.setInfo(info);
			mapSign.setPosId(action.getParameterInt("posId"));
			mapSign.setDistance(action.getParameterInt("distance"));
			mapSign.setFlag(action.getParameterFlag("flag"));
			mapSign.setNode(world.getMapNode(mapSign.getPosId()));
			mapSign.init();
			world.addMapSign(mapSign);
         response.sendRedirect("farmMapSign.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	List vec = mapSignList.subList(paging.getStartIndex(),paging.getEndIndex());
	MapSignBean mapSign = null;

	%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
	地图路标
	<br />

	<table width="100%" class="farmTable">
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
				地图结点id
			</td>
			<td>
				距离^2
			</td>
			<td>
				标志
			</td>
		</tr>
		<%for (int i = 0; i < vec.size(); i++) {
			mapSign = (MapSignBean) vec.get(i);
%>
		<tr>
			<form method="post" action="farmMapSign.jsp?delete=<%=mapSign.getId()%>">
			<td>
				<%=mapSign.getId()%>
			</td>
			<td>
				<a href="editMapSign.jsp?id=<%=mapSign.getId()%>"><%=mapSign.getName()%></a>
			</td>
			<td>
				<%=mapSign.getInfo()%>
			</td>
			<td>
				<%=mapSign.getPosId()%>
			</td>
			<td>
				<%=mapSign.getDistance()%>
			</td>
				<td>
			<%for(int flag=0;flag<MapSignBean.FLAG_COUNT;flag++){%>
			 <%if(mapSign.isFlag(flag)){%><%=MapSignBean.flagString[flag]%>&nbsp;<%}%>
			 <%}%><br/>
				</td>
		</tr>
		</form>
		<%}%>
	</table>
	<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
<%@include file="bottom.jsp"%>
	<br />
	<form method="post" action="farmMapSign.jsp?add=1">
	    id：<input id="id" name="id">&nbsp;
		名称：<input id="name" name="name"><br/>
		描述：<textarea id="info" name="info"cols="60" rows="2"></textarea><br/>
		地图位置：<input name="posId"><br/>
		距离：<input name="distance" value="200"><br/>
		标志：
		<%for(int flag=0;flag<MapSignBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox name="flag" value="<%=flag%>"><%=MapSignBean.flagString[flag]%>
		 <%}%><br/>
		<input type="submit" id="add" name="add" value="增加">
		<br />
	</form>
	<br />
</body>
</html>
