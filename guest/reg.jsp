<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,java.util.regex.Matcher,java.util.regex.Pattern,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   Guest guest = action.getGuestBean();
   if (guest == null){
	   guest = action.createGuest();
   }
   int mode = 0;
   String tip = "";
   int register = action.getParameterInt("r");
   if (register == 1){
	   // 注册
	   if (guest.getPhone() != null && !("".equals(guest.getPhone()))){
		   tip = "您已经注册过了,请注销后再注册.";
	   } else {
		   String phone = action.getParameterNoEnter("p").trim();
		   String password = action.getParameterNoEnter("ps").trim();
		   Pattern pattern = Pattern.compile("(1\\d{10})");
		   Matcher isPhone = pattern.matcher(phone);
		   if (!isPhone.matches()){
			   tip = "手机号格式输入错误.";
		   } else if ("".equals(password) || password.length() < 3 || password.length() > 9){
			   tip = "密码应为3－9位字符.";
		   } else {
			   guest.setPhone(phone);
			   guest.setPassword(password);
			   boolean result = action.register(guest);
			   if (result){
				   request.setAttribute("top","恭喜您成为乐酷初级会员!您要修改您的昵称,以便更好聊天吗?<br/>");
//				   request.getRequestDispatcher("modify.jsp").forward(request,response);
//				   return;
				   mode = 1;
			   } else {
				   tip = (String)request.getAttribute("tip");
			   }
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if ( mode == 0 ){
%>该注册只对游客聊天室有效.<br/>
手机号:<input name="phone" value="" maxlength="11" format="*N" /><br/>
密码:<input name="ps" value="" maxlength="9" />(3-9位)<br/>
输入后点击确认,您以后就可以成为初级会员登录游客聊天室啦.<br/>
<anchor>
确认
<go href="reg.jsp?r=1" method="post">
	<postfield name="p" value="$phone" />
	<postfield name="ps" value="$ps" />
</go>
</anchor>|<a href="chat.jsp">返回</a><br/><%	
} else {
%>恭喜你成为乐酷初级用户,您的资料已经保存,尽情享受交友的快乐吧!<br/>
<a href="../register.jsp">我想升级成免费高级用户</a><br/>
<a href="chat.jsp">进入游客聊天室</a><br/><%	
}
} else {
	%><%=tip%><br/><a href="reg.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>