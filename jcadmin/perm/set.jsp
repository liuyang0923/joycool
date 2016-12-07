<%@ page contentType="text/html;charset=utf-8" %><%@include file="../filter.jsp"%><%
	
	AdminAction action = new AdminAction(request);
	if(!action.isMethodGet()) {
		String old = request.getParameter("old");
		String pwd = request.getParameter("pwd");
		String pwd2 = request.getParameter("pwd2");
		if(old==null||!old.equals(adminUser.getPassword())){
			action.tip("tip","老密码不正确!");
		} else if(pwd == null || pwd.length()<6) {
			action.tip("tip","新密码至少6位!");
		} else if(!pwd.equals(pwd2)) {
			action.tip("tip","两次输入的新密码不匹配!");
		} else {
			SqlUtil.executeUpdate("update admin set password='"+StringUtil.toSql(pwd)+"' where id="+adminUser.getId(), 0);
			adminUser.setPassword(pwd);
			action.tip("tip","密码修改成功");
		}
	}
%>
<html>
<title>小店后台管理</title>
<script>
</script>
<script type="text/javascript" src="../js/JS_functions.js"></script>
<link href="/adult-admin/css/global.css" rel="stylesheet" type="text/css">
<body style="margin-left:12px;">
<%if(action.isResult("tip")){%><font color=red><%=action.getTip()%></font><br/><%}%>
修改密码<br/>
<form method=post action="set.jsp">
原密码:<input type=password name=old><br/>
新密码:<input type=password name=pwd><br/>
新密码:<input type=password name=pwd2><br/>
<input type=submit value="确认修改"><br/>
</form>
</body>
</html>