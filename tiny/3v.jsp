<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="header.jsp"%><%
action.view3();
TinyGame3 tg = (TinyGame3)action.getGame();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isGameOver()){%>
恭喜答对了!<br/>
<a href="<%=(action.getReturnURLWml())%>">游戏完成</a><br/>
<%}else{%>
还没有猜对,请再接再厉!<br/>
第<%=tg.getMoveCount()%>次尝试<br/>
我猜：<input name="guess" format="*N" maxlength="10" value=""/><br/>
<anchor title="确定">试试这个
  <go href="3v.jsp" method="post">
    <postfield name="guess" value="$guess"/>
  </go>
</anchor><br/>
<%}%>
<%=tg.getLogString()%>
<br/>
<%if(!action.isGameOver()){%>
答案为<%=tg.getDigit()%>位，每位可能是0到<%=tg.getNumberLimit()-1%>之间任意数字，每位数字不同<br/>
<a href="3h.jsp">查看规则</a><br/>
<a href="giveup.jsp">放弃</a><br/>
<%}%>
<%@include file="footer.jsp"%>
</p>
</card>
</wml>