<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
int answerId=0;
if(null!=request.getParameter("answerId"))
	answerId=StringUtil.toInt(request.getParameter("answerId"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心理测试" ontimer="<%=response.encodeURL("result.jsp?answerId="+answerId)%>" >
<timer value="30" />
<p align="left">
就快要出结果了~~(3秒钟跳转)<br/>
<a href="result.jsp?answerId=<%=answerId%>">直接进入</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
