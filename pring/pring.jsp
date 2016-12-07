<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
int id = StringUtil.toInt(request.getParameter("id"));
String name = null;
String singer = null;
int catalogId = 0;
DbOperation dbOp = null;
ResultSet rs = null;
String query = null;
if (id != -1) {
	query = "select name, singer, catalog_id from pring where id = " + id;	
	dbOp = new DbOperation();
	dbOp.init();
	rs = dbOp.executeQuery(query);
	if(rs.next()){
		name = rs.getString("name");
		singer = rs.getString("singer");
		catalogId = rs.getInt("catalog_id");
	} else {
		dbOp.release();
		//response.sendRedirect(("http://wap.joycool.net"));
		BaseAction.sendRedirect(null, response);
		return;
	}
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
歌手：<%=StringUtil.toWml(singer)%><br/>
格式列表：<br/>
<%
query = "select * from pring_file where pring_id = " + id;
rs = dbOp.executeQuery(query);
String fileUrl = null;
while(rs.next()){
	fileUrl = "http://wap.joycool.net/joycool-rep/pring/" + rs.getString("file");
%>
<%=rs.getString("file_type")%>|<%=(rs.getInt("size") / 1000) + 1%>K<br/>
<a href="<%=fileUrl%>">下载到手机</a><br/>
<%
}
dbOp.release();
%>
<br/>
<anchor name="prev"><prev/>返回上一级</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>