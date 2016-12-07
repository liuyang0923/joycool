<%@include file="checkMobile2.jsp"%><%
if(notMobile) {
response.sendRedirect("/enter/not.jsp");
return;
}
%>