<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();
List stoneList = world.stoneList;
		
int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,stoneList.size(),20);

String prefixUrl = "farmStone.jsp";

%>
<%if (null != request.getParameter("add")) {
	String name = action.getParameterString("name");
	if (!name.equals("")) {
			FarmStoneBean stone = new FarmStoneBean();
			stone.setId(action.getParameterInt("id"));
            stone.setName(name);
            stone.setType(action.getParameterInt("type"));
            stone.setInfo(request.getParameter("info"));
            stone.setValue(request.getParameter("value"));
            int pos = action.getParameterInt("pos");
            FarmWorld.nodeMoveObj(stone.getPos(), pos, stone);
            stone.setPos(pos);
			world.addStone(stone);
         response.sendRedirect("farmStone.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	List vec = stoneList.subList(paging.getStartIndex(),paging.getEndIndex());
	FarmStoneBean stone = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
	七彩石后台
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
		所在地图
	</td>
	<td>
		类型
	</td>
	<td>
		参数
	</td>
	<td>
		操作
	</td>
</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		stone = (FarmStoneBean) vec.get(i);
%>
<tr>
	<td>
		<%=stone.getId()%>
	</td>
	<td>
		<%=stone.getName()%>
	</td>
	<td>
		<%=stone.getInfo()%>
	</td>
	<td>
<% MapNodeBean mapNode = FarmWorld.getWorld().getMapNode(stone.getPos());
if(mapNode==null){%>无<%}else{%>
<a href="editMapNode.jsp?id=<%=mapNode.getId()%>"><%=mapNode.getName()%></a>
<%}%>	(<%=stone.getPos()%>)
		</td>
		<td>
			<%=stone.getType()%>
		</td>
		<td>
			<%=stone.getValue()%>
		</td>
		<td>
			<a href="editStone.jsp?id=<%=stone.getId()%>">编辑</a>
		</td>
	</tr>
	
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
<form method="post" action="farmStone.jsp?add=1">
	id：<input name="id"><br/>
	名称：<input name="name"><br/>
	内容：<textarea name="info" cols="60" rows="2"></textarea><br/>
	所在地图：<input name="pos"><br/>
	类型：<input name="type">&nbsp;
	参数：<input name="value"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<br />
</body>
</html>
