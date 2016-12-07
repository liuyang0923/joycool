<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.stage.*"%><%
StageAdminAction action = new StageAdminAction();
action.addQuestion(request, response);

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

response.sendRedirect("questionList.jsp");
%>