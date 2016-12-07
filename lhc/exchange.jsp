<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.lhc.LhcAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.action.lhc.LhcResultBean" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
action.exchange();
String result =(String)request.getAttribute("result");
String url=("/lhc/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=LhcAction.LHC_NAME%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回六合彩首页)<br/>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{

LhcResultBean lhcResult =(LhcResultBean)request.getAttribute("lhcResult");
boolean flag= ((Boolean)request.getAttribute("flag")).booleanValue();
%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
第<%=lhcResult.getId()%>期开奖结果：<br/>
<%=lhcResult.getNum1()%>
<%=lhcResult.getNum2()%>
<%=lhcResult.getNum3()%>
<%=lhcResult.getNum4()%>
<%=lhcResult.getNum5()%>
<%=lhcResult.getNum6()%>
(<%=lhcResult.getNum7()%>)<br/>
中奖名称：<br/>
<%=action.countName(lhcResult.getId())%><br/>
<%
if(flag){
	String totalMoney = (String)request.getAttribute("totalMoney");
	%>
	恭喜您中奖了，获得了<%=totalMoney%>乐币！（奖金已打入您的银行账户）<br/>
<%}else{%>
	很遗憾您没有中奖，下次继续努力吧<br/>
<%}%>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>