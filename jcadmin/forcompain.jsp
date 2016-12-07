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
String update=request.getParameter("update");
if(update!=null){
    chatService.updateJCRoomCompain("mark=1","id="+update);
    //response.sendRedirect("forcompain.jsp");
    BaseAction.sendRedirect("/jcadmin/forcompain.jsp", response);
    return;
}
String delete=request.getParameter("delete");
if(delete!=null){
    chatService.delJCRoomCompain("id="+delete);
    //response.sendRedirect("forcompain.jsp");
    BaseAction.sendRedirect("/jcadmin/forcompain.jsp", response);
    return;
}
UserBean toUser1 = null;
UserBean toUser2 = null;
Vector compainList=chatService.getJCRoomCompainList();
JCRoomCompainBean compain=null;
String mark=null;
%>
<html>
<head>
<script>
function operate()
{
 if (confirm('您确定要执行该操作嘛？')) {
  return true;
 } else {
  return false;
 }
}
</script>
</head>
<%
if(compainList.size()>0){%>
<body> 
<table border="2">
<th>序号</th>
<th>投诉人</th>
<th>被投诉人</th>
<th>投诉内容</th>
<th>处理标志</th>
<th>投诉时间</th>
<th>操作</th>
<th>操作</th>
<th>添加到小黑屋</th>
<%
Iterator itr = compainList.iterator();
 while(itr.hasNext()){
compain = (JCRoomCompainBean) itr.next();
%>
<tr>
<td><%=compain.getId()%></td>
<%
toUser1 = UserInfoUtil.getUser(compain.getLeftUserId());
if(toUser1!=null){
%>
<td><a href="forContentList.jsp?check=<%=compain.getLeftUserId()%>"><%=toUser1.getUserName()%>(<%=toUser1.getNickName()%>)</a></td>
<%}else{%><td>无此用户信息</td><%}
toUser2 = UserInfoUtil.getUser( compain.getRightUserId());
if(toUser2!=null){
%>
<td><a href="forContentList.jsp?check=<%=compain.getRightUserId()%>"><%=toUser2.getUserName()%>(<%=toUser2.getNickName()%>)</a></td>
<%}else{%><td>无此用户信息</td><%}%>
<td><%=compain.getContent()%></td>
<%
mark=compain.getMark()>0?"已处理":"未处理";
%>
<td><%=mark%></td>
<td><%=compain.getEnterDateTime()%></td>
<td><a href="forcompain.jsp?update=<%=compain.getId()%>" >更新</a></td>
<td><a href="forcompain.jsp?delete=<%=compain.getId()%>" onClick="return operate()">删除</a></td>
<td align="center"><a href="http://wap.joycool.net/jcadmin/forchatbid.jsp?userId=<%=compain.getRightUserId()%>" onClick="return operate()">添加</a></td>
</tr>
<%}}else{%>
没有投诉记录!"
<%}%>
</table>
<br/><a href="http://wap.joycool.net/jcadmin/forchatbid.jsp">添加用户到小黑屋列表</a><br/>
</body>
</html>
