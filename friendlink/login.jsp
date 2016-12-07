<%@ page contentType="text/html;charset=utf-8"%>
<%
if(request.getMethod().equalsIgnoreCase("post")){
	String userName = request.getParameter("userName");
	String password = request.getParameter("password");
	if("lbj".equals(userName) && "123456".equals(password)){
		session.setAttribute("login", new Integer(1));
		//response.sendRedirect("linkManage.jsp");
		BaseAction.sendRedirect("/friendlink/linkManage.jsp", response);
		return;
	}
}
%>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>登录</title>
</head>

<form action="login.jsp" method="post">
  <table border="1" cellspacing="1" width="100%" class="ccc">
    <tr>
      <td width="30%" align="left">用户名</td>
      <td width="70%"><input type="text" name="userName" size="40"></td>
    </tr>
    <tr>
      <td width="30%" align="left">密码</td>
      <td width="70%"><input type="password" name="password" size="40"></td>
    </tr>    
  </table>
  <p align="center"><input type="submit" value="提交" name="B1"><input type="reset" value="全部重写" name="B2"></p>
</form>
</body>

</html>
</logic:notPresent>