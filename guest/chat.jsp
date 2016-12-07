<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! public static int COUNT_PRE_PAGE = 8; %>	
<% response.setHeader("Cache-Control","no-cache");
   GuestAction action = new GuestAction(request);
   GuestHallAction action2 = new GuestHallAction(request,response);
   int remove = action.getParameterInt("r");
   int toId = action.getParameterInt("tid");	// 给谁发信息？
   String tip = "";
   GuestChat chatBean = null;
   List list = null;
   List list2 = null;
   PagingBean paging1 = null;
   PagingBean paging2 = null;
//  Guest guest = action.getGuestBean();
//  if (guest == null){
//	   guest = action.createGuest();
//  }
   GuestUserInfo guestUser = action2.getGuestUser();
   action.enter(guestUser);	// 进入聊天室
   if (remove == 1){
	   action.exitHall(guestUser);			// 从聊天室里删除某人
	   response.sendRedirect("mess.jsp");
	   return;
   }
   if (guestUser != null){
	   action.addUser(guestUser);
	   list = GuestAction.service.getChatList(" gid1 = " + guestUser.getId() + " or gid2 = " + guestUser.getId() + " order by id desc");
	   paging1 = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p1");
   }
   list2 = GuestAction.getChatList();
   paging2 = new PagingBean(action, list2.size(), COUNT_PRE_PAGE, "p2");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游客聊天室" ontimer="<%=response.encodeURL("chat.jsp")%>"><timer value="500"/><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>==游客聊天室(<%=GuestAction.getChatUserList().size()%>人)==<br/><%=guestUser != null ? guestUser.getUserNameWml2() + "<br/>": ""%><%
if (list != null && list.size() > 0){
	for (int i = paging1.getStartIndex() ; i < paging1.getEndIndex() ; i++){
		chatBean = (GuestChat)list.get(i);
		%><%=i+1%>.<%if (chatBean.getGid1() == guestUser.getId()){%>我<%}else{%><a href="send.jsp?tid=<%=chatBean.getGid1()%>"><%=chatBean.getNcNameWml1()%></a><%} %>:<%if (chatBean.getGid2() == guestUser.getId()){%>你<%}else{%><a href="send.jsp?tid=<%=chatBean.getGid2()%>"><%=chatBean.getNcNameWml2()%></a><%} %>:<%=StringUtil.toWml(chatBean.getContent()) %>(<%=DateUtil.sformatTime(chatBean.getCreateTime()) %>)<%if(chatBean.getGid2() == guestUser.getId()){%><a href="send.jsp?tid=<%=chatBean.getGid1()%>">回复</a><%}%><br/><%
	}
}%><%=paging1 != null ? paging1.shuzifenye("chat.jsp",false,"|",response) : ""%><%
%><a href="send.jsp?tid=<%=toId%>">我要发言</a>.<a href="chat.jsp">刷新</a>.<a href="help.jsp">本室说明</a><br/>
==游客聊天大厅==<br/>
<% for (int i = paging2.getStartIndexR();i > paging2.getEndIndexR();i--){
chatBean = (GuestChat)list2.get(i);
if (guestUser != null){
%><%=paging2.getStartIndexR()-i+1%>.<%if (chatBean.getGid1() == guestUser.getId()){%>我<%}else{%><a href="send.jsp?tid=<%=chatBean.getGid1()%>"><%=chatBean.getNcNameWml1()%></a><%} %>:<%if (chatBean.getGid2() == guestUser.getId()){%>你<%}else{%><a href="send.jsp?tid=<%=chatBean.getGid2()%>"><%=chatBean.getNcNameWml2() %></a><%} %>:<%=chatBean.getContentWml()%>(<%=DateUtil.sformatTime(chatBean.getCreateTime()) %>)<br/><%
} else {
%><%=paging2.getStartIndexR()-i+1%>.<a href="send.jsp?tid=<%=chatBean.getGid1()%>"><%=chatBean.getNcNameWml1()%></a>:<a href="send.jsp?tid=<%=chatBean.getGid2()%>"><%=chatBean.getNcNameWml2() %></a>:<%=chatBean.getContentWml()%>(<%=DateUtil.sformatTime(chatBean.getCreateTime()) %>)<br/><%	
}
}%><%=paging2.shuzifenye("chat.jsp",false,"|",response)%>
<a href="list.jsp">查看聊天室的聊客</a><br/>
<a href="../chat/hall.jsp">查看正式用户的聊天室</a><br/>
点击<a href="chat.jsp?r=1">退出聊天室</a>,不被骚扰<br/>
您的临时昵称:<%=guestUser != null ? guestUser.getUserNameWml2() : ""%><br/>
<a href="index.jsp">返回游乐园</a><br/>
<%
} else {
	%><%=tip%><br/><a href="/lswjs/index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>