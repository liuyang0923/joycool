<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.flush.*"%><%
FlushAdminAction action = new FlushAdminAction();
action.addLink(request, response);

String result = (String) request.getAttribute("result");

if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip"); 
%>
<script>
alert("<%=tip%>");
history.back(-1);
</script>
<%
	return;
}

response.sendRedirect("linkList.jsp");
%>