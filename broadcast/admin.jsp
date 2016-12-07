<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.bean.broadcast.BStatusBean"%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理员登陆</title>
</head>
<%
String user = (String)session.getAttribute("login");
if(user == null)
{
	String password = (String)request.getParameter("password");
	if(password==null || !password.equals("04tech"))
		//response.sendRedirect("login.jsp");
	    BaseAction.sendRedirect("login.jsp", response);
	else
		session.setAttribute("login", "ok");
}
%>

<body  bgcolor="#DAF9FE">
<table width="100%" border="0" align="center" cellspacing="0" bordercolor="#B0E1B3">
  <tr>
    <td bordercolor="#FFFFFF" bgcolor="#80D7F0">
    <table width="100%" border="0" align="center">
        <tr>
          <td width="90%">
				NBA直播状态：
				</td>
        </tr>
      </table>
      </td>
  </tr>
  <tr><td>

    <form name="updateBStatus" method=post action="UpdateBStatus.do">
    <table width="90%"  border="0" align="center">
        <tr>
          <td width="10%">第一行</td>
          <td><input type=text  name="line1"  size="80" value="<%=BStatusBean.line1%>"></td>
        </tr>
        <tr>
          <td width="10%">第二行</td>
          <td><input type=text  name="line2"  size="80" value="<%=BStatusBean.line2%>"></td>
        </tr>
        <tr>
          <td width="10%" colspan="2" align="center">          
          	<input type="submit" name="submit" value="提交">
          </td>
        </tr>
      </table>
      
      </form>


  </td></tr>
</table>
<table width="100%" border="0" align="center" cellspacing="0" bordercolor="#B0E1B3">
  <tr>
    <td bordercolor="#FFFFFF" bgcolor="#80D7F0">
    <table width="100%" border="0" align="center">
        <tr>
          <td width="90%">
				NBA直播解说：
				</td>
        </tr>
      </table>
      </td>
  </tr>
  <tr><td>

    <form name="addBroadcast" method=post action="AddBroadcast.do">
    <table width="90%"  border="0" align="center">
        <tr>
          <td width="10%">直播员</td>
          <td><input type=text  name="broadcaster"  size="20" value="<bean:write name="broadcast" property="broadcaster" />"></td>
        </tr>
        <tr>
          <td width="10%">内容</td>
          <td><textarea  name="msg"  rows="6" cols="70"><bean:write name="broadcast" property="msg" /></textarea></td>
        </tr>
        <tr>
          <td width="10%" colspan="2" align="center">          
          	<input type="submit" name="submit" value="提交">
          </td>
        </tr>
      </table>
      </form>


  </td></tr>
</table>

<hr>

<table width="100%" border="0" align="center" cellspacing="0" bordercolor="#B0E1B3">
  <tr>
    <td bordercolor="#FFFFFF" bgcolor="#80D7F0">
    <table width="100%" border="0" align="center">
        <tr>
          <td width="90%">
				最近直播内容：
		  </td>
	          <td> 
			  </td>
	          <td>
	          </td>
	      <td> </td>
        </tr>
      </table></td>
  </tr>

        <tr>
    <td >
    <table width="100%"  border="1" align="center" cellspacing="0" cellpadding="0">
    <tr align="center">
           <td width="40">id</td>
           <td>broadcaster</td>
           <td width="400">msg</td>
          <td>time</td>
           <td width="80">操作</td>
      </tr>
      <logic:present name="list" scope="request">
      <logic:iterate id="element" name="list">
      <tr align="center">
           <td><bean:write name="element" property="id" /></td>
           <td><bean:write name="element" property="broadcaster"/></td>
           <td><bean:write name="element" property="msg"/></td>
           <td><bean:write name="element" property="time"/></td>
        <td>      		
	        <a href="Admin.do?action=u&id=<bean:write name="element" property="id" />" ><IMG SRC="images/update.gif" WIDTH="13" HEIGHT="13" BORDER="0" ALT=""></ak>
	        <a href="Admin.do?action=d&id=<bean:write name="element" property="id" />"onclick="javascript:return confirm('确认删除？')"> <IMG SRC="images/delete.gif" WIDTH="13" HEIGHT="13" BORDER="0" ALT=""></a>
          </td>
       </tr>
       </logic:iterate>
       </logic:present>
 </table>
  <hr>
      </td>
  </tr>
  

 
</table>

</body>
</html>
