<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*"%><%
MallAdminAction action = new MallAdminAction();
action.addTag(request, response);

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

response.sendRedirect("tagList.jsp");
%>