<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 30;
%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
CookBean2 cook = null;
String tip = "";
int did = action.getParameterInt("did");
// 删除一步
if (did > 0){
	SqlUtil.executeUpdate("delete from fm_yard_cook2 where id=" + did,5);
	response.sendRedirect("recipeproto3.jsp");
	return;
}
int add = action.getParameterInt("a");
if (add == 1){
		int recipeId = action.getParameterInt("recipeid");	// 菜谱ID
		String content = action.getParameterNoEnter("content");	// 说明
		int needTime = action.getParameterInt("needtime");	// 需要的时间（毫秒）
		int step = action.getParameterInt("step");	// 步骤
		int materialId = action.getParameterInt("materialid"); // 此步需要的物品
		int count = action.getParameterInt("count");	// 需要的数量
		if (recipeId <= 0 || needTime <= 0 || step <= 0 || materialId <= 0 || count <= 0)
			tip = "请输入正确的参数.";
		else if (content.length() > 255)
			tip = "输入的说明不可超过255个字";
		else {
			cook = new CookBean2();
			cook.setRecipeId(recipeId);
			cook.setContent(content);
			cook.setNeedTime(needTime);
			cook.setStep(step);
			cook.setMaterialId(materialId);
			cook.setCount(count);
		 	action.yardService.addCookBean2(cook);
		}
}
List list = action.yardService.getCookBeanList2("1 order by recpie_id desc,step asc");
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
<%=tip%><br/>
中级食谱步骤(<a href="recipeproto3.jsp">刷新</a>)<br/>
<table width="100%">
<tr>
	<td>ID</td>
	<td>食谱ID</td>
	<td>说明</td>
	<td>需要时间</td>
	<td>步骤</td>
	<td>需要的物品</td>
	<td>需要的数量</td>
	<td>操作</td>
</tr>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		cook = (CookBean2)list.get(i);
		if (cook != null){
			%><tr>
				<td><%=cook.getId()%></td>
				<td><%=cook.getRecipeId()%></td>
				<td><%=StringUtil.toWml(cook.getContent())%></td>
				<td><%=cook.getNeedTime() / 1000%>秒(<%=cook.getNeedTime()%>)</td>
				<td><%=cook.getStep()%></td>
				<td><%=cook.getMaterialId()%></td>
				<td><%=cook.getCount()%></td>
				<td><a href="recipeproto3.jsp?did=<%=cook.getId()%>" onClick="return confirm('真的要删除这一步吗?')">删除</a>|<a href="recipeproto4.jsp?mid=<%=cook.getId()%>">修改</a></td>
			</tr><%		
		}
	}
}%>
</table>
==========增加一步==========<br/>
<form action="recipeproto3.jsp?a=1" method="post">
食谱ID:<input type="text" name="recipeid" ><br/>
说明:<br/>
<textarea name="content" cols="60" rows="2"></textarea><br/>
需要的时间:(毫秒):<input type="text" name="needtime"><br/>
步骤:<input type="text" name="step"><br/>
所需要的物品ID:<input type="text" name="materialid"><br/>
需要的数量:<input type="text" name="count"><br/>
<input type="submit" value="提交">
</form>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
</body>
</html>