<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%!static String JSP_EXCEPTION = "javax.servlet.jsp.jspException";%><%
response.setStatus(200);
String uri = (String)request.getAttribute("javax.servlet.error.request_uri");
%><%if(!SecurityUtil.isInnerIp(request.getRemoteAddr())){


HttpSession session = request.getSession(false);
if(session!=null&&SetCharacterEncodingFilter.isWap2(request,session)){
// 2.0版本
response.setContentType("text/html");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "HTML" "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml"><head><meta name="viewport" content="width=240"/><meta name="MobileOptimized" content="236"/><title>乐酷游戏社区</title></head><link href="/proxy.css" rel="stylesheet" type="text/css"/><body><div align="center" class="div1"><font color="yellow">访问失败</font></div><div class="div2">
访问页面出错啦-_-!<br/>
<%if(uri!=null&&request.getMethod().equalsIgnoreCase("get")){
String queryString = request.getQueryString();
String url;
if(queryString!=null)
	url = uri + "?" + queryString;
else
	url = uri;
%><a href="<%=url%>">刷新重试</a><br/><%
}%>
<%if(request.getSession(false)!=null){%>
<a href="/bottom.jsp">ME</a>|<a href="/lswjs/index.jsp">导航</a>|<a href="wapIndex.jsp">乐酷首页</a><br/>
<%}else{%>
<a href="/wapIndex.jsp">返回乐酷首页</a><br/>
<%}%>
</div></body></html><%
}else{


%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷社区">
<p align="left">
访问页面出错啦-_-!<br/>
<%if(uri!=null&&request.getMethod().equalsIgnoreCase("get")){
String queryString = request.getQueryString();
String url;
if(queryString!=null)
	url = uri + "?" + queryString;
else
	url = uri;
%><a href="<%=response.encodeURL(url)%>">刷新重试</a><br/><%
}%>
<%if(request.getSession(false)!=null){%>
<a href="<%=response.encodeURL("/bottom.jsp")%>">ME</a>|<a href="<%=response.encodeURL("/lswjs/index.jsp")%>">导航</a>|<a href="<%=response.encodeURL("/wapIndex.jsp")%>">乐酷首页</a><br/>
<%}else{%>
<a href="/wapIndex.jsp">返回乐酷首页</a><br/>
<%}%>
</p>
</card>
</wml><%
}

}else{
response.setContentType("text/html;charset=utf-8");
Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
String output = "";
StringWriter sw = new StringWriter();
if(throwable instanceof ServletException) {
	throwable = ((ServletException) throwable).getRootCause();
	if(throwable == null)
		throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
	if(throwable!=null){
		throwable.printStackTrace(new PrintWriter(sw));
		output = sw.toString();
	}
	
	int ind = output.lastIndexOf("org.apache.jasper.runtime.HttpJspBase.service");
	if(ind != -1)
		output = output.substring(0, ind - 4);
} else if(throwable != null){
	throwable.printStackTrace(new PrintWriter(sw));
	output = sw.toString();
}
%>
<html><head><title>Error report</title>
<style><!--
H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} 
H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} 
H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} 
B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} 
P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}
--></style>
</head><body>
<h1>HTTP Status 500 - <%=uri%></h1>
<HR size="1" noshade="noshade"><p><b>type</b> Status report</p>
<p><b>message</b>
<u>Exception occurs</u></p>
<p><b>exception</b>
<pre><% if(throwable!=null){out.write(output);} %></pre></p>
<HR size="1" noshade="noshade"><h3>Joycool 2008</h3>
</body></html>
<%}%>