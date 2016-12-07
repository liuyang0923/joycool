<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="javax.servlet.http.Cookie"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.encoder.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser!=null && request.isRequestedSessionIdFromCookie()){	// 如果支持cookie，保存用户数据
	String password = Base64x.encodeMd5(Encoder.decrypt(loginUser.getPassword()));
	String id = Base64x.encodeInt(loginUser.getId());
	String data = password.substring(0, 5) + id + password.substring(5, 11);
	Cookie cookie = new Cookie("jcal", data);	// joycool auto login cookie
	cookie.setMaxAge(90000000);
	cookie.setPath("/");
	response.addCookie(cookie);
}
String red = (String)session.getAttribute("red");
if(red != null) {
	session.removeAttribute("red");
	response.sendRedirect(red);
	return;
}
String backTo = "/wapIndex.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="登录成功" ontimer="<%=response.encodeURL(backTo)%>">
<timer value="50"/>
<p align="left">
登录成功！5秒后自动跳转。<br/>
下次学会用书签登陆哦，不用输入ID和密码，更快捷哦：）<br/>
<a href="<%=backTo%>">直接跳转</a><br/>
<a href="/enter/index.jsp">免输入ID/密码直接登录的秘籍</a><br/>
<a href="/user/userInfo.jsp">修改密码</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>