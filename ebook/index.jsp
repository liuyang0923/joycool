<%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.sql.ResultSet"%><%
String fr = request.getParameter("fr");

if(SecurityUtil.oxcxMobile(request)){	
	String url = "http://wap.joycool.net/ebook/index.jsp?fr=" + fr;	
	LogUtil.totalRedirect ++;
	SecurityUtil.redirectGetMobile(response, url);
} else {	
	String enterUrl = PageUtil.getCurrentPageURL(request);
	String mobile = SecurityUtil.getPhone(request);	
	if(fr != null){
		session.setAttribute(fr, fr);
	}
	if(mobile != null){
		LogUtil.totalBack ++;
	}	
	if(mobile != null && !mobile.equals("")){	
		if(mobile.startsWith("86")){
		    mobile = mobile.substring(2);
	    }
		session.setAttribute("userMobile", mobile);
		int linkId = 19830805;
		/*
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		//String query = "select id from jc_log_mobile_ua where mobile='" + mobile + "' and enter_datetime > subdate(now(), interval 15 MINUTE)";
		//ResultSet rs = dbOp.executeQuery(query);
		//if(!rs.next()){
		    String query = "insert into jc_log_mobile_ua(mobile, ua, enter_datetime, ip_address, link_id, enter_url) values('" + mobile + "', '" + SecurityUtil.getUA(request) + "', now(), '" + request.getRemoteAddr() + "', " + linkId + ", '" + enterUrl + "')";
		    dbOp.executeUpdate(query);
		//}
		dbOp.release();
		*/
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId);
		Util.updatePhoneNumber(request, mobile);
	}

    if(fr != null){
		session.setAttribute(fr, fr);
	}    
    //response.sendRedirect(("http://wap.joycool.net/ebook/EBookCataList.do"));
    BaseAction.sendRedirect("/ebook/EBookCataList.do", response);
}
%>