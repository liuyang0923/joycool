<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%
response.setHeader("Cache-Control","no-cache");
String info=null;
info="对不起，调查赢奖活动已结束~";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="活动结束">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=info%><br/>
<a href="http://wap.g3me.cn">返回首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>