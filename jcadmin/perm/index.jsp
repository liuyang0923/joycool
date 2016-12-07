<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%>
<html>
<title>小店后台管理</title>
<script>
</script>
<script type="text/javascript" src="../js/JS_functions.js"></script>
<link href="/adult-admin/css/global.css" rel="stylesheet" type="text/css">
<body style="margin-left:12px;line-height:150%;font-size:14px;">
<br>
<a href="users.jsp">查看<font color=red>用户权限</font>设置</a><br/>
<a href="groups.jsp">查看<font color=red>权限组</font>设置</a><br/>
<a href="perms.jsp">查看<font color=red>权限</font>设置</a><br/>
<br/>
<a href="clearCache.jsp" onclick="return confirm('此操作将清除所有权限缓存，确认？')">清除权限组缓存</a><br/>
</body>
</html>