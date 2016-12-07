<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();		
int id = action.getParameterInt("id");

FarmQuestBean quest =null;
quest = world.getQuest(id);
if (null != request.getParameter("add")) {
	String name = action.getParameterString("name");
	String info = action.getParameterString("info");
	String endInfo = action.getParameterString("endInfo");
	int rank = action.getParameterInt("rank");
	int flag = action.getParameterFlag("flag");
	int interval = action.getParameterInt("interval");
	int price = action.getParameterInt("price");
	String material = action.getParameterString("material");
	String product = action.getParameterString("product");
	String prize = action.getParameterString("prize");
	String preQuestId = action.getParameterString("preQuestId");
	if (!name.equals("") ) {
			quest.setName(name);
			quest.setInfo(info);
			quest.setEndInfo(endInfo);
			quest.setRank(rank);
			quest.setPrice(price);
			quest.setInterval(interval);
			quest.setMaterial(material);
			quest.setProduct(product);
			quest.setPrize(prize);
			quest.setPreQuestId(preQuestId);
			quest.setFlag(flag);
			quest.setCreature(request.getParameter("creature"));
			quest.setTalk(request.getParameter("talk"));
			quest.setSearch(request.getParameter("search"));
			quest.setGive(request.getParameter("give"));
			quest.setObjective(request.getParameter("objective"));
			quest.setRequestInfo(request.getParameter("request_info"));
			quest.setNext(action.getParameterInt("next"));
			quest.setMutex(request.getParameter("mutex"));
			quest.setCondition(request.getParameter("condition"));
			quest.setPreCondition(request.getParameter("preCondition"));
			quest.init();
			world.updateQuest(quest);
            response.sendRedirect("farmQuest.jsp");
	} else {%>
<script>
alert("请填写正确各项参数！");
</script>
    <%}}%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
<table width="100%">
<form method="post" action="editQuest.jsp?add=1&id=<%=id%>">
		<tr>
			<td>
				名称
			</td>
			<td>
		<input type=text name="name" size="20" value="<%=quest.getName()%>">
		</td></tr>
			<tr>
			<td>
				任务描述
			</td>
			<td>
		<textarea name="info" cols="60" rows="2"><%=quest.getInfo()%></textarea>
		</td></tr>
			<tr>
			<td>
				任务完成描述
			</td>
			<td>
		<textarea name="endInfo" cols="60" rows="2"><%=quest.getEndInfo()%></textarea>
		</td></tr>
			<tr>
			<td>
				任务目标描述
			</td>
			<td>
		<textarea name="objective" cols="60" rows="2"><%=quest.getObjective()%></textarea>
		</td></tr>
			<tr>
			<td>
				任务未完成描述
			</td>
			<td>
		<textarea name="request_info" cols="60" rows="2"><%=quest.getRequestInfo()%></textarea>
		</td></tr>
			<tr>
			<td>
				等级
			</td>
			<td>
		<input type=text name="rank" size="20" value="<%=quest.getRank()%>">
		</td></tr>
			<tr>
			<td>
				任务需要的铜板
			</td>
			<td>
		<input type=text name="price" size="20" value="<%=quest.getPrice()%>">
		</td></tr>
			<tr>
			<td>
				给予
			</td>
			<td>
		<input type=text name="give" size="20" value="<%=quest.getGive()%>">
		</td>
			<tr>
			<td>
				材料
			</td>
			<td>
		<input type=text name="material" size="20" value="<%=quest.getMaterial()%>">
		</td></tr>
			<tr>
			<td>
				狩猎
			</td>
			<td>
		<input type=text name="creature" size="20" value="<%=quest.getCreature()%>">
		</td>
			<tr>
			<td>
				聊天
			</td>
			<td>
		<input type=text name="talk" size="20" value="<%=quest.getTalk()%>">
		</td>
			<tr>
			<td>
				搜索
			</td>
			<td>
		<input type=text name="search" size="20" value="<%=quest.getSearch()%>">
		</td>
		</tr>
			<tr>
			<td>
				产品
			</td>
			<td>
		<input type=text name="product" size="20" value="<%=quest.getProduct()%>">
		</td></tr>
			<tr>
			<td>
				其它奖励
			</td>
			<td>
		<input type=text name="prize" size="20" value="<%=quest.getPrize()%>">
		</td></tr>
			<tr>
			<td>
				先修任务
			</td>
			<td>
		<input type=text name="preQuestId" size="20" value="<%=quest.getPreQuestId()%>">
		</td></tr>
			<tr>
			<td>
				互斥任务
			</td>
			<td>
		<input type=text name="mutex" size="20" value="<%=quest.getMutex()%>">
		</td></tr>
			<tr>
			<td>
				先修条件
			</td>
			<td>
		<input type=text name="preCondition" size="20" value="<%=quest.getPreCondition()%>">&nbsp;<%=FarmWorld.getConditionString(quest.getPreConditionList())%>
		</td></tr>
			<tr>
			<td>
				完成条件
			</td>
			<td>
		<input type=text name="condition" size="20" value="<%=quest.getCondition()%>">&nbsp;<%=FarmWorld.getConditionString(quest.getConditionList())%>
		</td></tr>
			<tr>
			<td>
				重复完成周期(分钟)
			</td>
			<td>
		<input type=text name="interval" size="20" value="<%=quest.getIntervalMinute()%>">
		</td></tr>
			<tr>
			<td>
				后续任务
			</td>
			<td>
		<input type=text name="next" size="20" value="<%=quest.getNext()%>">
		<%
		FarmQuestBean next = world.getQuest(quest.getNext());
		if(next!=null){
		%>&nbsp;(<%=next.getName()%>)
		<%}%>
		</td></tr>
			<tr>
			<td width="100">
			    标志
			</td>
		
		<td>
		<%for(int flag=0;flag<FarmQuestBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox width="100" name="flag" value="<%=flag%>" <%if(quest.isFlag(flag)){%>checked<%}%>><%=FarmQuestBean.flagString[flag]%>
		 <%}%><br/>
		</td>
		</tr>
			<tr>
			<td>
				接受任务
			</td>
			<td>
		<%
		List beginList = FarmNpcWorld.getQuestBeginNpc(quest.getId());
		for(int i=0;i<beginList.size();i++){
		FarmNpcBean npc = (FarmNpcBean)beginList.get(i);
		%>
		<a href="editNpc.jsp?id=<%=npc.getId()%>"><%=npc.getName()%></a>,
		<%}%>
		</td></tr>
			<tr>
			<td>
				提交任务
			</td>
			<td>
		<%
		List endList = FarmNpcWorld.getQuestEndNpc(quest.getId());
		for(int i=0;i<endList.size();i++){
		FarmNpcBean npc = (FarmNpcBean)endList.get(i);
		%>
		<a href="editNpc.jsp?id=<%=npc.getId()%>"><%=npc.getName()%></a>,
		<%}%>
		</td></tr>
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
			<br />
</body>
</html>