<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request, response);
action.book();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源农场">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

FarmBookBean book = (FarmBookBean)request.getAttribute("book");
int p = action.getParameterInt("p");
if(p<0 || p>=book.getPageCount())
	p=0;
String url = "book.jsp?id=" + book.getId();
%>
《<%=book.getName()%>》
第<%=p+1%>页<br/>
<%=book.getPage(p)%><br/>
<%if(p < book.getPageCount() - 1){%>
<a href="<%=(url + "&amp;p=" + (p+1))%>">下一页</a>
<%}else{%>下一页<%}%>|
<%if(p > 0){%>
<a href="<%=(url + "&amp;p=" + (p-1))%>">上一页</a>
<%}else{%>上一页<%}%>
<br/>
<%}%>
<a href="map.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>