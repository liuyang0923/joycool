<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
int id = StringUtil.toInt(request.getParameter("id"));

int unionId = StringUtil.toInt(request.getParameter("unionId"));
if(unionId == -1){
	unionId = 0;
}
String redirect = "/Column.do?columnId="+request.getParameter("r");


	String enterUrl = PageUtil.getCurrentPageURL(request);
	int linkId = id;
	if(linkId == -1){
		linkId = 0;
	}	

	session.setAttribute("linkId", String.valueOf(linkId));
 
	String mobile = SecurityUtil.getPhone(request);

	String refer = request.getHeader("Referer");
	if(refer!=null && linkId>=70 && refer.length()>0) {
		if(refer.length()>100)
			refer=refer.substring(0,100);
		SqlUtil.executeUpdate("insert into jump_referer set create_time=now(),link_id="+linkId+",referer='"+StringUtil.toSql(refer)+"'", 5);
	}
		

	LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId + ":" + unionId);

    BaseAction.sendRedirect(redirect, response);

%>