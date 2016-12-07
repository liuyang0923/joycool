<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   boolean isMyself = true;
   boolean isFocused = false;
   GuestUserInfo guestUser = null;
   int uid = action.getParameterInt("uid");
   if (uid > 0){
	   guestUser = GuestHallAction.getGuestUser(uid);
	   if (guestUser == null){
		   tip = "要查看的用户不存在.";
	   } else {
		   isMyself = false;
		   // 查看我是不是关注了他
		   GuestUserInfo guestUser2 = action.getGuestUser();
		   if (guestUser2 == null){
			   response.sendRedirect("login.jsp");
			   return;
		   }
		   if (guestUser2.getId() == guestUser.getId()){
			   isMyself = true;		// 要查看的uid等于自己的uid
		   } else {
			   if (GuestHallAction.service.getUserFocus(" left_uid=" + guestUser2.getId() + " and right_uid=" + guestUser.getId()) != null){
				   isFocused = true;
			   }
		   }
	   }
   } else {
	   guestUser = action.getGuestUser();
	   if (guestUser == null){
		   response.sendRedirect("login.jsp");
		   return;
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="个人资料"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=guestUser.getUserNameWml()%>[ID:<%=guestUser.getId()%>]<br/>
<%if (isMyself){
if (guestUser.getFlag() == 0){
%><a href="pw.jsp">设置密码</a><br/><%
} else {
%><a href="info2.jsp">修改密码</a><br/><%
}
}%>
<%=guestUser.getGenderStr()%>/<%=guestUser.getAge()%>岁<br/>
<%if (isMyself){
%><a href="info2.jsp?t=1">修改资料</a><br/><%
}%>
等级:<%=guestUser.getLevel()%><br/>
经验值:<%=guestUser.getPoint()%><br/>
游币:<%=guestUser.getMoney()%><br/>
称号:<%=action.getTitleStr(guestUser,"info2.jsp",isMyself)%><br/>
<%if(isMyself){
%><a href="title.jsp">点击获得新的称号</a><br/><%	
}%>
被关注设置:<%=guestUser.getFocusString()%><br/>
<%if (isMyself){
%><a href="info2.jsp?t=2">修改关注设置</a><br/><%	
}else{
	if (isFocused){
		%><a href="focus.jsp?d=1&amp;fu=<%=guestUser.getId()%>">删除关注</a><%
	} else {
		%><a href="focus.jsp?a=2&amp;fu=<%=guestUser.getId()%>">添加关注</a><%
	}
%>|<a href="send.jsp?tid=<%=guestUser.getId()%>">与<%=guestUser.getGenderStr2()%>聊天</a><br/><%
}%>
<a href="index.jsp">返回游乐园</a><br/>
<%
} else {
%><%=tip%><br/><a href="index.jsp">返回游乐园</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>