<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
int id = StringUtil.toInt(request.getParameter("id"));
if(SecurityUtil.oxcxMobile(request)){
	String url = "http://wap.joycool.net/entry/gdentry.jsp?id=" + id;	
	LogUtil.totalRedirect ++;
	SecurityUtil.redirectGetMobile(response, url);
} else {	
	String enterUrl = PageUtil.getCurrentPageURL(request);
	int linkId = id;
	if(linkId == -1){
		linkId = 0;
	}	
	String mobile = SecurityUtil.getPhone(request);
	if(mobile != null){
		LogUtil.totalBack ++;
	}	
	if(mobile != null && !mobile.equals("")){
		if(mobile.startsWith("86")){
			mobile = mobile.substring(2);
		}
		session.setAttribute("userMobile", mobile);
		/*
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		//String query = "select id from jc_log_mobile_ua where mobile='" + mobile + "' and enter_datetime > subdate(now(), interval 15 MINUTE)";
		//ResultSet rs = dbOp.executeQuery(query);
		//if(!rs.next()){
		    String query = "insert into jc_log_mobile_ua(mobile, ua, enter_datetime, ip_address, link_id, enter_url) values('" + mobile + "', '" + SecurityUtil.getUA(request) + "', now(), '" + request.getRemoteAddr() + "', " + id + ", '" + enterUrl + "')";
		    dbOp.executeUpdate(query);			
		//}
		dbOp.release();
		*/
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId);
		Util.updatePhoneNumber(request, mobile);
	}

    //response.sendRedirect(("http://wap.joycool.net/lswjs/index.jsp"));
    BaseAction.sendRedirect("/lswjs/index.jsp", response);
}
%>