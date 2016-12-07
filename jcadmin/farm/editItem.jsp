<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

	DummyProductBean item = world.getItem(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		if (!name.equals("") ) {
	            item.setName(name);
	            item.setDescription(action.getParameterString("description"));
				item.setStack(action.getParameterInt("stack"));
				item.setBind(action.getParameterInt("bind"));
				item.setUnique(action.getParameterInt("unique"));
				item.setFlag(action.getParameterFlag("flag"));
				item.setGrade(action.getParameterInt("grade"));
				item.setRank(action.getParameterInt("rank"));
				world.updateItem(item);
                response.sendRedirect("farmItem.jsp");
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
	<form method="post" action="editItem.jsp?add=1&id=<%=id%>">
		<tr>
			<td>
				名称
			</td>
			<td>
		<input type=text name="name" size="20" value="<%=item.getName()%>">
		</td></tr>
			<tr>
			<td>
				描述
			</td>
			<td>
		<input type=text name="description" size="20" value="<%=item.getDescription()%>">
		    </td></tr>
		    
			<tr>
			<td>
				堆叠
			</td>
			<td>
		<input type=text name="stack" size="20" value="<%=item.getStack()%>">
		</td>
			</tr>
			<tr>
			<td>
				唯一
			</td>
			<td>
		<input type=text name="unique" size="20" value="<%=item.getUnique()%>">
		</td>
			</tr>
			<tr>
			<td>
				绑定
			</td>
			<td>
		<input type=text name="bind" size="20" value="<%=item.getBind()%>">
		</td>
		</tr>
			<tr>
			<td>
				品质
			</td>
			<td>
		<input type=text name="grade" size="20" value="<%=item.getGrade()%>">
		</td>
		</tr>
			<tr>
			<td>
				等级
			</td>
			<td>
		<input type=text name="rank" size="20" value="<%=item.getRank()%>">
		</td>
		</tr>
<tr>
			<td>
				标志位
			</td>
			<td>
		<%for(int flag=0;flag<DummyProductBean.FLAG_COUNT;flag++){%>
		 <input type=checkbox name="flag" value="<%=flag%>" <%if(item.isFlag(flag)){%>checked<%}%>><%=DummyProductBean.flagString[flag]%>
		 <%}%><br/>
		</td></tr>
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
<br />
<%@include file="usageTip.jsp"%>
	</body>
</html>