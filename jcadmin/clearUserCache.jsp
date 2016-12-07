<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*"%><%
int userId = Integer.parseInt(request.getParameter("id"));
UserInfoUtil.flushUserStatus(userId);
%>
done.
