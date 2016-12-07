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
int make=snowAction.getOneTypeTool();
int tid=snowAction.getParameterInt("tid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="造雪球"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(make==0||make==1){%>
该道具不存在!<br/>
<%}else if(make==2){ if(request.getAttribute("oneTool")!=null){ 
SnowGameToolTypeBean bean=(SnowGameToolTypeBean)request.getAttribute("oneTool");%>
<% String ct="/fm/game/img/xq.gif"; if(bean.getId()==4){ct="/fm/game/img/touxj.gif";}%>
<img alt="make" src="<%=ct%>" /><br/>
制作费用:<%=bean.getSpendMoney() %>雪币<br/>
制作时间:<%=bean.getSpendTime() %>秒<br/>
效果:对方增加积雪<%=bean.getSnowEffect() %> <%if(bean.getUseTime()>1){%>可以重复使用<%=bean.getUseTime() %>次<%}%><br/>
<a  href="reMake.jsp?tid=<%=tid %>&amp;mid=<%=mid %>">确定制作</a><br/>
<%} }%>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>