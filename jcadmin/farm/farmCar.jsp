<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();
List carList = world.carList;

PagingBean paging = new PagingBean(action,carList.size(),20,"p");

	String prefixUrl = "farmCar.jsp";

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
			FarmCarBean car = new FarmCarBean();
			car.setId(id);
			car.setName(name);
			car.setInfo(info);
			car.setLine(request.getParameter("line"));
			car.setQuestId(action.getParameterInt("questId"));
			car.setMoney(action.getParameterInt("money"));
			car.setCooldown(action.getParameterInt("cooldown")*1000);
			car.setFlag(action.getParameterFlag("flag"));
			car.init();
			world.addCar(car);
         response.sendRedirect("farmCar.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	List vec = carList.subList(paging.getStartIndex(),paging.getEndIndex());
	FarmCarBean car = null;

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
				任务
			</td>
			<td>
				价钱
			</td>
			<td>
				长度
			</td>
			<td>
				间隔
			</td>
			<td>
				标志
			</td>
		</tr>
		<%for (int i = 0; i < vec.size(); i++) {
			car = (FarmCarBean) vec.get(i);
%>
		<tr>
			<form method="post" action="farmCar.jsp?delete=<%=car.getId()%>">
			<td>
				<%=car.getId()%>
			</td>
			<td>
				<a href="editCar.jsp?id=<%=car.getId()%>"><%=car.getName()%></a>
			</td>
			<td>
				<%=car.getInfo()%>
			</td>
			<td>
			<% FarmQuestBean quest = world.getQuest(car.getQuestId());
			if(quest!=null){
			%><a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
			<%}else{%><%=car.getQuestId()%><%}%>
			</td>
			<td>
				<%=FarmWorld.formatMoney(car.getMoney())%>
			</td>
			<td>
				<%=car.getLineList().size()%>
			</td>
			<td>
				<%=car.getCooldown()/1000%>秒
			</td>
				<td>
			<%for(int flag=0;flag<FarmCarBean.FLAG_COUNT;flag++){%>
			 <%if(car.isFlag(flag)){%><%=FarmCarBean.flagString[flag]%>&nbsp;<%}%>
			 <%}%><br/>
				</td>
		</tr>
		</form>
		<%}%>
	</table>
	<%=paging.shuzifenye(prefixUrl, false, "|", response)%>
	<a href="index.jsp">返回新手管理首页</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
	<br />
	<form method="post" action="farmCar.jsp?add=1">
	    id：<input id="id" name="id">&nbsp;
		名称：<input id="name" name="name"><br/>
		描述：<textarea id="info" name="info"cols="60" rows="2"></textarea><br/>
		任务id：<input name="questId"><br/>
		冷却：<input name="cooldown" value="2">秒<br/>
		价钱：<input name="money"><br/>
		线路：<input name="line"><br/>
		标志：
		<%for(int flag=0;flag<FarmCarBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox name="flag" value="<%=flag%>"><%=FarmCarBean.flagString[flag]%>
		 <%}%><br/>
		<input type="submit" id="add" name="add" value="增加">
		<br />
	</form>
	<br />
</body>
</html>
