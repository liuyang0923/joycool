<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
%>
<html>
<body>
<a href="http://wap.joycool.net/jcadmin/chatmanage.jsp">聊天管理</a><br/>
<a href="http://wap.joycool.net/jcadmin/forum/forumIndex.jsp?id=14">贴图中心内容管理</a><br/>
<a href="http://wap.joycool.net/jcadmin/friendadver/friendAdverIndex.jsp">交友中心内容管理</a><br/>
<a href="http://wap.joycool.net/jcadmin/forum/forumIndex.jsp?id=15">客服留言管理</a><br/>
</body>
</html>