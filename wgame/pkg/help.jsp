<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
==礼品店说明==<br/>
在礼品店里,你可以根据自己的喜好,购买大小、外观、颜色各不相同的礼包,并且可以编写个性的礼签.你可以写一封祝福的书信放到礼包当中,并配上一个红包,可以使你的礼包更有意义.而且有一些的礼包还可以放入一些礼物,这样一来,你的礼包不仅拥有自己的个性,礼品也更有收藏价值.你还等什么,快去选购一款你最中意的礼包,送给你的好友吧！<br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>