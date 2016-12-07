<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BattleAction action = new BattleAction(request);
action.rest();
FarmUserBean user = action.getUser();
BattleStatus bs = user.getCurStat();
String returnURL = "rest.jsp";
if(user.hp==bs.hp&&user.mp==bs.mp&&user.sp==bs.sp)
	returnURL="../map.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="休息" ontimer="<%=response.encodeURL(returnURL)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
血:<%=user.hp%>/<%=bs.hp%><br/>
气力:<%=user.mp%>/<%=bs.mp%><br/>
体力:<%=user.sp%>/<%=bs.sp%><br/>
人物还在继续休息中…<br/>
<a href="cb.jsp">不休息了立即返回</a><br/>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>