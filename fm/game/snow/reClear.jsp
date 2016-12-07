<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
int isGameOver=snowAction.isGameOver(mid);// 比赛结束跳到别的页面
if(isGameOver==2||isGameOver==0){
	out.clearBuffer();
	response.sendRedirect("fight.jsp?mid="+mid);return;
}else if(isGameOver==3||isGameOver==4){
	out.clearBuffer();
	response.sendRedirect("/fm/game/fmgame.jsp");return;
}
int clear=snowAction.clearSnow();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="清雪" ontimer="<%=response.encodeURL("fight.jsp?mid="+mid)%>"><timer value="30" /><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(clear==1){ %>
不是家族成员(3秒后返回游戏)!<br/>
<%}else if(clear==2){ %>
未通过游戏资格审核(3秒后返回游戏)!<br/>
<%}else if(clear==3){ %>
您的钱不够(3秒后返回游戏)!<br/>
<%}else if(clear==4){ %>
已扣除<%=request.getAttribute("toolMoney") %>雪币.清扫成功,请返回游戏页面(3秒后返回游戏)!<br/>
<%}else if(clear==5){ %>
您正在道具使用CD中(3秒后返回游戏)!<br/>
<%}else if(clear==6){ %>
该道具不存在(3秒后返回游戏)!<br/>
<%} %>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>