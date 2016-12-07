<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");
	
	FarmBookBean book =null;
	book = world.getBook(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		String content = action.getParameterString("content");
		int pos = action.getParameterInt("pos");
		if (!name.equals("") ) {
	            book.setName(name);
	            book.setContent(content);
	            FarmWorld.nodeMoveObj(book.getPos(), pos, book);
	            book.setPos(pos);
				world.updateBook(book);
                response.sendRedirect("farmBook.jsp");
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
	<h3>编辑：书</h3>
	<table width="100%">
	<form method="post" action="editBook.jsp?add=1&id=<%=id%>">
			<tr>
				<td>
					名称
				</td>
				<td>
			<input type=text name="name" size="20" value="<%=book.getName()%>">
			</td>
				<tr>
				<td>
					内容
				</td>
				<td>
			<textarea name="content" cols="60" rows="2"><%=book.getContent()%></textarea>
			    </td>
				<tr>
				<td>
					所在地图
				</td>
				<td>
			<input type=text name="pos" size="20" value="<%=book.getPos()%>">
			</td>
				
	</table>
	<input type="submit" id="add" name="add" value="确认">
	</form>
				<br />
	</body>
</html>