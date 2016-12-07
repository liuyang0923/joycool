<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="掷骰子-帮助">
<p align="left">
<%=BaseAction.getTop(request, response)%>
掷骰子游戏说明<br/>
-------------------<br/>
系统会随机掷出3个骰子，游戏中玩家只需猜大或者猜小，并对大小下注即可。10点以上（不含10点）为大。猜中后积分翻倍。<br/>
<br/>
<a href="index.jsp">返回上一级</a><br/>
<a href="../index.jsp">返回赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>