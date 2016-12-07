<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.CookieUtil"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.spec.admin.*"%><%
if(session.getAttribute("adminLogin") == null){

	CookieUtil ck = new CookieUtil(request, response);		// 根据cookie登陆
	String username = ck.getCookieValue("jopau");
	String password = ck.getCookieValue("jopap");

	if(password!=null){
		AdminUserBean user = AdminAction.getAdminUser("name='" + StringUtil.toSql(username) +  "'");
		if(user!=null&&user.getPassword().equals(password))
			session.setAttribute("adminLogin", user);
		else{
			response.sendRedirect("logout.jsp");
			return;
		}
	} else {
		response.sendRedirect("logout.jsp");
		return;
	}
}
	AdminUserBean adminUser = (AdminUserBean)session.getAttribute("adminLogin");
	AdminGroupBean group = adminUser.getGroup();
%>