<%@ page import="net.joycool.wap.util.db.DbOperation"%><%
String mobile = (String) session.getAttribute("userMobile");
if(mobile != null){
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	String query = "insert into jc_yicha_log(mobile, jump_datetime) values('" + mobile + "', now())";			
    dbOp.executeUpdate(query);
	dbOp.release();
}

response.sendRedirect("http://u.yicha.cn/union/search.jsp?site=2145811913&pageBegin=1");
%>