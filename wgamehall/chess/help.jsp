<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.chess.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="中国象棋说明">
<p align="left">
==中国象棋说明==<br/>
进入一个棋局后选择一方加入,双方都有加入后棋局开始.<br/>
红方先下子.<br/>
棋盘上方为对手所在方,下方为自己所在方.<br/>
当轮到自己下棋时,点击自己的棋子选定,在下一个页面中选择位置即可将棋子移动到那里.之后等待对方下子.<br/>
本象棋规则采用通用的中国象棋规则,每一步时间5分钟,超时则对手可以选择直接胜出.<br/>
<a href="index.jsp">返回象棋大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>