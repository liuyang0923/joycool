<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   String title = "";
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("login.jsp");
	   return;
   }
   int type = action.getParameterInt("t");	// 0:改密码,1:改资料,2:改关注,3:改显示称号
   if (type < 0 || type > 3){
	   type = 0;
   }
   if (type == 0){
	   // 改密码
	   title = "修改密码";
	   int changePw = action.getParameterInt("cp");
	   if (changePw == 1){
		   String pw1 = action.getParameterNoEnter("pw1");
		   String pw2 = action.getParameterNoEnter("pw2");
		   int result = action.changePw(guestUser,pw1,pw2);
		   response.sendRedirect("back.jsp?i=" + result);
		   return;
	   }
   }
   if (type == 1){
	   // 改资料
	   title = "修改资料";
	   int changeInfo = action.getParameterInt("ci");
	   if (changeInfo == 1){
		   int age = action.getParameterInt("a");
		   int gender = action.getParameterInt("g");
		   if (age <=0 || age >= 150){
			   response.sendRedirect("back.jsp?i=16");
			   return;
		   }
		   if (gender < 0 || gender > 2){
			   gender = 2;
		   }
		   SqlUtil.executeUpdate("update guest_user_info set age=" + age + ",gender=" + gender + " where id=" + guestUser.getId(),6);
		   guestUser.setAge(age);
		   guestUser.setGender(gender);
		   response.sendRedirect("back.jsp?i=17");
		   return;
	   }
   }
   if (type == 2){
	   // 修改被关注
	   title = "修改关注";
	   int changeFocus = action.getParameterInt("cf");
	   if (changeFocus == 1){
		    int focus = action.getParameterInt("f");
	   		SqlUtil.executeUpdate("update guest_user_info set focus=" + focus + " where id=" + guestUser.getId(),6);
	   		guestUser.setFocus(focus);
	   		response.sendRedirect("back.jsp?i=17");
	   		return;
	   }
   }
   int chenghao = action.getParameterInt("ch");
   if (type == 3){
	   // 修改称号
	   title = "修改称号";
	   if (!action.isHaveTitle(guestUser,chenghao)){
		   response.sendRedirect("back.jsp?i=23");		// 你没有此称号
		   return;
	   } else {
		   int affirm = action.getParameterInt("af");
		   if (affirm == 1){
			   guestUser.setTitleNow(chenghao);
			   SqlUtil.executeUpdate("update guest_user_info set title_now=" + guestUser.getTitleNow() + " where id=" + guestUser.getId(),6);
			   response.sendRedirect("info.jsp");
			   return;
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (type == 0){
// 改密码
%>输入旧密码:<br/><input name="pw1"/><br/>
输入新密码(4~10个英文字母或数字):<br/><input name="pw2"/><br/>
<anchor>
	确认修改密码
	<go href="info2.jsp?cp=1" method="post">
		<postfield name="pw1" value="$pw1" />
		<postfield name="pw2" value="$pw2" />
	</go>
</anchor><br/><a href="info.jsp">返回</a><br/>
<%
} else if (type == 1){
// 改资料
%>年龄:<br/><input name="age" format="*N"/><br/>
性别:<br/>
<select name="gender" value="<%=guestUser.getGender()%>">
<option value="0">女</option>
<option value="1">男</option>
<option value="2">保密</option>
</select><br/>
<anchor>
	修改
	<go href="info2.jsp?t=1&amp;ci=1" method="post">
		<postfield name="a" value="$age" />
		<postfield name="g" value="$gender" />
	</go>
</anchor>|<a href="info.jsp">返回</a><br/>
<%if (guestUser.getFlag() == 0){
%><a href="pw.jsp">设置密码</a><br/><%
} else if (guestUser.getMobile() == null || "".equals(guestUser.getMobile())){
%><a href="phone.jsp">绑定手机</a><br/><%
}%>
<%
} else if(type == 2){
// 改关注
%>被关注设置:<br/>
<select name="focus" value="<%=guestUser.getFocus()%>">
<option value="0">允许任何人关注</option>
<option value="1">拒绝任何人关注</option>
<option value="2">需要身份验证</option>
</select><br/>
<anchor>
	修改
	<go href="info2.jsp?t=2&amp;cf=1" method="post">
		<postfield name="f" value="$focus" />
	</go>
</anchor>|<a href="info.jsp">返回</a><br/>
<%
} else {
// 改显示称号
%>确认将[<%=GuestHallAction.title[chenghao]%>]做为您的展示称号!<br/>
<a href="info2.jsp?t=3&amp;ch=<%=chenghao%>&amp;af=1">确认</a><br/>
<a href="info.jsp">返回</a><br/>
<%
}
} else {
%><%=tip%><br/><a href="index.jsp">返回游乐园</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>