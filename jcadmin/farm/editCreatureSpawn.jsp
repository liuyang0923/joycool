<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();
int id = action.getParameterInt("id");
CreatureSpawnBean cs = world.getCreatureSpawn(id);
if(request.getParameter("refresh")!=null) {
	world.processCreatureSpawn(cs);
	response.sendRedirect("farmCreatureSpawn.jsp");
	return;
}
CreatureTBean ct = world.getCreatureT(cs.getTemplateId());
if (null != request.getParameter("add")) {
	int templateId = action.getParameterInt("templateId");
	if (templateId > 0) {
		cs.setTemplateId(templateId);
		cs.setPos(request.getParameter("pos").replace("，",","));
		cs.setCount(action.getParameterInt("count"));
		cs.setCooldown(action.getParameterInt("cooldown")*1000);
		cs.setFlag(action.getParameterFlag("flag"));
		cs.init();
		world.updateCreatureSpawn(cs);
        response.sendRedirect("farmCreatureSpawn.jsp");
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
<form method="post" action="editCreatureSpawn.jsp?add=1&id=<%=id%>">
<tr>
	<td>
		怪物id
	</td>
	<td>
<input type=text name="templateId" size="20" value="<%=cs.getTemplateId()%>">
<%if(ct!=null){%><%=ct.getName()%><%}%>
</td></tr>
	<tr>
	<td>
		生长区域
	</td>
	<td>
<textarea name="pos" cols="60" rows="2"><%=cs.getPos()%></textarea>
    </td>
    </tr>
	<tr>
	<td>
		数量
	</td>
	<td>
<input type=text name="count" size="20" value="<%=cs.getCount()%>">
</td></tr>
	<tr>
	<td>
		刷新间隔
	</td>
	<td>
<input type=text name="cooldown" size="20" value="<%=cs.getCooldown()/1000%>">秒
</td></tr>
<tr>
	<td width="100">
	    标志
	</td>
	<td>
	<%for(int flag=0;flag<CreatureSpawnBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox width="100" name="flag" value="<%=flag%>" <%if(cs.isFlag(flag)){%>checked<%}%>><%=CreatureSpawnBean.flagString[flag]%>
	 <%}%><br/>
	</td>
</tr>
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
<br />
<%@include file="bottom.jsp"%>
	</body>
</html>