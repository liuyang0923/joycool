<%if(session.getAttribute("loginUser")==null){
response.sendRedirect("/user/login.jsp");
return;
}%>