<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
战斗中可以选择攻击,或者技能中的自动攻击.或者选择增加体力的药草等物品来使用.<br/>
胜利打败野兽后可能会掉落一些有用的物品和钱财,点击"掉落"可以查看和拾取.<br/>
选择逃跑总有一定概率失败.战斗中如果死亡,会损失战斗经验.<br/>

返回<a href="../cb/cb.jsp">战斗/休息</a>功能<br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>