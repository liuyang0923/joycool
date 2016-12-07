<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@include file="../bank/checkpw.jsp"%><%
response.setHeader("Cache-Control","no-cache");
String result =(String)request.getAttribute("result");
String url=("/lswjs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷" >
<p align="left">
请仔细填写下面的游戏数据<br/>
每个中奖楼的奖金(10万-10亿)<br/>
<input name="prize"  maxlength="10" value=""/><br/>
一共多少个中奖楼(5-20个)<br/>
<input name="number"  maxlength="2" value=""/><br/>
中奖楼的尾数(0-99任意数字)<br/>
<input name="floor"  maxlength="2" value=""/><br/>
广告语(15字以内)<br/>
<input name="content"  maxlength="15" value=""/><br/>
<anchor title="确定">提交
  <go href="/floor/result.jsp" method="post">
    <postfield name="prize" value="$prize"/>
    <postfield name="number" value="$number"/>
    <postfield name="floor" value="$floor"/>
    <postfield name="content" value="$content"/>
  </go>
</anchor><br/>
<a href="/floor/index.jsp">返回踩踩乐</a><br/>
<br/>
</p>
</card>
</wml>