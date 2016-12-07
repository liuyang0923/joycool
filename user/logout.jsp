<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
%><%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser!=null){
	JoycoolSessionListener.logout(request, loginUser);
	session.removeAttribute("allowVisit");
	session.removeAttribute("ipaddr");
	session.removeAttribute("guest");
	session.setMaxInactiveInterval(120);	// 这个session没用了，但保留2分钟便于用户再去登陆
}
//response.sendRedirect(("/user/login.jsp"));
BaseAction.sendRedirect("/user/login.jsp", response);
%>