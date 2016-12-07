<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
int id = StringUtil.toInt(request.getParameter("id"));
if(id<1){
	id = 0;
}
String content = "pushpushpush" + id;
LogUtil.logDebug(content);		

response.sendRedirect((BaseAction.INDEX_URL));
%>