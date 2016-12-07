<%@page contentType="text/vnd.wap.wml;charset=utf-8"%><%response.setHeader("Cache-Control","no-cache");%>
<%@page import="jc.guest.*,net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,jc.guest.battle.*"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request);%>
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="名字大作战">
<p><%=BaseAction.getTop(request,response)%>
很抱歉,您的游币不够了,无法进行游戏.<br/>
*温馨提示:<a href="/register.jsp">注册</a>乐酷正式用户后会获得10000游币,每天上线还可以多获得200游币哟~<br/>
<a href="game.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>