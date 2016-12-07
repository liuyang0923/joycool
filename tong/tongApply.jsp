<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongApply(request);
String result =(String)request.getAttribute("result");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>（3秒钟跳转到城帮首页）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("exist")){
TongBean tong = (TongBean)request.getAttribute("tong");
String url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml((String)request.getAttribute("tip"))%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean)request.getAttribute("tong");
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
欢迎加入<%=StringUtil.toWml(tong.getTitle()) %>！加入之后，就有人帮你畅游乐酷了，就不会受到欺负了哦：）<br/>
请填写你的入帮申请，帮主会尽快回复的：<br/>
<input name="content"  maxlength="100" value="申请入帮!"/><br/>
<anchor title="确定">确定入帮
  <go href="tongApplyResult.jsp" method="post">
    <postfield name="content" value="$content"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
  </go>
</anchor><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>