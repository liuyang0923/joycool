<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

	ItemSetBean set = world.getItemSet(id);
	if (null != request.getParameter("add")) {
		String name = action.getParameterString("name");
		if (!name.equals("") ) {
	            set.setName(name);
	            set.setInfo(action.getParameterString("info"));
				set.setAttribute(request.getParameter("attribute"));
				set.setItems(request.getParameter("items"));
				set.setCount(request.getParameter("count"));
				set.init();
				world.updateItemSet(set);
				world.initItemSet();
                response.sendRedirect("farmItemSet.jsp");
		} else {%>
<script>
	alert("请填写正确各项参数！");
</script>
                <%}}%>
<html>
<head>
</head>
<script language="JavaScript" src="js/JS_functions.js"></script>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
<table width="100%">
<form method="post" action="editItemSet.jsp?add=1&id=<%=id%>" name="editForm">
	<tr>
		<td>
			名称
		</td>
		<td>
	<input type=text name="name" size="20" value="<%=set.getName()%>">
	</td></tr>
		<tr>
		<td>
			描述
		</td>
		<td>
	<input type=text name="info" size="20" value="<%=set.getInfo()%>">
	    </td></tr>
	<tr>
		<td>
			组件
		</td>
		<td>
		<input type=text name="items" size="20" value="<%=set.getItems()%>">
		</td>
	</tr> 
	<tr>
		<td>
			属性
		</td>
		<td>
<input type=text name="attribute" size="20" value="<%=set.getAttribute()%>">
<input type="button"  name="" value="编辑属性" onClick="OpenWindow('inc/attrEquip.jsp',280,300,480,10,'属性编辑器')">
<script>
function getAttr(){
return editForm.attribute.value;
}
function setAttr(attr) {
	editForm.attribute.value = attr;
}
</script>
		</td>
	</tr>
		<tr>
		<td>
			件数
		</td>
		<td>
	<input type=text name="count" size="20" value="<%=set.getCount()%>">
	</td>
		</tr>
</table>
<input type="submit" id="add" name="add" value="确认">
</form>
</body>
</html>