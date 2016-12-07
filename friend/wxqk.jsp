<%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="java.net.*"%><%@ page import="java.sql.ResultSet"%><%
String fr = request.getParameter("fr");
if(fr == null){
	return;
}
if(SecurityUtil.getPhone(request) == null){	
	String url = "http://wap.joycool.net/friend/wxqk.jsp?fr=" + URLEncoder.encode(fr);
	SecurityUtil.redirectGetMobile(response, url);
} else {
	String mobile = SecurityUtil.getPhone(request);	
	String ua = SecurityUtil.getUA(request);
	int linkId = 19830810;
	if(mobile != null && !mobile.equals("")){
		if(mobile.startsWith("86")){
			mobile = mobile.substring(2);
		}	
		/*
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select id from jc_log_mobile_ua where mobile='" + mobile + "' and enter_datetime > subdate(now(), interval 30 MINUTE)";
		ResultSet rs = dbOp.executeQuery(query);
		if(!rs.next()){
		    query = "insert into jc_log_mobile_ua(mobile, ua, enter_datetime, ip_address, link_id) values('" + mobile + "', '" + SecurityUtil.getUA(request) + "', now(), '" + request.getRemoteAddr() + "', " + linkId + ")";			
		    dbOp.executeUpdate(query);
		}
		dbOp.release();
		*/
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId);
	} else {
		//System.out.println("mobile:" + mobile);
	    //System.out.println("UA:" + SecurityUtil.getUA(request));	
	}
	
	if(fr.indexOf("?") != -1){
		fr += "&mobile=" + mobile + "&ua=" + ua;
	} else {
		fr += "?mobile=" + mobile + "&ua=" + ua;
	}
	response.sendRedirect(fr);	
}
%>