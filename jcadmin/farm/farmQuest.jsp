<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List questList = FarmNpcWorld.questList;

PagingBean paging = new PagingBean(action,questList.size(),20,"p");

int j = 0;
String prefixUrl = "farmQuest.jsp";
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
	String name = action.getParameterString("name");
	String info = action.getParameterString("info");
	int rank = action.getParameterInt("rank");
	int price = action.getParameterInt("price");
	int interval = action.getParameterInt("interval");
	String material = action.getParameterString("material");
	String product = action.getParameterString("product");
	String prize = action.getParameterString("prize");
	String preQuestId = action.getParameterString("preQuestId");
	String endInfo = action.getParameterString("endInfo");
	int flag = action.getParameterFlag("flag");
	if (!name.equals("")) {
			FarmQuestBean quest = new FarmQuestBean();
			quest.setName(name);
			quest.setInfo(info);
			quest.setRank(rank);
			quest.setPrice(price);
			quest.setInterval(interval);
			quest.setMaterial(material);
			quest.setProduct(product);
			quest.setPrize(prize);
			quest.setPreQuestId(preQuestId);
			quest.setFlag(flag);
			quest.setEndInfo(endInfo);
			quest.setCreature(request.getParameter("creature"));
			quest.setTalk(request.getParameter("talk"));
			quest.setSearch(request.getParameter("search"));
			quest.setGive(request.getParameter("give"));
			quest.setObjective(request.getParameter("objective"));
			quest.setRequestInfo(request.getParameter("requestInfo"));
			quest.init();
			world.addQuest(quest);
      response.sendRedirect("farmQuest.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
</script>
<%}
	}
	//List vec = service.getMapNodeList
	//		(" 1=1 order by id limit "
	//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
	List vec = questList.subList(paging.getStartIndex(),paging.getEndIndex());
	FarmQuestBean quest = null;

	%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
新手任务后台
<br />

<table width="100%">
	<tr>
		<td>
			id
		</td>
		<td width="70">
			名称
		</td>
		<td>
			任务描述
		</td>
		<td>
			等级
		</td>
		<td width="40">
			需要铜板
		</td>
		<td width="80">
			给予
		</td>
		<td width="80">
			材料
		</td>
		<td>
			狩猎
		</td>
		<td>
			聊天
		</td>
		<td>
			搜索
		</td>
		<td>
			产品
		</td>
		<td>
			其它奖励
		</td>
		<td>
			先修任务
		</td>
		<td>
			后续任务
		</td>
		<td width="40">
			标志
		</td>
		<td>
			结束语
		</td>
		<td>
			操作
		</td>
	</tr>
		<%for (int i = 0; i < vec.size(); i++) {
			quest = (FarmQuestBean) vec.get(i);
%>
	<tr>
		<td>
			<%=quest.getId()%>
		</td>
		<td width="70">
			<%=quest.getName()%>
			<input type="hidden" id="id" name="id" value="<%=quest.getId() %>">
		</td>
		<td>
			<%=quest.getInfo()%>
		</td>
		<td>
			<%=quest.getRank()%>
		</td>
		<td width="40">
			<%=quest.getPrice()%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(quest.getGiveList())%>
		</td>
		<td width="80">
			<%=FarmWorld.getItemListString(quest.getMaterialList())%>
		</td>
		<td>
			<%=FarmWorld.getCreatureListString(quest.getCreatureList())%>
		</td>
		<td>
			<%=quest.getTalk()%>
		</td>
		<td>
			<%=quest.getSearch()%>
		</td>
		<td width="80">
			<%=FarmWorld.getItemListString(quest.getProductList())%>
		</td>
		<td width="60">
			<%=FarmWorld.getPrizeString(quest.getPrizeList())%>
		</td>
		<td>
			<%=quest.getPreQuestId()%>
		</td>
		<td>
			<%=quest.getNext()%>
		</td>
		<td width="40">
	<%for(int flag=0;flag<FarmQuestBean.FLAG_COUNT;flag++){%>
	 <%if(quest.isFlag(flag)){%><%=FarmQuestBean.flagString[flag]%>&nbsp;<%}%>
	 <%}%><br/>
		</td>
		<td>
			<%=quest.getEndInfo()%>
		</td>
		<%/*%><td>
			<a href="farmQuest.jsp?delete=<%=quest.getId()%>">删除</a>
		</td>
		<%*/%>
		<td>
			<a href="editQuest.jsp?id=<%=quest.getId()%>">编辑</a>
		</td>
	</tr>
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response, 20)%>
<br />
<form method="post" action="farmQuest.jsp?add=1">
	名称：
	<input id="name" name="name"><br/>
	任务描述：
	 <textarea id="info" name="info" cols="60" rows="2"></textarea><br/>
	等级：
	 <input id="rank" name="rank" value="0"><br/>
	 任务需要的铜板：
	 <input id="price" name="price" value="0"><br/>
	 给予：<input name="give">&nbsp;
	 材料：<input name="material"><br/>
	 狩猎：<input name="creature"><br/>
	 聊天：<input name="talk">&nbsp;
	 搜索：<input name="search"><br/>
	 产品：
	 <input id="product" name="product"><br/>
	 其它奖励：
	 <input id="prize" name="prize"><br/>
	 先修任务：
	 <input id="preQuestId" name="preQuestId"><br/>
	 重复完成间隔(分钟)：
	 <input id="interval" name="interval" value="0"><br/>
	 标志：
	<%for(int flag=0;flag<FarmQuestBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>"><%=FarmQuestBean.flagString[flag]%>
	 <%}%><br/>
	 任务目标描述：
	 <textarea name="objective" cols="60" rows="2"></textarea><br/>
	 结束语：<textarea name="endInfo" cols="60" rows="2"></textarea><br/>
	 未完成描述：<textarea name="requestInfo" cols="60" rows="2"></textarea><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>

<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
</body>
</html>
