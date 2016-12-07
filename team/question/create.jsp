<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);

UserBean loginUser = action.getLoginUser();
if(ForbidUtil.isForbid("team",loginUser.getId())){
response.sendRedirect(("index.jsp"));
return;
}

action.create();
if(action.isResult("create")) {
response.sendRedirect(("set.jsp?sid=" + request.getAttribute("id")));
return;
}
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
%>

测试标题:<input name="name"  maxlength="20" value="缘分测试"/><br/>
简单说明<input name="info"  maxlength="100"/><br/>
<anchor title="确定">确认创建
<go href="create.jsp" method="post">
    <postfield name="name" value="$name"/>
    <postfield name="info" value="$info"/>
</go></anchor><br/>

<%}%>
<a href="index.jsp">返回缘分测试首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>