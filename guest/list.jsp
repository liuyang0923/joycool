<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! public static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   GuestHallAction action2 = new GuestHallAction(request,response);
   String tip = "";
   int tmpId = 0;
   Guest guest = null;
   PagingBean paging = null;
   GuestUserInfo guestUser2 = null;
   GuestUserInfo guestUser = action2.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("nick.jsp?f=1");
	   return;
   }
   List chatUserList = GuestAction.getChatUserList();
   if (!action.addUser(guestUser)){
	   tip = (String)request.getAttribute("tip");
   } else {
	   paging = new PagingBean(action, chatUserList.size(), COUNT_PRE_PAGE, "p");
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>以下是在聊天中的人:<br/>
<%for (int i = paging.getStartIndexR();i > paging.getEndIndexR();i--){
tmpId = StringUtil.toInt(chatUserList.get(i).toString());
guestUser2 = action.getGuestById(tmpId);
if (guestUser2 != null){
%><%if (guestUser2.getId() != guestUser.getId()){%><a href="send.jsp?tid=<%=guestUser2.getId()%>"><%=guestUser2.getUserNameWml2()%></a><%}else{%><%=guestUser2.getUserNameWml2()%><%} %><br/><%	
}%><%	
}%><%=paging.shuzifenye("list.jsp",false,"|",response)%><%
%>
<a href="chat.jsp">返回游客聊天室</a><br/>
<a href="../register.jsp">我要升级成正式用户</a><br/>
<a href="../user/login.jsp">老用户登陆</a><br/>
<a href="chat.jsp">返回上一页,我想再转转</a><br/>
您的临时昵称:<%=guestUser.getUserNameWml2()%><br/>
<%
} else {
	%><%=tip%><br/><a href="chat.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>