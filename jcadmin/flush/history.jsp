<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.bean.flush.*,net.wxsj.action.flush.*,java.util.ArrayList"%><%
FlushAdminAction action = new FlushAdminAction();
action.history(request, response);

ArrayList list = (ArrayList) request.getAttribute("list");

int i, count;
HistoryBean bean = null;
%>
刷量记录<br/>
<table width="100%" border="1">
    <tr>
	<td><strong>日期</strong></td>
	<td><strong>点击数</strong></td>
	<td><strong>独立号数</strong></td>	
    </tr>
<%
count = list.size();
for(i = 0; i < count; i ++){
	bean = (HistoryBean) list.get(i);	
%>
    <tr>	
	<td><%=bean.getDate()%></td>
	<td><%=bean.getHitsCount()%></td>
	<td><%=bean.getMobileCount()%></td>	
	</tr>
<%
}
%>
</table>