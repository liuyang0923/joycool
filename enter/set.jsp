<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.HashMap"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
// 切换wap1.0和wap2.0版本
CustomAction action = new CustomAction(request);
int edition = action.getParameterInt("ed");
if(edition == 1){

	session.removeAttribute("wapEd");
	
	Cookie cookie = new Cookie("wed", "1");
	cookie.setMaxAge(0);
	cookie.setPath("/");
	response.addCookie(cookie);

} else if(edition == 2) {
	String accept = request.getHeader("accept");
	if(accept!=null&&accept.indexOf("text/html")!=-1){
		session.setAttribute("wapEd", Integer.valueOf(2));
		
		Cookie cookie = new Cookie("wed", "2");
		cookie.setMaxAge(25000000);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
response.sendRedirect("/lswjs/index.jsp");
%>