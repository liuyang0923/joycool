<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction fmAction=new SnowGameAction(request,response);
if (request.getParameter("mid")==null){
out.clearBuffer();
response.sendRedirect("/user/login.jsp");return;
}
int mid=fmAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
int isGameOver=fmAction.isGameOver(mid);// 比赛结束跳到别的页面
if(isGameOver==2||isGameOver==0){
	out.clearBuffer();
	response.sendRedirect("fight.jsp?mid="+mid);return;
}else if(isGameOver==3){
	out.clearBuffer();
	response.sendRedirect("/fm/game/fmgame.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="兑换雪币"><p align="left"><%=BaseAction.getTop(request, response)%>
<%
int could=fmAction.couldGame();
if(could!=6){
	out.clearBuffer();
	response.sendRedirect("fight.jsp?mid="+mid);return;		
}
String c="";
if(fmAction.isInGame()==true){
if(request.getParameter("c")==null){
out.clearBuffer();
response.sendRedirect("fight.jsp?mid="+mid);return;}else{c="&amp;c="+request.getParameter("c"); %>
游戏中兑换需要花费1酷币!<br/>10万乐币兑换1雪币,
<%}}int snow=fmAction.getOneSnowMoney();if(request.getParameter("c")==null){%>
请先兑换雪币(10万乐币兑换1雪币)!<br/>
<%} %>
您账户中有雪币余额<%=snow %>,最多还能兑换<%=10000-snow %>雪币<br/>
请输入你想兑换的雪币数量<br/>
<input name="change" format="*N" maxlength="5"/><br/>
<anchor title="兑换">
确定
    <go href="reSnowMoney.jsp?mid=<%=request.getParameter("mid") %><%=c %>" method="post">
        <postfield name="change" value="$(change)" />
      </go>
    </anchor><br/>
<%if(request.getParameter("c")==null){ %>
<a href="fight.jsp?mid=<%=mid %>">直接进入游戏</a><br/>
<a href="instruction.jsp?mid=<%=mid %>">雪币说明</a><br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%}else{ %>
<a href="instruction.jsp?mid=<%=mid %><%=c %>">雪币说明</a><br/>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%} %>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>