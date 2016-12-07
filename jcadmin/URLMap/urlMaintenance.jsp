<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.ICatalogService"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="java.util.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("http://wap.joycool.net/jcadmin/login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
ICatalogService catalogService = ServiceFactory.createCatalogService();
UrlMapBean urlMap = null; 
UrlSortBean urlSort= null; 
boolean flag=false;
CatalogBean catalog=null;
if(request.getMethod().equalsIgnoreCase("post")){
	String title = request.getParameter("title");
	String sortId = request.getParameter("sortId");
	String module = request.getParameter("module");
	String catalogId = request.getParameter("catalogId");
	String url = request.getParameter("url");
	urlMap=catalogService.getUrlMap("catalog_id="+catalogId);
	if(urlMap!=null){
%>
<script>
alert("请检查,输入的栏目ID已存在");
history.back(-1);
</script>
<%  return;
}else{
urlMap=new UrlMapBean();
urlMap.setModule(module);
urlMap.setCatalogId(StringUtil.toInt(catalogId));
urlMap.setReturnUrl(url);
urlMap.setTitle(title);
urlMap.setSortId(StringUtil.toInt(sortId));
flag=catalogService.addUrlMap(urlMap);
if(flag){
URLMap.clearURLCatalogMap();
}
%>
<script>
alert("添加成功！");
window.navigate("urlMaintenance.jsp");
</script>
<%
return;
}}
if(request.getParameter("delete") != null){
    String delete=request.getParameter("delete");
    int number=StringUtil.toInt(delete);
    if(number>=0){
          flag=catalogService.delUrlMap("id = " + number);
          if(flag){
			URLMap.clearURLCatalogMap();
			}
    }
%>
<script>
alert("删除成功！");
window.navigate("urlMaintenance.jsp");
</script>
<%}
String condition=request.getParameter("module");
if(condition==null || condition.equals("null")){
condition = "1=1";
}else{
condition="sort_id="+condition;
}
//设置分页参数
int numberPage = 10;
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
	pageIndex = 0;
}
String backTo = request.getParameter("backTo");
if (backTo == null) {
	backTo = "http://wap.joycool.net";
}
String condition1=condition.replace("sort_id","module");
String prefixUrl = "urlMaintenance.jsp?"+condition1;
int totalCount = catalogService.getUrlMapCount(condition);

int totalPageCount = totalCount / numberPage;
if (totalCount % numberPage != 0) {
	totalPageCount++;
}
if (pageIndex > totalPageCount - 1) {
	pageIndex = totalPageCount - 1;
}
if (pageIndex < 0) {
	pageIndex = 0;
}
// 取得要显示的消息列表
int start = pageIndex * numberPage;
int end = numberPage;
Vector urlMapList = catalogService.getUrlMapList( condition+" order by catalog_id desc limit "+ start + ", " + end);
int count = urlMapList.size();
%>
<html>
<head>
<script language="javascript">
  function operate1()
     {
     if (confirm('你确定要删除信息吗？')) {
       return true;
       } else {
        return false;
       }
      }
  function operate3()
     {
     if (confirm('你确定要修改信息吗？')) {
       return true;
       } else {
        return false;
       }
      }
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
<b>资源返回连接管理后台</b><br><hr><br>
<%
DbOperation dbOp = new DbOperation();
dbOp.init();
ResultSet rs=dbOp.executeQuery("select a.id,a.sort_name from jc_url_sort a join (select distinct(sort_id)  from jc_url_map) b on a.id=b.sort_id");
while(rs.next())
{%>
<a href="urlMaintenance.jsp?module=<%=rs.getString("id")%>"><%=rs.getString("sort_name")%></a>
<%}
//dbOp.release();
%>
<a href="urlMaintenance.jsp?columnId=null">全部</a>
<br/>
<table width="100%" border="2">
<tr>
<td width="3%">序号</td>
<td width="5%">模块</td>
<td width="7%">分类</td>
<td width="15%">栏目描述</td>
<td width="4%">栏目ID</td>
<td width="25%">返回url</td>
<td width="5%">返回连接名称</td>
<td width="2%">操作</td>
<td width="2%">操作</td>
</tr>
<%
for(int i = 0; i < count; i ++){
	urlMap = (UrlMapBean) urlMapList.get(i);
%>
<tr>
<td width="5%"><%=(i + 1)%></td>
<td width="5%"><%=urlMap.getModule()%></td>
<%
urlSort=catalogService.getUrlSort("id="+urlMap.getSortId());
String SortName=null;
if(urlSort!=null){SortName=urlSort.getSortName();}
else{SortName="无栏目分类";}%>
<td width="5%"><%=SortName%></td>
<%
catalog=catalogService.getCatalog("id="+urlMap.getCatalogId());
String catalogName=null;
if(catalog!=null){
catalogName=catalog.getName();
}else{catalogName="无描述";}
%>
<td width="5%"><%=catalogName%></td>
<td width="5%"><%=urlMap.getCatalogId()%></td>
<td width="5%"><%=urlMap.getReturnUrl()%></td>

<td width="10%"><%=urlMap.getTitle()%></td>
<td width="5%"><a href="editURLMap.jsp?modify=<%=urlMap.getId()%>" onClick="return operate3()">修改</a></td>
<td width="5%"><a href="urlMaintenance.jsp?delete=<%=urlMap.getId()%>" onClick="return operate1()">删除</a></td>
</tr>
<%}%>
</table>
<p align="center">
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
</P><br>
<b>添加资源返回连接</b><br><hr><br>
<form method="post" name="inser" action="urlMaintenance.jsp" onSubmit="return checkform()">
资源分类：
<select name="module">
<%
rs=dbOp.executeQuery("select distinct(module) from jc_url_map");
while(rs.next())
{
	out.print("<option value=" + rs.getString("module") + ">");
	out.print(rs.getString("module"));
	out.print("</option>");
}
%>
</select><br>
所属频道：
<select name="sortId">
<%
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
栏目ID：<input type="text" name="catalogId" size="30">(限制只能输入数字)<br>
返回连接名称：<input type="text" name="title" size="50"><br>
返回url：<input type="text" name="url" size="50"><br>
(请输入完整地址例如:http://wap.joycool.net/Column.do?columnId=2483)<br>
说明：栏目ID和返回url不许为空)<br>
<input type="submit" value="增加">
</form>
</body>
</html>