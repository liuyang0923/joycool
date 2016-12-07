<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setStatus(200);


HttpSession session = request.getSession(false);
if(session!=null&&SetCharacterEncodingFilter.isWap2(request,session)){
// 2.0版本
response.setContentType("text/html");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "HTML" "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml"><head><meta name="viewport" content="width=240"/><meta name="MobileOptimized" content="236"/><title>乐酷游戏社区</title></head><link href="/proxy.css" rel="stylesheet" type="text/css"/><body><div align="center" class="div1"><font color="yellow">访问失败</font></div><div class="div2">
访问的页面不存在-_-!<br/>
<%if(session!=null){%>
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
访问的页面不存在-_-!<br/>
<%if(session!=null){%>
<a href="<%=response.encodeURL("/bottom.jsp")%>">ME</a>|<a href="<%=response.encodeURL("/lswjs/index.jsp")%>">导航</a>|<a href="<%=response.encodeURL("/wapIndex.jsp")%>">乐酷首页</a><br/>
<%}else{%>
<a href="/wapIndex.jsp">返回乐酷首页</a><br/>
<%}%>
</p>
</card>
</wml><%}%>