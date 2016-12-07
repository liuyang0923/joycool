<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("userBagCheck")==null){
//response.sendRedirect(("/user/userBag.jsp"));
BaseAction.sendRedirect("/user/userBag.jsp", response);
return;
}
session.removeAttribute("userBagCheck");
UserBagAction action = new UserBagAction(request);
action.presentResult(request);
String result =(String)request.getAttribute("result");
String url=("/user/userBag.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/user/userBag.jsp">返回我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
赠送成功(3秒后跳转)!<br/>
<a href="/user/userBag.jsp">返回我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>