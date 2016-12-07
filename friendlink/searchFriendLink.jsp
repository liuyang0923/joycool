<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.service.infc.IFriendLinkService"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.bean.friendlink.*"%><%@ page import="java.sql.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%
IFriendLinkService service = ServiceFactory.createFriendLinkService();
%>
<html>
	<head>
		<title>Search Link</title>
	</head>
	<h1 align="center">Search Link</h1>
	<body>
		&nbsp;
		<%DbOperation dbOp = new DbOperation();
			dbOp.init();
			String sql = "SELECT * FROM link_record";
			ResultSet rs = dbOp.executeQuery(sql);
			try {
				if(rs.next()){
					rs.beforeFirst();%>
		<table border="1" width="982" height="29">
			<tr>
				<th width="50" align="center">
					ID
				</th>
				<th width="80" align="center">
					友链ID
				</th>
				<th width="150" align="center">
					网站名称
				</th>
				<th  width="250" align="center">
					网站地址
				</th>				
				<th width="86" align="center">
					网站类别
				</th>
				<th width="74" align="center">
					标志
				</th>
				<th width="74" align="center">
					操作
				</th>
				<th width="74" align="center">
					操作
				</th>
				<th width="227" align="center">
					申请时间
				</th>
			</tr>
				<%while (rs.next()) {
					LinkTypeBean type = service.getLinkType("id= " + rs.getInt(5));
						%>
			<tr>
				<td align="center">
					<%=rs.getInt(1)%>
				</td>
				<td align="center">
					<%=rs.getInt(2)%>
				</td>
				<td align="center">
					<%=rs.getString(3)%>
				</td>
				<td >
					<%=rs.getString(4)%>
				</td>
				<td align="center">
					<%=type.getName()%>
				</td>
				<td align="center">
					<%=rs.getInt(6)%>
				</td>
				<td align="center">
					<a href="/friendlink/update.do?id=<%=rs.getInt(1)%>">更新</a>
				</td>
				<td align="center">
					<a href="/friendlink/delete.do?id=<%=rs.getInt(1)%>">删除</a>
				</td>
				<td align="center">
					<%=rs.getString(7)%>
				</td>
			</tr>
			<%
			}dbOp.release();
				}else{%> <p align="center"> 没有记录！</p><%}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		%>
		</table>
		<P align="center">
			<form  action="/friendlink/searchlink.do" method="post" >
			按名称查找：<input type="text" name="name" value="">
				<br />
			按网址查找：<input type="text" name="url" value="">
				<br />
				<p align="center">
				         <input type="submit" value="查找">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					     <input type="reset" value="取消">
				</p>
			</form>
		</P>
		<P align="center">
		<a href="/friendlink/linkManage.jsp ">返回主页</a>
        </P>
	</body>
</html>
