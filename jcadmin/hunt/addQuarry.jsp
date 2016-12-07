<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page errorPage=""%>
<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
%>
<html >
<head>
<title>增加猎物</title>
</head>

<logic:present name="result" scope="request">
<p align="center">
<a href="/jcadmin/hunt/viewQuarry.jsp">显示猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/viewQuarryAppearRate.jsp">猎物出现机率</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/addQuarry.jsp">增加猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a>
</p>
<p align="center">
增加成功！<br/>
</p>
</logic:present>

<logic:notPresent name="result" scope="request">
<p align="center">
<a href="/jcadmin/hunt/viewQuarry.jsp">显示猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/viewQuarryAppearRate.jsp">猎物出现机率</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/addQuarry.jsp">增加猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<logic:present name="tip">
<p align="center"><font color="red"><bean:write name="tip"/></font></p><br/>
</logic:present>
<form name="quarryForm" ENCTYPE="multipart/form-data" method="post" action="/job/AddQuarry.do">
<table align="center" border="1">
<caption>增加新的猎物</caption>
<tr><th>猎物名称</th><td><input type="text" name="name" /></td></tr>
<tr><th>价值</th><td><input type="text" name="price" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>咬伤损失</th><td><input type="text" name="harmPrice" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>打中经验</th><td><input type="text" name="hitPoint" onKeyPress="if(event.keyCode<48 && event.keyCode!=45 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>图片</th><td><input type="file" name="image" /></td></tr>
<tr><th>对于弓箭出现机率</th><td><input type="text" name="arrow" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;" /></td></tr>
<tr><th>对于手枪出现机率</th><td><input type="text" name="handGun" onKeyPress="if(event.keyCode<48 ||event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>对于猎枪出现机率</th><td><input type="text" name="huntGun" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>对于AK47出现机率</th><td><input type="text" name="ak47" onKeyPress="if(event.keyCode<48 || event.keyCode>57 event.returnValue=false;)"/></td></tr>
<tr><th>对于AWP出现机率</th><td><input type="text" name="awp" onKeyPress="if(event.keyCode<48 || event.keyCode>57 event.returnValue=false;)"/></td></tr>
<tr><td colspan="2" align="center"><input type="submit" name="submit" value="提交"/><input type="reset" name="reset" value="重置"></td></tr>
</table>
</form>
</logic:notPresent>

</html>