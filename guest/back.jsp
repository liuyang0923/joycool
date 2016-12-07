<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! static String[] tips = {"您的输入有误,请重新输入.(3秒后自动返回登陆)",
							"您输入的昵称有误,请重新输入.(3秒后自动返回设置昵称)",
							"您输入的昵称已存在,请重新输入.(3秒后自动返回设置昵称)",
							"您输入的密码有误,请重新输入.(3秒后自动返回设置密码)",
							"手机号码输入错误,请重新输入.(3秒后自动返回绑定手机)",
							"您输入的手机号码已被其他用户绑定,请核对后重新输入.(3秒后自动返回绑定手机)",
							"恭喜您绑定手机号码成功!您以后就可以用绑定手机号进行登陆了~(3秒后自动返回游乐园)",
							"添加关注成功(3秒后自动返回关注列表)",
							"添加失败,您输入的ID有误,请重新输入(3秒后自动返回关注列表)",
							"对方将关注权限设置为:需要身份验证,请等待对方通过.(3秒后自动返回关注列表)",
							"添加失败,对方将关注权限设置为:拒绝任何人关注.(3秒后自动返回关注列表)",
							"你已添加过此用户.(3秒后自动返回关注列表)",
							"不可添加自己.(3秒后自动返回关注列表)",
							"密码修改已成功(3秒后自动返回个人资料)",
							"您输入的旧密码错误,请重新输入(3秒后自动返回修改密码)",
							"您输入的新密码错误,请重新输入(3秒后自动返回修改密码)",
							"年龄输入错误.(3秒后自动返回修改资料)",
							"修改成功.(3秒后自动返回个人资料)",
							"欢迎您回来",
							"你已经登陆过了.(3秒后自动返回游乐园)",
							"你已经设置过昵称了.(3秒后自动返回游乐园)",
							"已经成功删除关注.(3秒后自动返回游客聊天室)",
							"添加关注成功(3秒后自动返回游客聊天室)",
							"您没有此称号(3秒后返回个人资料)",
							"昵称包含不良词汇,请重新设置.",
							};
	static String[] back = {"login.jsp",
							"nick.jsp",
							"nick.jsp",
							"pw.jsp",
							"phone.jsp",
							"phone.jsp",
							"index.jsp",
							"focus.jsp",
							"focus.jsp",
							"focus.jsp",
							"focus.jsp",
							"focus.jsp",
							"focus.jsp",
							"info.jsp",
							"info2.jsp",
							"info2.jsp",
							"info2.jsp?t=1",
							"info.jsp",
							"index.jsp",
							"index.jsp",
							"index.jsp",
							"chat.jsp",
							"chat.jsp",
							"info.jsp",
							"nick.jsp",
							};
%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   GuestUserInfo guestUser = null;
   int uid = action.getParameterInt("uid");
   if (uid > 0){
	   guestUser = GuestHallAction.getGuestUser(uid);
   }
   int i = action.getParameterInt("i");
   if (i < 0 || i > tips.length){
	   i = 0;
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="返回" ontimer="<%=response.encodeURL("" + back[i] + "")%>"><timer value="30"/><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (guestUser != null && i == 18){
	%><%=tips[i] + guestUser.getUserNameWml()%>.(3秒后自动返回游乐园)<br/><a href="<%=back[i]%>">直接返回</a><br/><%
} else {
	%><%=tips[i]%><br/><a href="<%=back[i]%>">直接返回</a><br/><%
}
} else {
%><%=tip%><br/><a href="index.jsp">返回首页</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>