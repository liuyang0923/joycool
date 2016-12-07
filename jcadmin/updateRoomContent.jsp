<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.service.infc.IChatService" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentBean" %><%@ page import="net.joycool.wap.util.RoomCacheUtil" %><%@ page import="net.joycool.wap.framework.*"%><%

//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
response.setHeader("Cache-Control","no-cache");

String id=request.getParameter("id");
String fromNickName=request.getParameter("fromNickName");
String toNickName=request.getParameter("toNickName");
String roomId=request.getParameter("roomId");
String content=request.getParameter("content");
if(id!=null || fromNickName!=null || toNickName!=null || content!=null){
	RoomCacheUtil.updateRoomContent("from_nickname='"+fromNickName+"',to_nickname='"+toNickName+"',content='"+content+"'","id="+id,Integer.parseInt(id));
	//response.sendRedirect(("chatHall.jsp?roomId="+roomId));
	BaseAction.sendRedirect("/jcadmin/chatHall.jsp?roomId="+roomId, response);
	return;
}
IChatService chatService = ServiceFactory.createChatService();
String update=request.getParameter("update");
JCRoomContentBean roomContent=null;
if(update==null ||update.equals("")){
	//response.sendRedirect("http://wap.joycool.net/jcadmin/roomList.jsp");
	BaseAction.sendRedirect("/jcadmin/roomList.jsp", response);
	return;
}else{
roomContent=chatService.getContent("id="+update);
}

%>
<html>
<body>
<%if(roomContent!=null){%>
<form name="form" method="post" action="updateRoomContent.jsp">
<input type="hidden" name="id" value="<%=update%>"/>
<input type="hidden" name="roomId" value="<%=roomId%>"/>
<input type="text" name="fromNickName" value="<%=roomContent.getFromNickName()%>"/><br/>
<input type="text" name="toNickName" value="<%=roomContent.getToNickName()%>"/><br/>
<textarea  name="content" rows="5" cols="15" /><%=roomContent.getContent()%></textarea><br/>
<input type="submit" name="alter" value="更改" /><input type="reset" name="reset" value="重置"><br/>
</form>
<%}else{
	//response.sendRedirect("http://wap.joycool.net/jcadmin/roomList.jsp");
	BaseAction.sendRedirect("/jcadmin/roomList.jsp", response);
	return;
}%>
</body>
</html>