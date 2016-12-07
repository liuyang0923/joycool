<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.postOffice();
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
邮局<br/>
现金：<%=fsUser.getMoney()%>元<br/>
存款：<%=fsUser.getSaving()%>元<br/>
债务：<%=fsUser.getDebt()%>元<br/>
还<input name="loan" format="*N" maxlength="8" value="100"/>元<br/>
<anchor title="确定">确定
  <go href="postOfficeResult.jsp" method="post">
    <postfield name="loan" value="$loan"/>
  </go>
</anchor>|
<anchor title="确定">还清所有债务
  <go href="postOfficeResult.jsp" method="post">
    <postfield name="loan" value="<%=fsUser.getDebt()%>"/>
  </go>
</anchor><br/>
<%@include file="map.jsp"%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>