<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%	
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");

			FarmProBean pro =null;
			pro = world.getPro(id);
			if (null != request.getParameter("add")) {
				String name = action.getParameterString("name");
				int maxRank = action.getParameterInt("maxRank");
				String info = action.getParameterString("info");
				int point = action.getParameterInt("point");
				if (!name.equals("")) {
				        pro.setName(name);
						pro.setInfo(info);
						pro.setMaxRank(maxRank);
						pro.setPoint(point);
						world.updatePro(pro);
                        response.sendRedirect("farmPro.jsp");
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
	<form method="post" action="editPro.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=pro.getName()%>">
			</td>
				<tr>
				<td>
					描述
				</td>
				<td>
			<textarea  name="info"  cols="60" rows="2"><%=pro.getInfo()%></textarea>
			</td>
				<tr>
				<td>
					最高等级
				</td>
				<td>
			<input type=text name="maxRank" size="20" value="<%=pro.getMaxRank()%>">
			</td>
				<tr>
				<td>
					升级一次所需的点数
				</td>

			<td>
			<input type=text name="point" size="20" value="<%=pro.getPoint()%>">
			</td>
			</tr>
		
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>