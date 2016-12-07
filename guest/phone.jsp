<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.regex.Matcher,java.util.regex.Pattern,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   String mobile = action.getParameterNoEnter("m");
   GuestUserInfo guestUser = action.getGuestUser();	//此方法会先从session中，找从cookie中找
   if (guestUser == null){
	   response.sendRedirect("index.jsp");
	   return;
   }
   if (guestUser.getMobile() != null && !"".equals(guestUser.getMobile())){
	   tip = "你已经绑定过手机号了.";
   } else {
	   if (mobile != null && !"".equals(mobile)){
		   Pattern pattern = Pattern.compile("(1\\d{10})");
		   Matcher isPhone = pattern.matcher(mobile);
		   if (!isPhone.matches()){
			   // tip = "手机号码输入错误,请重新输入.(3秒后自动返回绑定手机)";
			   response.sendRedirect("back.jsp?i=4");
			   return;
		   } else {
			   int id = SqlUtil.getIntResult("select id from guest_user_info where mobile='" + mobile + "'",6);
			   if (id > 0){
				   // tip = "您输入的手机号码已被其他用户绑定,请核对后重新输入.(3秒后自动返回绑定手机)";
				   response.sendRedirect("back.jsp?i=5");
				   return;
			   } else {
				   SqlUtil.executeUpdate("update guest_user_info set mobile='" + mobile + "' where id=" + guestUser.getId(),6);
				   // tip = "恭喜您绑定手机号码成功!您以后就可以用绑定手机号进行登陆了~(3秒后自动返回游乐园)";
				   guestUser.setMobile(mobile);
				   response.sendRedirect("back.jsp?i=6");
				   return;
			   }
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="绑定手机"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>请输入您的手机号码:(绑定后不可修改)<br/>
<input name="mobile" maxlength="11"/><br/>
<anchor>确定<go href="phone.jsp" method="post">
	<postfield name="m" value="$mobile" />
</go></anchor>|<a href="index.jsp">返回</a><br/>
<%
} else {
	%><%=tip%><br/><a href="index.jsp">直接返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>