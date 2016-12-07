<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);

FarmWorld world = FarmWorld.getWorld();
List nodeList = world.nodeList;

			String prefixUrl = "farmMapNode.jsp";

			FarmService service = new FarmService();

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
                String ids = request.getParameter("id");
				String name = request.getParameter("name").trim();
				String info = request.getParameter("info").trim();
				String link = request.getParameter("link").trim();
				if (!name.equals("")&&ids!=null) {
					String[] sid = ids.split(",");
					for(int i=0;i<sid.length;i++){
						int id = StringUtil.toInt(sid[i]);
						if(id<=0) continue;
						MapNodeBean mapNode = new MapNodeBean();
						mapNode.setId(id);
						mapNode.setName(name);
						mapNode.setInfo(info);
						mapNode.setLink(link);
						mapNode.initLink(world);
						mapNode.setMapId(action.getParameterInt("mapId"));
						world.addMapNode(mapNode);
					}
                     response.sendRedirect("farmMapNode.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			//List vec = service.getMapNodeList
			//		(" 1=1 order by id limit "
			//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
MapNodeBean mapNode = null;
List vec = null;

int mapId = action.getParameterIntS("mapId");
if(mapId >= 0) {
	session.setAttribute("mapId", Integer.valueOf(mapId));
} else {
	Integer iid = (Integer)session.getAttribute("mapId");
	if(iid==null)
		mapId = -1;
	else
		mapId = iid.intValue();
}
List cut;
if(mapId >= 0) {
	cut = new ArrayList();
	for(int i = 0;i < nodeList.size();i++){
		MapNodeBean n = (MapNodeBean)nodeList.get(i);
		if(n.getMapId()==mapId)
			cut.add(n);
	}
} else {
	cut = nodeList;
}

MapNodeBean copyNode = null;
int copy = action.getParameterIntS("copy");
if(copy>0)
	copyNode = world.getMapNode(copy);
PagingBean paging = new PagingBean(action,cut.size(),20, "p");
vec = cut.subList(paging.getStartIndex(),paging.getEndIndex());

%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		新手地图后台
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
					内容
				</td>
				<td>
					所属地图
				</td>
				<td>
					位置
				</td>
				<td>
					经验
				</td>
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				mapNode = (MapNodeBean) vec.get(i);
				MapBean map = world.getMap(mapNode.getMapId());
%>
			<tr>
				<form method="post" action="farmMapNode.jsp?delete=<%=mapNode.getId()%>">
				<td>
					<%=mapNode.getId()%>
				</td>
				<td>
					<%=mapNode.getName()%>
					<input type="hidden" id="id" name="id" value="<%=mapNode.getId() %>">
				</td>
				<td>
					<%=mapNode.getInfo()%>
				</td>
				<td>
					<%if(map!=null){%><%=map.getName()%><%}else{%>无<%}%>
				</td>
				<td>
					<%=mapNode.getLink()%>
				</td>
				<td>
					<%=mapNode.getExp()%>
				</td>
				<%/*%><td>
					<a href="farmMapNode.jsp?delete=<%=mapNode.getId()%>">删除</a>
				</td>
				<%*/%>
				<td width="80">
					<a href="editMapNode.jsp?id=<%=mapNode.getId()%>">编辑</a>|<a href="farmMapNode.jsp?copy=<%=mapNode.getId()%>">复制</a>
				</td>
			</tr>
			</form>
			<%}%>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response, 30)%>

		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
		<form method="post" action="farmMapNode.jsp?add=1">
		    id：
			<input id="id" name="id">&nbsp;
			名称：
			<input id="name" name="name" <%if(copyNode!=null){%>value="<%=copyNode.getName()%>"<%}%>><br/>
			内容：
			 <textarea id="info" name="info"cols="60" rows="2"><%if(copyNode!=null){%><%=copyNode.getInfo()%><%}%></textarea><br/>
			连接结点：
			 <input id="link" name="link">&nbsp;
			 所属地图：
			 <input id="mapId" name="mapId" <%if(mapId>=0){%>value="<%=mapId%>"<%}%>><br/>
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>
		<br />
	</body>
</html>
