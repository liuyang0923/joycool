<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

	TriggerBean trigger =null;
	trigger = world.getTrigger(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		String bak = action.getParameterString("bak");
		String event = request.getParameter("event");
		boolean changeEvent = !trigger.getEvent().equals(event);		// 如果改变了event则要重新安装触发器
		if (!name.equals("") ) {
			if(changeEvent)  FarmWorld.unsetTrigger(trigger);
            trigger.setName(name);
            trigger.setBak(bak);
            trigger.setInfo(action.getParameterString("info"));
            trigger.setEvent(event);
            trigger.setCondition(request.getParameter("condition"));
            trigger.setAction(request.getParameter("action"));
            trigger.setFlag(action.getParameterFlag("flag"));
            trigger.init();
            if(changeEvent) FarmWorld.setTrigger(trigger);
			world.updateTrigger(trigger);
            response.sendRedirect("farmTrigger.jsp");
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
<h3>编辑：触发器</h3>
<table width="100%">
<form method="post" action="editTrigger.jsp?add=1&id=<%=id%>">
			<tr>
			<td>
				名称
			</td>
			<td>
		<input type=text name="name" size="20" value="<%=trigger.getName()%>">
		</td></tr>
			<tr>
			<td>
				事件
			</td>
			<td>
		<input type=text name="event" size="20" value="<%=trigger.getEvent()%>">
		</td></tr>
			<tr>
			<td>
				条件
			</td>
			<td>
		<input type=text name="condition" size="20" value="<%=trigger.getCondition()%>">
		</td>
		</tr>
			<tr>
			<td>
				执行
			</td>
			<td>
		<input type=text name="action" size="20" value="<%=trigger.getAction()%>">
		</td>
		</tr>
			<tr>
			<td width="100">
			    标志
			</td>
		<td>
		<%for(int flag=0;flag<TriggerBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox width="100" name="flag" value="<%=flag%>" <%if(trigger.isFlag(flag)){%>checked<%}%>><%=TriggerBean.flagString[flag]%>
		 <%}%><br/>
		</td>
		</tr>
			<tr>
			<td>
				显示信息
			</td>
			<td>
		<textarea name="info" cols="60" rows="2"><%=trigger.getInfo()%></textarea>
		</td>
		</tr>
			<tr>
			<td>
				备注
			</td>
			<td>
		<textarea name="bak" cols="60" rows="2"><%=trigger.getBak()%></textarea>
		</td>
		</tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
	<a href="farmTrigger.jsp">返回上一级</a>
				<br />
	</body>
</html>