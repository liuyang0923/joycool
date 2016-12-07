<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="javax.servlet.http.Cookie"%><%@ page import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.util.encoder.Base64x"%><%@ page import="java.net.URLEncoder,jc.util.VerifyUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.JoycoolSessionListener" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.Vector" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
int uid = StringUtil.toInt(request.getParameter("uid"));
String autoBookmark = null;	// 自动提示书签地址用于自动登陆
UserBean autoUser = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="登录乐酷">
<p align="left">
<%String mobile=(String)session.getAttribute("userMobile");
UserBean user=null;
Vector userList=null;

String backTo = (String)request.getAttribute("backTo");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL start
if(backTo == null){
	backTo=(String)session.getAttribute("loginReturnURL");
	if(backTo==null){
	backTo = "/wapIndex.jsp";
	}
}
if(backTo.toLowerCase().indexOf("logout")!=-1){
	backTo = "/wapIndex.jsp";
}
backTo = backTo.replace("&", "&amp;");
//zhul_2006-09-07 modify 如果request中没有backto参数，检查session中的returnURL end
if(session.getAttribute(Constants.LOGIN_USER_KEY) != null){
	out.clearBuffer();
	response.sendRedirect((backTo));
	return;
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
%>
该账号发现登陆异常,为保证账号安全,请输入验证码进行验证,防止他人盗取密码.<br/>
验证码：<br/>
<img src="<%=response.encodeURL("/enter/verify.jsp")%>" alt="?" /><br/>
<input name="ver" maxlength="20"/><br/>
<anchor title="确定">登录
<go href="Login.do?backTo=<%=1%>&amp;v=1" method="post"><%String red = (String)session.getAttribute("red");if(red!=null){%><postfield name="red" value="<%=red%>"/><%}%>
<postfield name="ver" value="$ver"/>
<postfield name="uid" value="<%=request.getParameter("uid")%>"/>
<postfield name="password" value="<%=request.getParameter("password")%>"/>
</go>
</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>