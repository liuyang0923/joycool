<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
/*
String url = request.getParameter("backTo");
if(url == null) {
	url = (String)session.getAttribute("backTo");
	if(url == null)
		url = "/wapIndex.jsp";
}*/
String url = "/wapIndex.jsp";
url = (url);
if(session.getAttribute(Constants.LOGIN_USER_KEY) != null) {
response.sendRedirect(url);
return;
}
String mid = (String)session.getAttribute("userMobile");
if(mid == null || mid.length() < 11){/*
	if(request.getParameter("g") == null){
		session.setAttribute("backTo", url);
		String eurl = ("/user/autologin.jsp?g=");
		response.sendRedirect("http://211.136.107.36/mnf/m-11534.fet?url=" + java.net.URLEncoder.encode(eurl, "utf-8"));
		return;
	}
	mid = request.getParameter("_mn_");	// 其实保存的是手机号……
	if(mid == null)
		mid = "";
	if (mid.startsWith("86")) {
		mid = mid.substring(2);
	}
	String linkId = (String)session.getAttribute("linkId");
	if(linkId == null)
		linkId = "0";
	LogUtil.logMobile(mid + "::" + request.getRemoteAddr() + ":" + linkId + "::0");*/
	response.sendRedirect("/register.jsp");
	return;
} else {
	UserBean user = null;
	int id = StringUtil.toInt(request.getParameter("id"));
	if(id>0){
		user = UserInfoUtil.getUser(id);
		if(user!=null&&!user.getMobile().equals(mid))
			user = null;
	} else {
		user = Util.getUserService().getLastLoginUser(mid);
	}
	if(user==null||SecurityUtil.checkForbidUserId(user.getId())){
		response.sendRedirect(("login.jsp"));
		return;
	}
	session.setAttribute("userMobile", mid);
	Util.updatePhoneNumber(request, mid);
}
session.removeAttribute("backTo");
response.sendRedirect(url);
%>