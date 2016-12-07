<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改</title>
</head>

<body  bgcolor="#DAF9FE">

<table width="100%" border="0" align="center" cellspacing="0" bordercolor="#B0E1B3">
  <tr>
    <td bordercolor="#FFFFFF" bgcolor="#80D7F0">
    <table width="100%" border="0" align="center">
        <tr>
          <td width="90%">
		  	 修改
		  </td>
  		  <td>&nbsp;</td>
  		  <td>&nbsp;</td>
  		  <td>&nbsp;</td>	
        </tr>
      </table></td>
  </tr>
  <tr>
    <td >
    <form name="updatesForm" method=post action="UpdateBroadcast.do">
    <table width="90%"  border="0" align="center">
        <tr>
          <td width="10%">直播员</td>
          <td><input type=text  name="broadcaster"  size=20 value="<bean:write name='broadcast' property='broadcaster' />"></td>
        </tr>
        <tr>
          <td width="10%">内容</td>
          <td><textarea  name="msg"  rows="6" cols="70"><bean:write name='broadcast' property='msg' /><bean:write name='broadcast' property='msg' /></textarea></td>
        </tr>
        <tr>
          <td width="10%" colspan="2" align="center">          
          	<input type="submit" name="submit" value="提交">
          	<input type="button" name="submit" value="返回" onclick="history.back();">
          </td>
        </tr>
        <input type="hidden" name="id" value="<bean:write name='broadcast' property='id' />">
      </table>
      </form>
      </td>
  </tr>
</table>
<center>
</center>

</body>
</html>
