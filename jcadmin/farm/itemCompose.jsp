<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*" %><%@ page import="net.joycool.wap.bean.item.ComposeBean" %><%!
static int[] productDef = {0, 1};
%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();

List vec = new ArrayList(ServiceFactory.createUserService().getItemComposeMap("1").values());

	%>
<html>
	<head>
<link href="common.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		物品合成公式查询
		<br />

<table width="1000" border="1">
	<tr>
		<td>
			id
		</td>
		<td>
			合成卡名称
		</td>
		<td>
			介绍
		</td>
		<td>
			材料
		</td>
		<td>
			产品
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		ComposeBean skill = (ComposeBean) vec.get(i);
%>
	<tr>
		<td>
			<%=skill.getId()%>
		</td>
		<td>
		<% DummyProductBean item = FarmWorld.getItem(skill.getItemId());
		if(item!=null)
		{%><%=item.getName()%>
		<%}else{%>
			(未知)
		<%}%>
		</td>
		<td width=150>
			<%=item.getDescription()%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(StringUtil.toIntss(skill.getMaterial(),2,productDef))%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(StringUtil.toIntss(skill.getProduct(),2,productDef))%>
		</td>
	</tr>
	<%}%>
</table>
<br />

<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
</body>
</html>
