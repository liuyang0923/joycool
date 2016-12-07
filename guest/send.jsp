<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%><%!
	%><% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   GuestHallAction action2 = new GuestHallAction(request,response);
   List actionList = GuestAction.getChatActionList();
   UserFocus focus = null;
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
   if (myGuest == null){
	   response.sendRedirect("nick.jsp?f=1");
	   return;
   }
   if (toGuest == null){
	   response.sendRedirect("chat.jsp");
	   return;
   } else if (myGuest.getId() == toGuest.getId()){
	   tip = "不能与自己聊天哦.";
   } else {
	   focus = GuestHallAction.service.getUserFocus(" left_uid=" + myGuest.getId() + " and right_uid=" + toGuest.getId());
	   if (submit == 1){
		   // 私聊
		   if (action.isCooldown("chat",5000)){
			   String content = action.getParameterNoEnter("c");
			   
			   if (content == null || "".equals(content) || " ".equals(content) || content.length() > 50){
				   tip = "没有输入聊天内容或内容太长.";
			   } else {
			   	  content = nick_jsp.wordsFilter.matcher(content).replaceAll(",");
				   GuestChat chat = new GuestChat();
				   chat.setGid1(myGuest.getId());
				   chat.setNcName1(myGuest.getUserName2());
				   chat.setGid2(toGuest.getId());
				   chat.setNcName2(toGuest.getUserName2());
				   chat.setContent(content);
				   action.doPrivChat(chat);
				   response.sendRedirect("chat.jsp");
				   return;
			   }
		   } else {
			   tip ="你发言太快了.";
		   }
	   } else if (submit == 2){
		   // 大厅
		   if (action.isCooldown("chat",5000)){
			   int actionId = action.getParameterInt("aid");
			   ChatAction ca = (ChatAction)GuestAction.getChatActionList().get(actionId);
			   if (ca == null){
				   tip = "此动作不存在.";
			   } else {
				   // 写入聊天List,同时写入数据库
				   String content = action.doGuestChat(myGuest,toGuest,ca);
				   response.sendRedirect("chat.jsp");
				   return;
			   }
		   } else {
			   tip ="你发言太快了.";
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>对方:<%=toGuest.getUserNameWml2() %><br/>性别:<%=toGuest.getGenderStr()%>,年龄:<%=toGuest.getAge()%><br/>
<input name="content" value="" maxlength="100" /><br/>
<anchor>
	发言
	<go href="send.jsp?s=1&amp;tid=<%=toId %>" method="post">
		<postfield name="c" value="$content" />
	</go>
</anchor><br/>
对<%=toGuest.getGenderStr2()%>做规定动作:(直接点击)<br/>
<%for (int i = 0;i < 5;i++){
chatAction = (ChatAction)actionList.get(i);
%><a href="send.jsp?s=2&amp;aid=<%=chatAction.getId()-1%>&amp;tid=<%=toId%>"><%=StringUtil.toWml(chatAction.getTitle())%></a><br/><%
}
%>
<a href="more.jsp?tid=<%=toId%>">更多>></a><br/>
<a href="info.jsp?uid=<%=toGuest.getId()%>">查看资料</a>|<%if (focus==null){%><a href="focus.jsp?a=2&amp;fu=<%=toGuest.getId()%>">添加关注</a><%} else {%><a href="focus.jsp?d=1&amp;fu=<%=toGuest.getId()%>">删除关注</a><%}%><br/>
============<br/>
<a href="chat.jsp">返回游客聊天室</a><br/>
您的临时昵称:<%=myGuest.getUserNameWml2()%><br/>
<%
} else {
	%><%=tip%><br/><%if (toId != myGuest.getId()){%><a href="send.jsp?tid=<%=toId%>">返回</a><%}else{%><a href="list.jsp">返回</a><%} %><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>