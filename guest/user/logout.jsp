<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.user.*"%>
<% response.setHeader("Cache-Control","no-cache");
UserAction2 action = new UserAction2(request);
UserBean2 user = action.getLoginUser2();
if (user != null){
	session.removeAttribute(UserAction2.LOGIN_USER_KEY2);
}
response.sendRedirect("/user/login.jsp");
%>