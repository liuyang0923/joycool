<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="javax.servlet.http.Cookie"%><%@ page import="java.util.*,net.joycool.wap.framework.JoycoolSpecialUtil"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.encoder.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><%
UserBean user = (UserBean)session.getAttribute("user");
if(user==null){
	user = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
}
if(user==null||user.getCreateDatetime()!=null&&System.currentTimeMillis()-user.getCreateDatetime().getTime()>86400000l){
	response.sendRedirect("/lswjs/index.jsp");
	return;
}

String userName = StringUtil.toWml(user.getNickName());
String oripwd = Encoder.decrypt(user.getPassword());
String password = StringUtil.toWml(oripwd);

if(request.isRequestedSessionIdFromCookie()){	// 如果支持cookie，保存用户数据
	String password2 = Base64x.encodeMd5(oripwd);
	String id = Base64x.encodeInt(user.getId());
	String data = password2.substring(0, 5) + id + password2.substring(5, 11);
	Cookie cookie = new Cookie("jcal", data);	// joycool auto login cookie
	cookie.setMaxAge(90000000);
	cookie.setPath("/");
	response.addCookie(cookie);
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="注册消息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
恭喜你,注册成功了!<br/>
欢迎来到乐酷游戏社区!赠送您新人卡一张,祝您玩的愉快.<br/>
您的ID是<%= user.getId() %>,密码<%= password %>,请牢牢记住哦.<br/>
请先看一下<a href="/beginner/index.jsp">新手帮助</a>,有赚币窍门的:)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
