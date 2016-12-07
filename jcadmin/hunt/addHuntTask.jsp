<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
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
<title>增加打猎任务</title>
</head>

<logic:present name="result" scope="request">
<p align="left">
增加打猎活动成功！<br/>
<a href="/jcadmin/hunt/addHuntTask.jsp">继续添加</a><br/>
<a href="/jcadmin/hunt/viewHuntTask.jsp">查看打猎活动安排</a><br/>
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/>
</p>
</logic:present>

<logic:notPresent name="result" scope="request">
<logic:present name="tip">
<p align="center"><font color="red"><bean:write name="tip"/></font></p><br/>
</logic:present>
<form name="quarryForm" ENCTYPE="multipart/form-data" method="post" action="/job/AddHuntTask.do">
<table align="center" border="1">
<caption>增加新的打猎活动</caption>
<tr><th>活动开始时间</th><td>
<select name="startDay">
<%
Calendar cal=Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
 for(int i=0;i<30;i++) {
	cal.add(Calendar.DAY_OF_MONTH,1);
%>
<option value="<%=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)%>"><%=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)%></option>
<%}%>
</select>-
<select name="startHour" >
<%
for(int i=0;i<24;i++){
%>
<option value="<%=i<10?"0"+i:i%>"><%=i<10?"0"+i:i%></option>
<%}%>
</select>时
</td></tr>
<tr><th>活动结束时间</th><td>
<select name="endDay">
<%
cal=Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-1);
 for(int i=0;i<30;i++) {
	cal.add(Calendar.DAY_OF_MONTH,1);
%>
<option value="<%=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)%>"><%=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)%></option>
<%}%>
</select>-
<select name="endHour" >
<%
for(int i=0;i<24;i++){
%>
<option value="<%=i<10?"0"+i:i%>"><%=i<10?"0"+i:i%></option>
<%}%>
</select>时
</td></tr>
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
<tr><th>站内通知</th><td><textarea name="notice" rows="5" cols="18"></textarea></td></tr>
<tr><td colspan="2" align="center"><input type="submit" name="submit" value="提交"/><input type="reset" name="reset" value="重置"></td></tr>
</table>
</form>
<p align="center"><a href="/jcadmin/hunt/viewHuntTask.jsp">查看打猎活动安排</a><br/><a href="/jcadmin/manage.jsp">返回管理中心</a></p><br/>
</logic:notPresent>

</html>