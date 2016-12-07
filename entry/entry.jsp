<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
int id = StringUtil.toInt(request.getParameter("id"));
//liuyi 2006-11-11 网盟统计 start
int unionId = StringUtil.toInt(request.getParameter("unionId"));
if(unionId == -1){
	unionId = 0;
}
//liuyi 2006-11-11 网盟统计 end

if(id >= 50000 && id < 60000){
	response.sendRedirect("http://wap.g3me.cn/entry/entry.jsp?id=" + id);
	return;
}

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
	//liuyi 2006-12-22 友链pv统计 start
	session.setAttribute("linkId", String.valueOf(linkId));
    //liuyi 2006-12-22 友链pv统计 end
	String mobile = SecurityUtil.getPhone(request);
	//liuyi 2006-12-11 统计后台修改 start
	//if(mobile != null){
	//	LogUtil.totalBack ++;
	//}	
	//if(mobile != null && !mobile.equals("")){
		//if(mobile.startsWith("86")){
			//mobile = mobile.substring(2);
		//}
		//session.setAttribute("userMobile", mobile);
		
		//liuyi 2006-11-11 网盟统计 start
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId + ":" + unionId);
		//liuyi 2006-11-11 网盟统计 end
		//Util.updatePhoneNumber(request, mobile);
	//}
	//liuyi 2006-12-11 统计后台修改 end

    //response.sendRedirect(("http://wap.joycool.net/wapIndex.jsp?linkId=" + id));
    BaseAction.sendRedirect("/wapIndex.jsp?linkId=" + id, response);
}
%>