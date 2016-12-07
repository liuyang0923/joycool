<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.bean.money.*"%><%
String key = request.getParameter("key");
if(key!=null){
	OsCacheUtil.flushGroup(key);
}

//response.sendRedirect("/jcadmin/cache/cacheAdmin.jsp");
BaseAction.sendRedirect("/jcadmin/cache/cacheAdmin.jsp", response);
%>