<%--mcq add 2006-07-5--%>
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
JCRoomChatAction action=new JCRoomChatAction(request);
action.roomUserList(request);
Vector userList = (Vector)request.getAttribute("userList");
Vector managerList = (Vector)request.getAttribute("managerList");
Vector manager11= new Vector();
JCRoomBean room=(JCRoomBean)request.getAttribute("room");
%>
<html>
<body>
房间名称:<%=room.getName()%><br/>
管理员:<br/>
<table border="1">
<th>昵称</th><th>权限</th>
<%
RoomManagerBean manager=null;
UserBean user=null;
for(int i=0;i<managerList.size();i++){
    manager=(RoomManagerBean)managerList.get(i); 
    manager11.add(String.valueOf(manager.getUserId()));
//    user=action.getUser("id="+manager.getUserId()); 
	//zhul 2006-10-12 优化用户信息查询
	user=UserInfoUtil.getUser(manager.getUserId());
%>
<tr><td><%=StringUtil.toWml(user.getNickName())%></td>
<%if(manager.getUserId()==room.getCreatorId()){%>
<td>房间创建者</td></tr>
<%}else{%><td>被授权者</td></tr><%}}%></table><%if(room.getGrantMode()==0){%>
<table width="73" border="1">
<th width="73">昵称</th>
用户:<br/>
<%
JCRoomOnlineBean online=null;
for(int i=0;i<userList.size();i++){
    online=(JCRoomOnlineBean)userList.get(i); 
    if(manager11.contains(String.valueOf(online.getUserId())))
         break;
   // user=action.getUser("id="+online.getUserId()); 
   user=UserInfoUtil.getUser(online.getUserId()); 
%>
<tr><td><%=StringUtil.toWml(user.getNickName())%></td></tr>
<%}%></table><%}else if(room.getGrantMode()==1){%>
<table border="1">
<th width="63">昵称</th>
用户:<br/>
<%
RoomUserBean roomUser=null;
for(int i=0;i<userList.size();i++){
    roomUser=(RoomUserBean)userList.get(i); 
        if(manager11.contains(String.valueOf(roomUser.getUserId())))
         break; 
    //user=action.getUser("id="+roomUser.getUserId()); 
    user=UserInfoUtil.getUser(roomUser.getUserId());
%> 
<tr><td><%=StringUtil.toWml(user.getNickName())%></td></tr>
<%}%></table><%}%>
<br/><a href="<%=("roomList.jsp") %>">返回</a>
</body>
</html>