<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants,net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="慈善账户规则">
<p align="left">
<%=BaseAction.getTop(request, response)%>
每捐献一笔钱，以<%= Constants.CHARITARIAN_USER_MONEY %>乐币为单位，增加1慈善指数。捐赠过的人，可以查看自己的慈善账户，自己捐献的钱，都分别给过哪些用户了。<br/>
每个低于4级的新用户，当乐币小于<%= Constants.CHARITARIAN_USER_MONEY %>的时候，就会自动得一笔<%= Constants.CHARITARIAN_USER_MONEY %>的费用，并会告诉他是从哪个富人的慈善账户中取得的。这样，捐款的人，就会永远被新人祝福的：）很可能交到新朋友哦<br/>
请捐款:
<%session.setAttribute("charitarianCommit","true");%>
<input name="count"  maxlength="11" value="1"/>份 
<anchor title="确定">确定
<go href="result.jsp" method="post">
<postfield name="count" value="$count"/>
</go>
</anchor><br/>
(注:一份<%= Constants.CHARITARIAN_USER_MONEY %>乐币,一次最少捐助1份,最多捐助20万份)<br/>
<a href="/charitarian/index.jsp">慈善基金首页</a><br/>
<a href="/charitarian/history.jsp">查看您的慈善账户</a><br/>
<a href="/charitarian/rule.jsp">慈善基金使用规则</a><br/>
<a href="/top/charitarianTop.jsp">查看慈善排行榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>