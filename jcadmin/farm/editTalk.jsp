<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();		
int id = action.getParameterInt("id");

			FarmTalkBean talk =null;
			talk = world.getTalk(id);
			if (null != request.getParameter("add")) {
				String title = (String)request.getParameter("title");
				String content = (String)request.getParameter("content");
				String link = (String)request.getParameter("link");
				if (!title.equals("")) {
				talk.setTitle(title);
				talk.setContent(content);
				talk.setLink(link);
				talk.setQuestBegin(action.getParameterInt("questBegin"));
				talk.setQuestEnd(action.getParameterInt("questEnd"));
				talk.setQuest(action.getParameterInt("quest"));
				talk.setPreQuest(action.getParameterInt("preQuest"));
				talk.setCondition(request.getParameter("condition"));
				talk.initLink(world);
				talk.init();
                world.updateTalk(talk);
                response.sendRedirect("farmTalk.jsp");
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
	<form method="post" action="editTalk.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="title" size="20" value="<%=talk.getTitle()%>">
			</td>
				<tr>
				<td>
					说明
				</td>
				<td>
			<textarea name="content" cols="60" rows="2"><%=talk.getContent()%></textarea>
			</td>
				<tr>
				<td>
					后续内容
				</td>
			<td>
			<input type=text name="link" size="20" value="<%=talk.getLink()%>">
			</td>
			</tr>
				<tr>
				<td>
					接受任务
				</td>
			<td>
			<input type=text name="questBegin" size="20" value="<%=talk.getQuestBegin()%>">
	<%  if(talk.getQuestBegin()>0){
		FarmQuestBean quest = world.getQuest(talk.getQuestBegin());
		if(quest!=null){
	%>
	<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
	<%}}%>
			</td>
			</tr>
				<tr>
				<td>
					提交任务
				</td>
			<td>
			<input type=text name="questEnd" size="20" value="<%=talk.getQuestEnd()%>">
	<%  if(talk.getQuestEnd()>0){
		FarmQuestBean quest = world.getQuest(talk.getQuestEnd());
		if(quest!=null){
	%>
	<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
	<%}}%>
			</td>
			</tr>
				<tr>
				<td>
					所属任务
				</td>
			<td>
			<input type=text name="quest" size="20" value="<%=talk.getQuest()%>">
	<%  if(talk.getQuest()>0){
		FarmQuestBean quest = world.getQuest(talk.getQuest());
		if(quest!=null){
	%>
	<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
	<%}}%>
			</td>
			</tr>
<tr>
				<td>
					先修任务
				</td>
			<td>
			<input type=text name="preQuest" size="20" value="<%=talk.getPreQuest()%>">
	<%  if(talk.getPreQuest()>0){
		FarmQuestBean quest = world.getQuest(talk.getPreQuest());
		if(quest!=null){
	%>
	<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
	<%}}%>
			</td>
			</tr>
			<tr>
			<td>
				可见条件
			</td>
			<td>
		<input type=text name="condition" size="20" value="<%=talk.getCondition()%>">&nbsp;<%=FarmWorld.getConditionString(talk.getConditionList())%>
		</td></tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	<a href="farmTalk.jsp">返回上一级</a><br/>
	</form>
		<br />
		<%for(int i2 = 0;i2<talk.getLinkList().size();i2++){
		FarmTalkBean talk2 = (FarmTalkBean)talk.getLinkList().get(i2);
		if(talk2==null){%>(未知)<%}else{%>
			<%=i2+1%>.<a href="editTalk.jsp?id=<%=talk2.getId()%>"><%=talk2.getTitle()%></a><br/>
		<%}}%>
</body>
</html>