<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();
List factoryList = FarmNpcWorld.factoryList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,factoryList.size(),20);

			String prefixUrl = "farmFactory.jsp";

			FarmService service = new FarmService();
			%>
<%if (null != request.getParameter("add")) {
                int id = action.getParameterInt("id");
				String name = action.getParameterString("name");
				String info = action.getParameterString("info");
				int rank = action.getParameterInt("rank");
				int interval = action.getParameterInt("interval");
				int pos = action.getParameterInt("pos");
				if (!name.equals("")) {
						FactoryBean factory = new FactoryBean();
			            factory.setName(name);
			            factory.setRank(rank);
			            factory.setInfo(info);
			            factory.setInterval(interval);
			            FarmWorld.nodeMoveObj(factory.getPos(), pos, factory);
			            factory.setPos(pos);
						world.addFactory(factory);
                     response.sendRedirect("farmFactory.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = factoryList.subList(paging.getStartIndex(),paging.getEndIndex());
			FactoryBean factory = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		加工厂的数据后台
		<br />
		<br />
		<form method="post" action="farmFactory.jsp?add=1">
			名称：
			<input id="name" name="name"><br/>
			需要等级：
			 <input id="rank" name="rank"><br/>
			介绍：
			 <textarea name="info" cols="60" rows="2"></textarea><br/>
			加工间隔：
			 <input id="interval" name="interval"><br/>
			所在地图：
			 <input id="pos" name="pos"><br/>
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
					介绍
				</td>
				<td>
					加工间隔
				</td>
				<td>
					所在地图
				</td>
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				factory = (FactoryBean) vec.get(i);
%>
			<tr>
				<td>
					<%=factory.getId()%>
				</td>
				<td>
					<%=factory.getName()%>
				</td>
				<td>
					<%=factory.getRank()%>
				</td>
				<td>
					<%=factory.getInfo()%>
				</td>
				<td>
					<%=factory.getInterval()%>
				</td>
				<td>
					<%=factory.getPos()%>
				</td>
				
				<td>
					<a href="editFactory.jsp?id=<%=factory.getId()%>">编辑</a>
				</td>
				<td>
					<a href="farmFactoryCompose.jsp?factoryId=<%=factory.getId()%>">订单</a>&nbsp;&nbsp;
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
