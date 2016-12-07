<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   int submit = action.getParameterInt("s");
   Guest myGuest = action.getGuestBean();
   if (myGuest == null){
	   myGuest = action.createGuest();
   }
   int mode = 0;
   String tip = "";
   if (!action.addUser(myGuest)){
	   tip = (String)request.getAttribute("tip");
   } else {
	   if (submit == 1){
		   String name = action.getParameterNoEnter("name");
		   int age = action.getParameterInt("age");
		   int gender = action.getParameterInt("gender");
		   if (name == null || "".equals(name) || name.length() > 6){
			   tip = "没有填写昵称或昵称太长.";
		   } else if (age <= 0 || age > 100){
			   tip = "没有填写年龄,或年龄太大.";
		   } else {
			   // 修改昵称
			   Guest guest1 = new Guest();
			   guest1.setId(myGuest.getId());
			   guest1.setDbId(myGuest.getDbId());
			   guest1.setNickName(name);
			   guest1.setUid(myGuest.getUid());
			   guest1.setPhone(myGuest.getPhone());
			   guest1.setPassword(myGuest.getPassword());
			   guest1.setGender(gender);
			   guest1.setAge(age);
			   guest1.setCreateTime(myGuest.getCreateTime());
			   // 修改游客列表中的Bean
			   GuestAction.getGuestMap().put(new Integer(myGuest.getId()),guest1);
			   // 加入已修改昵称用户列表
			   action.addToChangedList(myGuest.getId());
			   if (guest1.getPhone() != null && guest1.getPhone().length() > 0){
				   GuestAction.service.modifyGuest(guest1);
				   response.sendRedirect("chat.jsp");
				   return;
			   }
			   mode = 1;
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (mode == 0){
%>修改资料:<br/>
昵称:<input name="name" value="" maxlength="6" /><br/>
年龄:<input name="age" value="" maxlength="3"  format="*N"/><br/>
性别:<select name="gender" value="3">
<option value="1">男</option>
<option value="2">女</option>
<option value="3">保密</option>
</select><br/>
<anchor>
修改<go href="modify.jsp?s=1" method="post">
	<postfield name="name" value="$name" />
	<postfield name="age" value="$age" />
	<postfield name="gender" value="$gender" />
</go>
</anchor>|<a href="chat.jsp">返回</a><br/><a href="../register.jsp">我想升级成免费高级用户</a><br/><%
} else {
%>修改资料成功.<br/>
<a href="/register.jsp">直接升成免费高级用户</a><br/>
<a href="chat.jsp">进入游客聊天室</a><br/><%	
}
} else {
	%><%=tip%><br/><a href="modify.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>