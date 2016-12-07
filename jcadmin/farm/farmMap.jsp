<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List nodeList = world.mapList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,nodeList.size(),20);

			String prefixUrl = "farmMap.jsp";

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
						MapBean mapNode = new MapBean();
						mapNode.setId(id);
						mapNode.setName(name);
						mapNode.setInfo(info);
						mapNode.setCondition(request.getParameter("condition"));
						mapNode.setX(action.getParameterInt("x"));
						mapNode.setY(action.getParameterInt("y"));
						mapNode.setFlag(action.getParameterFlag("flag"));
						mapNode.setExp(action.getParameterInt("exp"));
						mapNode.setRank(action.getParameterInt("rank"));
						mapNode.setSp(action.getParameterInt("sp"));
						mapNode.setCooldown(action.getParameterInt("cooldown"));
						mapNode.setEntryNode(action.getParameterInt("entryNode"));
						mapNode.init();
						world.addMap(mapNode);
                     response.sendRedirect("farmMap.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			//List vec = service.getMapNodeList
			//		(" 1=1 order by id limit "
			//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
			List vec = nodeList.subList(paging.getStartIndex(),paging.getEndIndex());
			MapBean mapNode = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
新手地图后台
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
			x坐标
		</td>
		<td>
			y坐标
		</td>
		<td>
			入口id
		</td>
		<td>
			标志
		</td>
		<td>
			经验值
		</td>
		<td>
			体力
		</td>
		<td>
			冷却
		</td>
		<td>
			等级
		</td>
		<td>
			条件
		</td>
		<td>
			地图
		</td>
		<td>
			操作
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		mapNode = (MapBean) vec.get(i);
%>
	<tr>
		<form method="post" action="farmMap.jsp?delete=<%=mapNode.getId()%>">
		<td>
			<%=mapNode.getId()%>
		</td>
		<td>
			<a href="farmMapNode.jsp?mapId=<%=mapNode.getId()%>"><%=mapNode.getName()%></a>
		</td>
		<td>
			<%=mapNode.getInfo()%>
		</td>
		<td>
			<%=mapNode.getX()%>
		</td>
		<td>
			<%=mapNode.getY()%>
		</td>
		<td>
			<%=mapNode.getEntryNode()%>
		</td>
		<td>
	<%for(int flag=0;flag<MapBean.FLAG_COUNT;flag++){%>
	 <%if(mapNode.isFlag(flag)){%><%=MapBean.flagString[flag]%>&nbsp;<%}%>
	 <%}%>
		</td>
		<td>
			<%=mapNode.getExp()%>
		</td>
		<td>
			<%=mapNode.getSp()%>
		</td>
		<td>
			<%=mapNode.getCooldown()%>
		</td>
		<td>
			<%=mapNode.getRank()%>
		</td>
		<td>
			<%=FarmWorld.getConditionString(mapNode.getConditionList())%>
		</td>
		<td>
			<img src="/farm/img/map/<%=mapNode.getId()%>.gif">
		</td>
		<td>
			<a href="editMap.jsp?id=<%=mapNode.getId()%>">编辑</a><br/><br/><a href="farmChat.jsp?id=<%=mapNode.getId()%>">聊天</a>
		</td>
	</tr>
	</form>
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
<form method="post" action="farmMap.jsp?add=1">
    id：
	<input id="id" name="id">&nbsp;
	名称：
	<input id="name" name="name"><br/>
	描述：
	 <textarea id="info" name="info"cols="60" rows="2"></textarea><br/>
	x坐标：
	 <input id="place" name="x">&nbsp;
	 y坐标：
	 <input id="map" name="y"><br/>
	 入口id：
	 <input id="map" name="entryNode"><br/>
	标志：
	<%for(int flag=0;flag<MapBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>"><%=MapBean.flagString[flag]%>
	 <%}%><br/>
	 经验值：<input id="exp" name="exp">&nbsp;
	 等级:<input name="rank"><br/>
	 体力:<input name="sp">&nbsp;冷却:<input name="cooldown"><br/>
	 进入条件:<input name="condition"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<br />
</body>
</html>
