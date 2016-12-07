<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.job.CardAction" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//删除机会卡
//CardAction card=new CardAction(request);
//card.deleteQuarry(request);
//String tip=(String)request.getAttribute("tip");

TreeMap cardMap=LoadResource.getCardMap();
System.out.print(cardMap.size());
%>
<html >
<head>
<title>显示猎物</title>
</head>
<p align="center">
<a href="/jcadmin/card/addQuarry.jsp">增加机会卡</a>&nbsp;&nbsp;
<a href="/jcadmin/manage.jsp">返回管理中心</a><br/><hr>
</p>
<table align="center" border="1" >
<caption>显示机会卡</caption>
<tr><th>id</th><th>卡类型</th><th>出现几率</th><th>值类型</th><th>大小</th><th>处理类型</th><th>操作</th><th>描述</th><th>操作</th></tr>
<%
Iterator it=cardMap.keySet().iterator();
while(it.hasNext())
{
Integer id = (Integer)it.next();
if(id.intValue()==10000)continue;
CardBean card=(CardBean)cardMap.get(id);
%>
<tr>
<td><%=card.getId()%></td>
<td><%=CardAction.typeId[card.getTypeId()-1]%></td>
<td><%=card.getAppearRate()%></td>
<td><%=CardAction.valueType[card.getValueType()]%></td>
<td><%=card.getActionValue()%></td>
<td><%=CardAction.actionField[card.getActionfield()]%></td>
<td><%=CardAction.actionDirection[card.getActionDirection()]%></td>
<td><%=card.getDescription()%></td>
<td><a href="/jcadmin/card/alterQuarry.jsp?cardId=<%=id.intValue()%>">修改</a></td>
</tr>
<%--<td><a href="/jcadmin/hunt/viewQuarry.jsp?quarryId=<%=quarry.getId()%>">删除</a></td>--%>
<%}%>
</table>
</html>