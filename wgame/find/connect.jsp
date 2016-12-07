<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
int r = net.joycool.wap.util.RandomUtil.nextInt(5);
String mode = request.getParameter("mode");
if(mode == null) mode = "1";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="移动梦网">
<p align="left">
<%if(r == 0){%>
您正在使用手机上网业务，若继续浏览网页，会产生GPRS通信费。<br/>
资费提醒：每K0.03元<br/>
<a href="wrong.jsp">梦网管家</a><br/>
<a href="wrong.jsp">我的邮箱（免费）</a><br/>
<a href="wrong.jsp">收藏和书签</a><br/>
<a href="wrong.jsp">查询搜索</a><br/>
<a href="wrong.jsp">常用网址</a><br/>
<a href="right.jsp?mode=<%=mode%>">继续</a><br/>
<%}else if(r == 1){%>
<a href="right.jsp?mode=<%=mode%>">点击继续访问》</a><br/>
wap.joycool.net |13xxxxxxxxx 您好,<br/>
资费提醒：每K0.03元，详情请参见<a href="wrong.jsp">帮助</a>,
<a href="wrong.jsp">取消资费提醒！</a><br/>
<a href="wrong.jsp">梦网管家</a><br/>
<a href="wrong.jsp">我的邮箱（免费）</a><br/>
<a href="wrong.jsp">收藏和书签</a><br/>
<a href="wrong.jsp">查询搜索</a><br/>
<a href="wrong.jsp">真人语音聊天</a><br/>
<%}else if(r == 2){%>
<a href="right.jsp?mode=<%=mode%>">继续访问》</a><br/>
wap.joycool.net |13xxxxxxxxx 您好,<br/>
您正在使用手机上网业务，若继续浏览网页，会产生GPRS通信费,<a href="wrong.jsp">查看详情</a><br/>
<a href="wrong.jsp">诱人少女</a><br/>
<a href="wrong.jsp">热火美图</a><br/>
<a href="wrong.jsp">收藏和书签</a><br/>
<a href="wrong.jsp">查询搜索</a><br/>
<a href="wrong.jsp">我的好友12</a><br/>
<%}else if(r == 3){%>
wap.joycool.net |13xxxxxxxxx 您好,<br/>
您正在手机上网，在花钱啊！<a href="wrong.jsp">看我花了多少钱</a><br/>
<a href="wrong.jsp">诱人少女</a><br/>
<a href="wrong.jsp">热火美图）</a><br/>
<a href="wrong.jsp">免费彩信</a><br/>
<a href="wrong.jsp">超好看的书</a><br/>
<a href="wrong.jsp">我的好友12</a><br/>
<a href="right.jsp?mode=<%=mode%>">点击继续访问》</a><br/>
<%}else{%>
您正在使用手机上网业务，若继续浏览网页，会产生GPRS通信费。<br/>
资费提醒：每K0.03元<br/>
<a href="right.jsp?mode=<%=mode%>">点击继续访问》</a><br/>
<%}%>
</p>
</card>
</wml>