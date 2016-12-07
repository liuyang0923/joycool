<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IJobService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation" %><%response.setHeader("Cache-Control","no-cache");%>
<%
IJobService jobService=ServiceFactory.createJobService();
if(null!=request.getParameter("delete")){
	int categoryId=StringUtil.toInt(request.getParameter("delete"));
	HappyCardTypeBean cardType=null;
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	dbOp.startTransaction();
	Vector vecType=jobService.getHappyCardTypeList(" category_id="+categoryId);
	for(int i=0;i<vecType.size();i++){
		cardType=(HappyCardTypeBean)vecType.get(i);
		dbOp.executeUpdate("delete from jc_happy_card where type_id="+cardType.getId());
	}
	dbOp.executeUpdate("delete from jc_happy_card_type where category_id="+categoryId);
	dbOp.executeUpdate("delete from jc_happy_card_category where id="+categoryId);
	dbOp.commitTransaction();
	dbOp.release();
}
%>
<%
if(null!=request.getParameter("add")){
	String categoryName=null;
	categoryName=request.getParameter("categoryName").trim();
	if(!categoryName.equals("")){
		HappyCardCategoryBean category=null;
		category=jobService.getHappyCardCategory("name='"+categoryName+"'");
		if(category==null){
			category=new HappyCardCategoryBean();
			category.setName(categoryName);
			jobService.addHappyCardCategory(category);
		}else{%>
			<script>
			alert("该类别已经存在！");
			</script>
		<%}
	}else{%>
			<script>
			alert("请填写正确各项参数！");
			</script>
		<%
	}
}
if(null!=request.getParameter("update")){
	String categoryName=null;
	int categoryId=0;
	categoryId=StringUtil.toInt(request.getParameter("categoryId"));
	categoryName=request.getParameter("categoryNameUpdate").trim();
	if(!categoryName.equals("")){
		HappyCardCategoryBean category=null;
		category=jobService.getHappyCardCategory("name='"+categoryName+"'");
		if(category==null){
			jobService.updateHappyCardCategory("name='"+categoryName+"'","id="+categoryId);
		}else{%>
			<script>
			alert("该类别已经存在！");
			</script>
		<%}
	}else{%>
	<script>
		alert("请填写正确各项参数！");
		</script>
	<%
	}
}
%>
<%
Vector vecCardCategory=jobService.getHappyCardCategoryList(" 1=1 ");
HappyCardCategoryBean category=null;
%>
<html>
<head>
</head>
<body>
贺卡维护<br/><br/>
<table width="800" border="1">
<tr><td>编号</td><td >类别</td><td >类别修改</td><td>操作</td></tr>
<%
for(int i=0;i<vecCardCategory.size();i++){
	category=(HappyCardCategoryBean)vecCardCategory.get(i);
%>
<tr>
<form method="post" action="index.jsp?update=1" >
<td><%=i+1%></td>
<td><%=category.getName()%>
<input type="hidden" id="categoryId" name="categoryId" value="<%=category.getId() %>"></td>
<td><input id="categoryName2" name="categoryNameUpdate"></td>
<td>
<input type="submit" id="update"  name="update" value="修改">&nbsp;&nbsp;
<a href="index.jsp?delete=<%=category.getId()%>">删除</a>&nbsp;&nbsp;
<a href="category.jsp?id=<%=category.getId()%>">查看详细</a>
</td></tr>
</form>
<%}%>
<tr>
</table>
<form method="post" action="index.jsp?add=1">
类别名称：<input id="categoryName" name="categoryName"><input type="submit" id="add" name="add" value="增加"><br/>
</form>
<font color="red">说明：点击删除会删除类别，该类别下的子分类，以及子分类下的贺卡</font><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">返回贺卡管理首页</a><br/>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html> 