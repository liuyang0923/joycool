<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%>               
清除缓存&nbsp;&nbsp;<a href="index.jsp">返回</a><br>
<%
OsCacheUtil.flushGroup(OsCacheUtil.LINK_LIST_GROUP);
OsCacheUtil.flushGroup(OsCacheUtil.LINK_GROUP);
%>
done.