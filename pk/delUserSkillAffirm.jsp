<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
//String result =(String)request.getAttribute("result");
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String query = request.getQueryString().toString();
if(query==null){
BaseAction.sendRedirect("/pk/index.jsp",response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/pk/delUserSkill.jsp?<%=query%>">确认</a> 
<a href="/pk/userSkillInfo.jsp?<%=query%>">返回</a><br/><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>