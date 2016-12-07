<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
response.setHeader("ServiceMonitor","0");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="系统提示">
<p align="left">
系统处理失败,可能您要查看的页面已经过期<br/>
<%
String backTo = (String) request.getParameter("backTo");
if(backTo == null){
%>
<anchor><prev/>返回上一页</anchor><br/>
<%
} else {
%>
<a href="<%=backTo%>">返回上一页</a><br/>
<%
}
%>

</p>
</card>
</wml>