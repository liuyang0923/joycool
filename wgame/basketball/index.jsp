<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="篮球飞人">
<p align="left">
<%=BaseAction.getTop(request, response)%>
篮球飞人<br/>
-------------------<br/>
<a href="start.jsp">我要下注</a><br/>
<a href="history.jsp">查询战绩</a><br/>
篮球飞人游戏说明<br/>
-------------------<br/>
玩家选择进攻方式，如果防守球员选择不合理的防守方式,球进，玩家赢得1倍下注积分。<br/>
如果防守球员判断正确，球不进，玩家输1倍下注积分。<br/>
<br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>