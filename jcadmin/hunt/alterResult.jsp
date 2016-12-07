<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
HuntAction hunt=new HuntAction(request);
hunt.alterQuarry(request);
String tip=(String)request.getAttribute("tip");
%>
<html >
<head>
<title>修改猎物</title>
</head>
<p align="center">
<a href="/jcadmin/hunt/viewQuarry.jsp">显示猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/viewQuarryAppearRate.jsp">猎物出现机率</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/addQuarry.jsp">增加猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<%=tip%><br/>
<a href="alterQuarry.jsp?quarryId=<%=request.getParameter("quarryId")%>">返回</a>
</html>