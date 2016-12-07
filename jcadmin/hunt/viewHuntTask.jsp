<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*,java.text.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
IJobService jobService = ServiceFactory.createJobService();
String taskId=request.getParameter("taskId");
if(taskId!=null)
{
	jobService.deleteHuntTask("id="+taskId);
}

Vector huntTaskList=jobService.getHuntTaskList(null);	
%>
<html >
<head>
<title>显示打猎任务</title>
</head>
<table align="center" border="1" >
<caption>打猎活动安排</caption>
<tr>
<th>开始时间</th><th>结束时间</th><th>猎物名称</th><th>价值</th><th>咬伤损失</th><th>打中经验</th>
<th>图片</th><th>对于弓箭出现机率</th><th>对于手枪出现机率</th><th>对于猎枪出现机率</th><th>对于AK47出现机率</th><th>对于AWP出现机率</th>
<th>创建时间</th><th>站内通知</th><th>操作</th></tr>
<%
SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
for(int i=0;i<huntTaskList.size();i++)
{
	HuntTaskBean task=(HuntTaskBean)huntTaskList.get(i);
	Date ds=sdf.parse(task.getStartTime().substring(0,19));
	Date de=sdf.parse(task.getEndTime().substring(0,19));
	boolean t=false;
	Calendar cal=Calendar.getInstance();
	if(cal.getTime().after(ds) && cal.getTime().before(de)) t=true;
%>
<tr>
<td><%=task.getStartTime().substring(0,19)%></td>
<td><%=task.getEndTime().substring(0,19)%></td>
<td><%=task.getQuarryName()%></td>
<td><%=task.getPrice()%></td>
<td><%=task.getHarmPrice()%></td>
<td><%=task.getHitPoint()%></td>
<%-- liuyi 20070102 图片路径修改 start --%>
<td><img src="<%=Constants.JOB_HUNT_IMG_PATH%><%=task.getImage()%>" alt="loading..."/></td>
<%-- liuyi 20070102 图片路径修改 end --%>
<td><%=task.getArrow()%></td>
<td><%=task.getHandGun()%></td>
<td><%=task.getHuntGun()%></td>
<td><%=task.getAk47()%></td>
<td><%=task.getAwp()%></td>
<td><%=task.getCreateTime().substring(0,19)%></td>
<td><textarea  rows="3" cols="10" readonly="readonly" ><%=task.getNotice()%></textarea></td>
<td><%if(t){%>活动进行中<%}else{%><a href="viewHuntTask.jsp?taskId=<%=task.getId()%>">删除</a><%}%></td></tr>
<%}%>
</table>
<p align="center">
<a href="/jcadmin/hunt/addHuntTask.jsp">增加打猎活动</a><br/>
<a href="/jcadmin/manage.jsp">返回管理中心</a>
</p>

</html>