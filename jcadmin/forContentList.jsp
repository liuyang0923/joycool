<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.UserInfoUtil,net.joycool.wap.framework.*" %><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

IChatService chatService = ServiceFactory.createChatService();
IUserService userService = ServiceFactory.createUserService();
String check=request.getParameter("check");
UserBean toUser1 = null;
UserBean toUser2 = null;
Vector contentList=chatService.getContentList("from_id="+check);
JCRoomContentBean content=null;
String mark=null;
String pic=null;
%>
<html>
<head>
</head>

<%
if(contentList.size()>0){%>
<body> 
<table border="2">
<th>序号</th>
<th>发言人</th>
<th>接收人</th>
<th>发言人昵称</th>
<th>接收人昵称</th>
<th>内容</th>
<th>附件</th>
<th>发表时间</th>
<th>是否私聊</th>
<th>房间号</th>
<%
Iterator itr = contentList.iterator();
 while(itr.hasNext()){
content = (JCRoomContentBean) itr.next();
%>
<tr>
<td><%=content.getId()%></td>
<%
toUser1 = UserInfoUtil.getUser(content.getFromId());
if(toUser1!=null){
%>
<td><%=toUser1.getUserName()%></td>
<td><%=toUser1.getNickName()%></td>
<%}else{%><td>无此用户信息</td><td>无此用户信息</td><%}
toUser2 = UserInfoUtil.getUser( content.getToId());
if(toUser2!=null){
%>
<td><%=toUser2.getUserName()%></td>
<td><%=toUser2.getNickName()%></td>
<%}else{%><td>无此用户信息</td><td>无此用户信息</td><%}%>
<td><%=content.getContent()%></td>
<%
pic=content.getAttach();
if(!pic.equals("")){
%>
<td><img src="<%=content.ATTACH_URL_ROOT+"/"+pic%>" alt="图片"/></td>
<%}else{%><td>无附件上传</td><%}%>
<td><%=content.getSendDateTime()%></td>
<%
mark=content.getIsPrivate()>0?"公聊":"私聊";
%>
<td><%=mark%></td>
<td><%=content.getRoomId()%></td>
</tr>
<%}}else{%>
没有聊天记录!"
<%}%>
</table>
<br/><a href="http://wap.joycool.net/jcadmin/forchatbid.jsp">添加用户到小黑屋列表</a><br/>
</body>
</html>
