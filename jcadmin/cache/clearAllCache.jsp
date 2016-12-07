<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*"%><%
//清除所有缓存
String user=request.getParameter("user");
String password = request.getParameter("password");
if("cache".equals(user) && "abc321".equals(password)){
    OsCacheUtil.flushAll();
}
%>
done.<br>