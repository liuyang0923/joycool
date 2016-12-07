<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*" %> <%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
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
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="投雪球"><p align="left"><%=BaseAction.getTop(request, response)%>
<%List toolsList=snowAction.getToolsList();
if(toolsList!=null){
if(toolsList.size()>0){
for(int i=0;i<toolsList.size();i++){
SnowGameToolsBean tool=(SnowGameToolsBean)toolsList.get(i);if(tool.getTid()==1){%>
<a href="throw.jsp?tid=1&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">小雪球</a><br/>
<%}else if(tool.getTid()==2){%>
<a href="throw.jsp?tid=2&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">中雪球</a><br/>
<%}else if(tool.getTid()==3){%>
<a href="throw.jsp?tid=3&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">大雪球</a><br/>
<%}else if(tool.getTid()==4){%>
<a href="throw.jsp?tid=4&amp;mid=<%=mid %>&amp;id=<%=tool.getId() %>">投雪机</a><br/>
<%}}}}else{%>
无可用道具!<br/>
<%} %>
<%if(request.getAttribute("toolOwns")!=null&&!"".equals(request.getAttribute("toolOwns"))){%>
<%=request.getAttribute("toolOwns") %>
<%}%> 
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>