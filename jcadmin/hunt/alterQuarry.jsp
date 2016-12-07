<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*,java.io.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.job.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
HashMap quarryMap=LoadResource.getQuarryMap();	
String quarryId=request.getParameter("quarryId");
HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(new Integer(StringUtil.toInt(quarryId)));
if(quarry==null)
{
	//response.sendRedirect("/jcadmin/hunt/viewQuarry.jsp");
	BaseAction.sendRedirect("/jcadmin/hunt/viewQuarry.jsp", response);
	return;
}

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
<table align="center" border="1" >
<caption>修改猎物</caption>
<form method="post" action="alterResult.jsp">
<tr><th>猎物名称</th><td><input type="text" name="name" value="<%=quarry.getName()%>" /><input type="hidden" name="quarryId" value="<%=quarry.getId()%>"/></td></tr>
<tr><th>价值</th><td><input type="text" name="price" value="<%=quarry.getPrice()%>" onKeyPress="if(event.keyCode<28 || event.keyCode>77) event.returnValue=false;"/></td></tr>
<tr><th>咬伤损失</th><td><input type="text" name="harmPrice" value="<%=quarry.getHarmPrice()%>" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>打中经验</th><td><input type="text" name="hitPoint" value="<%=quarry.getHitPoint()%>" onKeyPress="if(event.keyCode<48 && event.keyCode!=45 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><td  align="center" colspan="2" ><input type="submit" name="submit" value="确定"/><input type="reset" name="reset" value="重置"/></td></tr>
</form>
</table>
</html>