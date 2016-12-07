<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);

UserBean loginUser = action.getLoginUser();
if(ForbidUtil.isForbid("team",loginUser.getId())){
response.sendRedirect(("index.jsp"));
return;
}

action.create();
if(action.isResult("redirect")) {
response.sendRedirect(("chat.jsp"));
return;
}
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>

名称:<input name="name" maxlength="6"/><br/>
介绍:<input name="info" maxlength="100"/><br/>
<select name="flag" multiple="true">
<option  value="0">允许陌生人查看</option>
<option  value="1">允许陌生人发言</option>
</select><br/>
<anchor title="确定">确定创建
  <go href="create.jsp" method="post">
    <postfield name="name" value="$name"/>
    <postfield name="info" value="$info"/>
    <postfield name="flag" value="$flag"/>
  </go>
</anchor><br/>

<%}%>
<a href="index.jsp">返回</a><br/>
</p>
</card>
</wml>