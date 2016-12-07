<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*,net.wxsj.bean.mall.*"%><%
MallAdminAction action = new MallAdminAction();
action.deleteReply(request, response);

ReplyBean reply = (ReplyBean) request.getAttribute("reply");
response.sendRedirect("info.jsp?id=" + reply.getParentId());
%>