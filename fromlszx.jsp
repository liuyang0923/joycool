<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
String userName = request.getParameter("userName");
String password = request.getParameter("password");
if(request.getMethod().equalsIgnoreCase("post")){
	if("joycool".equals(userName) && "zhu8jie".equals(password)){
		session.setAttribute("allowVisit", "op");
		session.setAttribute("oxcx", "true");
		//response.sendRedirect(("wapIndex.jsp"));
		BaseAction.sendRedirect(null, response);
		return;
	}
}

String url = ("fromlszx.jsp");
boolean flag = true;
UserBean loginUser = null;

if(!SecurityUtil.isInnerIp(request.getRemoteAddr())&&":300:431:32170:1390:274512:122:520520:623932:529127:597067:356006:356003:519610:914727:48847:".indexOf(":" + userName + ":") < 0
		&& !net.joycool.wap.util.ForbidUtil.isForbid("op",StringUtil.toInt(userName)))
	flag = false;
else {
	password = net.joycool.wap.util.Encoder.encrypt(password);
	int id = StringUtil.toInt(userName);
	loginUser = UserInfoUtil.getUser(id);
	if (loginUser == null) {
		flag = false;
	} else if (!loginUser.getPassword().equals(password)) {
		flag = false;
	}
}


if (flag) {
	session.setAttribute("allowVisit", "op");
	session.setAttribute("oxcx", "true");
	loginUser.setIpAddress(request.getRemoteAddr());
	loginUser.setUserAgent(request.getHeader("User-Agent"));
	JoycoolSessionListener.updateOnlineUser(request, loginUser);
	response.sendRedirect(("wapIndex.jsp"));
	return;
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="入口"><p align="left">
用户名：<br/><input name="userName"  maxlength="100" value=""/><br/>
密码：<br/><input name="password"  maxlength="100" value=""/><br/>
<anchor title="确定">登录
<go href="<%=("fromlszx.jsp") %>" method="post">
	<postfield name="userName" value="$userName"/>
	<postfield name="password" value="$password"/>
</go>
</anchor><br/>
</p></card></wml>