<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 30;
%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
CookBean2 cook = null;
String tip = "";
int mid = action.getParameterInt("mid");
cook = action.yardService.getCookBean2("id=" + mid);
if (cook == null)
	tip = "要修改的BEAN不存在.";
else {
	int s = action.getParameterInt("s");
	if (s == 1){
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
		 	SqlUtil.executeUpdate("update fm_yard_cook2 set recpie_id=" + recipeId + ",content='" + StringUtil.toSql(content) + "',need_time=" + needTime + ",step=" + step + ",material_id=" + materialId + ",`count`=" + count + " where id=" + cook.getId(),5);
			response.sendRedirect("recipeproto3.jsp");
			return;
		}
	}
}
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
<%if ("".equals(tip)){
%><form action="recipeproto4.jsp?s=1&mid=<%=cook.getId()%>" method="post">
ID:<%=cook.getId()%><br/>
食谱ID:<input type="text" name="recipeid" value="<%=cook.getRecipeId()%>"><br/>
说明:<br/>
<textarea name="content" cols="60" rows="2"><%=cook.getContent()%></textarea><br/>
需要的时间:(毫秒):<input type="text" name="needtime" value="<%=cook.getNeedTime()%>"><br/>
步骤:<input type="text" name="step" value="<%=cook.getStep()%>"><br/>
所需要的物品ID:<input type="text" name="materialid" value="<%=cook.getMaterialId()%>"><br/>
需要的数量:<input type="text" name="count" value="<%=cook.getCount()%>"><br/>
<input type="submit" value="提交">
</form><%
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>

</body>
</html>