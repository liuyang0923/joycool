<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.regex.Matcher,java.util.regex.Pattern,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("nick.jsp");
	   return;
   }
   if (guestUser.getFlag() == 1){
	   tip  = "你已经设置过密码了.";
   } else {
	   String pw = action.getParameterNoEnter("pw");
	   if (pw != null || "".equals(pw)){
		   Pattern pattern = Pattern.compile("[0-9a-zA-Z]*");	// 只匹配字符和数字
		   Matcher match = pattern.matcher(pw);
		   if (!match.matches()){
		   		// tip ="您输入的密码有误,请重新输入.";
		   		response.sendRedirect("back.jsp?i=3");
		   		return;
		   } else if (pw.length() < 4 || pw.length() > 10){
			   // tip = "您输入的密码有误,请重新输入.";
		   		response.sendRedirect("back.jsp?i=3");
		   		return;
		   } else {
			   guestUser.setPassword(pw);
			   guestUser.setFlag(1);
			   SqlUtil.executeUpdate("update guest_user_info set `password`='" + pw + "',flag=1 where id=" + guestUser.getId() ,6);
			   // 重写cookie
			   action.saveToCookie(guestUser);
			   // 重放入session
			   session.setAttribute(GuestHallAction.GUEST_KEY,guestUser);
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="设置密码"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (guestUser.getFlag() == 0){
// 用户还没有修改过密码
%>请输入4~10个英文字母或数字作为密码:<br/>
<input name="pw" maxlength="10"/><br/>
<anchor>
	确定
	<go href="pw.jsp" method="post">
		<postfield name="pw" value="$pw" />
	</go>
</anchor><br/>
<%	
} else {
%>恭喜您设置密码成功,您以后就可以通过昵称进行登录了哦~<br/>
<a href="/guest/chat.jsp">游客聊天室</a>|<a href="/jcforum/index.jsp">论坛</a><br/>
<a href="/guest/nb/game.jsp">名字作战</a>|<a href="/guest/pt/instructions.jsp">智慧拼图</a><br/>
<a href="/guest/question/index.jsp">问答接龙</a>|<a href="/guest/fish/index.jsp">欢乐渔场</a><br/>
<a href="/guest/dicedx/index.jsp">骰子</a>|<a href="/guest/showhand/index.jsp">梭哈</a>|<a href="/guest/tiger/index.jsp">老虎机</a><br/>
<a href="/guest/farmer/index.jsp">完美农夫</a>|<a href="/guest/sd/index.jsp">数独</a><br/>
*温馨提示:为了您可以更好的享受我们带给您的服务,我们建议您<a href="phone.jsp">免费绑定手机号码</a>,绑定后您可以用您的手机号进行登陆,并且能保障您的虚拟财产安全!<br/>
<%
}
} else {
	%><%=tip%><br/><%
}%><a href="index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>