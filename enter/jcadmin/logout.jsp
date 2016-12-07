<%@ page contentType="text/html;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.util.CookieUtil"%><%
HttpSession session = request.getSession(false);
if(session != null && session.getAttribute("adminLogin") != null) {
	CookieUtil ck = new CookieUtil(request,response);
	ck.removeCookie("jopau");
	ck.removeCookie("jopap");
	session.invalidate();
}
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script>
parent.location="login.jsp";
</script>
<html xmlns="http://www.w3.org/1999/xhtml">
</html>