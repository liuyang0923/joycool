<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*"%><%
MallAdminAction action = new MallAdminAction();
action.validate(request, response);

String result = (String) request.getAttribute("result");
out.print(result);
%>