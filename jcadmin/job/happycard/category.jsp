<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IJobService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation" %><%response.setHeader("Cache-Control","no-cache");%>
<%
IJobService jobService=ServiceFactory.createJobService();
HappyCardCategoryBean categoryBean=null;
int categoryId=0;
if(null!=request.getParameter("id"))
	categoryId=StringUtil.toInt(request.getParameter("id"));
categoryBean=jobService.getHappyCardCategory("id="+categoryId);
if(null!=categoryBean){
	if(null!=request.getParameter("delete")){
		int typeId=StringUtil.toInt(request.getParameter("delete"));
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		dbOp.startTransaction();
		dbOp.executeUpdate("delete from jc_happy_card where type_id="+typeId);
		dbOp.executeUpdate("delete from jc_happy_card_type where id="+typeId);
		dbOp.commitTransaction();
		dbOp.release();
	}
	%>
	<%
	if(null!=request.getParameter("add")){
		String typeName=null;
		String description=null;
		typeName=request.getParameter("type").trim();
		description=request.getParameter("description").trim();
		if(!typeName.equals("")&&!description.equals("")){
			HappyCardTypeBean typeBean=null;
			typeBean=jobService.getHappyCardType("name='"+typeName+"' and category_id="+categoryId);
			if(typeBean==null){
				typeBean=new HappyCardTypeBean();
				typeBean.setName(typeName);
				typeBean.setDescription(description);
				typeBean.setCategoryId(categoryId);
				jobService.addHappyCardType(typeBean);
			}else{%>
				<script>
				alert("该子类别已经存在！");
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
		int typeId=0;
		String typeName=null;
		String description=null;
		typeName=request.getParameter("type").trim();
		description=request.getParameter("description").trim();
		typeId=StringUtil.toInt(request.getParameter("typeId"));
		if(!typeName.equals("")&&!description.equals("")){
			HappyCardTypeBean typeBean=null;
			typeBean=jobService.getHappyCardType("name='"+typeName+"' and category_id="+categoryId);
			if(typeBean==null){
				jobService.updateHappyCardType("name='"+typeName+"',description='"+description+"'","id="+typeId);
			}else{%>
				<script>
				alert("该子类别已经存在！");
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
	Vector vecCardType=jobService.getHappyCardTypeList(" category_id="+categoryId);
	HappyCardTypeBean typeBean=null;
%>
<html>
<head>
</head>
<body>
贺卡管理<br/><br/>
类别：<%=categoryBean.getName()%>
<table width="800" border="1">
<tr><td>编号</td><td >名称简写</td><td >名称全称</td><td>修改名称简写</td><td>修改名称全称</td><td>操作</td></tr>
<%
for(int i=0;i<vecCardType.size();i++){
	typeBean=(HappyCardTypeBean)vecCardType.get(i);
%>
<tr>
<form method="post" action="category.jsp?update=1" >
<td><%=i+1%></td>
<td><%=typeBean.getName()%>
<input type="hidden" id="categoryId" name="id" value="<%=categoryId%>">
<input type="hidden" id="typeId" name="typeId" value="<%=typeBean.getId() %>">
</td>
<td><%=typeBean.getDescription()%></td>
<td><input id="type" name="type"></td>
<td><input id="description" name="description"></td>
<td>
<input type="submit" id="update"  name="update" value="修改">&nbsp;&nbsp;
<a href="category.jsp?delete=<%=typeBean.getId()%>&id=<%=categoryId%>">删除</a>&nbsp;&nbsp;
<a href="type.jsp?id=<%=typeBean.getId()%>&categoryId=<%=categoryId%>">查看详细</a>
</td>
</form>
<%}%>
<tr>
</table>
<form method="post" action="category.jsp?add=1">
<input type="hidden" id="categoryId" name="id" value="<%=categoryId%>">
名称：<input id="type" name="type"><br/>
描述：<input id="description" name="description"><br/>
<input type="submit" id="add" name="add" value="增加"><br/>
</form>
<font color="red">点击删除会删除子类别，以及子分类下的贺卡</font><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/index.jsp">返回贺卡维护首页</a><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">返回贺卡管理首页</a><br/>
</body>
</html>
<%}else{%>
<html>
<body>
无该类别<br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/index.jsp">返回贺卡维护首页</a><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">返回贺卡管理首页</a><br/>
</body>
</html>
<%}%>