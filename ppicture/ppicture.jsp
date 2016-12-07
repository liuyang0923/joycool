<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
int id = StringUtil.toInt(request.getParameter("id"));
String name = null;
int kb = 0;
String fileUrl = null;
if (id != -1) {
	String query = "select name, kb, file_url from ppicture where id = " + id;	
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	ResultSet rs = dbOp.executeQuery(query);
	if(rs.next()){
		name = rs.getString("name");
		kb = rs.getInt("kb");
		fileUrl = "http://wap.joycool.net/joycool-rep/picture/" + rs.getString("file_url");
	} else {
		dbOp.release();
		//response.sendRedirect(("http://wap.joycool.net"));
		BaseAction.sendRedirect(null, response);
		return;
	}
	dbOp.release();
} else {
		//response.sendRedirect(("http://wap.joycool.net"));
		BaseAction.sendRedirect(null, response);
		return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(name)%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(name)%><br/>
大小:<%=kb%>K<br/>
<img src="<%=fileUrl%>" alt="loading..."/><br/>
<a href="<%=fileUrl%>">下载到手机</a><br/>
<br/>
<anchor name="prev"><prev/>返回上一级</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>