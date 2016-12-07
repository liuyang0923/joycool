<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.ICatalogService"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
ICatalogService catalogService = ServiceFactory.createCatalogService();
UrlMapBean urlMap = null; 
boolean flag=false;
if(request.getMethod().equalsIgnoreCase("post")){
String id = request.getParameter("id");
String sortId = request.getParameter("sortId");
String module = request.getParameter("module");
String catalogId = request.getParameter("catalogId");
String url = request.getParameter("url");
String title = request.getParameter("title");
urlMap=catalogService.getUrlMap("catalog_id="+catalogId);
	if(urlMap!=null){
	if(urlMap.getId()!=StringUtil.toInt(id)){
%>
<script>
alert("请检查,输入的栏目ID已存在");
history.back(-1);
</script>
<%  return;
}else{
flag=catalogService.updateUrlMap("module='"+module+"',sort_id="+sortId+",catalog_id="+catalogId+",return_url='"+url+"',title='"+title+"'","id="+id);
if(flag){
URLMap.clearURLCatalogMap();
}
%>
<script>
alert("修改成功！");
window.navigate("urlMaintenance.jsp");
</script>
<%
return;
}}else{
flag=catalogService.updateUrlMap("module='"+module+"',sort_id="+sortId+",catalog_id="+catalogId+",return_url='"+url+"',title='"+title+"'","id="+id);
if(flag){
URLMap.clearURLCatalogMap();
}
%>
<script>
alert("修改成功！");
window.navigate("urlMaintenance.jsp");
</script>
<%
return;
}}
String modify=request.getParameter("modify");
urlMap=catalogService.getUrlMap("id="+modify);
%>
<html>
<head>
<script language="javascript">
function checkform(){
    if(document.inser.catalogId.value == ''){
		alert("栏目ID不能为空！");
		return false;
	}else if(document.inser.url.value == ''){
		alert("返回url不能为空！");
		return false;
	}else if(document.inser.title.value == ''){
		alert("返回url不能为空！");
		return false;
	}else if(isNaN(document.inser.catalogId.value)){
		alert("栏目ID必须是数字！");
		return false;
	}else if(document.inser.url.value.indexOf("http://")){
		alert("返回url必须包含http://");
		return false;
	}else{
		  return true;
	}
}
</script>
</head>
<body>
<b>修改资源返回连接</b><br><hr><br>
<form method="post" name="update" action="editURLMap.jsp" onSubmit="return checkform()">
<input type="hidden" name="id" readonly="readonly" value="<%=urlMap.getId()%>" />
资源分类：
<select name="module" %>>
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs=dbOp.executeQuery("select distinct(module) from jc_url_map");%>
<option value="<%=urlMap.getModule()%>" selected><%=urlMap.getModule()%></option><%
while(rs.next())
{
	out.print("<option value=" + rs.getString("module") + ">");
	out.print(rs.getString("module"));
	out.print("");
}
%>
</select><br>
所属频道：
<select name="sortId">
<%
UrlSortBean urlSort=catalogService.getUrlSort("id="+urlMap.getSortId());
if(urlSort!=null){
%>
<option value="<%=urlSort.getId()%>" selected><%=urlSort.getSortName()%></option>
<%}
rs=dbOp.executeQuery("select * from jc_url_sort");
while(rs.next())
{
	out.print("<option value=" + rs.getInt("id") + ">");
	out.print(rs.getString("sort_name"));
	out.print("</option>");
}
dbOp.release();
%>
</select><br>
<%
CatalogBean catalog=catalogService.getCatalog("id="+urlMap.getCatalogId());
String catalogName=null;
if(catalog!=null){
catalogName=catalog.getName();
}else{catalogName="无描述";}
%>
栏目描述:
<input type="text" name="id" readonly="readonly" value="<%=catalogName%>" />(不能修改,只供参考)<br>
栏目ID：<input type="text" name="catalogId" size="30" value=<%=urlMap.getCatalogId()%>>(限制只能输入数字)<br>
返回连接名称：<input type="text" name="title" size="50" value=<%=urlMap.getTitle()%>><br>
返回url：<input type="text" name="url" size="50"  value=<%=urlMap.getReturnUrl()%>><br>
(请输入完整地址例如:http://wap.joycool.net/Column.do?columnId=2483)<br>
说明：栏目ID和返回url不许为空)<br>
<input type="submit" value="修改"><br/>
</form>
<a href="http://wap.joycool.net/jcadmin/URLMap/urlMaintenance.jsp">返回上一级管理后台</a><br/>
</body>
</html>