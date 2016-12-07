<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.floor.FloorAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
FloorAction action = new FloorAction(request);
action.registerFloor(request);
String result =(String)request.getAttribute("result");
String content = (String)request.getAttribute("content");
String floor= (String)request.getAttribute("floor");
long prize = StringUtil.toLong((String)request.getAttribute("prize"));
int number = StringUtil.toInt((String)request.getAttribute("number"));
long num = prize*number;
String url=("/floor/addPrize.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/floor/addPrize.jsp">返回重新填写</a><br/>
</p>
</card>
<%}else{%>
<card title="乐酷">
<p align="left">
您将要出奖金加入踩踩乐游戏：<br/>
<%=content%><br/>
每个中奖楼奖金：<%=StringUtil.bigNumberFormat(prize)%>乐币<br/>
一共的中奖楼：<%=number%>个<br/>
中奖楼的尾数：<%=floor%><br/>
确认后将马上从您的银行扣除<br/>
奖金<%=StringUtil.bigNumberFormat(num)%>乐币，开始游戏.<br/>
<anchor title="确定">确认
  <go href="/floor/sure.jsp" method="post">
    <postfield name="content" value="<%=content%>"/>
    <postfield name="prize" value="<%=prize%>"/>
    <postfield name="number" value="<%=number%>"/>
    <postfield name="floor" value="<%=floor%>"/>
  </go>
</anchor><br/>
</p>
</card>
<%}%>
</wml>




