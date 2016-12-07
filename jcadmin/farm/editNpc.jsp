<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();		
int id = action.getParameterInt("id");

FarmNpcBean npc =null;
npc = world.getNpc(id);
if (null != request.getParameter("add")) {
	String name = action.getParameterString("name");
	String intro = action.getParameterString("intro");
	String skill = action.getParameterString("skill");
	int flag = action.getParameterFlag("flag");
	String begin = action.getParameterString("begin");
	String end = action.getParameterString("end");
	int pos = action.getParameterInt("pos");
	String talk = action.getParameterString("talk");
	if (!name.equals("") ) {
	        npc.setName(name);
			npc.setIntro(intro);
			npc.setLearnSkill(skill);
			npc.setFlag(flag);
			npc.setQuestBegin(begin);
			npc.setQuestEnd(end);
			FarmWorld.nodeMoveObj(npc.getPos(), pos, npc);
			npc.setPos(pos);
			npc.setTalk(talk);
			npc.initTalk(world);
			npc.setCars(request.getParameter("cars"));
			npc.init();
			world.updateNpc(npc);
            response.sendRedirect("farmNpc.jsp");
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
<h3>编辑：NPC</h3>
<table width="100%">
<form method="post" action="editNpc.jsp?add=1&id=<%=id%>">
	<tr>
		<td>
			名称
		</td>
		<td>
	<input type=text name="name" size="20" value="<%=npc.getName()%>">
	</td></tr>
		<tr>
		<td>
			说明
		</td>
		<td>
	<textarea name="intro" cols="60" rows="2"><%=npc.getIntro()%></textarea>
	    </td></tr>
	    
		<tr>
		<td>
			从这个npc可以学的技能
		</td>
		<td>
	<input type=text name="skill" size="20" value="<%=npc.getLearnSkill()%>">
	</td></tr>
		<tr>
		<td>
			驿站路线
		</td>
		<td>
	<input type=text name="cars" size="20" value="<%=npc.getCars()%>">
	</td></tr>
		<tr>
		<td>
			标志位
		</td>
		<td>
	<%for(int flag=0;flag<FarmNpcBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>" <%if(npc.isFlag(flag)){%>checked<%}%>><%=FarmNpcBean.flagString[flag]%>
	 <%}%><br/>
	</td></tr>
		<tr>
		<td>
			开始任务
		</td>
		<td>
	<input type=text name="begin" size="20" value="<%=npc.getQuestBegin()%>">
	<% List questBeginList = npc.getQuestBeginList();
	for(int i=0;i<questBeginList.size();i++){
		Integer iid = (Integer)questBeginList.get(i);
		FarmQuestBean quest = world.getQuest(iid);
		if(quest==null) continue;
	%>
	<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
	<%}%>
	</td></tr>
		<tr>
		<td>
			结束任务
		</td>
		<td>
	<input type=text name="end" size="20" value="<%=npc.getQuestEnd()%>">
	<% List questEndList = npc.getQuestEndList();
	for(int i=0;i<questEndList.size();i++){
		Integer iid = (Integer)questEndList.get(i);
		FarmQuestBean quest = world.getQuest(iid);
		if(quest==null) continue;
	%>
	<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
	<%}%>
	</td></tr>
		<tr>
		<td>
			位置
		</td>
		<td>
	<input type=text name="pos" size="20" value="<%=npc.getPos()%>">
	<% MapNodeBean mapNode = FarmWorld.getWorld().getMapNode(npc.getPos());
	if(mapNode==null){%>无<%}else{%>
	<a href="editMapNode.jsp?id=<%=mapNode.getId()%>"><%=mapNode.getName()%></a>
	<%}%>
	</td></tr>
		<tr>
		<td>
			npc聊天内容
		</td>
	<td>
	<input type=text name="talk" size="20" value="<%=npc.getTalk()%>">
	</td></tr>
		
	
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
<br/>
<a href="farmNpc.jsp">返回上一级</a><br/><h3>聊天内容</h3>
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
			后续内容
		</td>
		<td>
			操作
		</td>
	</tr>
	<% List vec = npc.getTalkList();
	for (int i = 0; i < vec.size(); i++) {
		FarmTalkBean talk = (FarmTalkBean)vec.get(i);
		if(talk==null)continue;
	%>
	<tr>
		<td>
			<%=talk.getId()%>
		</td>
		<td>
			<%=talk.getTitle()%>
		</td>
		<td>
			<%=talk.getContent()%>
		</td>
		<td>
		<%for(int i2 = 0;i2<talk.getLinkList().size();i2++){
		FarmTalkBean talk2 = (FarmTalkBean)talk.getLinkList().get(i2);%>
			<%=i2+1%>.<a href="editTalk.jsp?id=<%=talk2.getId()%>"><%=talk2.getTitle()%></a><br/>
		<%}%>
		</td>
		<%/*%><td>
			<a href="farmTalk.jsp?delete=<%=talk.getId()%>">删除</a>
		</td>
		<%*/%>
		<td>
			<a href="editTalk.jsp?id=<%=talk.getId()%>">编辑</a>
		</td>
	</tr>
	<%}%>
</table>
	</body>
</html>