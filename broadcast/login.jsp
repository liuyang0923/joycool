<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理员登陆</title>
</head>

<body>
<table width="96%" height="467" border="0" align="center">
  <tr>
    <td width="32%">&nbsp;</td>
    <td width="35%">&nbsp;</td>
    <td width="33%">&nbsp;</td>
  </tr>
  <tr>
    <td height="229">&nbsp;</td>
    <td><table width="93%" height="139" border="0" align="center">
	    <form  method="post" action="Admin.do">
        <tr> 
          <td width="24%" height="29">&nbsp;</td>
          <td width="59%"><div align="center">管理员登陆</div></td>
          <td width="17%">&nbsp;</td>
        </tr>
        <tr> 
          <td height="34">用户名：</td>
          <td>
              <input type="text" name="username" style="width:160">
		  </td>
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td height="24">密码：</td>
          <td>
              <input type="password" name="password" style="width:160">
		  </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td height="42">&nbsp;</td>
          <td>
              <input type="submit" name="Submit" value="登陆">
		  </td>
          <td>&nbsp;</td>
        </tr>
	  </form>
      </table></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>
