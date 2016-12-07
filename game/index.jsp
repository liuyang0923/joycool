<%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.sql.ResultSet"%><%
String fr = request.getParameter("fr");

if(SecurityUtil.oxcxMobile(request)){
	String url = "http://wap.joycool.net/game/index.jsp?fr=" + fr;	
	LogUtil.totalRedirect ++;
	SecurityUtil.redirectGetMobile(response, url);
} else {	
	String enterUrl = PageUtil.getCurrentPageURL(request);
	int linkId = 0;
	if(fr != null){
		session.setAttribute(fr, fr);
	}
	String mobile = SecurityUtil.getPhone(request);
	if(mobile != null){
		LogUtil.totalBack ++;
	}
	//3g
	if("3g".equals(fr)){
		linkId = 19830807;
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
		    String query = "insert into jc_log_mobile_ua(mobile, ua, enter_datetime, ip_address, link_id, enter_url) values('" + mobile + "', '" + SecurityUtil.getUA(request) + "', now(), '" + request.getRemoteAddr() + "', " + linkId + ", '" + enterUrl + "')";
		    dbOp.executeUpdate(query);
		//}
		dbOp.release();
		*/
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId);
		Util.updatePhoneNumber(request, mobile);
	}

    //if("3g".equals(fr)){
	//	session.setAttribute("linkId", "19830807");
	//}
    //response.sendRedirect(("http://wap.joycool.net/game/GameCataList.do"));
    BaseAction.sendRedirect("/game/GameCataList.do", response);
}
%>