<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%
if(request.getParameter("clear") != null){
	Vector forbidIpList = SecurityUtil.forbidIpList;
	if(forbidIpList != null){
		forbidIpList.removeAllElements();		
	}
	forbidIpList.add("218.249.10");
	forbidIpList.add("61.135");
	forbidIpList.add("66.151.181");
	forbidIpList.add("202.108.40");
	forbidIpList.add("222.210.246");
	forbidIpList.add("220.184.115");
	forbidIpList.add("211.101.160");
	forbidIpList.add("125.96.20");	
	//response.sendRedirect("forbid.jsp");
	BaseAction.sendRedirect("/forbid.jsp", response);
}
%>
<p><a href="forbid.jsp?clear=1">重新设定</a></p>
<p>封禁列表</p>
<table width="100%" border="2">
<%
Vector forbidIpList = SecurityUtil.forbidIpList;
if(forbidIpList != null){
	Iterator itr = forbidIpList.iterator();
	int i = 1;
	while(itr.hasNext()){
%>
<tr>
<td width="10%"><%=i%></td>
<td width="90%"><%=((String)itr.next())%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>