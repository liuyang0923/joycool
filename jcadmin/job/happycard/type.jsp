<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.IJobService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>


<%response.setHeader("Cache-Control","no-cache");%>
<%
IJobService jobService=ServiceFactory.createJobService();
HappyCardTypeBean typeBean=null;
int typeId=0;
int categoryId=0;
if(null!=request.getParameter("id"))
	typeId=StringUtil.toInt(request.getParameter("id"));
if(null!=request.getAttribute("typeId"))
	typeId=StringUtil.toInt((String)request.getAttribute("typeId"));
categoryId=StringUtil.toInt(request.getParameter("categoryId"));
if(null!=request.getAttribute("categoryId"))
	categoryId=StringUtil.toInt((String)request.getAttribute("categoryId"));
typeBean=jobService.getHappyCardType("id="+typeId);
if(null!=typeBean){

	if(null!=request.getParameter("delete")){
		int cardId=StringUtil.toInt(request.getParameter("delete"));
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		dbOp.executeUpdate("delete from jc_happy_card where id="+cardId);
		dbOp.release();
	}
	String tip=(String)request.getAttribute("tip");
	if(null!=tip){
	%>
	<script>
			alert("<%=tip%>");
	</script>
	<%
	}
	Vector vecCard=jobService.getHappyCardList(" type_id="+typeId);
	HappyCardBean cardBean=null;
%>
<html>
<head>
</head>
<body>
贺卡管理<br/><br/>
类别：<%=typeBean.getName()%>
<table width="800" border="1">
<tr><td>编号</td><td >名称</td><td >祝福语</td><td>图片</td><td>修改名称</td><td>修改祝福语</td><td>修改图片</td><td>操作</td></tr>
<%
for(int i=0;i<vecCard.size();i++){
	cardBean=(HappyCardBean)vecCard.get(i);
%>
<tr>
<form name="happyCardForm1" ENCTYPE="multipart/form-data" method="post" action="/job/AddHappyCard.do">
<td><%=i+1%></td>
<td><%=cardBean.getTitle()%>
<input type="hidden" id="categoryId" name="id" value="<%=typeId%>">
<input type="hidden" id="typeId" name="categoryId" value="<%=categoryId%>">
<input type="hidden" id="cardId" name="cardId" value="<%=cardBean.getId()%>">
<input type="hidden" id="update" name="update" value="<%=cardBean.getId()%>">
</td>
<td><%=cardBean.getContent()%></td>
<td><img src="<%=cardBean.getImageUrl()%>" ></td>
<td><input id="title" name="title"></td>
<td><input id="content" name="content"></td>
<td><input type="file" name="image" /></td>
<td>
<input type="submit" id="update"  name="update" value="修改">&nbsp;&nbsp;
<a href="http://wap.joycool.net/jcadmin/job/happycard/type.jsp?delete=<%=cardBean.getId()%>&id=<%=typeId%>&categoryId=<%=categoryId%>">删除</a>&nbsp;&nbsp;
</td>
</form>
<%}%>
<tr>
</table>
<form name="happyCardForm2" ENCTYPE="multipart/form-data" method="post" action="/job/AddHappyCard.do">
<input type="hidden" id="typeId" name="id" value="<%=typeId%>">
<input type="hidden" id="categoryId" name="categoryId" value="<%=categoryId%>">
名称：<input id="title" name="title"><br/>
内容：<input id="content" name="content"><br/>
图片：<input type="file" name="image" /><br/>
<input type="submit" id="add" name="add" value="增加"><br/>
</form>
<a href="http://wap.joycool.net/jcadmin/job/happycard/category.jsp?id=<%=categoryId%>">返回上一级</a><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/index.jsp">返回贺卡维护首页</a><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">返回贺卡管理首页</a><br/>
</body>
</html>
<%}else{%>
<html>
<body>
无该子类别<br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/index.jsp">返回贺卡维护首页</a><br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">返回贺卡管理首页</a><br/>
</body>
</html>
<%}%>