<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.hospital();
String result =(String)request.getAttribute("result");
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回城市)<br/>
<a href="/fs/index.jsp">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
FSUserBean fsUser = action.getFsUser();
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
医院<br/>
患者朋友，本医院绝对不收红包！先去照个CT查个爱滋做个全身化疗放疗，然后再吃5个疗程的进口中药吧！什么？只是感冒？好，一个健康点<%=FSAction.CURE_MONEY%>，爱治不治！<br/>
健康：<%=fsUser.getHealth()%>点<br/>
现金：<%=fsUser.getMoney()%>元<br/>
治<input name="count" format="*N" maxlength="3" value="10"/>点<br/>
<anchor title="确定">确定
  <go href="hospitalResult.jsp" method="post">
    <postfield name="count" value="$count"/>
  </go>
</anchor><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>