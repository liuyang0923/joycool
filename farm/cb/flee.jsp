<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
BattleAction action = new BattleAction(request);
action.flee();
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="战斗">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<%if(farmUser.isInBattle()){%><a href="flee.jsp">逃跑</a>|<%}%>
<a href="cb.jsp">返回</a>|<a href="../map.jsp">返回场景</a><br/>
<%if(action.getAttribute2("hide_log")==null){%><%=farmUser.log.getLogString(10)%><%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>