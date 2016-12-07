<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
int id = StringUtil.toInt(request.getParameter("id"));
if(SecurityUtil.oxcxMobile(request)){
	String url = "http://wap.joycool.net/entry/entry.jsp?id=" + id;	
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
	if(linkId == 299991){
        response.sendRedirect(("http://wap.joycool.net/lswjs/index.jsp"));
	}
	else if(linkId == 299992){
        response.sendRedirect(("http://wap.joycool.net/ebook/EBookCataList.do"));
	}
	else if(linkId == 299993){
        response.sendRedirect(("http://wap.joycool.net/Column.do?columnId=2705"));
	}
	else if(linkId == 299994){
        response.sendRedirect(("http://wap.joycool.net/Column.do?columnId=3865"));
	}
	else if(linkId == 299995){
        response.sendRedirect(("http://wap.joycool.net/game/GameCataList.do"));
	}
	else if(linkId == 299996){
        response.sendRedirect(("http://wap.joycool.net/Column.do?columnId=3480"));
	}
	else if(linkId == 299997){
        response.sendRedirect(("http://wap.joycool.net/Column.do?columnId=2483"));
	}
	else if(linkId == 299998){
        response.sendRedirect(("http://wap.joycool.net/Column.do?columnId=1196"));
	}
	else if(linkId == 299999){
        response.sendRedirect(("http://kurt.joycool.net/Page.do?lineId=1539"));
	}
}
%>