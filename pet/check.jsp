<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
if(action.getPetUser() == null){
String url = ("/pet/index.jsp");
response.sendRedirect(url);
return;
}
%>