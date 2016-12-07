<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
String badGuyId = request.getParameter("badGuyId");
if(badGuyId==null){
	badGuyId="431";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的黑名单">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
你真的要把对方放入黑名单里么？<br/>
<a href="/user/OperBadGuy.do?add=1&amp;badGuyId=<%=badGuyId%>">确认很讨厌他</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=badGuyId%>">点错了返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>