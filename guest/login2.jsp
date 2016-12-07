<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   UserBean user = action.getLoginUser();
   GuestUserInfo autoUser = action.getGuestUserCk();	// 尝试从cookie中取
   GuestUserInfo guestUser = action.getGuestUser();
//**********************从session中找,找到就直接返回**********************
   if (guestUser != null){
	   response.sendRedirect("back.jsp?i=19");
	   return;
   }
//**********************从cookie中查找,如果找到就直接登陆了**********************
   int autoLogin = action.getParameterInt("al");
   if (autoLogin == 1 && autoUser != null){
	   session.setAttribute(GuestHallAction.GUEST_KEY,autoUser);
	   // 给今日奖励
//	   action.getAwardToday(autoUser);
	   response.sendRedirect("back.jsp?i=18&uid=" + autoUser.getId());
	   return;
   }
//**********************如果是登陆用户的话,就用他绑定的游客信息登陆.如果没有,就给他新建一个**********************
   if (user != null){
	   guestUser = GuestHallAction.service.getUserInfo(" buid=" + user.getId() + " order by id limit 1");
	   if (guestUser != null){
		   // 写入cookie
		   action.saveToCookie(guestUser);
		   // 写入session
		   session.setAttribute(GuestHallAction.GUEST_KEY,guestUser);
		   // 绑定的游客用户登陆成功
		   // 给今日奖励
//	   	   action.getAwardToday(guestUser);
		   response.sendRedirect("back.jsp?i=18&uid=" + guestUser.getId());
		   return;
	   } else {
		   // 没有，就给它建立一个
		   String userName = user.getNickName();
		   guestUser = GuestHallAction.service.getUserInfo(" user_name='" +StringUtil.toSql(userName) + "'");
		   if (guestUser != null){
			   // 该用户的昵称和某位游客的昵称重名了
			   userName = userName + user.getId();
		   }
		   int result = action.addUser(userName);	// 此方法在添加用户时已经将用户的bean放入session中，并且写了cookie了。不要再写一次。
		   // 这里的guestUser得从session里重新取一遍才是正确的
		   if (result == 0){
			   guestUser = action.getGuestUser();
			   // 给今日奖励
//		   	   action.getAwardToday(guestUser);
			   response.sendRedirect("back.jsp?i=18&uid=" + guestUser.getId());
			   return;
		   } else {
			   response.sendRedirect("back.jsp?i=" + result);
			   return;
		   }
	   }
   }
//**********************根据输入的信息登陆**********************
   String nick = action.getParameterNoEnter("n");
   String pw = action.getParameterNoEnter("pw");
   if (nick != null && pw != null){
	   if (nick.length() < 2 || nick.length() > 11 || pw.length() < 4 || pw.length() > 10){
		   // tip = "没有找到此昵称用户,请重新输入.(3秒后自动返回登陆)";
		   response.sendRedirect("back.jsp");
		   return;
	   } else {
		   guestUser = action.getGuestUser(nick,pw);
		   if (guestUser == null){
			   // tip = "没有找到此昵称用户,请重新输入.(3秒后自动返回登陆)";
			   response.sendRedirect("back.jsp");
			   return;
		   } else {
			   // 写入cookie
			   action.saveToCookie(guestUser);
			   // 写入session
			   session.setAttribute(GuestHallAction.GUEST_KEY,guestUser);
			   // 完成登陆。欢迎回来。
			   // 给今日奖励
//	   	       action.getAwardToday(guestUser);
			   out.clearBuffer();
			   response.sendRedirect("back.jsp?i=18&uid=" + guestUser.getId());
			   return;
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="登陆"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (autoUser != null){
%>欢迎回来,<%=autoUser.getUserNameWml()%><br/><a href="login2.jsp?al=1">>>我要自动登陆</a><br/><%
}
if (guestUser == null){
%>请输入您的昵称或绑定手机号:<br/>
<input name="nick" maxlength="11"/><br/>
请输入密码:<br/>
<input name="pw" maxlength="10"/><br/>
<anchor>
	登陆
	<go href="login2.jsp" method="post">
		<postfield name="n" value="$nick" />
		<postfield name="pw" value="$pw" />
	</go>
</anchor><br/>
若您还没有设置过昵称,请先点<a href="nick.jsp">这里</a>.<br/>
<a href="index.jsp">返回游乐园</a><br/><%
}
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>