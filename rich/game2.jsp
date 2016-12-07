<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.game2();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInGame2()&&!action.isResult("tip")){	 //离开
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
欢迎来到对决屋<br/>
每次游戏需要花费300<br/>
<%if(game.lkUser!=null){%>
现在坐庄的是<%=game.lkUser.getWmlName()%><br/>
<%}else{%>
选择一个坐庄:<br/>
<%}%>
<a href="game2.jsp?a=1&amp;o=0">我选乐</a><br/>
<a href="game2.jsp?a=1&amp;o=1">我选酷</a><br/>
<a href="game2.jsp?a=2">离开对决屋</a><br/>
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