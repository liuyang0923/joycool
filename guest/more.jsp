<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   GuestHallAction action2 = new GuestHallAction(request,response);
   List actionList = GuestAction.getChatActionList();
   ChatAction chatAction = null;
   int toId = action.getParameterInt("tid");	// 给谁发信息？
   int submit = action.getParameterInt("s");
   String tip = "";
   if (toId <= 0){
	   response.sendRedirect("list.jsp");
	   return;
   }
   GuestUserInfo toGuest = action.getGuestById(toId);
   GuestUserInfo myGuest = action2.getGuestUser();
   if (toGuest == null){
	   tip = "游客不存在.";
   } else if (myGuest.getId() == toGuest.getId()){
	   tip = "不能与自己聊天哦.";
   } else {

   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>对他做规定动作:(直接点击)<br/>
<%for (int i = 0;i < actionList.size();i++){
chatAction = (ChatAction)actionList.get(i);
%><a href="send.jsp?s=2&amp;aid=<%=chatAction.getId()-1 %>&amp;tid=<%=toId%>"><%=StringUtil.toWml(chatAction.getTitle())%></a>.<%if((i+1) % 3 == 0){%><br/><%}%><%
}
%><br/><a href="send.jsp?tid=<%=toId%>">返回上一页</a><br/>
<a href="chat.jsp">返回游客聊天室</a><br/>
<%
} else {
	%><%=tip%><br/><a href="chat.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>