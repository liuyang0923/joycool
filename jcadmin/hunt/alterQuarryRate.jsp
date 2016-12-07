<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*,java.io.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
String quarryId=request.getParameter("quarryId");
String quarryName=request.getParameter("quarryName");
String arrow=request.getParameter("arrow");
String handGun=request.getParameter("handGun");
String huntGun=request.getParameter("huntGun");
String ak47=request.getParameter("ak47");
String awp=request.getParameter("awp");
%>
<html >
<head>
</head>

<p align="center">
<a href="/jcadmin/hunt/viewQuarry.jsp">显示猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/viewQuarryAppearRate.jsp">猎物出现机率</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/addQuarry.jsp">增加猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<table align="center" border="1" >
<caption>修改猎物出现机率</caption>
<form method="post" action="viewQuarryAppearRate.jsp">
<tr><th>猎物名称</th><td><input type="text" name="name" value="<%=quarryName%>" readonly="readonly"/><input type="hidden" name="quarryId" value="<%=quarryId%>"/></td></tr>
<tr><th>对于弓箭出现机率</th><td><input type="text" name="arrow" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"  value="<%=arrow%>"/></td></tr>
<tr><th>对于手枪出现机率</th><td><input type="text" name="handGun" value="<%=handGun%>" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>对于猎枪出现机率</th><td><input type="text" name="huntGun" value="<%=huntGun%>" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>对于ak47出现机率</th><td><input type="text" name="ak47" value="<%=ak47%>" onKeyPress="if(event.keyCode<48  || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>对于AWP出现机率</th><td><input type="text" name="awp" value="<%=awp%>" onKeyPress="if(event.keyCode<48  || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><td colspan="2" align="center" ><input type="submit" name="submit" value="确定"/><input type="reset" name="reset" value="重置"/></td></tr>
</form>
</table>
</html>