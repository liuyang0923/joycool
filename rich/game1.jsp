<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.game1();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInGame1()&&!action.isResult("tip")){	 //离开法院
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
欢迎来到猜数字游戏<br/>
猜中的奖励点券100！<br/>
<%for(int i=game.numStart;i<=game.numEnd;i++){%>
<a href="game1.jsp?a=1&amp;o=<%=i%>">猜<%=i%></a> <%if(i%4==0){%><br/><%}%>
<%}%><br/>
<a href="game1.jsp?a=2">离开猜数字</a><br/>
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