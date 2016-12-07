<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.*"%><%
int userId = StringUtil.toInt(request.getParameter("userId"));
int noticeId = StringUtil.toInt(request.getParameter("noticeId")); 
String title = request.getParameter("title");

if(noticeId>0 && title!=null){
	String sql = "update jc_notice set title='" + title + "' where id=" + noticeId;
	SqlUtil.executeUpdate(sql, Constants.DBShortName);
	
	OsCacheUtil.flushGroup(OsCacheUtil.NOTICE_GROUP, noticeId + "");
}

//response.sendRedirect("/jcadmin/notice/manageUserNotice.jsp?userId=" + userId);
BaseAction.sendRedirect("/jcadmin/notice/manageUserNotice.jsp?userId=" + userId, response);
%>