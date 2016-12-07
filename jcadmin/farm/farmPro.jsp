<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%	
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List proList = world.proList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,proList.size(),20);

			String prefixUrl = "farmPro.jsp";
			List vec = proList.subList(paging.getStartIndex(),paging.getEndIndex());
			FarmProBean pro = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>
	采集专业
		<table width="100%">
			<tr>
				<td>
					id
				</td>
				<td>
					名称
				</td>
				<td>
					描述
				</td>
				<td>
					最高等级
				</td>
				<td>
					升级一次所需的点数
				</td>
				<td>
					编辑
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				pro = (FarmProBean) vec.get(i);
%>
			<tr>
				
				<td>
					<%=pro.getId()%>
				</td>
				<td>
					<%=pro.getName()%>
				</td>
				<td>
					<%=pro.getInfo()%>
				</td>
				<td>
					<%=pro.getMaxRank()%>
				</td>
				<td>
					<%=pro.getPoint()%>
				</td>
				<td>
					<a href="editPro.jsp?id=<%=pro.getId()%>">编辑</a>
				</td>
			</tr>
			
			<%}%>
			<tr>
		</table>
		<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

		<a href="index.jsp">返回新手管理首页</a><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
