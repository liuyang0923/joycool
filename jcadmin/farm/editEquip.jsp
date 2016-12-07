<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%!
static int[] usages = {1,3,4,5,6,9,10,11,12,13,14,15};
static String[] usageString = {
"攻击力", "防御+", "血+", "气力+", "体力+", "攻击速度+", "攻击速度",
"力量+", "敏捷+", "耐力+", "智力+", "精神+",
};
static int[] cnt = {2,1,1,1,1,1,1,1,1,1,1,1,1,1};
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
				item.setBind(action.getParameterInt("bind"));
				item.setUnique(action.getParameterInt("unique"));
				item.setClass1(action.getParameterInt("class1"));
				item.setClass2(action.getParameterInt("class2"));
				item.setGrade(action.getParameterInt("grade"));
				item.setRank(action.getParameterInt("rank"));
				item.setAttribute(request.getParameter("attribute"));
				item.setTime(action.getParameterInt("time"));
				item.setFlag(action.getParameterFlag("flag"));
				item.init();
				world.updateItem(item);
                response.sendRedirect("farmEquip.jsp");
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
<form method="post" action="editEquip.jsp?add=1&id=<%=id%>" name="editForm">
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
		属性
	</td>
	<td>
<input type=text name="attribute" size="20" value="<%=item.getAttribute()%>">
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
			耐久度
		</td>
		<td>
		<input type=text name="time" size="20" value="<%=item.getTime()%>">
		</td>
	</tr>
	<tr>
		<td>
			需求等级
		</td>
		<td>
		<input type=text name="rank" size="20" value="<%=item.getRank()%>">
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
<%@include file="itemTip.jsp"%>
	</body>
</html>