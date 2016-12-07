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
<title>增加机会卡</title>
</head>
<p align="center">
<a href="/jcadmin/card/viewQuarry.jsp">显示机会卡</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<form method="post" action="addResult.jsp">
<table align="center" border="1" >
<caption>添加机会卡</caption>
<tr>
	<th>类型</th>
	<td>
	<select name=typeId>
        <option value=1>穷神卡
        <option value=2>财神卡
        <option value=3>衰神卡
        <option value=4>福神卡
        <option value=5>均富卡
        <option value=6>教会卡
        <option value=7>天使卡
        <option value=8>革命卡
        <option value=9>升级卡
        <option value=10>牛市卡
        <option value=11>道具卡
	</select>
	</td>
</tr>
<tr>
	<th>出现几率</th>
	<td><input type="text" name="appearRate" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/>(此值不能为10000)
	</td>
</tr>
<tr>
	<th>数值类型</th>
	<td>
	<select name=valueType>
        <option value=0>数值
        <option value=1>百分比
	</select>
	</td>
</tr>
<tr>
	<th>值大小</th>
	<td><input type="text" name="actionValue" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/>
	</td>
</tr>
<tr>
	<th>激励类型</th>
	<td>
	<select name=actionField>
        <option value=0>乐币
        <option value=1>经验
        <option value=2>等级
        <option value=3>物品
	</select>
	</td>
</tr>
<tr>
	<th>加减方式</th>
	<td>
	<select name=actionDirection>
        <option value=0>加
        <option value=1>减
        <option value=3>物品
	</select>
	</td>
</tr>
<tr>
	<th>描述</th>
	<td><input type="text" name="description" size="50" /></td>
</tr>
<tr>
	<td  align="center" colspan="2" ><input type="submit" name="submit" value="确定"/>
	<input type="reset" name="reset" value="重置"/>
	</td>
</tr>
</table>
</form>
</html>