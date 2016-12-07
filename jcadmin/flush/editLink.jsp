<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.flush.*,net.wxsj.bean.flush.*,java.util.*"%><%
FlushAdminAction action = new FlushAdminAction();
action.editLink(request, response);

String result = (String) request.getAttribute("result");

if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip"); 
%>
<script>
alert("<%=tip%>");
history.back(-1);
</script>
<%
	return;
}
else if("success".equals(result)){
    response.sendRedirect("linkList.jsp");
	return;
}

LinkBean link = (LinkBean) request.getAttribute("flushLink");
%>
<form action="editLink.jsp" method="post">
<table width="100%" border="0">
  <tr>
    <td width="100">标题：</td>
	<td><input type="text" name="name" size="50" value="<%=link.getName()%>"/></td>
  </tr>
  <tr>
    <td>地址：</td>
	<td><input type="text" name="link" size="50" value="<%=link.getLink()%>"/></td>
  </tr>
  <tr>
    <td>备注：</td>
	<td><textarea name="remark" cols="50" rows="5"><%=link.getRemark()%></textarea></td>
  </tr>
  <tr>
    <td>最大点击数：</td>
	<td><input type="text" name="maxHits" size="5" value="<%=link.getMaxHits()%>"/></td>
  </tr>
  <tr>
    <td>最大手机号：</td>
	<td><input type="text" name="maxMobile" size="5" value="<%=link.getMaxMobile()%>"/></td>
  </tr>
  <tr>
    <td>脚本：</td>
	<td><textarea name="script" cols="50" rows="5"><%=link.getScript()%></textarea></td>
  </tr>  
  <tr>
    <td></td>
	<td><input type="submit" name="B" value="修改友链"/></td>
  </tr>
</table>
<input type="hidden" name="id" value="<%=link.getId()%>"/>
</form>