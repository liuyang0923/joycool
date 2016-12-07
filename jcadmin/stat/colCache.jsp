<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.cache.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
CustomAction action = new CustomAction(request);
if(!action.isMethodGet()){
int id = action.getParameterInt("id");

CacheManage.column.srm("parent_id = " + id + " and if_display = 1 ORDER BY line_index");
CacheManage.column.srm("c if_display = 1 and parent_id = " + id);
CacheManage.column.srm(new Integer(id));

response.sendRedirect("colCache.jsp");
return;
}
String reload = request.getParameter("reload");
if(reload!=null){
	if(reload.equals("all"))
		net.joycool.wap.call.CallMethod.reloadAll();
	else if(reload.equals("reset"))
		net.joycool.wap.call.CallMethod.resetClassLoader();
}
%><html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body onload="document.f1.id.focus()">
<p>页面缓存</p>
<form action="colCache.jsp" method=post name="f1">
页面columnId = <input type=text name="id" value="6765">
<input type=submit value="清空缓存" onclick="return confirm('确定清空该页面缓存？')">
</form>
<br/>
<%if(group.isFlag(0)){%>
<a href="colCache.jsp?reload=all" onclick="return confirm('确定重新载入所有class？')">重新载入所有class</a><br />
<a href="colCache.jsp?reload=reset" onclick="return confirm('恢复classLoader？')">恢复classLoader</a><br />
<%}%>
</body>
</html>
