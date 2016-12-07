<%@ page contentType="text/html;charset=utf-8"%>
<%!
static int[] usages = {1,2,3,4,5,6};
static String[] usageString = {
"血+", "气力+", "体力+",
"血%+", "气力%+", "体力%+",
};
%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");

DummyProductBean item = world.getItem(id);
if (null != request.getParameter("add")) {
	String name = action.getParameterString("name");
	if (!name.equals("") ) {
            item.setName(name);
            item.setIntroduction(action.getParameterString("info"));
			item.setStack(action.getParameterInt("stack"));
			item.setBind(action.getParameterInt("bind"));
			item.setClass1(action.getParameterInt("class1"));
			item.setClass2(action.getParameterInt("class2"));
			item.setGrade(action.getParameterInt("grade"));
			item.setUnique(action.getParameterInt("unique"));
			item.setUsage(request.getParameter("usage"));
			item.init();
			world.updateItem(item);
            response.sendRedirect("farmUsage.jsp");
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
	<form method="post" action="editUsage.jsp?add=1&id=<%=id%>" name="editForm">
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
		<input type=text name="info" size="20" value="<%=item.getIntroduction()%>">
	    </td>
	</tr>
	<tr>
		<td>
			使用属性
		</td>
		<td>
<input type=text name="usage" size="20" value="<%=item.getUsage()%>">
<input type="button"  name="" value="编辑属性" onClick="OpenWindow('inc/attrUsage.jsp',280,300,480,10,'属性编辑器')">
<script>
function getAttr(){
return editForm.usage.value;
}
function setAttr(attr) {
	editForm.usage.value = attr;
}
</script>
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
			类别
		</td>
		<td>
		<input type=text name="class1" size="20" value="<%=item.getClass1()%>">
		</td>
	</tr>
	<tr>
		<td>
			子类别
		</td>
		<td>
		<input type=text name="class2" size="20" value="<%=item.getClass2()%>">
		</td>
	</tr>
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
	</table>
	<input type="submit" id="add" name="add" value="确认">   	<input type="reset" value="取消修改">
	</form>
<%@include file="usageTip.jsp"%>
	</body>
</html>