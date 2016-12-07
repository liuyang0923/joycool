<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmWorld.getNpcWorld();
int id = action.getParameterInt("id");

	FactoryBean factory =null;
	factory = world.getFactory(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		String info = action.getParameterString("info");
		int rank = action.getParameterInt("rank");
		int interval = action.getParameterInt("interval");
		int pos = action.getParameterInt("pos");
		if (!name.equals("") ) {
            factory.setName(name);
            factory.setRank(rank);
            factory.setInfo(info);
            factory.setInterval(interval);
            FarmWorld.nodeMoveObj(factory.getPos(), pos, factory);
            factory.setPos(pos);
			world.updateFactory(factory);
            response.sendRedirect("farmFactory.jsp");
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
<form method="post" action="editFactory.jsp?add=1&id=<%=id%>">
	<tr>
		<td>
			名称
		</td>
		<td>
	<input type=text name="name" size="20" value="<%=factory.getName()%>">
	</td>
		<tr>
		<td>
			介绍
		</td>
		<td>
	<textarea name="info" cols="60" rows="2"><%=factory.getInfo()%></textarea>
	    </td>
	    
		<tr>
		<td>
			需要等级
		</td>
		<td>
	<input type=text name="rank" size="20" value="<%=factory.getRank()%>">
	</td>
		<tr>
		<td>
			加工间隔
		</td>
		<td>
	<input type=text name="interval" size="20" value="<%=factory.getInterval()%>">
	</td>
		<tr>
		<td>
			所在地图
		</td>
		<td>
	<input type=text name="pos" size="20" value="<%=factory.getPos()%>">
	</td>
			
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
<a href="farmFactory.jsp">返回上一级</a><br/>
<br/>
<p>剩余时间：<%=factory.getTimeLeft()%></p>
<table width="600px">
<%
List list = factory.getProcessList();
PagingBean paging = new PagingBean(action, list.size(), 20, "p");
for(int i = paging.getStartIndex();i < paging.getEndIndex();i++){
FactoryProcBean fp = (FactoryProcBean)list.get(i);
FactoryComposeBean compose = world.getFactoryCompose(fp.getComposeId());
FarmUserBean user = FarmWorld.one.getFarmUserCache(fp.getUserId());
%><tr>
<td><%if(user!=null){%><%=user.getName()%><%}%>(<%=fp.getUserId()%>)</td>
<td><%=compose.getName()%>(<%=fp.getComposeId()%>)-<%=compose.getTime()%>秒</td>
<td><%=DateUtil.formatSqlDatetime(fp.getCreateTime())%></td>
<td><%=DateUtil.formatSqlDatetime(fp.getDoneTime())%></td>
<td><%=fp.getStatus()%></td>
</tr>
<%}%>
</table>
<%=paging.shuzifenye("editFactory.jsp?id="+id, true, "|", response)%>
</body>
</html>