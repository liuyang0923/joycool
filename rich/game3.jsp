<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.game3();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInGame3()&&!action.isResult("tip")){	 //离开
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
RichGame game = action.world.game;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(!action.isResult("tip")){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
欢迎来到彩票屋<br/>
现在累计奖金为<%=game.lottery%><br/>
<%for(int i=1;i<=6;i++){%>
<a href="game3.jsp?a=1&amp;o=<%=i%>">买<%=i%></a><%if(i%3==0){%><br/><%}%>
<%}%>
<a href="game3.jsp?a=2">离开彩票屋</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="go.jsp">确定</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%></wml>