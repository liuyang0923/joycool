<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.admin.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.SecurityUtil"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.LogUtil"%><%@ page import="net.joycool.wap.util.CookieUtil"%><%@ page import="net.joycool.wap.framework.*"%><%

CookieUtil ck = new CookieUtil(request,response);
if(request.getParameter("dc")!=null) {		// 删除cookie的操作
	ck.removeCookie("ju");
	ck.removeCookie("jp");
	ck.removeCookie("jru");
	ck.removeCookie("jrp");
	response.sendRedirect("login.jsp");
	return;
}
String username = ck.getCookieValue("ju");
String password = ck.getCookieValue("jp");
boolean ru=ck.getCookieValue("jru")==null||ck.getCookieValue("jru").equals("1");
boolean rp=ck.getCookieValue("jrp")!=null&&ck.getCookieValue("jrp").equals("1");
String focus = "username";
if(username==null||!ru)
	username="";
else		// 已经保存了密码则焦点放到密码框
	focus="password";
if(password==null||!rp)
	password="";
else
	focus = "username";

if(request.getMethod().equalsIgnoreCase("post")){
	username = request.getParameter("username");
	password = request.getParameter("password");
	
	
	if(username != null) {		// post提交才有效
		boolean ru2 = request.getParameter("ru") != null;
    	if(ru2) {		// 记住用户名
    		if(username.length() > 0)
    			ck.setCookie("ju", username, 2000000000);
    		ck.setCookie("jru", "1", 2000000000);
    	} else {
    		ck.removeCookie("ju");
    		ck.setCookie("jru", "0", 2000000000);
    	}
	}
	
	if(password!=null){
		AdminUserBean user = net.joycool.wap.spec.admin.AdminAction.getAdminUser("name='" + StringUtil.toSql(username) +  "'");
		if(user!=null&&user.getPassword().equals(password)){
			session.setAttribute("adminLogin", user);
			LogUtil.logAdmin("login-"+user.getName() + "-"+request.getRemoteAddr());
			//response.sendRedirect("http://wap.joycool.net/jcadmin/manage.jsp");
			
	    	ck.setCookie("jopau", username, -1);
	    	ck.setCookie("jopap", password, -1);
	    	
	    	boolean rp2 = request.getParameter("rp") != null;

	    	if(rp2) {		// 记住密码
	    		ck.setCookie("jp", password, 2000000000);
	    		ck.setCookie("jrp", "1", 2000000000);
	    	} else {
	    		ck.removeCookie("jp");
	    		ck.setCookie("jrp", "0", 2000000000);
	    	}
			
			response.sendRedirect("/jcadmin/index.jsp");
		} else {
			if(SqlUtil.isTest&&SecurityUtil.isInnerIp(request.getRemoteAddr())){
				user = new AdminUserBean();
				user.setName("test");
				AdminGroupBean g = new AdminGroupBean();
				g.setFlag(0xFFFF);
				user.setGroup(g);
				session.setAttribute("adminLogin", user);
				response.sendRedirect("/jcadmin/index.jsp");
				return;
			}
		}
	}
//	if("admin".equals(username) && "123456".equals(password)){
//		session.setAttribute("adminLogin", "true");
//		//response.sendRedirect("http://wap.joycool.net/jcadmin/newManage.jsp");
//		BaseAction.sendRedirect("/jcadmin/newManage.jsp", response);
//	}
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style>
#dl_dv1 {
	background-image: url(image/denglu_03.jpg);
	background-repeat: no-repeat;
	height: 198px;
	width: 313px;
	padding: 0px;
	margin-top: 20%;
	margin-left: 33%;
}
#dl_dv2 {
	margin-top: 80px;
	margin-left: 20px;
	position: absolute;
}
#dl_dv2 input {
	height: 16px;
	width: 100px;
	border: 1px solid #666666;
}
#dl_dv2 ul {
	list-style-position: outside;
	list-style-type: none;
	margin: 0px;
	padding: 0px;
	font-family: "宋体";
	font-size: 12px;
	text-align: right;
}
#dl_dv2 li {
	margin-bottom: 5px;
}
#dl_dv3 {
	font-family: "宋体";
	font-size: 12px;
	margin-top: 134px;
	margin-left: 16px;
	position: absolute;
}
#dl_dv4 {
	font-family: "宋体";
	font-size: 12px;
	margin-top: 160px;
	margin-left: 30px;
	position: absolute;
}
</style>
</head>

<body id="body" onload="document.f1.<%=focus%>.focus();document.f1.<%=focus%>.select();">
<div id="dl_dv1">
<form action="login.jsp" method="post" name="f1">
<div id="dl_dv2">
<ul>
<li><strong>用户名：&nbsp;</strong><input type="text" name="username" value="<%=username%>"/></li>
<li><strong>密&nbsp;&nbsp;码：&nbsp;</strong><input type="password" name="password" /></li>
</ul>
</div>

<div id="dl_dv3">
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="90"><input name="ru" type="checkbox" value="1" <%if(ru){%>checked<%}%> class="input2">记住用户名</td>
    <td><input name="rp" type="checkbox" value="1"  <%if(rp){%>checked<%}%> onclick="alert('保存密码很不安全，本功能暂时禁止使用。');return false;" class="input2">记住密码</td>
  </tr>
</table>
</div>
	
<div id="dl_dv4"><table border="0" cellpadding="0" cellspacing="0">
  <tr>
	<td style="text-decoration:underline" width="90"><a href="login.jsp?dc=1"><font color="#000000">删除Cookie</font></a></td>
    <td><input type=image src="image/denglu_2_03.jpg" width="47" height="21" border="0" /></td>
  </tr>
</table>
</div>
</form>
</div>
</body>
</html>

<html>