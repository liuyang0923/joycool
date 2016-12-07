<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="javax.servlet.http.Cookie"%><%
response.setHeader("Cache-Control","no-cache");

// 检测cookie



String act = request.getParameter("a");
if(act==null){

}



if(request.getParameter("a")!=null){
Cookie cookie = new Cookie("jctck", "ok");
cookie.setMaxAge(0);
cookie.setPath("/");
response.addCookie(cookie);
}

// 没有列入手机网关

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷检测">
<p align="left">
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>