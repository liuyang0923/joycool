<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
String aaa = "";
for(int i=0;i<31;i++)
aaa += (char)i;
Enumeration headers = request.getHeaderNames();
while(headers.hasMoreElements()){
String n=(String)headers.nextElement();
System.out.println(n + "----");
Enumeration h=request.getHeaders(n);
while(h.hasMoreElements()){
String v=(String)h.nextElement();
System.out.println(v);
}
}
System.out.println(request.getRemoteAddr());
System.out.println(request.getQueryString());
if(true){
	response.sendRedirect("/bottom.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷门户">
<p align="left">
<%Map p = request.getParameterMap();
Iterator iter = p.entrySet().iterator();
while(iter.hasNext()){
Map.Entry e = (Map.Entry)iter.next();
%>
<%=e.getKey()%>=<%String[] s = (String[])e.getValue();for(int i=0;i<s.length;i++){%><%=s[i]%>,<%}%><br/>
<%}%><br/>
<%=request.getRequestURI()%><br/>
<%=request.getRequestURL()%><br/>
<%=request.getQueryString()%>
</p>
</card>
</wml>