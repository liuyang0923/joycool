<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IFriendService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="org.apache.struts.upload.FormFile" %><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.io.File" %><%response.setHeader("Cache-Control", "no-cache");
			if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}
%>
<%IFriendService friendService = ServiceFactory.createFriendService();
			int id = 0;
			Vector cartoon = null;

			if (null != request.getParameter("delete")) {
				int categoryId = StringUtil.toInt(request
						.getParameter("delete"));
				FriendCartoonBean cardType = null;
				DbOperation dbOp = new DbOperation();
				dbOp.init();
				dbOp.executeUpdate("delete from jc_friend_cartoon where id="
						+ categoryId);
				dbOp.release();
			}
			String tip = (String) request.getAttribute("tip");
			if (null != tip) {

				%>
	<script>
			alert("<%=tip%>");
	</script>
	<%}
			if (null != request.getParameter("id")) {
				id = StringUtil.toInt(request.getParameter("id"));

				cartoon = friendService.getFriendCartoonList("type=" + id);
			}

			%>
<html>
	<head>
	</head>
	<body>
		交友卡通维护:请小心删除操作，因为可能有用户使用正在提交的头像，所以删除时会给用户带来不好体验。
		<br />
		<br />
		<table width="800" border="1">
			<tr>
				<td>
					编号
				</td>
				<td>
					图片
				</td>
								<td>
					编号
				</td>
				<td>
					操作
				</td>
			</tr>
			<tr>
				<form method="post" action="cartoonList.jsp">
					<%if (cartoon == null)
				cartoon = new Vector();
			for (int i = 0; i < cartoon.size(); i++) {
				FriendCartoonBean bean = (FriendCartoonBean) cartoon.get(i);%>
				<td>
					<%=i + 1%>
					.
				</td>
				<td>
					<img src="<%=bean.getUrl()%>" alt="头像" />
				</td>
						<td>
					<%=bean.getPic()%>
				</td>
				<td>
					<a href="http://wap.joycool.net/jcadmin/friend/cartoonList.jsp?id=<%=id%>&delete=<%=bean.getId()%>">删除</a>
				</td>
			</tr>
			</tr>
			<%}%>
			</form>
			<tr>
		</table>
		<form name="cartoonForm" ENCTYPE="multipart/form-data" method="post" action="/AddCartoon.do">
			<input type="hidden" id="id" name="id" value="<%=id%>">
			图片：
			<input type="file" name="image" />
			<br />
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
