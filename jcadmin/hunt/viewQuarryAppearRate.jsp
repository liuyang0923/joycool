<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="java.util.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
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
//更新猎物出现机率，
HuntAction hunt=new HuntAction(request);
hunt.updateQuarryRate(request);
String tip=(String)request.getAttribute("tip");

HashMap quarryMap=LoadResource.getQuarryMap();	
IJobService jobService = ServiceFactory.createJobService();
%>
<html >
<head>
<title>显示猎物出现机率</title>
</head>

<p align="center">
<a href="/jcadmin/hunt/viewQuarry.jsp">显示猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/viewQuarryAppearRate.jsp">猎物出现机率</a>&nbsp;&nbsp;
<a href="/jcadmin/hunt/addQuarry.jsp">增加猎物</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<font color="red"><%=tip!=null?tip:""%><br/></font>
<table align="center" border="1" >
<caption>显示猎物出现机率</caption>
<tr>
<th>动物名称</th><th>动物Id</th><th>动物价格</th><th>对弓箭机率</th><th>对手枪机率</th><th>对猎枪机率</th><th>对AK47机率</th><th>对AWP机率</th><th>修改</th></tr>
<%
Set key=quarryMap.keySet();
Iterator it=key.iterator();
while(it.hasNext())
{
	HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(it.next());
	Vector rateList=jobService.getHuntQuarryAppearRateList("quarry_id="+quarry.getId()+" order by weapon_id");
	if(rateList.size()==0)continue;
	int[] rate=new int[rateList.size()];
	for(int i=0;i<rateList.size();i++)
	{
	HuntQuarryAppearRateBean rateBean=(HuntQuarryAppearRateBean)rateList.get(i);
	rate[i]=rateBean.getAppearRate();
	}
%>
<form name="form1" method="post" action="alterQuarryRate.jsp">
<input type="hidden" name="quarryName" value="<%=quarry.getName()%>"/>
<input type="hidden" name="quarryId" value="<%=quarry.getId()%>"/>
<input type="hidden" name="arrow" value="<%=rate[0]%>"/>
<input type="hidden" name="handGun" value="<%=rate[1]%>"/>
<input type="hidden" name="huntGun" value="<%=rate[2]%>"/>
<input type="hidden" name="ak47" value="<%=rate[3]%>"/>
<input type="hidden" name="awp" value="<%=rate[4]%>"/>
<tr>
<td><%=quarry.getName()%></td>
<td><%=quarry.getId()%></td>
<td><%=quarry.getPrice()%></td>
<td><%=rate[0]%></td>
<td><%=rate[1]%></td>
<td><%=rate[2]%></td>
<td><%=rate[3]%></td>
<td><%=rate[4]%></td>
<td><input type="submit" name="submit" value="修改"/></td>
</form>
<%}%>
</table>
</html>