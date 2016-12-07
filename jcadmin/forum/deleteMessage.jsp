<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.forum.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.util.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

response.setHeader("Cache-Control","no-cache");
int id = Integer.parseInt(request.getParameter("id"));
IForumMessageService service = ServiceFactory.createForumMessageService();
ForumMessageBean message = service.getForumMessage("id = " + id);
//lbj_log_oper_start
LogUtil.logOperation("论坛删贴: " + message.getTitle());
//lbj_log_oper_end
service.deleteForumMessage("id = " + id + " or parent_id = " + id);
if(message.getParentId() != 0){
	//response.sendRedirect("forumMessage.jsp?id=" + message.getParentId());
	BaseAction.sendRedirect("/jcadmin/forum/forumMessage.jsp?id=" + message.getParentId(), response);
} else {
	//response.sendRedirect("forumIndex.jsp?id=" + message.getForumId());
	BaseAction.sendRedirect("/jcadmin/forum/forumIndex.jsp?id=" + message.getForumId(), response);
}
%>