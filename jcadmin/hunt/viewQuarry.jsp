<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//删除猎物
//HuntAction hunt=new HuntAction(request);
//hunt.deleteQuarry(request);
//String tip=(String)request.getAttribute("tip");

HashMap quarryMap=LoadResource.getQuarryMap();

%>
<html >
<head>
<title>显示猎物</title>
</head>

<p align="center">
<a href="/jcadmin/hunt/viewQuarry.jsp">显示猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/viewQuarryAppearRate.jsp">猎物出现机率</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/addQuarry.jsp">增加猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<%--<font color="red"><%=tip!=null?tip:""%><br/></font>--%>
<table align="center" border="1" >
<caption>显示猎物</caption>
<tr>
<th>动物名称</th><th>价值</th><th>咬伤损失</th><th>打中经验</th><th>图片</th><th>修改</th></tr>
<%
Set key=quarryMap.keySet();
Iterator it=key.iterator();
while(it.hasNext())
{
	HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(it.next());
%>
<tr>
<td><%=quarry.getName()%></td>
<td><%=quarry.getPrice()%></td>
<td><%=quarry.getHarmPrice()%></td>
<td><%=quarry.getHitPoint()%></td>
<%-- liuyi 20070102 图片路径修改 start --%>
<td><img src="<%=Constants.JOB_HUNT_IMG_PATH%><%=quarry.getImage()%>" alt="loading..."/></td>
<%-- liuyi 20070102 图片路径修改 end --%>
<td><a href="/jcadmin/hunt/alterQuarry.jsp?quarryId=<%=quarry.getId()%>">修改</a></td>
<%--<td><a href="/jcadmin/hunt/viewQuarry.jsp?quarryId=<%=quarry.getId()%>">删除</a></td>--%>
<%}%>
</table>
</html>