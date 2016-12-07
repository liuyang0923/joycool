<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List npcList = world.npcList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,npcList.size(),20);

	int j = 0;
	String prefixUrl = "farmNpc.jsp";

	NpcService service = new NpcService();

	if (null != request.getParameter("delete")) {/*
		int id = StringUtil.toInt(request.getParameter("delete"));
		dbOp = new DbOperation();
		dbOp.init();
		dbOp
				.executeUpdate("delete from farm_npc where id="
						+ id);
		dbOp.release();*/
	}

	%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = action.getParameterString("name");
	String intro = action.getParameterString("intro");
	String skill = action.getParameterString("skill");
	int flag = action.getParameterFlag("flag");
	String begin = action.getParameterString("begin");
	String end = action.getParameterString("end");
	int pos = action.getParameterInt("pos");
	String talk = action.getParameterString("talk");
	if (!name.equals("") ) {
		FarmNpcBean npc = new FarmNpcBean();
		
		npc.setName(name);
		npc.setIntro(intro);
		npc.setLearnSkill(skill);
		npc.setFlag(flag);
		npc.setQuestBegin(begin);
		npc.setQuestEnd(end);
		npc.setPos(pos);
		FarmWorld.nodeAddObj(pos, npc);
		npc.setTalk(talk);
		npc.initTalk(world);
		npc.init();
		world.addNpc(npc);
        response.sendRedirect("farmNpc.jsp");
	} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
	}
	//List vec = service.getMapNodeList
	//		(" 1=1 order by id limit "
	//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
	List vec = npcList.subList(paging.getStartIndex(),paging.getEndIndex());
	FarmNpcBean npc = null;

	%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
	npc后台
	<br />
	<table width="100%">
	<tr>
		<td>
			id
		</td>
		<td >
			名称
		</td >
		<td>
			内容
		</td>
		<td>
			技能
		</td>
		<td>
			标志位
		</td>
		<td>
			开始任务
		</td>
		<td>
			结束任务
		</td>
		<td>
			地图位置
		</td>
		<td>
			npc聊天
		</td>
		<td>
			操作
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		npc = (FarmNpcBean) vec.get(i);
%>	<tr>
		<td>
			<%=npc.getId()%>
		</td>
		<td>
			<%=npc.getName()%>
			<input type="hidden" id="id" name="id" value="<%=npc.getId() %>">
		</td>
		<td>
			<%=npc.getIntro()%>
		</td>
		<td <%if(npc.getLearnSkill().length()>20){%>width=200<%}%> >
			<%=npc.getLearnSkill()%>
		</td>
		<td width=60>
	<%for(int flag=0;flag<FarmNpcBean.FLAG_COUNT;flag++){%>
	 <%if(npc.isFlag(flag)){%><%=FarmNpcBean.flagString[flag]%>&nbsp;<%}%>
	 <%}%><br/>
		</td>
		<td <%if(npc.getQuestBegin().length()>10){%>width=60<%}%> >
			<%=npc.getQuestBegin()%>
		</td>
		<td <%if(npc.getQuestEnd().length()>10){%>width=60<%}%> >
			<%=npc.getQuestEnd()%>
		</td>
		<td>
			<%=npc.getPos()%>
		</td>
		<td <%if(npc.getTalk().length()>10){%>width=60<%}%> >
			<%=npc.getTalk()%>
		</td>
		<td>
			<a href="editNpc.jsp?id=<%=npc.getId()%>">编辑</a>
		</td>
	</tr><%}%>
	</table>
	<%=paging.shuzifenye(prefixUrl, false, "|", response, 20)%>
    <a href="index.jsp">返回新手管理首页</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
<form method="post" action="farmNpc.jsp?add=1">
    id：
	<input id="id" name="id"><BR>
	名称：
	<input id="name" name="name"><br/>
	内容：
	 <textarea id="intro" name="intro" cols="60" rows="2"></textarea><br/>
	从这个npc可以学的技能：
	 <input id="skill" name="skill"><br/>
	位置：
	 <input id="pos" name="pos" value="0"><br/>
	标志：
	<%for(int flag=0;flag<FarmNpcBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>"><%=FarmNpcBean.flagString[flag]%>
	 <%}%><br/>
	 开始任务：
	 <input id="begin" name="begin"><br/>
	  结束任务：
	 <input id="end" name="end"><br/>
	 npc聊天内容:
	 <input id="talk" name="talk"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<br />
</body>
</html>
