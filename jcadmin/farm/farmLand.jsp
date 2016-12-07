<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List landList = world.landList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,landList.size(),10);

			String prefixUrl = "farmLand.jsp";

			FarmService service = new FarmService();
			%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = action.getParameterString("name");
	int width = action.getParameterInt("width");
	int height = action.getParameterInt("height");
	String grid = action.getParameterString("grid");
	int rank = action.getParameterInt("rank");
	int pos = action.getParameterInt("pos");
	String item1 = action.getParameterString("item1");
	String item1Grid = action.getParameterString("item1Grid");
	String item2 = action.getParameterString("item2");
	String item2Grid = action.getParameterString("item2Grid");
	if (!name.equals("")) {
			LandMapBean land = new LandMapBean();
            land.setName(name);
            land.setRank(rank);
            land.setWidth(width);
            land.setHeight(height);
            land.setGrid(grid);
            land.setItem1(item1);
            land.setItem2(item2);
            land.setItem1Grid(item1Grid);
            land.setItem2Grid(item2Grid);
            FarmWorld.nodeMoveObj(land.getPos(), pos, land);
            land.setPos(pos);
            land.init();
			world.addLand(land);
			world.landMap.put(Integer.valueOf(land.getId()), land);
         response.sendRedirect("farmLand.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	List vec = landList.subList(paging.getStartIndex(),paging.getEndIndex());
	LandMapBean land = null;

	%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		采集的地图后台
		<br />
		<br />
		<form method="post" action="farmLand.jsp?add=1">
			名称：
			<input id="name" name="name"><br/>
			需要等级：
			 <input id="rank" name="rank"><br/>
			宽：
			 <input id="width" name="width"><br/>
			高：
			 <input id="height" name="height"><br/>
			位置：
			 <input id="pos" name="pos"><br/>
			 网格：
			 <textarea name="grid" cols="60" rows="2"></textarea><br/>
			 格1：
			 <input id="item1" name="item1"><br/>
			 格1内容：
			 <input id="item1Grid" name="item1Grid"><br/>
			 格2：
			 <input id="item2" name="item2"><br/>
			 格2内容：
			 <input id="item2Grid" name="item2Grid"><br/>
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>


		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					名称
				</td>
				<td>
					需要等级
				</td>
				<td>
					宽
				</td>
				<td>
					高
				</td>
				<td>
					位置
				</td>
				<td>
					网格
				</td>
				<td>
					格1
				</td>
				<td>
					格1内容
				</td>
				<td>
					格2
				</td>
				<td>
					格2内容
				</td>
				
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				land = (LandMapBean) vec.get(i);
%>
			<tr>
				<td>
					<%=land.getId()%>
				</td>
				<td>
					<%=land.getName()%>
					<input type="hidden" id="id" name="id" value="<%=land.getId() %>">
				</td>
				<td>
					<%=land.getRank()%>
				</td>
				<td>
					<%=land.getWidth()%>
				</td>
				<td>
					<%=land.getHeight()%>
				</td>
				<td>
					<%=land.getPos()%>
				</td>
				<td>
					<%=land.getGrid()%>
				</td>
				<td>
					<%=land.getItem1()%>
				</td>
				<td>
					<%=land.getItem1Grid()%>
				</td>
				<td>
					<%=land.getItem2()%>
				</td>
				<td>
					<%=land.getItem2Grid()%>
				</td>
				<td>
					<a href="editLand.jsp?id=<%=land.getId()%>">编辑</a>
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
