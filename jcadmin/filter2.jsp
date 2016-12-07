<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.admin.*"%><%@ page import="java.util.*"%><%
	AdminUserBean adminUser = (AdminUserBean)session.getAttribute("adminLogin");
	AdminGroupBean group = adminUser.getGroup();
%>