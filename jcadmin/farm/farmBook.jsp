<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List bookList = world.bookList;
		
int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,bookList.size(),20);

			String prefixUrl = "farmBook.jsp";

			%>
<%if (null != request.getParameter("add")) {
                int id = action.getParameterInt("id");
				String name = action.getParameterString("name");
				String content = action.getParameterString("content");
				int pos = action.getParameterInt("pos");
				if (!name.equals("")) {
						FarmBookBean book = new FarmBookBean();
						book.setId(id);
			            book.setName(name);
			            book.setContent(content);
			            FarmWorld.nodeMoveObj(book.getPos(), pos, book);
			            book.setPos(pos);
						world.addBook(book);
                     response.sendRedirect("farmBook.jsp");
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = bookList.subList(paging.getStartIndex(),paging.getEndIndex());
			FarmBookBean book = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
		书籍后台
		<br />
		<br />
		<form method="post" action="farmBook.jsp?add=1">
			名称：
			<input id="name" name="name"><br/>
			内容：
			 <textarea name="content" cols="60" rows="2"></textarea><br/>
			所在地图：
			 <input id="pos" name="pos"><br/>
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>


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
					所在地图
				</td>
				<td>
					操作
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				book = (FarmBookBean) vec.get(i);
%>
			<tr>
				<td>
					<%=book.getId()%>
				</td>
				<td>
					<%=book.getName()%>
				</td>
				<td>
					<%=book.getContent()%>
				</td>
				<td>
					<%=book.getPos()%>
				</td>
				
				<td>
					<a href="editBook.jsp?id=<%=book.getId()%>">编辑</a>
				</td>
			</tr>
			
			<%}%>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
